package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.OptionCreationReq;
import com.eihabitat.eihabitat_server.dto.request.VoteCreationReq;
import com.eihabitat.eihabitat_server.dto.response.VoteRecordResponse;
import com.eihabitat.eihabitat_server.dto.response.VoteResponse;
import com.eihabitat.eihabitat_server.entity.Option;
import com.eihabitat.eihabitat_server.entity.Vote;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    // Mapping from VoteRequestDto to Vote entity
    @Mapping(target = "id", ignore = true)
    Vote toVote(VoteCreationReq request);

    // Mapping from Vote to VoteResponseDto
    @Mapping(target = "userId", source = "vote.userId")
    VoteResponse toVoteResponse(Vote vote);

    Option toOption(OptionCreationReq request);

    // Mapping from VoteRecord to VoteRecordResponseDto
    VoteRecordResponse toVoteRecordResponse(Option voteRecord);

    // Mapping list of VoteRecord to list of VoteRecordResponseDto
    List<VoteRecordResponse> toVoteRecordResponseList(List<Option> voteRecords);
}
