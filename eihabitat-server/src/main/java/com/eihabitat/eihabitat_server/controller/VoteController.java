package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.VoteCreationReq;
import com.eihabitat.eihabitat_server.dto.request.VoteRecordReq;
import com.eihabitat.eihabitat_server.dto.response.VoteRecordResponse;
import com.eihabitat.eihabitat_server.dto.response.VoteResponse;
import com.eihabitat.eihabitat_server.service.VoteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
    public String castVote(@RequestBody VoteRecordReq voteRecordReq) {
        voteService.castVote(voteRecordReq);
        return "Vote successfully casted!";
    }

    @GetMapping("/{voteId}/records")
    public ApiResponse<List<VoteRecordResponse>> getVoteRecords(@PathVariable String voteId) {
        List<VoteRecordResponse> voteRecords = voteService.getVoteRecords(voteId);
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(voteRecords);
        return resp;
    }
}
