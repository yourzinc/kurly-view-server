package com.kurly.kurlyview.repository;

import com.kurly.kurlyview.domain.member.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {
    Optional<Member> findById(@Param("id") String id);
    Optional<Member> findByEmail(String email);
}
