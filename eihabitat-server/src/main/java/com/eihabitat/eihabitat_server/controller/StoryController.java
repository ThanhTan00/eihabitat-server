package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.StoryCreationReq;
import com.eihabitat.eihabitat_server.entity.Story;
import com.eihabitat.eihabitat_server.service.StoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/story")
public class StoryController {
    StoryService storyService;

    @PostMapping
    public ResponseEntity<Story> createStory(@Valid @RequestBody StoryCreationReq storyRequest) {
        Story createdStory = storyService.createStory(storyRequest);
        return new ResponseEntity<>(createdStory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Story> getStoryById(@PathVariable String id) throws Exception {
        Story story = storyService.getStoryById(id);
        return ResponseEntity.ok(story);
    }

    @GetMapping
    public ResponseEntity<List<Story>> getAllStories() {
        List<Story> stories = storyService.getAllStories();
        return ResponseEntity.ok(stories);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<Story>> getStoriesByAuthor(@PathVariable String author) {
        List<Story> stories = storyService.getStoriesByAuthor(author);
        return ResponseEntity.ok(stories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable String id) throws Exception {
        storyService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }

}
