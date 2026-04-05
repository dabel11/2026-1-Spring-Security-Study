package com.gdghongik.springsecurity.domain.member.service;

import com.gdghongik.springsecurity.domain.member.dto.MemberCreateRequest;
import com.gdghongik.springsecurity.domain.member.dto.MemberInfoResponse;
import com.gdghongik.springsecurity.domain.member.dto.MemberUpdateRequest;
import com.gdghongik.springsecurity.domain.member.entity.Member;
import com.gdghongik.springsecurity.domain.member.entity.MemberRole;
import com.gdghongik.springsecurity.domain.member.repository.MemberRepository;
import com.gdghongik.springsecurity.global.exception.CustomException;
import com.gdghongik.springsecurity.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gdghongik.springsecurity.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MemberInfoResponse getMyInfo(Long userId) {
        Member member = memberRepository.findById(userId).
                orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        return new MemberInfoResponse(member);
    }

    @Transactional
    public void createMember(MemberCreateRequest request) {

        boolean isDuplicate = memberRepository.existsByUsername(request.getUsername());

        // 중복되는 유저네임이 있으면 에러
        if (isDuplicate) {
            throw new CustomException(MEMBER_USERNAME_DUPLICATE);
        }

        String encoded = passwordEncoder.encode(request.getPassword());
        Member member = new Member(request.getUsername(), encoded, MemberRole.REGULAR);
        memberRepository.save(member);

        // 멤버를 저장한다
    }

    @Transactional(readOnly = true)
    public List<MemberInfoResponse> getMembers() {
        // 모든 멤버를 가져온다
        List<MemberInfoResponse> list = new ArrayList<>();

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            MemberInfoResponse memberInfoResponse = new MemberInfoResponse(member);
            list.add(memberInfoResponse);
        }
        return list;


    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberByUsername(String username) {
        // 해당하는 멤버를 가져온다. 없으면 에러
        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        return new MemberInfoResponse(member);
    }

    @Transactional
    public void updateMember(Long memberId, MemberUpdateRequest request) {
        // 해당하는 멤버를 가져온다. 없으면 에러
        Member member = memberRepository.findById(memberId).
                orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));


        // 해당하는 멤버 정보를 갱신한다
        member.updateUsername(request.getUsername());
    }

    @Transactional
    public void deleteMember(Long memberId) {
        // 해당하는 멤버를 가져온다. 없으면 에러
        Member member = memberRepository.findById(memberId).
                orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        // 해당 멤버를 삭제한다
        memberRepository.delete(member);

    }
}
