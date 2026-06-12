package com.quocchung.product_webflux.service.Impl;


import static reactor.netty.http.HttpConnectionLiveness.log;

import com.quocchung.product_webflux.dto.CategoryStats;
import com.quocchung.product_webflux.dto.CreateProductRequest;
import com.quocchung.product_webflux.dto.ProductResponse;
import com.quocchung.product_webflux.model.Product;
import com.quocchung.product_webflux.repository.ProductRepository;
import com.quocchung.product_webflux.service.ProductService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  @Override
  @Transactional
  public Mono<ProductResponse> createProduct(CreateProductRequest request) {
    return productRepository.existsByNameIgnoreCase(request.getName())
        .flatMap(exists -> {
              if (exists) {
                return Mono.error(new IllegalArgumentException(
                    "Sản phẩm với tên '" + request.getName() + "' đã tồn tại"
                ));
              }
                Product product = mapToEntity(request);
                return productRepository.save(product);
            }
        )
        .map(this::mapToResponse)
         .doOnSuccess(p -> log.info("Product created with id: {}", p.getId()));
  }

  public Flux<ProductResponse> streamProducts() {
    return productRepository.findAll()
        .map(this::mapToResponse)
        .delayElements(Duration.ofSeconds(1))
        .doOnNext(p -> log.info("Streaming product: {}", p.getName()));
  }

  @Override
  public Flux<ProductResponse> bulkCreate(List<CreateProductRequest> requests) {
    return Flux.fromIterable(requests)
        .flatMap(request ->
            productRepository.existsByNameIgnoreCase(request.getName())
                .flatMap(exists -> exists
                    ? Mono.empty()
                    : productRepository.save(mapToEntity(request))
                )
        )
        .map(this::mapToResponse)
        .doOnComplete(() -> log.info("Bulk create completed"));
  }

  @Override
  public Flux<CategoryStats> getCategoryStats() {
    return productRepository.countGroupByCategory();
  }

  private Product mapToEntity(CreateProductRequest request) {
    return Product.builder()
        .name(request.getName())
        .price(request.getPrice())
        .quantity(request.getQuantity())
        .category(request.getCategory())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  private ProductResponse mapToResponse(Product product) {
    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .quantity(product.getQuantity())
        .category(product.getCategory())
        .createdAt(product.getCreatedAt())
        .updatedAt(product.getUpdatedAt())
        .build();
  }
}
