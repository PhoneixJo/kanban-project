package com.example.prello.user.service;

import com.example.prello.common.SessionName;
import com.example.prello.exception.UserErrorCode;
import com.example.prello.member.repository.MemberRepository;
import com.example.prello.user.dto.DeleteRequestDto;
import com.example.prello.user.dto.LoginRequestDto;
import com.example.prello.user.dto.SignUpRequestDto;
import com.example.prello.user.dto.UserResponseDto;
import com.example.prello.user.entity.User;
import com.example.prello.user.repository.UserRepository;
import com.example.prello.common.PasswordEncoder;
import com.example.prello.workspace.dto.WorkspacePermissionDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final HttpSession session;
    private final MemberRepository memberRepository;

    public UserResponseDto signUp(SignUpRequestDto requestDto) {

        Optional<User> findUser = userRepository.findByEmail(requestDto.getEmail());
        if(findUser.isPresent()) {
            if (findUser.get().getDeletedAt() != null) {
                throw new IllegalArgumentException(UserErrorCode.DELETED_ACCOUNT.getMessage());
            }
            throw new IllegalArgumentException(UserErrorCode.DUPLICATED_EMAIL.getMessage());
        }

        String encodedPassword = PasswordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getEmail(), requestDto.getName(), encodedPassword, requestDto.getAuth());
        User savedUser = userRepository.save(user);
        return UserResponseDto.toDto(savedUser);
    }

    public User login(LoginRequestDto requestDto) {
        User findUser = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(()-> new IllegalArgumentException(UserErrorCode.NOT_FOUND_USER.getMessage()));

        if(findUser.getDeletedAt()!=null) {
            throw new IllegalArgumentException(UserErrorCode.DELETED_ACCOUNT.getMessage());
        }
        if(!PasswordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new IllegalArgumentException(UserErrorCode.INVALID_PASSWORD.getMessage());
        }

        session.setAttribute(SessionName.USER_ID, findUser.getId());
        session.setAttribute(SessionName.USER_AUTH, findUser.getAuth());

        // todo member서비스로 가져올 경우 순환참조 문제 발생
        List<WorkspacePermissionDto> permissions = memberRepository.findWorkspacePermissionsByUserId(findUser.getId());
        session.setAttribute(SessionName.WORKSPACE_PERMIT, permissions);

        return findUser;
    }

    public void delete(Long userId, DeleteRequestDto requestDto) {
        User findUser = findByIdOrElseThrow(userId);

        if(!PasswordEncoder.matches(requestDto.getPassword(), findUser.getPassword())){
            throw new IllegalArgumentException(UserErrorCode.INVALID_PASSWORD.getMessage());
        }

        findUser.deleteSoftly();
        userRepository.save(findUser);
    }

    public User findByIdOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public User findUserByEmailOrElseThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 사용자를 찾을 수 없습니다: " + email));
    }
}