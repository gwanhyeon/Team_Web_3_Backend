package com.web.yapp.server.domain.service;
import com.web.yapp.server.controller.dto.*;
import com.web.yapp.server.domain.*;
import com.web.yapp.server.domain.repository.MusicianRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;
import javax.persistence.EntityManager;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MusicianServiceTest {

    @Autowired MusicianService musicianService;
    @Autowired MusicianRepository musicianRepository;
    @Autowired EntityManager entityManager;

    @Test
    public void createMusician() throws Exception{

        // given
        MusicianDto musicianDto = new MusicianDto();
        musicianDto.setCareer("캐리어");
        musicianDto.setNickNm("이름");
        musicianDto.setIntroduction("소개");
        musicianDto.setProfileUrl("profile url");

        Musician musician = new Musician();
        musician.setId(1L);
        Musician.builder().career("캐리어")
                .nickNm("닉네임")
                .introduction("소개")
                .profileUrl("프로필 URL")
                .build();
        musician.setCareer("캐리어");
        musician.setNickNm("이름");
        musician.setIntroduction("소개");
        musician.setProfileUrl("프로필 URL");

        Map<String,Object> map = new HashMap<>();
        map.put("a","a");
        map.put("b","a");
        map.put("c","a");

        AtmosphereDto mItem = new AtmosphereDto();
        mItem.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"Hammer"})));

        GenreDto mItem1 = new GenreDto();
        mItem1.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"Hammer"})));

        InstrumentDto mItem2 = new InstrumentDto();
        mItem2.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"Hammer"})));

        ThemeDto mItem3 = new ThemeDto();
        mItem3.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"Hammer"})));

        List<AtmosphereDto> atmosphereList = new ArrayList<>();
        List<GenreDto> genreList = new ArrayList<>();
        List<InstrumentDto> instrumentList= new ArrayList<>();
        List<ThemeDto> themeList1 = new ArrayList<>();

        atmosphereList.add(mItem);
        genreList.add(mItem1);
        instrumentList.add(mItem2);
        themeList1.add(mItem3);

        SpecialDto specialDto = new SpecialDto();
        specialDto.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"11"})));

        AtmosphereDto atmosphereDto = new AtmosphereDto();
        atmosphereDto.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"11"})));

        GenreDto genreDto = new GenreDto();
        genreDto.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"11"})));

        InstrumentDto instrumentDto = new InstrumentDto();
        instrumentDto.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"11"})));

        ThemeDto themeDto = new ThemeDto();
        themeDto.setTagNM(new ArrayList<>(Arrays.asList(new String[]{"11"})));

        // when
        Long saveId = musicianService.saveRegister(musicianDto,atmosphereDto,genreDto,instrumentDto,themeDto,specialDto);
        musician = musicianDto.toEntity();
        entityManager.flush();

        Musician one = musicianRepository.findOne(saveId);
        List<Musician> allMusician = musicianRepository.findAllMusician();
        List<Musician> byName = musicianRepository.findByNickNm(musician.getNickNm());

        // then
        assertEquals(musician, musicianRepository.findOne(saveId));
        assertEquals(musician, one);
        assertEquals(musician.getNickNm(), byName.get(0).getNickNm());
        assertEquals(musician,allMusician.get(0));

    }

    @Test(expected = IllegalStateException.class)
    public void validMusician() throws Exception{
        Musician musician  = new Musician();
        musician.setCareer("캐리어");
        musician.setNickNm("이름");
        musician.setIntroduction("소개");
        musician.setProfileUrl("프로필 URL");

        Musician musician1  = new Musician();
        musician1.setCareer("캐리어");
        musician1.setNickNm("이름1");
        musician1.setIntroduction("소개");
        musician1.setProfileUrl("프로필 URL");
    }

    @Test
    public void curation() throws Exception{
        // given
        List<MusicianDto> musicianDtoList = new LinkedList<>();
        List<String> tagList = new LinkedList<>();
        tagList.add("기타");
        tagList.add("힙합");
    }
}

