package com.scu.textnarrator;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.widget.TextView;

public class StartActivity extends Activity {

	 private Handler mHandler = new Handler();

	    private Runnable mUpdateTimeTask = new Runnable() {
	        public void run() {
	            startActivity(new Intent(StartActivity.this, MainActivity.class));
	            StartActivity.this.finish();
	        }
	    };

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_start);
	        TextView textView = (TextView) findViewById(R.id.txtStart);
	        
	        textView.setText(
	                Html.fromHtml("<font color='#8A0868'><b>Text Narrator</b></font>")
	                );
	        mHandler.postDelayed(mUpdateTimeTask, 1000);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}


}
