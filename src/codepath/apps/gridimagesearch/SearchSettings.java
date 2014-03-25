package codepath.apps.gridimagesearch;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SearchSettings implements ISearchRequest {

	private static final String SHARED_PREFERENCES_KEY = "codepath.apps.gridimagesearch";
	
	private final String IMAGE_SIZE = "imageSize";
	
	private final String COLOR_FILTER = "colorFilter";
	
	private final String IMAGE_TYPE = "imageType";
	
	private final String SITE_FILTER = "siteFilter";
	
	private final String NULL_STRING = null;
	
	public static final SearchSettings instance = new SearchSettings();
	
	private String imageSize;
	
	private String colorFilter;
	
	private String imageType;
	
	private String siteFilter;
	
	private String searchQuery;
	
	private int searchStart;
	
	private int maxSearchStart;

	/* (non-Javadoc)
	 * @see codepath.apps.gridimagesearch.ISearchRequest#getImageSize()
	 */
	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	/* (non-Javadoc)
	 * @see codepath.apps.gridimagesearch.ISearchRequest#getColorFilter()
	 */
	public String getColorFilter() {
		return colorFilter;
	}

	public void setColorFilter(String colorFilter) {
		this.colorFilter = colorFilter;
	}

	/* (non-Javadoc)
	 * @see codepath.apps.gridimagesearch.ISearchRequest#getImageType()
	 */
	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	/* (non-Javadoc)
	 * @see codepath.apps.gridimagesearch.ISearchRequest#getSiteFilter()
	 */
	public String getSiteFilter() {
		return siteFilter;
	}

	public void setSiteFilter(String siteFilter) {
		this.siteFilter = siteFilter;
	}
	
	/* (non-Javadoc)
	 * @see codepath.apps.gridimagesearch.ISearchRequest#getSearchQuery()
	 */
	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	/* (non-Javadoc)
	 * @see codepath.apps.gridimagesearch.ISearchRequest#getSearchStart()
	 */
	public int getSearchStart() {
		return searchStart;
	}

	public void setSearchStart(int start) {
		this.searchStart = start;
		this.maxSearchStart = Math.max(this.maxSearchStart, this.searchStart);
	}

	public int getMaxSearchStart() {
		return maxSearchStart;
	}

	public void load(Activity activity) {
		SharedPreferences sharedPrefs = getSharedPreferences(activity);
		setImageSize(sharedPrefs.getString(IMAGE_SIZE, NULL_STRING));
		setColorFilter(sharedPrefs.getString(COLOR_FILTER, NULL_STRING));
		setImageType(sharedPrefs.getString(COLOR_FILTER, NULL_STRING));
		setSiteFilter(sharedPrefs.getString(SITE_FILTER, NULL_STRING));
	}
	
	public void save(Activity activity) {
		SharedPreferences sharedPrefs = getSharedPreferences(activity);
		Editor editor = sharedPrefs.edit();
		editor.putString(IMAGE_SIZE, getImageSize());
		editor.putString(COLOR_FILTER, getColorFilter());
		editor.putString(IMAGE_TYPE, getImageType());
		editor.putString(SITE_FILTER, getSiteFilter());
		editor.commit();
	}
	
	private SharedPreferences getSharedPreferences(Activity activity) {
		SharedPreferences sharedPrefs = activity.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
		return sharedPrefs;
	}

}
