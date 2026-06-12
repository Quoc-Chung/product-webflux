package com.quocchung.product_webflux.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPageResponse <T> {
    private List<T> data;
    private Long totalElement;
    private Integer page;
    private Integer size;
    private Integer totalPage;
}
