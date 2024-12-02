package com.eihabitat.eihabitat_server.dto.response;

import com.eihabitat.eihabitat_server.entity.Role;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDemoResponse {
    String id;
    String email;
    String profileName;
    String profileAvatar;
    String firstName;
    String lastName;
    String userUrl;
}
