package com.web.yapp.server.controller;


import com.web.yapp.server.controller.dto.MusicianCardResponseDto;
import com.web.yapp.server.controller.dto.SessionUserDto;
import com.web.yapp.server.domain.repository.MusicianRepository;
import com.web.yapp.server.domain.service.MusicianService;
import com.web.yapp.server.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TUNA MAIN 컨트롤러
 */
@CrossOrigin("*")
@SessionAttributes
@RequiredArgsConstructor
@RestController
@Slf4j
public class MainController {
    private final static int expired = 3600;
    private final HttpSession httpSession;
    private final UserService userService;
    private final MusicianService musicianService;
    private final MusicianRepository musicianRepository;
    private final HttpServletResponse httpServletResponse;

    @GetMapping("/")
    public String home(Model model, HttpSession session, HttpServletRequest request) {
        List<HashMap<String, Object>> accessTokenList = new ArrayList<>();
        HashMap<String, Object> resultMap = new HashMap<>();
        cookiesSetting(session);
        return "redirect:" + request.getRequestURL();
    }

    private void cookiesSetting(HttpSession session) {
        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");
        String accessToken = (String) httpSession.getAttribute("accessToken");
        session.setAttribute("login", "locallogin");
        session.setAttribute("user", user);
        httpSession.setAttribute("user", user);
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setMaxAge(expired);
        httpServletResponse.addCookie(cookie);
    }

    @PostMapping("/authToken")
    public @ResponseBody
    HashMap<String, Object> authToken(@RequestBody String reqAccessToken) {       // 모델에 유저 정보
        HashMap<String, Object> resultMap = new HashMap<>();
        if(reqAccessToken != "" || reqAccessToken != null){
            SessionUserDto sessionUserDto = (SessionUserDto) httpSession.getAttribute("user");
            String sessionUserRole = sessionUserDto.getRole().getKey().toString();
            String accessToken = (String) httpSession.getAttribute("accessToken");
            Long userId = null;
            if ("ROLE_USER".equals(String.valueOf(sessionUserRole))){
                resultMap.put("isMusician", false);
            } else {
                resultMap.put("isMusician", true);
            }
            if (sessionUserDto != null) {
                userId = userService.findUserIdByEmail(sessionUserDto.getEmail());
                resultMap.put("success", "1");
                resultMap.put("userId", String.valueOf(userId));
                resultMap.put("user", sessionUserDto);
                resultMap.put("accessToken", accessToken);
            }
        }
        else {
            resultMap.put("success", "0");
        }
        log.info("role:"+resultMap.get("isMusician").toString());
        return resultMap;
    }

    @GetMapping("/main")
    public Map<String, Object> main() {
        return musicianService.getMainResponse();
    }

    // 카테고리
    @GetMapping(value="/categorys")
    public HashMap<String, Object> categorySelect(@RequestParam String categoryNm) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        List<MusicianCardResponseDto> musicianList;
        try{
            musicianList = musicianService.findMusicianBySearch(categoryNm);
            if(musicianList == null){
                resultMap.put("success", "0");
            }else {
                resultMap.put("success", "1");
                resultMap.put("categoryList", musicianList);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

}
