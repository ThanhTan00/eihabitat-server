package com.eihabitat.eihabitat_server.service;

import com.eihabitat.eihabitat_server.dto.request.AlbumReq;
import com.eihabitat.eihabitat_server.dto.response.AlbumResponse;
import com.eihabitat.eihabitat_server.entity.Album;
import com.eihabitat.eihabitat_server.entity.PostContent;
import com.eihabitat.eihabitat_server.exception.AppException;
import com.eihabitat.eihabitat_server.exception.ErrorCode;
import com.eihabitat.eihabitat_server.mapper.AlbumMapper;
import com.eihabitat.eihabitat_server.repository.AlbumRepository;
import com.eihabitat.eihabitat_server.repository.PostContentRepository;
import com.eihabitat.eihabitat_server.repository.SavedPostRepository;
import com.eihabitat.eihabitat_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AlbumService {
    UserRepository userRepository;
    PostContentRepository postContentRepository;
    AlbumRepository albumRepository;
    SavedPostRepository savedPostRepository;
    AlbumMapper albumMapper;

    public String createAlbum(AlbumReq req) {
        if (!userRepository.existsById(req.getUserId())) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        Album album = albumMapper.toAlbum(req);
        album.setNameTag(req.getName().toLowerCase().replace(" ", "-"));
        albumRepository.save(album);
        return "Album saved successfully";
    }

    public String deleteAlbum(String albumId) {
        if (!albumRepository.existsById(albumId)){
            throw new AppException(ErrorCode.ALBUM_NOT_FOUND);
        }
        albumRepository.deleteById(albumId);
        return "Album deleted successfully";
    }

    public List<AlbumResponse> getAllAlbumsOfUser (String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        List<AlbumResponse> albumResponses = new ArrayList<>();
        List<Album> albums = albumRepository.findAllByUserId(userId);
        for (Album album : albums) {
            AlbumResponse albumResponse = new AlbumResponse();
            List<String> representImages = new ArrayList<String>();
            albumResponse.setAlbum(album);
            List<String> postIds = savedPostRepository.findAllPostIdsByAlbumId(album.getId());
            for (String postId : postIds) {
                List<PostContent> postContents = postContentRepository.findAllByPostId(postId).stream().toList();
                representImages.add(postContents.getFirst().getImageId());
            }
            albumResponse.setRepresentImages(representImages);
            albumResponses.add(albumResponse);
        }
        return  albumResponses;
    }
}
