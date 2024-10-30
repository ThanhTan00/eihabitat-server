package com.eihabitat.eihabitat_server.dto.request;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoteCastReq {
    private String voteId;
    private String optionId;
}
