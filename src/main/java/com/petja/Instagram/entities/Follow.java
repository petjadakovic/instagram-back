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
@Table(name = "follows")
public class Follow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER)
	private User user1;
	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER)
	private User user2;
	private Date date;
	
	public Follow() {
		date = new Date();
	}

	public Follow(long id, User user1, User user2, Date date) {
		super();
		this.id = id;
		this.user1 = user1;
		this.user2 = user2;
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Follow [id=" + id + ", user1=" + user1 + ", user2=" + user2 + ", date=" + date + "]";
	}

	
	
	
}
