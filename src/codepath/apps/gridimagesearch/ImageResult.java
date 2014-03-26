package codepath.apps.gridimagesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class ImageResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fullUrl;
	
	private String thumbUrl;
	
	private String imageText;
	
	private static final String LOGGER_NAME = "GridImageSearch";
	
	public ImageResult(JSONObject json) {
		if (json != null){
			try {			
				this.fullUrl = json.getString("url");
				this.thumbUrl = json.getString("tbUrl");
				this.imageText = json.getString("titleNoFormatting");
			} catch(Exception e) {
				this.fullUrl = null;
				this.thumbUrl = null;
				Log.e(LOGGER_NAME, "Image Result Unable to parse JSON",e);
			}			
		}
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public String getImageText() {
		return imageText;
	}

	@Override
	public String toString() {
		return thumbUrl;
	}
	
	public static final List<ImageResult> fromJSONArray(JSONArray array) {
		List<ImageResult> results = new ArrayList<ImageResult>();
		for (int i = 0, n = ((JSONArray) array).length(); i < n; i++) {
			try {
				results.add(new ImageResult((JSONObject) ((JSONArray) array).get(i)));
			} catch (JSONException e) {
				Log.e(LOGGER_NAME, "Unable to read JSONArray", e);
			}
		}
		return results;
	}

}
