package com.eihabitat.eihabitat_server.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String email;
    String password;
    LocalDate signupDate;
    String firstName;
    String lastName;
    String profileName;
    String profileAvatar;
    String bio;
    String phone;
    String address;
    String gender;
    LocalDate dateOfBirth;
    String nationality;
    boolean account_verified = false;
    @ManyToMany
    Set<Role> roles;
}
