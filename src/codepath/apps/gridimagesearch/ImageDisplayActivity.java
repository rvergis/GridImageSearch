package codepath.apps.gridimagesearch;

import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class ImageDisplayActivity extends Activity {

	private Toast toast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		
		ImageResult imageResult = (ImageResult) getIntent().getSerializableExtra("imageResult");
		SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		ivImage.setImageResource(android.R.color.transparent);
		if (imageResult.getFullUrl() != null) {
			ivImage.setImageUrl(imageResult.getFullUrl());			
		}
		
		toast = Toast.makeText(this, imageResult.getImageText(), Toast.LENGTH_LONG);
		toast.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);
		return true;
	}

}
