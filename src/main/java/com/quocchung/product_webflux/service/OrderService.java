package com.quocchung.product_webflux.service;

import com.quocchung.product_webflux.dto.CreateOrderRequest;
import com.quocchung.product_webflux.dto.OrderResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
  Mono<OrderResponse> createOrder( CreateOrderRequest request);

 Flux<OrderResponse> getAllOrders();

 Mono<OrderResponse> getOrderById( Long id);


  Mono<OrderResponse> updateStatus(
     Long id,
     String status);

 Mono<Void> deleteOrder(Long id);
}
