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

@Entity
@Table(name = "likes")
public class Like {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@JoinColumn(name="post_id")
	@OneToOne(targetEntity=Post.class, fetch=FetchType.EAGER)
	private Post post;
	@JoinColumn(name="user_id")
	@OneToOne(targetEntity=User.class, fetch=FetchType.EAGER)
	private User user;
	private Date date;
	public Like(long id, Post post_id, User user, Date date) {
		super();
		this.id = id;
		this.post = post_id;
		this.user = user;
		this.date = date;
	}
	
	public Like() {
		date = new Date();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public User getUser() {
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
