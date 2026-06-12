package com.quocchung.product_webflux.repository;

import com.quocchung.product_webflux.model.Order;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends R2dbcRepository<Order, Long> {
  Flux<Order> findByCustomerName(String customerName);
  Flux<Order> findByStatus(String status);
}
