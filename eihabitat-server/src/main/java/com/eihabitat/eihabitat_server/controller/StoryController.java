package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.response.StoryResponse;
import com.eihabitat.eihabitat_server.service.StoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/story")
public class StoryController {
    StoryService storyService;

    @PostMapping("/{authorId}")
    public ApiResponse<StoryResponse> createStory(@PathVariable String authorId, @RequestParam("images") List<MultipartFile> files) throws IOException {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(storyService.createStory(authorId, files));
        return resp;
    }

    @GetMapping("/{storyId}")
    public ApiResponse<StoryResponse> findStoryById(@PathVariable String storyId) throws Exception {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(storyService.findStoryById(storyId));
        return resp;
    }

    @GetMapping("/active/{authorId}")
    public ApiResponse<StoryResponse> getActiveStory(@PathVariable String authorId) throws Exception {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(storyService.getActiveStories(authorId));
        return resp;
    }

    @DeleteMapping("/{storyId}")
    public ApiResponse<StoryResponse> deleteStory(@PathVariable String storyId) throws Exception {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(storyService.deleteStory(storyId));
        return resp;
    }

}
