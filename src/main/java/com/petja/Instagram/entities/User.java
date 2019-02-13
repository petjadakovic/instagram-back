package com.petja.Instagram.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(unique=true)
	private String username;
	private String email;
	private String password;
	private boolean enabled;
	private long profile_picture_id;
//	@JoinColumn(name="user_id")
//	@OneToMany(targetEntity=Post.class, fetch=FetchType.EAGER)
//	List<Post> posts;
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST})
	 @JoinTable(name = "follows",
     joinColumns = { @JoinColumn(name = "user1_id") },
     inverseJoinColumns = { @JoinColumn(name = "user2_id") })
	List<User> followers;
	
	@ManyToMany(mappedBy="followers",fetch=FetchType.LAZY,cascade = {CascadeType.PERSIST})
	List<User> following;
	
	public User() {
		this.enabled = false;
	}

	public User(long id, String username, String email, String password, boolean enabled, long profile_picture_id) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.profile_picture_id = profile_picture_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getProfile_picture_id() {
		return profile_picture_id;
	}

	public void setProfile_picture_id(long profile_picture_id) {
		this.profile_picture_id = profile_picture_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", enabled=" + enabled + ", profile_picture_id=" + profile_picture_id + "]";
	}

	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}
	
	
	
}
