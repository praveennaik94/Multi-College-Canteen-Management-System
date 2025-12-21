package com.canteen.multi_canteen.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {

    // PLACED, PREPARING, COMPLETED, CANCELLED
    private String status;
}
