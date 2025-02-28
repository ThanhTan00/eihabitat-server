package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.entity.SavedPost;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.repository.AlbumRepository;
import com.eihabitat.eihabitat_server.repository.SavedPostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SavedPostService {
    AlbumRepository albumRepository;
    SavedPostRepository savedPostRepository;

    public String savePost(SavedPost savedPost) {
        if (!albumRepository.existsById(savedPost.getAlbumId())){
            throw new AppException(ErrorCode.ALBUM_NOT_FOUND);
        }
        savedPostRepository.save(savedPost);
        return "Post saved successfully!";
    }
}
