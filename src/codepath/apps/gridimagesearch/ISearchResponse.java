package codepath.apps.gridimagesearch;

public interface ISearchResponse {

	public void onSearchSuccess(Object request, Object results);
	
	public void onSearchError(Object request, Object error);
}
