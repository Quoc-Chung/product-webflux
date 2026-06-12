package com.quocchung.product_webflux.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
  private String name;

  private BigDecimal price;

  private Integer quantity;

  private String category;
}
