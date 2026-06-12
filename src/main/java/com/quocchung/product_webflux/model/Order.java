package com.quocchung.product_webflux.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
public class Order {
  @Id
  private Long id;
  private String customerName;
  private String status;
  private BigDecimal totalAmount;
  private LocalDateTime createdAt;
}
