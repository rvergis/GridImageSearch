package codepath.apps.gridimagesearch;

public interface ISearchRequest {

	public String getImageSize();

	public String getColorFilter();

	public String getImageType();

	public String getSiteFilter();

	public String getSearchQuery();
	
	public int getSearchStart();
	
}