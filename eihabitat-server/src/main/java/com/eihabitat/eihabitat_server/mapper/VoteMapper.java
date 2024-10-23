package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.VoteCreationReq;
import com.eihabitat.eihabitat_server.dto.request.VoteRecordReq;
import com.eihabitat.eihabitat_server.dto.response.VoteRecordResponse;
import com.eihabitat.eihabitat_server.dto.response.VoteResponse;
import com.eihabitat.eihabitat_server.entity.Vote;
import com.eihabitat.eihabitat_server.entity.VoteRecord;
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

    // Mapping from VoteRecordRequestDto to VoteRecord entity
    @Mapping(target = "id", ignore = true)
    VoteRecord toVoteRecord(VoteRecordReq request, String userId);

    // Mapping from VoteRecord to VoteRecordResponseDto
    @Mapping(target = "userId", source = "voteRecord.userId")
    VoteRecordResponse toVoteRecordResponse(VoteRecord voteRecord);

    // Mapping list of VoteRecord to list of VoteRecordResponseDto
    List<VoteRecordResponse> toVoteRecordResponseList(List<VoteRecord> voteRecords);
}
