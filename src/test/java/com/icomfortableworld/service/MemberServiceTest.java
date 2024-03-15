package com.icomfortableworld.service;

import com.icomfortableworld.domain.follow.repository.FollowRepository;
import com.icomfortableworld.domain.member.dto.request.LoginRequestDto;
import com.icomfortableworld.domain.member.dto.request.MemberUpdateRequestDto;
import com.icomfortableworld.domain.member.dto.request.SignupRequestDto;
import com.icomfortableworld.domain.member.dto.response.LoginResponseDto;
import com.icomfortableworld.domain.member.dto.response.MemberResponseDto;
import com.icomfortableworld.domain.member.dto.response.MemberUpdateResponseDto;
import com.icomfortableworld.domain.member.entity.Member;
import com.icomfortableworld.domain.member.entity.MemberRoleEnum;
import com.icomfortableworld.domain.member.entity.PasswordHistory;
import com.icomfortableworld.domain.member.model.MemberModel;
import com.icomfortableworld.domain.member.repository.MemberRepository;
import com.icomfortableworld.domain.member.repository.PasswordHistoryRepository;
import com.icomfortableworld.domain.member.service.MemberService;
import com.icomfortableworld.domain.member.service.MemberServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import com.icomfortableworld.domain.message.repository.MessageJpaRepository;
import com.icomfortableworld.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordHistoryRepository passwordHistoryRepository;
    @Mock
    private FollowRepository followRepository;
    @Mock
    private MessageJpaRepository messageJpaRepository;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private PasswordEncoder passwordEncoder;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberServiceImpl(memberRepository, passwordHistoryRepository, passwordEncoder, followRepository, messageJpaRepository, jwtProvider);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signup() {
        //given
        SignupRequestDto signupRequestDto = new SignupRequestDto("testuser1", "testuser1@naver.com", "testuser1", "password123!", "info1", false, "");
        MemberModel memberModel = new MemberModel();
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(memberModel);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        //when
        memberService.signup(signupRequestDto);

        //then
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(passwordEncoder, times(1)).encode(signupRequestDto.getPassword());
        verify(passwordHistoryRepository, times(1)).save(any(PasswordHistory.class));
    }

    @Test
    @DisplayName("로그인 테스트")
    void login() {
        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto("testuser1", "password123!");
        MemberModel memberModel = new MemberModel();
        memberModel.setUsername("testuser1");
        memberModel.setPassword("encodedPassword");
        memberModel.setMemberRoleEnum(MemberRoleEnum.USER);
        when(memberRepository.findByUsernameOrElseThrow(anyString())).thenReturn(memberModel);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtProvider.createToken(anyString(), any(MemberRoleEnum.class))).thenReturn("generatedToken");

        //when
        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto);

        //then
        verify(memberRepository, times(1)).findByUsernameOrElseThrow(loginRequestDto.getUsername());
        verify(passwordEncoder, times(1)).matches(loginRequestDto.getPassword(), memberModel.getPassword());
        verify(jwtProvider, times(1)).createToken(memberModel.getUsername(), memberModel.getMemberRoleEnum());

        assertThat(loginResponseDto.getUsername()).isEqualTo(memberModel.getUsername());
        assertThat(loginResponseDto.getMemberRoleEnum()).isEqualTo(memberModel.getMemberRoleEnum());
        assertThat(loginResponseDto.getToken()).isEqualTo("generatedToken");
    }

    @Test
    @DisplayName("회원 조회")
    void getMemeber() {
        //given
        Long memberId = 1L;
        MemberModel memberModel = new MemberModel();
        when(memberRepository.findByIdOrElseThrow(memberId)).thenReturn(memberModel);
        when(followRepository.findByFromId(memberId)).thenReturn(List.of());

        //when
        MemberResponseDto memberResponseDto = memberService.getMemeber(memberId);

        //then
        verify(memberRepository, times(1)).findByIdOrElseThrow(memberId);
        verify(followRepository, times(1)).findByFromId(memberId);

        assertThat(memberResponseDto.getUsername()).isEqualTo(memberModel.getUsername());
        assertThat(memberResponseDto.getNickname()).isEqualTo(memberModel.getNickname());
        assertThat(memberResponseDto.getIntroduction()).isEqualTo(memberModel.getIntroduction());
    }

    @Test
    @DisplayName("회원 업데이트")
    void updateMember() {
        // Given
        Long memberId = 1L;
        MemberUpdateRequestDto memberUpdateRequestDto = new MemberUpdateRequestDto("newNickname", "newIntroduction", "oldPassword", "newPassword");
        MemberModel memberModel = new MemberModel();
        memberModel.setPassword("encodedOldPassword");
        when(memberRepository.findByIdOrElseThrow(memberId)).thenReturn(memberModel);
        when(passwordEncoder.matches(memberUpdateRequestDto.getPassword(), memberModel.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(memberUpdateRequestDto.getNewPassword(), memberModel.getPassword())).thenReturn(false);
        when(memberRepository.updateMember(anyLong(), anyString(), anyString(), anyString())).thenReturn(memberModel);

        // When
        MemberUpdateResponseDto memberUpdateResponseDto = memberService.updateMember(memberId, memberUpdateRequestDto);

        // Then
        verify(memberRepository, times(1)).findByIdOrElseThrow(memberId);
        verify(passwordEncoder, times(1)).matches(memberUpdateRequestDto.getPassword(), memberModel.getPassword());
        verify(passwordEncoder, times(1)).matches(memberUpdateRequestDto.getNewPassword(), memberModel.getPassword());
        verify(memberRepository, times(1)).updateMember(memberId, memberUpdateRequestDto.getNickname(), memberUpdateRequestDto.getIntroduction(), memberUpdateRequestDto.getNewPassword());

        assertThat(memberUpdateResponseDto.getNickname()).isEqualTo(memberUpdateRequestDto.getNickname());
        assertThat(memberUpdateResponseDto.getIntroduction()).isEqualTo(memberUpdateRequestDto.getIntroduction());
    }
}