package com.eihabitat.eihabitat_server.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateReq {
    String firstName;
    String lastName;
    String profileName;
    String phone;
    String address;
    String gender;
    String bio;
    LocalDate dateOfBirth;
}
