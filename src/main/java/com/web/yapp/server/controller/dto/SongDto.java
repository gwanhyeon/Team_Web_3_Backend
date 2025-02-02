package com.web.yapp.server.controller.dto;

import com.web.yapp.server.domain.Song;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class SongDto {
    private String title;
    private String coverUrl;
    private String songUrl;
    private int represent;
    private MultipartFile multipartFile;

    public Song toEntity(){
        return Song.builder()
                .title(title)
                .coverUrl(coverUrl)
                .songUrl(songUrl)
                .represent(represent)
                .build();
    }

    public SongDto(Song Entity){
        this.title = Entity.getTitle();
        this.coverUrl = Entity.getCoverUrl();
        this.songUrl = Entity.getSongUrl();
        this.represent = Entity.getRepresent();
    }

}
