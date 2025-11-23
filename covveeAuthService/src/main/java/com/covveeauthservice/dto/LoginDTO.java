package com.covveeauthservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginDTO {
    @Email
    @NotEmpty(message = "email should not be empty")
    private String email;
    @NotBlank(message = "password should not be blank ")
    @NotEmpty
    private String password;
}
