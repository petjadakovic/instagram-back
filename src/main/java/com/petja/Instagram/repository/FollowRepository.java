package com.petja.Instagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petja.Instagram.entities.Follow;
import com.petja.Instagram.entities.Photo;
import com.petja.Instagram.entities.User;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
	public List<Follow> findByUser1Id(long userId);
	public List<Follow> findByUser2Id(long userId);
	public Follow findByUser1IdAndUser2Id(long user1Id, long user2Id);
}
