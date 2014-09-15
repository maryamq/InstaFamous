package com.maryamq.instafamous;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.widget.ListView;

public class CommentsDialog {

    public static Dialog getDialog(Context context, List<Comment> comments) {
		Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_comments);
		dialog.setTitle("Comments");
		// Populate view
	
    	InstagramCommentsAdapter commentsAdapter = new InstagramCommentsAdapter(dialog.getContext(), comments);
		ListView lvComments = (ListView) dialog.findViewById(R.id.lvComments);
		lvComments.setAdapter(commentsAdapter);
		return dialog;
    }
}