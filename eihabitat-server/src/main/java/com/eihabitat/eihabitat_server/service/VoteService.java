package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.OptionCreationReq;
import com.eihabitat.eihabitat_server.dto.request.VoteCastReq;
import com.eihabitat.eihabitat_server.dto.request.VoteCreationReq;
import com.eihabitat.eihabitat_server.dto.response.VoteResponse;
import com.eihabitat.eihabitat_server.entity.Option;
import com.eihabitat.eihabitat_server.entity.Vote;
import com.eihabitat.eihabitat_server.mapper.VoteMapper;
import com.eihabitat.eihabitat_server.repository.VoteOptionRepository;
import com.eihabitat.eihabitat_server.repository.VoteRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VoteService {
    private VoteRepository voteRepository;
    private VoteMapper voteMapper;
    VoteOptionRepository voteOptionRepository;
    VoteOptionRepository optionRepository;

    public VoteResponse createVote(VoteCreationReq voteRequestDto) {
        Vote vote = voteRepository.save(voteMapper.toVote(voteRequestDto));
        vote.setCreatedAt(LocalDateTime.now());
        List<String> options = new ArrayList<>();
        for (OptionCreationReq optionCreationReq : voteRequestDto.getOptions()) {
            Option o = voteMapper.toOption(optionCreationReq);
            o.setVoteId(vote.getId());
            voteOptionRepository.save(o);
            options.add(optionCreationReq.getTitle());
        }
        VoteResponse response = voteMapper.toVoteResponse(vote);
        response.setOptions(options);
        return response;
    }

    public void castVote(VoteCastReq voteRecordRequestDto, String userId) {
        Option option = optionRepository.findById(voteRecordRequestDto.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

        if (!option.getUserIds().contains(userId)) {
            option.getUserIds().add(userId);
            option.setNumberOfChoices(option.getNumberOfChoices() + 1);
            // Calculate percentage (this might require additional logic depending on the total votes)
            option.setPercentage(calculatePercentage(option.getNumberOfChoices(), option.getVoteId()));
            optionRepository.save(option);
        } else {
            throw new IllegalArgumentException("User has already voted for this option");
        }
    }

    private double calculatePercentage(int optionVotes, String voteId) {
        // Fetch all options for this vote ID
        List<Option> options = optionRepository.findByVoteId(voteId);

        // Calculate the total votes across all options
        int totalVotes = options.stream().mapToInt(Option::getNumberOfChoices).sum();

        // Calculate the percentage for the specific option
        return totalVotes > 0 ? ((double) optionVotes / totalVotes) * 100 : 0.0;
    }
}

