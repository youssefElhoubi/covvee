package com.covveeauthservice.dto;

import com.covveeauthservice.enums.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    @NotNull(message = "user name should not null ")
    @NotBlank(message = "user name should not be blank ")
    @Size(min = 6,max = 22,message = "user name should not be less than 6 charecters or more than 22 charecters")
    private String username;
    @NotNull(message = "password should not null ")
    @NotBlank(message = "password should not be blank ")
    @Size(min = 6,max = 22,message = "password should not be less than 6 charecters or more than 22 charecters")
    private String password;
    @Email
    private String email;
    @NotNull
    @NotBlank
    private Role role;

    private String image;
}
