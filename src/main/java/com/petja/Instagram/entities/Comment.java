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
@Table(name = "comments")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@JoinColumn(name="post_id")
	@ManyToOne(targetEntity=Post.class, fetch=FetchType.EAGER)
	private Post post_id;
	@JoinColumn(name="user_id")
	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER)
	private User user;
	private Date date;
	private String text;
	
	
	
	public Comment() {
		super();
		date = new Date();
	}
	
	
	public Comment(long id, Post post_id, User user, Date date, String text) {
		super();
		this.id = id;
		this.post_id = post_id;
		this.user = user;
		this.date = date;
		this.text = text;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Post getPost_id() {
		return post_id;
	}
	public void setPost_id(Post post_id) {
		this.post_id = post_id;
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
