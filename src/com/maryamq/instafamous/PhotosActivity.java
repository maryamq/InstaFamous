package com.maryamq.instafamous;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PhotosActivity extends Activity {
	static final String CLIENT_ID = "e76164196b7e4a7a81c4c59962d5bf1b";
	static final String POP_URL = "https://api.instagram.com/v1/media/popular?client_id="
			+ CLIENT_ID;
	static final String LOCATION_URL = "https://api.instagram.com/v1/locations/search?lat=%s&lng=%s&access_token="
			+ CLIENT_ID;
	private ArrayList<InstagramPhoto> photos;
	private InstagramPhotoAdapter aPhotos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_photo_list);
		photos = new ArrayList<InstagramPhoto>();
		this.setupSwipe();
		fetchPopularPhotos();
	}

	private void setupSwipe() {
		SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		// Setup refresh listener which triggers new data loading
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				fetchPopularPhotos();
			}
		});
		// Configure the refreshing colors
		swipeContainer.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
	}

	private String getStringIfNotNull(JSONObject json, String key,
			String defaultIfNull) throws JSONException {
		return json != null && json.has(key) && json.getString(key) != null ? json
				.getString(key) : defaultIfNull;
	}

	private int getIntIfNotNull(JSONObject json, String key, int defaultIfNull)
			throws JSONException {
		return json != null && json.has(key) ? json.getInt(key) : defaultIfNull;
	}

	private long getLongIfNotNull(JSONObject json, String key, long defaultIfNull)
			throws JSONException {
		return json != null && json.has(key) ? json.getLong(key) : defaultIfNull;
	}
	
	private void showToast(String text) {
		Toast.makeText(this.getBaseContext(), text, Toast.LENGTH_LONG).show();
	}

	private void fetchPopularPhotos() {
		if (aPhotos == null) {
			aPhotos = new InstagramPhotoAdapter(this, photos);
			ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
			lvPhotos.setAdapter(aPhotos);
		}
		photos.clear();

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(POP_URL, new JsonHttpResponseHandler() {
			// define successful response
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// fired when the successful response comes back.
				JSONArray photosJSON = null;
				int i = 0;
				try {
					photosJSON = response.getJSONArray("data");
					for (i = 0; i < photosJSON.length(); i++) {
						JSONObject photoJSON = photosJSON.getJSONObject(i);
						// Check if this is an image
						if (!getStringIfNotNull(photoJSON, "type", "image")
								.equals("image")) {
							continue;
						}

						InstagramPhoto photo = new InstagramPhoto();
						String userName = getStringIfNotNull(
								photoJSON.getJSONObject("user"), "username", "");
						if (!photoJSON.isNull("caption")) {
							photo.caption = getStringIfNotNull(
									photoJSON.getJSONObject("caption"), "text",
									"");
						}
						photo.imageUrl = getStringIfNotNull(
								photoJSON.getJSONObject("images")
										.getJSONObject("standard_resolution"),
								"url", "");
						photo.imageHeight = getIntIfNotNull(
								photoJSON.getJSONObject("images")
										.getJSONObject("standard_resolution"),
								"height", 0);
						photo.likesCount = getIntIfNotNull(
								photoJSON.getJSONObject("likes"), "count", 0);

						String profileImageUrl = getStringIfNotNull(
								photoJSON.getJSONObject("user"),
								"profile_picture", "");
						photo.user = new User();
						photo.user.profilePictureUrl = profileImageUrl;
						photo.user.userName = userName;
						photo.creationTime = getLongIfNotNull(photoJSON, "created_time", 0);
						
						JSONObject likesContainer = photoJSON
								.getJSONObject("likes");
						photo.likesCount = getIntIfNotNull(likesContainer,
								"count", 0);

						// parse commentors
						JSONObject commentsContainer = photoJSON
								.getJSONObject("comments");
						if (commentsContainer != null) {
							photo.commentsCount = getIntIfNotNull(
									commentsContainer, "count", 0);
							JSONArray comments = commentsContainer
									.getJSONArray("data");
							parseComments(photo, comments);
						}

						photo.location = "Unknown Location";
						if (!photoJSON.isNull("location")) {
							// photo.location = getStringIfNotNull(photoJSON,
							// "location", "");
							photo.location = "World";
						}

						photos.add(photo);
					}
					aPhotos.notifyDataSetChanged();
					SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
					swipeContainer.setRefreshing(false);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						Log.i("INFO", photosJSON.getJSONObject(i).toString());
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			private void parseComments(InstagramPhoto photo, JSONArray comments)
					throws JSONException {
				for (int j = 0; comments != null && j < comments.length(); j++) {
					JSONObject photoComment = comments.getJSONObject(j);
					Comment c = new Comment();
					c.user = new User();
					c.user.userName = getStringIfNotNull(
							photoComment.getJSONObject("from"), "username", "");
					c.user.profilePictureUrl = getStringIfNotNull(
							photoComment.getJSONObject("from"),
							"profile_picture", "");
					c.text = getStringIfNotNull(photoComment, "text", "");
					c.createdTime = getLongIfNotNull(photoComment, "created_time", System.currentTimeMillis());
					photo.comments.add(c);

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				showToast("Error loading images. !");
			}

		});

	}
}
