package com.eihabitat.eihabitat_server.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateReq {
    String email;
    String password;
    String firstName;
    String lastName;
    String profileName;
    String profileAvatar;
    String phone;
    String address;
    String gender;
    LocalDate dateOfBirth;
    String nationality;

}
