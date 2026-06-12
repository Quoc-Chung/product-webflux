package com.quocchung.product_webflux.service.Impl;

import com.quocchung.product_webflux.dto.CreateOrderRequest;
import com.quocchung.product_webflux.dto.OrderResponse;
import com.quocchung.product_webflux.dto.OrderResponse.OrderItemResponse;
import com.quocchung.product_webflux.model.Order;
import com.quocchung.product_webflux.model.OrderItem;
import com.quocchung.product_webflux.model.Product;
import com.quocchung.product_webflux.repository.OrderItemRepository;
import com.quocchung.product_webflux.repository.OrderRepository;
import com.quocchung.product_webflux.repository.ProductRepository;
import com.quocchung.product_webflux.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final ProductRepository productRepository;


  @Override
  @Transactional
  public Mono<OrderResponse> createOrder(CreateOrderRequest request) {

    // Bước 1: Lấy tất cả products để validate + lấy giá
    return Flux.fromIterable(request.getItems())
        .flatMap(item ->
            productRepository.findById(item.getProductId())
                .switchIfEmpty(Mono.error(
                    new RuntimeException("Không tìm thấy product id: " + item.getProductId())
                ))
                // Gắn quantity vào product để tính tiền
                .map(product -> Map.entry(product, item.getQuantity()))
        )
        .collectList()

        // Bước 2: Tính total + tạo Order
        .flatMap(entries -> {
          BigDecimal total = entries.stream()
              .map(e -> e.getKey().getPrice()
                  .multiply(BigDecimal.valueOf(e.getValue())))
              .reduce(BigDecimal.ZERO, BigDecimal::add);

          Order order = Order.builder()
              .customerName(request.getCustomerName())
              .status("PENDING")
              .totalAmount(total)
              .createdAt(LocalDateTime.now())
              .build();

          return orderRepository.save(order)
              // Bước 3: Save từng OrderItem
              .flatMap(savedOrder ->
                  Flux.fromIterable(entries)
                      .flatMap(e -> {
                        Product product  = e.getKey();
                        Integer quantity = e.getValue();

                        OrderItem item = OrderItem.builder()
                            .orderId(savedOrder.getId())
                            .productId(product.getId())
                            .quantity(quantity)
                            .unitPrice(product.getPrice())
                            .build();

                        return orderItemRepository.save(item);
                      })
                      .collectList()
                      // Bước 4: Assemble response
                      .map(savedItems -> buildResponse(savedOrder, savedItems, entries))
              );
        });
  }

  private OrderResponse buildResponse(Order order, List<OrderItem> items,
      List<Map.Entry<Product, Integer>> entries) {
    List<OrderItemResponse> itemResponses = items.stream()
        .map(item -> {
          String productName = entries.stream()
              .filter(e -> e.getKey().getId().equals(item.getProductId()))
              .map(e -> e.getKey().getName())
              .findFirst().orElse("Unknown");

          return OrderItemResponse.builder()
              .id(item.getId())
              .productId(item.getProductId())
              .productName(productName)
              .quantity(item.getQuantity())
              .unitPrice(item.getUnitPrice())
              .subtotal(item.getUnitPrice()
                  .multiply(BigDecimal.valueOf(item.getQuantity())))
              .build();
        })
        .toList();

    return OrderResponse.builder()
        .id(order.getId())
        .customerName(order.getCustomerName())
        .status(order.getStatus())
        .totalAmount(order.getTotalAmount())
        .createdAt(order.getCreatedAt())
        .items(itemResponses)
        .build();
  }

  @Transactional
  public Mono<OrderResponse> updateStatus(Long id, String newStatus) {
    return orderRepository.findById(id)
        .switchIfEmpty(Mono.error(new RuntimeException("Order không tồn tại")))
        .map(order -> {
          order.setStatus(newStatus);
          return order;
        })
        .flatMap(orderRepository::save)
        .flatMap(order -> getOrderById(order.getId()));
  }

  @Override
  @Transactional
  public Mono<Void> deleteOrder(Long id) {
    return orderItemRepository.deleteByOrderId(id)
        .then(orderRepository.deleteById(id));
  }

  @Override
  public Mono<OrderResponse> getOrderById(Long id) {
    Mono<Order> orderMono = orderRepository.findById(id)
        .switchIfEmpty(Mono.error(new RuntimeException("Order không tồn tại: " + id)));

    return orderMono.zipWith(
        orderItemRepository.findByOrderId(id).collectList(),
        (order, items) -> Map.entry(order, items)
    ).flatMap(entry -> enrichItems(entry.getKey(), entry.getValue()));

  }

  @Override
  public Flux<OrderResponse> getAllOrders() {
    return null;
  }

  private Mono<OrderResponse> enrichItems(Order order, List<OrderItem> items){
    return Flux.fromIterable(items)
        .flatMap(item ->
            productRepository.findById(item.getProductId())
        .map(product -> OrderItemResponse.builder()
            .id(item.getId())
            .productId(item.getProductId())
            .productName(product.getName())
            .quantity(item.getQuantity())
            .unitPrice(item.getUnitPrice())
            .subtotal(item.getUnitPrice())
            .build()
        ))
        .collectList()
        // ca cai phan ben tren duoc bien doi thanh List
        .map(fullOrderItem ->
            OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(fullOrderItem)
                .build());
  }



}
