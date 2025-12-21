package com.canteen.multi_canteen.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMenuItemRequest {

    private String name;
    private Double price;

    // Breakfast / Snacks / Chinese / Biryani / Drinks
    private String category;

    private int availableQuantity;
}
