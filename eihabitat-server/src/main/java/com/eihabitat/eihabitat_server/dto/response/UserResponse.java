package com.eihabitat.eihabitat_server.dto.response;

import com.eihabitat.eihabitat_server.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String email;
    LocalDate signupDate;
    String firstName;
    String lastName;
    String profileName;
    String profileAvatar;
    String phone;
    String address;
    String gender;
    String bio;
    LocalDate dateOfBirth;
    String nationality;
    Set<Role> roles;
    int followers;
    int following;
}
