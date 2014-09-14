package com.maryamq.instafamous;

import java.util.ArrayList;
import java.util.List;

public class InstagramPhoto {
	public User user;
	public String caption = "";
	public int imageHeight;
	public int likesCount;
	public String imageUrl;
	public String location = "";
	public List<Comment> comments = new ArrayList<Comment>();

}


class Comment {
	public User user;
	public String comment;
}
class User {
	String userName;
	String profilePictureUrl;
}
