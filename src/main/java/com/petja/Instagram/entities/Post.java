package com.petja.Instagram.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.transaction.Transactional;

@Entity
@Table(name = "posts")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@JoinColumn(name="photo_id")
	@OneToOne(targetEntity=Photo.class, fetch=FetchType.EAGER)
	private Photo photo;
	@JoinColumn(name="user_id")
	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER)
	private User user;
	private Date date;
	
	public Post(long id, Photo photo, User user, Date date) {
		super();
		this.id = id;
		this.photo = photo;
		this.user = user;
		this.date = date;
	}
	
	public Post() {
		date = new Date();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Photo getPhoto() {
		photo.setUser(null);
		return photo;
	}
	
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	public User getUser() {
		user.followers = null;
		user.following = null;
		user.setPassword(null);
		user.setEmail(null);
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
