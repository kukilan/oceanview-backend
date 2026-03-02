package com.oceanview.security;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtClaims {

    private String username;
    private Integer userId;
    private String role;
}