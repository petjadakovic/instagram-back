package com.petja.Instagram.entities;

import java.util.List;

public class UserInfo {
	public String username;
	public long id;
	public long profile_picture_id;
	public int follow_count;
	public int following_count;
	public List<Post> posts;
	public boolean following_him;
	public boolean following_me;
	public boolean is_me;
}
