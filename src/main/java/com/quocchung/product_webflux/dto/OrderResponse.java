package com.quocchung.product_webflux.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
  private Long id;
  private String customerName;
  private String status;
  private BigDecimal totalAmount;
  private LocalDateTime createdAt;
  private List<OrderItemResponse> items;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
  }
}
