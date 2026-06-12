package com.quocchung.product_webflux.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryStats {
  private String category;
  private Long count;
}