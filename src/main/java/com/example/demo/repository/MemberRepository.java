package com.example.demo.repository;

import com.example.demo.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, String> {
    Optional<Member> findByUsername(String username);
}
