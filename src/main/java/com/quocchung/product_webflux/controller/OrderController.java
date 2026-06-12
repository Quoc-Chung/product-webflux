package com.quocchung.product_webflux.controller;

import com.quocchung.product_webflux.dto.CreateOrderRequest;
import com.quocchung.product_webflux.dto.OrderResponse;
import com.quocchung.product_webflux.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<OrderResponse> createOrder( @RequestBody CreateOrderRequest request) {
    return orderService.createOrder(request);
  }

  @GetMapping
  public Flux<OrderResponse> getAllOrders() {
    return orderService.getAllOrders();
  }

  @GetMapping("/{id}")
  public Mono<OrderResponse> getOrderById(@PathVariable Long id) {
    return orderService.getOrderById(id);
  }

  @PatchMapping("/{id}/status")
  public Mono<OrderResponse> updateStatus(
      @PathVariable Long id,
      @RequestParam String status) {
    return orderService.updateStatus(id, status);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteOrder(@PathVariable Long id) {
    return orderService.deleteOrder(id);
  }



}
