package com.eihabitat.eihabitat_server.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreationReq {
    String name;
    String description;
    Set<String> permissions;
}
