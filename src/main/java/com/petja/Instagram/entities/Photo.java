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
@Table(name = "photos")
public class Photo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String src;
	@JoinColumn(name="user_id")
	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER)
	private User user;
	private Date date;
	
	public Photo() {
		date = new Date();
	}

	public Photo(long id, String src, User user, Date date) {
		super();
		this.id = id;
		this.src = src;
		this.user = user;
		this.date = date;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
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

	@Override
	public String toString() {
		return "Photo [id=" + id + ", src=" + src + ", user_id=" + user.getUsername() + ", date=" + date + "]";
	}
	
	
	
}
