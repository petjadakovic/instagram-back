package com.petja.Instagram.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petja.Instagram.entities.Photo;
import com.petja.Instagram.entities.User;

@Repository
public interface UserRepository extends  JpaRepository<User, Long> {
	User findById(long id);
	User findByUsername(String username);
	User findByEmail(String email);
	List<User> findByUsernameLikeAndEnabled(String username,boolean enabled, Pageable pageable);
}
