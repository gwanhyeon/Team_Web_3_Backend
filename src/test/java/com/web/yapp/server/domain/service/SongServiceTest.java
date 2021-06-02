package com.web.yapp.server.domain.service;

import com.web.yapp.server.controller.dto.*;
import com.web.yapp.server.domain.Musician;
import com.web.yapp.server.domain.Song;
import com.web.yapp.server.domain.repository.MusicianRepository;
import com.web.yapp.server.domain.repository.SongRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional

public class SongServiceTest {
    @Autowired SongService songService;
    @Autowired SongRepository songRepository;
    @Autowired MusicianService musicianService;
    @Autowired MusicianRepository musicianRepository;
    @Autowired EntityManager entityManager;

    @Test
    public void createSong() throws Exception{
        // given
        MusicianDto musician = new MusicianDto();
        musician.setCareer("Career");
        musician.setNickNm("Name");
        musician.setIntroduction("Introduction");
        musician.setProfileUrl("프로필 URL");

        AtmosphereDto atmosphereDto = new AtmosphereDto();
        atmosphereDto.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"11"})));

        GenreDto genreDto = new GenreDto();
        List<String> genreList = new ArrayList<>();
        genreList.add("1");
        genreDto.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"11"})));

        InstrumentDto instrumentDto = new InstrumentDto();
        List<String> instrumentList = new ArrayList<>();
        instrumentList.add("1");

        ThemeDto themeDto = new ThemeDto();
        themeDto.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"11"})));

        SpecialDto specialDto = new SpecialDto();
        specialDto.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"11"})));

        // when
        Long savedId = musicianService.saveRegister(
                musician,atmosphereDto,genreDto,instrumentDto,themeDto, specialDto);
        Musician musician1 = new Musician();
        musician1.setId(1L);
        Song song = Song.builder()
                .musician(musician1)
                .represent(1)
                .coverUrl("cover")
                .songUrl("song")
                .title("title")
                .build();
        List<MultipartFile> multipartFiles = new LinkedList<>();
        List<Long> songSave = songService.songSave(multipartFiles, 1L);

        // then
        Assertions.assertThat(song.getId()).isEqualTo(1L);
    }

}
