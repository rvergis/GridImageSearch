package codepath.apps.gridimagesearch;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.Uri;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SearchClient {
	
	public static final String STATUS_CODE = "STATUS_CODE";
	
	public static final String HEADERS = "HEADERS";
	
	public static final String RESPONSE = "RESPONSE";
	
	public static final String THROWABLE = "THROWABLE";

	public static final SearchClient instance = new SearchClient();
	
	private final AsyncHttpClient httpClient;
	
	private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images";
	
	private static final String IS_SITESEARCH = "as_sitesearch";
	
	private static final String IS_IMAGETYPE = "imgtype";
	
	private static final String IS_IMAGESIZE = "imgsz";
	
	private static final String IS_IMAGECOLOR = "imgcolor";
	
	private static final String IS_IMAGEQUERY = "q";
	
	private static final String IS_IMAGESAFE = "safe";
	
	private static final String IS_IMAGEVERSION = "v";
	
	private static final String IS_IMAGESTART = "start";
	
	private SearchClient() {
		httpClient = new AsyncHttpClient();
	}

	public void search(final ISearchRequest searchRequest, final ISearchResponse searchResponse) {
		RequestParams params = getSearchParams(searchRequest);
		httpClient.get(getSearchURL(searchRequest), params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				Map<String, Object> errorResponseMap = SearchClient.this.constructErrorResponse(statusCode, headers, responseBody, e);
				searchResponse.onSearchError(searchRequest, errorResponseMap);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable e, JSONArray errorResponse) {
				Map<String, Object> errorResponseMap = SearchClient.this.constructErrorResponse(statusCode, headers, errorResponse, e);
				searchResponse.onSearchError(searchRequest, errorResponseMap);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable e, JSONObject errorResponse) {
				Map<String, Object> errorResponseMap = SearchClient.this.constructErrorResponse(statusCode, headers, errorResponse, e);
				searchResponse.onSearchError(searchRequest, errorResponseMap);
			}

			@Override
			public void onFailure(int statusCode, Throwable e,
					JSONArray errorResponse) {
				Map<String, Object> errorResponseMap = SearchClient.this.constructErrorResponse(statusCode, null, errorResponse, e);
				searchResponse.onSearchError(searchRequest, errorResponseMap);
			}

			@Override
			public void onFailure(int statusCode, Throwable e,
					JSONObject errorResponse) {
				Map<String, Object> errorResponseMap = SearchClient.this.constructErrorResponse(statusCode, null, errorResponse, e);
				searchResponse.onSearchError(searchRequest, errorResponseMap);
			}

			@Override
			public void onFailure(Throwable e, JSONArray errorResponse) {
				Map<String, Object> errorResponseMap = SearchClient.this.constructErrorResponse(-1, null, errorResponse, e);
				searchResponse.onSearchError(searchRequest, errorResponseMap);
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				Map<String, Object> errorResponseMap = SearchClient.this.constructErrorResponse(-1, null, errorResponse, e);
				searchResponse.onSearchError(searchRequest, errorResponseMap);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				Map<String, Object> successResponseMap = SearchClient.this.constructSuccessResponse(statusCode, headers, response);
				searchResponse.onSearchSuccess(searchRequest, successResponseMap);				
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Map<String, Object> successResponseMap = SearchClient.this.constructSuccessResponse(statusCode, headers, response);
				searchResponse.onSearchSuccess(searchRequest, successResponseMap);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseBody) {
				Map<String, Object> successResponseMap = SearchClient.this.constructSuccessResponse(statusCode, headers, responseBody);
				searchResponse.onSearchSuccess(searchRequest, successResponseMap);
			}

			@Override
			public void onSuccess(int statusCode, JSONArray response) {
				Map<String, Object> successResponseMap = SearchClient.this.constructSuccessResponse(statusCode, null, response);
				searchResponse.onSearchSuccess(searchRequest, successResponseMap);
			}
			
		});
	}
	
	private String getSearchURL(ISearchRequest request) {
		return BASE_URL;
	}

	private RequestParams getSearchParams(ISearchRequest request) {
		RequestParams requestParams = new RequestParams();
		// version
		requestParams.add(IS_IMAGEVERSION, "1.0");		
		// safe search
		requestParams.add(IS_IMAGESAFE, "active");
		// query
		requestParams.add(IS_IMAGEQUERY, Uri.encode(request.getSearchQuery()));
		requestParams.add(IS_IMAGESTART, Integer.toString(request.getSearchStart()));
		// image color
		if (request.getColorFilter() != null) {
			requestParams.add(IS_IMAGECOLOR, request.getColorFilter());
		}
		// image size
		if (request.getImageSize() != null) {
			requestParams.add(IS_IMAGESIZE, request.getImageSize());
		}
		// image type
		if (request.getImageType() != null) {
			requestParams.add(IS_IMAGETYPE, request.getImageType());
		}
		// site filter
		if (request.getSiteFilter() != null) {
			requestParams.add(IS_SITESEARCH, request.getSiteFilter());
		}
		return requestParams;
	}
	
	private Map<String, Object> constructErrorResponse(int statusCode, Header[] headers, Object response, Throwable e) {
		Map<String, Object> errorResponse = new HashMap<String, Object>();
		errorResponse.put(STATUS_CODE, statusCode);
		errorResponse.put(HEADERS, headers);
		errorResponse.put(RESPONSE, response);
		errorResponse.put(THROWABLE, e);
		return errorResponse;		
	}
	
	private Map<String, Object> constructSuccessResponse(int statusCode, Header[] headers, Object response) {
		Map<String, Object> successResponse = new HashMap<String, Object>();
		successResponse.put(STATUS_CODE, statusCode);
		successResponse.put(HEADERS, headers);
		successResponse.put(RESPONSE, response);
		return successResponse;		
	}
	
	
}
