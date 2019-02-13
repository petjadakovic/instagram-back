package com.petja.Instagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petja.Instagram.entities.Photo;
import com.petja.Instagram.entities.Post;
import com.petja.Instagram.entities.User;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
	public List<Post> findByUserId(long userId);
}
