package com.maryamq.instafamous;

import java.util.List;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class InstagramCommentsAdapter extends ArrayAdapter<Comment> {

	public InstagramCommentsAdapter(Context context, List<Comment> comments) {
		super(context, android.R.layout.simple_list_item_1, comments);
		// TODO Auto-generated constructor stub
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		Comment comment = getItem(position);
		if (convertView == null) {
			// load the new view since this is not recycled.
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
		}
		if (comment == null) {
			return convertView;
		}
		
		TextView tvSingleComment = (TextView)convertView.findViewById(R.id.tvSingleComment);
		ImageView ivCommentProfile = (ImageView)convertView.findViewById(R.id.ivCommentProfile);
		
		String commentsHtml = String.format("<b> <span style='color:#6666FF'>%s</span></b>&nbsp;<span>%s</span>", comment.user.userName, comment.text);
		tvSingleComment.setText(Html.fromHtml(commentsHtml));

		Picasso.with(getContext()).load(comment.user.profilePictureUrl).into(ivCommentProfile);
		
		return convertView;
	}
}
