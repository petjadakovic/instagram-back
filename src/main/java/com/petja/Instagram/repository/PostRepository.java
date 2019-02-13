package com.petja.Instagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petja.Instagram.entities.Photo;
import com.petja.Instagram.entities.Post;
import com.petja.Instagram.entities.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	public List<Post> findAllByUserId(long userId);
	public List<Post> findAllByUserFollowers(User user);
	public List<Post> findAllByUserFollowing(User user);
}
