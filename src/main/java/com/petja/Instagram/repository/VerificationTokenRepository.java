package com.petja.Instagram.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petja.Instagram.entities.User;
import com.petja.Instagram.entities.VerificationToken;

@Repository
public interface VerificationTokenRepository extends  JpaRepository<VerificationToken, Long> {
	VerificationToken findByUser(User user);
	VerificationToken findByToken(String token);
}
