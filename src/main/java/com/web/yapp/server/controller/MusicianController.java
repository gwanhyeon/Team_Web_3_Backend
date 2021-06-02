package com.web.yapp.server.controller;

import com.web.yapp.server.controller.dto.*;
import com.web.yapp.server.domain.Role;
import com.web.yapp.server.domain.User;
import com.web.yapp.server.domain.repository.UserClassRepository;
import com.web.yapp.server.domain.repository.UserRoleRepository;
import com.web.yapp.server.domain.service.MusicianService;
import com.web.yapp.server.domain.service.MusicianTagService;
import com.web.yapp.server.domain.service.SongService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class MusicianController {
    private final HttpSession httpSession;
    private final MusicianTagService musicianTagService;
    private final MusicianService musicianService;
    private final UserClassRepository userClassRepository;
    private final UserRoleRepository userRoleRepository;
    private final SongService songService;

    /**
     *  뮤지션 값 전체조회
     *
     * @return
     */
    @RequestMapping(value = "/musicians", method=RequestMethod.GET)
    public Map<String,Object> getMusicianAllInfo(){
        List<MusicianDto> musicianListAllInfo = new ArrayList<>();
        Map<String,Object> musicianList = new HashMap<String,Object>();
        musicianListAllInfo = musicianService.findAllMusician();
        musicianList.put("musicianListAllInfo", musicianListAllInfo);
        return musicianList;

    }

    // 뮤지션 상세프로필과 마이페이지 별도로 API 필요
    /**
     * 뮤지션 id값 조회
     * @param id
     *
     * @returnS
     */
    @RequestMapping(value = "/musicians/v1/{id}", method=RequestMethod.GET)
    public Map<String,Object> getMusicianIdInfo(@PathVariable("id") Long id){
        MusicianDto musicianListIdInfo = new MusicianDto();
        Map<String,Object> musicianList = new HashMap<String,Object>();
        musicianListIdInfo = musicianService.findByIdMusician(id);
        musicianList.put("musicianListIdInfo", musicianListIdInfo);
        return musicianList;
    }

    @GetMapping("/musician/detail/{musicianId}")
    public HashMap<String,Object> getMusicianDetail(@PathVariable Long musicianId){

        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        MusicianDto musicianDto = musicianService.findByIdMusician(musicianId);
        List<SongDto> songList= songService.findSongByMusicianId(musicianId);
        resultMap.put("musicianList", musicianDto);
        resultMap.put("songList", songList);
        return resultMap;
    }

    // 뮤지션 저장부분 따로
    /**
     *
     * @return
     */
    @PostMapping(value = "/musicians",consumes = "multipart/form-data")
    public List<Map<String, Object>> createMusician(HttpSession session,
                                                    @RequestBody MusicianDto musicianDto){
        AtmosphereDto atmosphereList = musicianDto.getAtmosphereList();
        GenreDto genreList = musicianDto.getGenreList();
        InstrumentDto instrumentList = musicianDto.getInstrumentList();
        ThemeDto themeList = musicianDto.getThemeList();
        SpecialDto specialList = musicianDto.getSpecialList();
        List<MultipartFile> multipartFiles = musicianDto.getMultipartFiles();

        // 세션 처리
        List<Map<String,Object>> resultMapList = new ArrayList<>();
        Map<String,Object> resultMap = new HashMap<>();

        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");
        String accessToken = (String) httpSession.getAttribute("accessToken");
        try {
            Long musicianId = musicianService.saveRegister(musicianDto,atmosphereList,genreList,instrumentList,themeList,specialList);           // 뮤지션 id값 채번
            songService.songSave(multipartFiles, musicianId);

            if(musicianId != null){
                resultMap.put("success", "1");
                resultMap.put("musicianId", musicianId);
                if(user != null){
                    String userEmail = user.getEmail();
                    User user_role = userRoleRepository.findByEmail(userEmail);
                    user_role.setRole(Role.MUSICIAN);
                }
            }else {
                resultMap.put("success","0");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultMapList.add(resultMap);
        return resultMapList;
    }

    /**
     *
     * @param curationReqDto
     * @return
     */
    @PostMapping("/musicians/curation")
    public Map<String,Object> musicianCuration(@RequestBody CurationReqDto curationReqDto) {
        return musicianService.musicianCuration(curationReqDto);
    }

    /**
     * 뮤지션이 가진 태그 조회
     * @param id
     * @return
     */
    @RequestMapping(value = "/musicians/tag/{id}", method=RequestMethod.GET)
    public Map<String, Object> getTagsByMusician(@PathVariable("id") Long id){
        return musicianTagService.findTagByMusician(id);
    }
}