package com.maryamq.instafamous;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PhotosActivity extends Activity {
	static final String CLIENT_ID = "e76164196b7e4a7a81c4c59962d5bf1b";
	static final String POP_URL = "https://api.instagram.com/v1/media/popular?client_id="
			+ CLIENT_ID;
	static final String LOCATION_URL = "https://api.instagram.com/v1/locations/search?lat=%s&lng=%s&access_token=" + CLIENT_ID;
	private ArrayList<InstagramPhoto> photos;
	private InstagramPhotoAdapter aPhotos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_photo_list);
		fetchPopularPhotos();
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

	private void showToast(String text) {
		Toast.makeText(this.getBaseContext(), text, Toast.LENGTH_LONG).show();
	}
	

	private void fetchPopularPhotos() {
		photos = new ArrayList<InstagramPhoto>();
		aPhotos = new InstagramPhotoAdapter(this, photos);
		ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
		lvPhotos.setAdapter(aPhotos);

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
						InstagramPhoto photo = new InstagramPhoto();
						photo.userName = getStringIfNotNull(
								photoJSON.getJSONObject("user"), "username", "");
						if (!photoJSON.isNull("caption")) {
							photo.caption = getStringIfNotNull(
									photoJSON.getJSONObject("caption"), "text", "");
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
						
						photo.profileImageUrl = getStringIfNotNull(
								photoJSON.getJSONObject("user"), "profile_picture", "");
						if (!photoJSON.isNull("location")) {
							//photo.location = getStringIfNotNull(photoJSON, "location", "");
							photo.location = "World";
						}
						
						photos.add(photo);
					}
					aPhotos.notifyDataSetChanged();
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
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				showToast("Error loading images!");
			}

		});

	}
}
