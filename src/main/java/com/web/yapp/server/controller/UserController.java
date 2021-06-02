package com.web.yapp.server.controller;

//import com.web.yapp.server.controller.dto.OAuthAttributesDto;
//import com.web.yapp.server.controller.dto.SessionUserDto;

import com.web.yapp.server.controller.dto.SessionUserDto;
import com.web.yapp.server.domain.User;
import com.web.yapp.server.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@Controller
@RequiredArgsConstructor
public class UserController {
    private final HttpSession httpSession;
    private final UserService userService;

    @GetMapping("/user/{userId}")
    public Map<String,Object> getUser(@PathVariable Long userId){
        HashMap<String,Object> map = new HashMap<String,Object>();
        User user = userService.findUserById(userId);
        SessionUserDto sessionUserDto = new SessionUserDto(user);
        map.put("user",sessionUserDto);
        return map;
    }


}
