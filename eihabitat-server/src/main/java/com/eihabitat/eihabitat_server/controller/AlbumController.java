package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.AlbumReq;
import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.response.PostResponse;
import com.eihabitat.eihabitat_server.entity.SavedPost;
import com.eihabitat.eihabitat_server.service.AlbumService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/album")
public class AlbumController {
    AlbumService albumService;

    @PostMapping
    public ApiResponse<String> createAlbum(@Valid @RequestBody AlbumReq albumReq) {
        String result = albumService.createAlbum(albumReq);
        return ApiResponse.<String>builder()
                .code(1000)
                .data(result)
                .build();
    }
}
