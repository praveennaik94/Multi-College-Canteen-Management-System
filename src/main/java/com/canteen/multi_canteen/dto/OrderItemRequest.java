package com.canteen.multi_canteen.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    private Long menuItemId;
    private Integer quantity;
}
