package com.eihabitat.eihabitat_server.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationReq {
    @Email(message = "EMAIL_INVALID")
    String email;

    @Size(min = 8, message = "USER_PASSWORD_INVALID")
    String password;
    LocalDate signupDate;
    String firstName;
    String lastName;

    @Size(min = 6, message = "PROFILE_NAME_INVALID")
    String profileName;


}
