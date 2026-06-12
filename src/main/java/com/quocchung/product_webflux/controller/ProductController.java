package com.quocchung.product_webflux.controller;

import com.quocchung.product_webflux.dto.CategoryStats;
import com.quocchung.product_webflux.dto.CreateProductRequest;
import com.quocchung.product_webflux.dto.ProductResponse;
import com.quocchung.product_webflux.repository.ProductRepository;
import com.quocchung.product_webflux.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
    return productService.createProduct(request);
  }

  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ProductResponse> streamProducts() {
    log.info("Client connected to SSE stream");
    return productService.streamProducts();
  }

  @GetMapping("/stats/category")
  public Flux<CategoryStats> getCategoryStats() {
    return productService.getCategoryStats();
  }
}
