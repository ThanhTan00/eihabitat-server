package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.VoteCreationReq;
import com.eihabitat.eihabitat_server.dto.request.VoteRecordReq;
import com.eihabitat.eihabitat_server.dto.response.VoteRecordResponse;
import com.eihabitat.eihabitat_server.dto.response.VoteResponse;
import com.eihabitat.eihabitat_server.entity.User;
import com.eihabitat.eihabitat_server.entity.Vote;
import com.eihabitat.eihabitat_server.entity.VoteRecord;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.VoteMapper;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import com.eihabitat.eihabitat_server.repository.VoteRecordRepository;
import com.eihabitat.eihabitat_server.repository.VoteRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VoteService {
    private VoteRepository voteRepository;
    private VoteRecordRepository voteRecordRepository;
    private VoteMapper voteMapper;
    UserRepository userRepository;

    public VoteResponse createVote(VoteCreationReq voteRequestDto) {
        Vote vote = voteMapper.toVote(voteRequestDto);
        vote.setUserId(voteRequestDto.getUserId());
        vote.setCreatedAt(LocalDateTime.now());
        Vote savedVote = voteRepository.save(vote);
        return voteMapper.toVoteResponse(savedVote);
    }
    public void castVote(VoteRecordReq voteRecordRequestDto) {
        Vote vote = voteRepository.findById(voteRecordRequestDto.getVoteId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid vote ID"));

        if (voteRecordRepository.existsByVoteIdAndUserId(vote.getId(), voteRecordRequestDto.getUserId())) {
            throw new IllegalArgumentException("User has already voted for this topic");
        }

        VoteRecord voteRecord = voteMapper.toVoteRecord(voteRecordRequestDto, voteRecordRequestDto.getUserId());
        voteRecordRepository.save(voteRecord);
    }

    public List<VoteRecordResponse> getVoteRecords(String voteId) {
        List<VoteRecord> voteRecords = voteRecordRepository.findByVoteId(voteId);

        // Convert VoteRecord entities to VoteRecordResponseDto and return the list
        return voteRecords.stream()
                .map(voteRecord -> new VoteRecordResponse(
                        voteRecord.getUserId(),
                        voteRecord.getSelectedOption(),
                        voteRecord.getVotedAt()
                ))
                .collect(Collectors.toList());
    }
}
