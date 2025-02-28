package com.eihabitat.eihabitat_server.mapper;

import com.eihabitat.eihabitat_server.dto.request.AlbumReq;
import com.eihabitat.eihabitat_server.entity.Album;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
    Album toAlbum (AlbumReq albumReq);
}
