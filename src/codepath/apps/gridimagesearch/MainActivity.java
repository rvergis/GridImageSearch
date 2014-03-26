package codepath.apps.gridimagesearch;

import java.util.ArrayList;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements ISearchResponse {

	private static final int REQUEST_CODE = 1;	
	
	private static final String LOGGER_NAME = "GridImageSearch";
	
	private static final int GOOGLE_IMAGE_SEARCH_MAX_ALLOWED = 64; // i.e page 60
	
	private static final int GOOGLE_IMAGE_SEARCH_START_ALLOWED = 8;
	
	private ImageResultsArrayAdapter imageAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);			
		initGrid();
		toggleGrid(false);
		handleIntent(getIntent());
	}
	
	@Override 
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
	}
	
	public void onSettingsPressed(MenuItem mi) {
		Intent intent = new Intent(this, SearchSettingsActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);		
		initSearch(menu);		
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		saveSettings(requestCode, resultCode);
	}
	
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			imageAdapter.clearImageResults();	
			SearchSettings.instance.setSearchStart(0);
			search(intent.getStringExtra(SearchManager.QUERY));
		}
	}

	@Override
	public void onSearchSuccess(Object request, Object response) {
		int results = imageAdapter.processResponse(response);
		int start = SearchSettings.instance.getSearchStart();
		if (results > 0) {
			toggleGrid(true);
		} else {
			if (start == 0) {
				toggleGrid(false);
			}
		}
		
	}

	@Override
	public void onSearchError(Object request, Object error) {
		toggleGrid(false);
		Toast toast = Toast.makeText(getApplicationContext(), "Could not retrieve results", Toast.LENGTH_SHORT);
		toast.show();
	}
	
	private void toggleGrid(boolean show) {
		GridView gvResults = (GridView) findViewById(R.id.gvResults);		
		gvResults.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
		TextView tvNoResults = (TextView) findViewById(R.id.tvNoResults);
		tvNoResults.setVisibility(show ? View.INVISIBLE : View.VISIBLE);				
	}
	
	private void initGrid() {
		imageAdapter = new ImageResultsArrayAdapter(this, new ArrayList<ImageResult>(0));
		GridView gvResults = (GridView) findViewById(R.id.gvResults);
		gvResults.setAdapter(imageAdapter);		
		gvResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageAdapter.getImageResult(position);
				if (imageResult != null) {
					intent.putExtra("imageResult", imageResult);
					startActivity(intent);					
				} else {
					Log.w(LOGGER_NAME, "Got a null image result at position " + position + " id " + id);
				}
			}			
		});		
		gvResults.setOnScrollListener(new OnScrollListener() {	
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				Log.i(LOGGER_NAME, "scroll state changed");				
				int start = SearchSettings.instance.getSearchStart();
				start = start + 1;
				SearchSettings.instance.setSearchStart(start);
				SearchClient.instance.search(SearchSettings.instance, MainActivity.this);																	
			}			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (totalItemCount >= GOOGLE_IMAGE_SEARCH_MAX_ALLOWED) {
					Toast toast = Toast.makeText(getApplicationContext(), "Cannot search beyond " + GOOGLE_IMAGE_SEARCH_MAX_ALLOWED + " images", Toast.LENGTH_SHORT);
					toast.show();
				}
				if (visibleItemCount >= totalItemCount) {
					int start = SearchSettings.instance.getSearchStart();
					start = start + 1;
					SearchSettings.instance.setSearchStart(start);
					SearchClient.instance.search(SearchSettings.instance, MainActivity.this);

				}
			}
		});
	}
	
	private void initSearch(Menu menu) {
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));		
	}
	
	private void saveSettings(int requestCode, int resultCode) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Saved search settings", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Unable to save search settings", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void search(String searchQuery) {
		SearchSettings.instance.setSearchQuery(searchQuery);			
		SearchClient.instance.search(SearchSettings.instance, this);		
	}
	
}
