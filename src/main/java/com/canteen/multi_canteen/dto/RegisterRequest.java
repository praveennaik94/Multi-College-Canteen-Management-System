package com.canteen.multi_canteen.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private String collegeName;
}
