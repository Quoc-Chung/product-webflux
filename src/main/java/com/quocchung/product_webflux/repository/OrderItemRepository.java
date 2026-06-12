package com.quocchung.product_webflux.repository;

import com.quocchung.product_webflux.model.OrderItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderItemRepository extends R2dbcRepository<OrderItem, Long> {
  Flux<OrderItem> findByOrderId(Long orderId);

  Mono<Void> deleteByOrderId(Long orderId);
}