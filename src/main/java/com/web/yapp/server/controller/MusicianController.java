package com.web.yapp.server.controller;

import com.web.yapp.server.controller.dto.*;
//import com.web.yapp.server.controller.dto.SessionUserDto;
import com.web.yapp.server.domain.Role;
import com.web.yapp.server.domain.User;
import com.web.yapp.server.domain.repository.UserRepository;
import com.web.yapp.server.domain.repository.UserRoleRepository;
import com.web.yapp.server.domain.service.MusicianService;
import com.web.yapp.server.domain.service.MusicianTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
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
//    private final HttpServletRequest request;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    /**
     * 뮤지션 생성 페이지 이동 컨트롤러
     * @param model
     * @return
     */
    @RequestMapping(value = "/musicians/new", method=RequestMethod.POST)
    public String createMusicianPage(Model model){
        model.addAttribute("memberForm", new MusicianDto());
        return "createMusicianPage";
    }

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

    /**
     * 뮤지션 닉네임값 조회
     * @param nickNm
     * @return
     */
    @RequestMapping(value = "/musicians/v2/{nickNm}", method=RequestMethod.GET)
    public Map<String,Object> getMusicianNickNmInfo(@PathVariable("nickNm") String nickNm){
        List<MusicianDto> musicianListNickNmInfo = new ArrayList<>();
        Map<String,Object> musicianList = new HashMap<String,Object>();
        musicianListNickNmInfo = musicianService.findByNickNmMusician(nickNm);
        musicianList.put("musicianListNickNmInfo",musicianListNickNmInfo);
        return musicianList;
    }


    /**
     *
     * @return
     */
    @RequestMapping(value = "/musicians", method=RequestMethod.POST)
    public List<Map<String, Object>> createMusician(
                                                    Model model,
                                                    HttpSession session,

                                                    /*Valid MusicianDto musicianDto,
                                                    BindingResult result,
                                                    */
                                                    @RequestBody HashMap<String,Object> paramMap
                                                    /*@RequestBody(required = false) List<String> atmoList,
                                                    @RequestBody(required = false) List<String> genreList,
                                                    @RequestBody(required = false) List<String> instruList,
                                                    @RequestBody(required = false) List<String> themeList,
                                                    @RequestBody(required = false) List<String> spclNoteList*/
    ){
        List<Map<String,Object>> resultMapList = new ArrayList<>();
        Map<String,Object> resultMap = new HashMap<>();
        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");
        String accessToken = (String) httpSession.getAttribute("accessToken");
        //Long musicianId = musicianService.saveRegister(musicianDto,atmoList,genreList,instruList,themeList,spclNoteList);
        System.out.println(paramMap);

       // if(musicianId != null){
        resultMap.put("success", "1");
        String userEmail = user.getEmail();
        User user_role = userRoleRepository.findByEmail(userEmail);
        user_role.setRole(Role.MUSICIAN);
        userRepository.save(user_role);

//                user.setRole(Role.MUSICIAN);
//            String email = user.getEmail();
//            userRepository.save(user);
//            user.setRole(Role.MUSICIAN);
//            httpSession.setAttribute("user", new SessionUserDto(user)); //세션에 사용자 정보 저장

      /*  }else {
            paramMap.put("success", "0");
        }*/
        // 로그인정보가 Null이 아닐경우만 로직처리
        //        if(user != null){
        //
        //        }
        //
        //        // @Vaild 체크이후 올바르지 않는 값이 있다면 result에 담겨져 있음.
        //        if(result.hasErrors()){
        //            return "musicians/createMusicianPage";      // 페이지 실패처리
        //        }
        /* 파일업로드 Save + 뮤지션 id 값 */
        /* 카테고리 별 Save + 뮤지션 id 값 */
        resultMapList.add(resultMap);
        return resultMapList;
    }

    /**
     *
     * @param atmoList
     * @param genreList
     * @param instruList
     * @param themeList
     * @return
     */
    @GetMapping("/musicians/curation") //뮤지션 + 작업태그 돌려주기
    public Map<String,Object> musicianCuration(@RequestParam List<String> atmoList, @RequestParam List<String> genreList,
                                               @RequestParam List<String> instruList, @RequestParam List<String> themeList) {

        return musicianService.musicianCuration(atmoList, genreList, instruList, themeList);
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



    /**
     * 뮤지션 리스너들의 선택
     *
     */

//    @RequestMapping(value = "/musicians/choice", method=RequestMethod.GET)
//    public List<Object> getMusicianChoice(){
//
//        List<Object> musicianChoiceInfoMap = new ArrayList<>();
//
//        musicianChoiceInfoMap = musicianService.findMusicianByChoice();
//        return musicianChoiceInfoMap;
//    }

    /**
     * 등장 새로운 뮤지션
     *
     */

//    @RequestMapping(value = "/musicians/new", method=RequestMethod.GET)
//    public List<Object> getMusicianNew(){
//        List<Object> musicianChoiceInfoMap = new ArrayList<>();
//        musicianChoiceInfoMap = musicianService.findMusicianByNew();
//        return musicianChoiceInfoMap;
//    }
}