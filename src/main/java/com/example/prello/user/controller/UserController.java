package com.example.prello.user.controller;

import com.example.prello.common.SessionName;
import com.example.prello.user.dto.DeleteRequestDto;
import com.example.prello.user.dto.LoginRequestDto;
import com.example.prello.user.dto.UserResponseDto;
import com.example.prello.user.entity.User;
import com.example.prello.user.service.UserService;
import com.example.prello.user.dto.SignUpRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(
            @Valid @RequestBody SignUpRequestDto requestDto
    ){
        UserResponseDto responseDto = userService.signUp(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequestDto requestDto,
            HttpServletRequest request
    ){
        User loginUser = userService.login(requestDto);
        HttpSession session = request.getSession();
        session.setAttribute(SessionName.USER_ID, loginUser.getId());
        session.setAttribute(SessionName.USER_AUTH, loginUser.getAuth());

        return new ResponseEntity<>("로그인을 성공했습니다.", HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request
    ){
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return new ResponseEntity<>("로그아웃을 성공했습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<String> delete(
            @SessionAttribute("userId") Long userId,
            @Valid @RequestBody DeleteRequestDto requestDto,
            HttpServletRequest request
    ){
        userService.delete(userId, requestDto);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity<>("탈퇴가 완료되었습니다.", HttpStatus.OK);
    }
}