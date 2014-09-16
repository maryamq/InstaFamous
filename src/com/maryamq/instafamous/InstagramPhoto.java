package com.maryamq.instafamous;

import java.util.ArrayList;
import java.util.List;

public class InstagramPhoto {
	public User user;
	public String caption = "";
	public int imageHeight;
	public int likesCount;
	public long creationTime;
	public String imageUrl;
	public String location = "";
	public List<Comment> comments = new ArrayList<Comment>();
	protected int commentsCount;

}

class Comment {
	public User user;
	public String text;
	public long createdTime;
}
class User {
	String userName;
	String profilePictureUrl;
}
