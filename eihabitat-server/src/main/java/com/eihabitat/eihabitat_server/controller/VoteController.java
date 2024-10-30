package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.OptionCreationReq;
import com.eihabitat.eihabitat_server.dto.request.VoteCastReq;
import com.eihabitat.eihabitat_server.dto.request.VoteCreationReq;
import com.eihabitat.eihabitat_server.dto.response.VoteResponse;
import com.eihabitat.eihabitat_server.service.VoteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/vote")
public class VoteController {
    private VoteService voteService;

    @PostMapping("/create")
    public ApiResponse<VoteResponse> createVote(@RequestBody VoteCreationReq voteRequestDto) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(voteService.createVote(voteRequestDto));
        return resp;
    }

    @PostMapping("/cast")
    public String castVote(@RequestBody VoteCastReq voteCastRequestDto, @RequestParam String userId) {
        voteService.castVote(voteCastRequestDto, userId);
        return "Vote successfully cast!";
    }
}
