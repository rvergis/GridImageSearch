package codepath.apps.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchSettingsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		SearchSettings searchSettings = SearchSettings.instance;		
		searchSettings.load(this);
		
		initSpinner(R.id.spImageSize, R.array.image_sizes, searchSettings.getImageSize());
		initSpinner(R.id.spColorFilter, R.array.image_colors, searchSettings.getColorFilter());
		initSpinner(R.id.spImageType, R.array.image_types, searchSettings.getImageType());
		
		EditText etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		etSiteFilter.setText(SearchSettings.instance.getSiteFilter());		
	}
	
	public void onSaveSettingsPressed(MenuItem miSave) {
		
		SearchSettings searchSettings = SearchSettings.instance;		
		
		Spinner spinner = (Spinner) findViewById(R.id.spImageSize);
		if (spinner.getSelectedItem() != null) {
			searchSettings.setImageSize(spinner.getSelectedItem().toString());
		}
		
		spinner = (Spinner) findViewById(R.id.spColorFilter);
		if (spinner.getSelectedItem() != null) {
			searchSettings.setColorFilter(spinner.getSelectedItem().toString());
		}
		
		spinner = (Spinner) findViewById(R.id.spImageType);
		if (spinner.getSelectedItem() != null) {
			searchSettings.setImageType(spinner.getSelectedItem().toString());
		}
		
		EditText etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		if (etSiteFilter.getText() != null) {
			searchSettings.setSiteFilter(etSiteFilter.getText().toString());			
		}
		
		searchSettings.save(this);
		
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	private void initSpinner(int resId, int arrayId, String value) {
		Spinner spinner = (Spinner) findViewById(resId);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayId, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spinner.setAdapter(adapter);
		if (value != null) {
			spinner.setSelection(adapter.getPosition(value), true);
		}
	}
	
}
