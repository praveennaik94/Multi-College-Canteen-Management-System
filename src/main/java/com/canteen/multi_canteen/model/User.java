package com.canteen.multi_canteen.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // ✅ Kept as String (since you removed college_id from users table)
    private String collegeName;

    private String gender;

    @Column(unique = true, nullable = false)
    private String email;

    private String mobile;

    @Column(nullable = false)
    private String password;

    // ✅ STUDENT or ADMIN
    @Column(nullable = false)
    private String role;
}
