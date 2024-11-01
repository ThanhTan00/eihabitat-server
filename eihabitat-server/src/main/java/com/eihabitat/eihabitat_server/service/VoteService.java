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

    public VoteResponse createVote(VoteCreationReq voteRequestDto, String userId) {
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
        response.setUserId(userId);
        return response;
    }

    public void castVote(VoteCastReq voteCastRequestDto, String userId) {
        Option option = optionRepository.findById(voteCastRequestDto.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

        // Add user to the userIds list if they haven't voted already
        if (option.getUserIds() == null) {
            option.setUserIds(new ArrayList<>()); // Initialize if null
        }

        if (!option.getUserIds().contains(userId)) {
            option.getUserIds().add(userId);
            option.setNumberOfChoices(option.getNumberOfChoices() + 1);
            optionRepository.save(option);

            // Update percentages for all options associated with this vote
            updatePercentageForOptions(option.getVoteId());
        } else {
            throw new IllegalArgumentException("User has already voted for this option");
        }
    }

    private void updatePercentageForOptions(String voteId) {
        // Retrieve all options for the given voteId
        List<Option> options = optionRepository.findByVoteId(voteId);

        // Calculate the total votes for all options
        int totalVotes = options.stream().mapToInt(Option::getNumberOfChoices).sum();

        options.forEach(option -> {
            if (totalVotes > 0) {
                // Calculate percentage based on total votes
                double percentage = ((double) option.getNumberOfChoices() / totalVotes) * 100;
                option.setPercentage(percentage);
            } else {
                option.setPercentage(0.0);
            }
        });

        // Save updated options back to the repository
        optionRepository.saveAll(options);
    }

    public void deleteVoteCast(VoteCastReq voteCastRequestDto, String userId) {
        Option option = optionRepository.findById(voteCastRequestDto.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

        // Ensure user has already voted on this option
        if (option.getUserIds() != null && option.getUserIds().contains(userId)) {
            option.getUserIds().remove(userId);  // Remove the user from the list of voters
            option.setNumberOfChoices(option.getNumberOfChoices() - 1); // Decrement the vote count

            optionRepository.save(option); // Save updated option

            // Recalculate percentages for all options under the same vote
            updatePercentageForOptions(option.getVoteId());
        } else {
            throw new IllegalArgumentException("User has not voted for this option");
        }
    }

}

