package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.StoryCreationReq;
import com.eihabitat.eihabitat_server.dto.response.StoryResponse;
import com.eihabitat.eihabitat_server.service.StoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/story")
public class StoryController {
    StoryService storyService;

    @PostMapping
    public ApiResponse<StoryResponse> createStory(@RequestBody StoryCreationReq storyRequest) {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(storyService.createStory(storyRequest));
        return resp;
    }

    @GetMapping("/{storyId}")
    public ApiResponse<StoryResponse> findStoryById(@PathVariable String storyId) throws Exception {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(storyService.findStoryById(storyId));
        return resp;
    }

//    @GetMapping
//    public ResponseEntity<List<Story>> getAllStories() {
//        List<Story> stories = storyService.getAllStories();
//        return ResponseEntity.ok(stories);
//    }
//
//    @GetMapping("/author/{author}")
//    public ResponseEntity<List<Story>> getStoriesByAuthor(@PathVariable String author) {
//        List<Story> stories = storyService.getStoriesByAuthor(author);
//        return ResponseEntity.ok(stories);
//    }

    @DeleteMapping("/{storyId}")
    public ResponseEntity<Void> deletePost(@PathVariable String storyId) throws Exception {
        storyService.deleteStory(storyId);
        return ResponseEntity.noContent().build();
    }

}
