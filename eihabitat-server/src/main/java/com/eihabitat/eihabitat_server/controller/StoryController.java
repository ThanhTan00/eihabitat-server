package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.StoryCreationReq;
import com.eihabitat.eihabitat_server.dto.response.AlbumResponse;
import com.eihabitat.eihabitat_server.dto.response.StoryResponse;
import com.eihabitat.eihabitat_server.service.StoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/story")
public class StoryController {
    StoryService storyService;

    @PostMapping
    public ApiResponse<StoryResponse> createStory(@RequestParam("imageFile") MultipartFile imageFile, @RequestParam("caption") String caption, @RequestParam("authorId") String authorId) throws IOException {

        return ApiResponse.<StoryResponse>builder()
                .code(1000)
                .data(storyService.createStory(StoryCreationReq.builder()
                        .authorId(authorId)
                        .imageFile(imageFile)
                        .caption(caption)
                        .build()))
                .build();
    }

    @GetMapping("/{authorId}")
    public ApiResponse<List<StoryResponse>> findAllByAuthorId(@PathVariable String authorId) throws Exception {
        return ApiResponse.<List<StoryResponse>>builder()
                .code(1000)
                .data(storyService.getActiveStories(authorId))
                .build();
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
    public ApiResponse<StoryResponse> deleteStory(@PathVariable String storyId) throws Exception {
        ApiResponse resp = new ApiResponse();
        resp.setCode(1000);
        resp.setData(storyService.deleteStory(storyId));
        return resp;
    }

}
