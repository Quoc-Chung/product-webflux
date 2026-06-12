package com.quocchung.product_webflux.repository;

import com.quocchung.product_webflux.dto.CategoryStats;
import com.quocchung.product_webflux.model.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Long> {
  Mono<Boolean> existsByNameIgnoreCase(String name);

  @Query(
      "SELECT category, COUNT(*) AS count "
      + "FROM products "
      + "GROUP BY category "
      + "ORDER BY count DESC"
  )
  Flux<CategoryStats> countGroupByCategory();
}
