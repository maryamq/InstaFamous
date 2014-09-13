package com.maryamq.instafamous;

import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {

	public InstagramPhotoAdapter(Context context, List<InstagramPhoto> photos) {
		super(context, android.R.layout.simple_list_item_1, photos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InstagramPhoto photo = getItem(position);
		if (convertView == null) {
			// load the new view since this is not recycled.
			convertView = LayoutInflater.from(getContext())
					.inflate(R.layout.item_photo, parent, false /*
																 * do not attach
																 * yet
																 */);
		}
		TextView tvCaption = (TextView) convertView
				.findViewById(R.id.tvCaption);
		ImageView imgPhoto = (ImageView) convertView
				.findViewById(R.id.imgPhoto);
		tvCaption.setText(photo.caption);
		imgPhoto.getLayoutParams().height = photo.imageHeight;
		imgPhoto.setImageResource(0); // clear out image until image is
										// successfully loaded.
		Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);

		return convertView;

	}
}
