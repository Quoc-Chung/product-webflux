package com.quocchung.product_webflux.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
  private Long id;
  private String name;
  private BigDecimal price;
  private Integer quantity;
  private String category;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
