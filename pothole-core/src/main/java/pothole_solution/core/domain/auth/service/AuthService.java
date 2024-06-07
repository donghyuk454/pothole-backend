package pothole_solution.core.domain.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import pothole_solution.core.domain.member.dto.MemberBaseResponseDto;
import pothole_solution.core.domain.auth.dto.AuthJoinRequestDto;
import pothole_solution.core.domain.auth.dto.AuthLoginRequestDto;

public interface AuthService {
    MemberBaseResponseDto join(AuthJoinRequestDto requestDto);
    MemberBaseResponseDto login(AuthLoginRequestDto requestDto, HttpServletRequest httpServletRequest);
    MemberBaseResponseDto logout(Long memberId, HttpServletRequest httpServletRequest);
}
