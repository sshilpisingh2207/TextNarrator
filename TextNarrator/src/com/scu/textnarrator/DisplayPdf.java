package com.scu.textnarrator;

import java.io.File;
import java.io.IOException;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfReader;
import com.qoppa.android.pdf.PDFException;
import com.qoppa.android.pdfProcess.PDFDocument;
import com.qoppa.android.pdfProcess.PDFPage;
import com.qoppa.android.pdfViewer.fonts.StandardFontTF;
import com.scu.textnarrator.PdfTextActivity.OnGestureSwipeListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class DisplayPdf extends Activity {

	ImageView imgPdf;
	String path;
	static int pageNo = -1;
	static int pageCount = 0;
	PdfReader reader;
	Bitmap bm;
	private OnGestureSwipeListener gesturelistner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_pdf);
		imgPdf = (ImageView) (findViewById(R.id.imgPdf));
		Intent intent = getIntent();
		long id = intent.getLongExtra("Id", -1);
		path = intent.getStringExtra("Path");
		File f = new File(path);
		gesturelistner = new OnGestureSwipeListener(this);
		imgPdf.setOnTouchListener(gesturelistner);
		if (f.length() == 0) {

			finish();
		} else {
			if (id != -1) {
				try {
					reader = new PdfReader(path);
					pageNo = 0;
					pageCount = reader.getNumberOfPages();
					displayPage(pageNo);
				} catch (PDFException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// Dispplay page as a bitmap image
	private void displayPage(int pageNo) throws PDFException {
		// TODO Auto-generated method stub
		StandardFontTF.mAssetMgr = getAssets();
		PDFDocument pdf;
		PDFPage page;
		int width;
		int height;

		File f = new File(path);
		if (f.length() != 0) {
			// Reference :
			// http://www.qoppa.com/files/android/pdfsdk/guide/javadoc/
			pdf = new PDFDocument(path, null);
			// Get first page of the pdf
			System.out.println(pageNo);
			page = pdf.getPage(pageNo);
			width = (int) Math.ceil(page.getDisplayWidth());
			height = (int) Math.ceil(page.getDisplayHeight());
			bm = page.getBitmap(width, height, false);

			if (bm != null) {
				imgPdf.setImageBitmap(bm);
			}

		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
		return gesturelistner.onTouch(null, ev);
	}

	class OnGestureSwipeListener implements OnTouchListener {

		private final GestureDetector gestureDetector;

		public OnGestureSwipeListener(Context ctx) {
			gestureDetector = new GestureDetector(ctx, new GestureListener());
		}

		private final class GestureListener extends SimpleOnGestureListener {

			private static final int SWIPE_THRESHOLD = 100;
			private static final int SWIPE_VELOCITY_THRESHOLD = 100;

			@Override
			public boolean onDown(MotionEvent e) {
				// Log.d("DEBUG_TAG", "OnDown");
				return true;
			}

			// Handles swipe gestures of user
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				Log.d("DEBUG_TAG", "OnFling");
				boolean result = false;
				try {
					float diffY = e2.getY() - e1.getY();
					float diffX = e2.getX() - e1.getX();
					if (Math.abs(diffX) > Math.abs(diffY)) {
						if (Math.abs(diffX) > SWIPE_THRESHOLD
								&& Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
							if (diffX > 0) {
								// Displays previous page
								onSwipeRight();
							} else {
								// Displays next page
								onSwipeLeft();
							}
						}
					}
					// } else {
					// if (Math.abs(diffY) > SWIPE_THRESHOLD &&
					// Math.abs(velocityY)
					// > SWIPE_VELOCITY_THRESHOLD) {
					// if (diffY > 0) {
					// onSwipeBottom();
					// } else {
					// onSwipeTop();
					// }
					// }
					// }
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				return result;
			}
		}

		public void onSwipeRight() throws Exception {
			Log.d("DEBUG_TAG", "OnSwipeRight");
			if (pageNo >= 0 && pageNo < pageCount) {
				if (pageNo > 0) {
					pageNo--;
					// displayText(path, pageNo);
					displayPage(pageNo);
				}

			}

		}

		public void onSwipeLeft() throws Exception {
			Log.d("DEBUG_TAG", "OnSwipeLefffftttt");
			System.out.println(pageCount);
			if (pageNo >= 0 && pageNo < pageCount) {
				if (pageNo < pageCount - 1) {
					pageNo++;
					displayPage(pageNo);
					// displayText(path, pageNo);
				}

			}
		}

		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			// TODO Auto-generated method stub
			return gestureDetector.onTouchEvent(event);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_pdf, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_help:
			Intent helpIntent = new Intent(DisplayPdf.this,HelpScreen.class);
			startActivity(helpIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (bm != null)
			bm.recycle();

	}
}
