package com.kurly.kurlyview.repository;

import com.kurly.kurlyview.domain.member.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, String> {
    Optional<Member> findByEmail(String email);
}
