package codepath.apps.gridimagesearch;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ImageResultsArrayAdapter extends ArrayAdapter<ImageResult> {

	private static final String LOGGER_NAME = "GridImageSearch";
	
	private List<ImageResult> imageResults;
	
	public ImageResultsArrayAdapter(Context context, List<ImageResult> imageResults) {
		super(context, R.layout.item_image_result, imageResults);
		this.imageResults = imageResults;
	}
	
	public void clearImageResults() {
		imageResults.clear();
	}
	
	public ImageResult getImageResult(int position) {
		return imageResults.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SmartImageView ivImage = null;
		if (position < this.getCount()) {
			ImageResult imageInfo = this.getItem(position);
			if (convertView == null) {
				LayoutInflater inflator = LayoutInflater.from(getContext());
				ivImage = (SmartImageView) inflator.inflate(R.layout.item_image_result, parent, false); 
			} else {
				ivImage = (SmartImageView) convertView;
			}
			ivImage.setImageResource(android.R.color.transparent);
			if (imageInfo.getThumbUrl() != null) {
				ivImage.setImageUrl(imageInfo.getThumbUrl());			
			}			
		}
		return ivImage;
	}

	@SuppressWarnings("unchecked")
	public int processResponse(Object responseObj) {
		Map<String, Object> responseMap = (Map<String, Object>) responseObj;
		Object response = responseMap.get(SearchClient.RESPONSE);
		JSONArray results = null;
		try {
			if (response instanceof String) {
				response = new JSONObject((String) response);
			}			
			if (response instanceof JSONObject) {
				if (((JSONObject) response).getInt("responseStatus") == 200) {
					if (((JSONObject) response).getJSONObject("responseData") != null) { 
						if (((JSONObject) response).getJSONObject("responseData").getJSONArray("results") != null) {
							results = ((JSONObject) response).getJSONObject("responseData").getJSONArray("results");						
						}
					}
				}
			} 			
		} catch(JSONException e) {
			results = null;
			Log.e(LOGGER_NAME, "Unable to parse JSON results ",e);			
		}		
		if (results != null && results.length() > 0) {
			List<ImageResult> processedList = ImageResult.fromJSONArray(results);
			if (processedList != null && processedList.size() > 0) {
				for (int i = 0; i < processedList.size(); i++) {
					imageResults.add(processedList.get(i));								
				}
				notifyDataSetChanged();
				return results.length();
			}
		}
		return 0;
	}
	
}
