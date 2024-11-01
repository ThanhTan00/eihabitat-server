package com.eihabitat.eihabitat_server.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "vote_options")
public class Option {
    @Id
    String id;
    String optionTitle;
    String voteId;
    List<String> userIds = new ArrayList<>();
    int numberOfChoices = 0;
    double percentage = 0.0;
}
