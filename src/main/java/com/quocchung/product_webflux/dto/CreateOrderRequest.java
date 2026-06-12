package com.quocchung.product_webflux.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
  private String customerName;

  private List<OrderItemRequest> items;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class OrderItemRequest{
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
  }
}
