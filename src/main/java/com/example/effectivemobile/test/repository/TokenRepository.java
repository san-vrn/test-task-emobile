package com.example.effectivemobile.test.repository;

import com.example.effectivemobile.test.entity.user.User;
import com.example.effectivemobile.test.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String jwt);

    List<Token> findAllValidTokenByUser(User user);
}
