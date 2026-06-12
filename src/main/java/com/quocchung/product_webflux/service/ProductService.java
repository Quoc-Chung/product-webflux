package com.quocchung.product_webflux.service;

import com.quocchung.product_webflux.dto.CategoryStats;
import com.quocchung.product_webflux.dto.CreateProductRequest;
import com.quocchung.product_webflux.dto.ProductResponse;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
  Mono<ProductResponse> createProduct(CreateProductRequest request);

  Flux<ProductResponse> streamProducts();

  Flux<ProductResponse> bulkCreate(List<CreateProductRequest> requests);

  Flux<CategoryStats> getCategoryStats();
}
