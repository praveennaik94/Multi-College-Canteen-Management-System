package com.canteen.multi_canteen.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    private Long studentId;      // required
    private Long collegeId;      // required
    private List<OrderItemRequest> items;  // required (menuItemId + quantity)
}
