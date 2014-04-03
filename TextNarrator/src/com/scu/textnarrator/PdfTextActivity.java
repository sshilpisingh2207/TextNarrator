package com.scu.textnarrator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.view.GestureDetectorCompat;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PdfTextActivity extends Activity implements OnInitListener {

	TextView txtdisplay;
	TextView txtpage;
	private boolean isStarted;
	// private GestureDetector mDetector;
	private final String TAG = "TAG";
	private OnGestureSwipeListener gesturelistner;
	static int pageNo = 0;
	String path;
	int pageCount = 0;
	PdfReader reader;
	private TextToSpeech tts = null;
	StringBuilder sb = null;
	Scanner sc2 = null;
	String tempStr;
	HashMap<String, String> map;
	int length;
	int start = 0;
	Spannable str;
	String orignalStr;
	boolean isPaused = false;
	final String expression = "[^.,:+\\-\\w\\s]";
	float speechRate = 1.0f;
	int bgColor = Color.CYAN;
	String strPageNo;

	// BackgroundColorSpan bgcolor = new BackgroundColorSpan(Color.CYAN);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdf_text);

		PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);
		txtdisplay = (TextView) findViewById(R.id.txtDisplay);
		txtpage = (TextView) findViewById(R.id.txtpage);
		txtpage.setTypeface(null, Typeface.BOLD);
		// To handle gesture events
		gesturelistner = new OnGestureSwipeListener(this);
		txtdisplay.setOnTouchListener(gesturelistner);
		Intent intent = getIntent();
		long id = intent.getLongExtra("Id", -1);
		path = intent.getStringExtra("Path");
		File f = new File(path);
		if (f.length() == 0) {
			finish();
		} else {
			if (id != -1) {
				try {
					reader = new PdfReader(path);
					pageNo = 1;
					pageCount = reader.getNumberOfPages();
					// display the first page
					displayText(path, pageNo);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void displayText(String path, int pageNo) throws IOException {
		// TODO Auto-generated method stub

		Log.d("TAG", path + "  ");
		Log.d(TAG, String.valueOf(pageNo));
		if (pageNo >= 1 && pageNo <= reader.getNumberOfPages()) {
			// Reference: http://api.itextpdf.com/itext/
			isStarted = false;
			if (tts != null) {
				tts.stop();
				tts.shutdown();
			}
			// Set the toggle button to play
			invalidateOptionsMenu();
			start = 0;

			PdfReaderContentParser parser = new PdfReaderContentParser(reader);

			TextExtractionStrategy strategy;
			sb = new StringBuilder();
			// Reference : http://api.itextpdf.com/itext/
			strategy = parser.processContent(pageNo,
					new SimpleTextExtractionStrategy());
			String strText = strategy.getResultantText();
			sb.append(strText);
			// Log.d("TAG", sb.toString());
			txtdisplay.setText(sb.toString());
			length = txtdisplay.getText().toString().length();
			removeSpan(start, length);
			Log.d("TAG", txtdisplay.getText().toString());
			txtpage.setText("Page " + pageNo + " of " + pageCount);
		}
	}

	private void removeSpan(int startSelection, int endSelection) {
		// Set the string to default from index startselection to index end
		// Selection
		str = new SpannableString(txtdisplay.getText());
		str.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), start, length,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				txtdisplay.setText(str);

			}
		});

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
		return gesturelistner.onTouch(null, ev);
	}

	// OnGestureListner class
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

			// Called when user flings on screen
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
								// Display the prev page
								onSwipeRight();

							} else {
								// Display the next page
								onSwipeLeft();
							}
						}
					}
					
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				return result;
			}
		}

		public void onSwipeRight() throws Exception {
			Log.d("DEBUG_TAG", "OnSwipeRight");
			if (pageNo >= 1 && pageNo <= reader.getNumberOfPages()) {
				if (pageNo > 1) {
					isPaused = false;
					pageNo--;
					displayText(path, pageNo);
				}

			}

		}

		public void onSwipeLeft() throws Exception {
			Log.d("DEBUG_TAG", "OnSwipeLefffftttt");
			if (pageNo >= 1 && pageNo <= reader.getNumberOfPages()) {
				if (pageNo < reader.getNumberOfPages()) {
					isPaused = false;
					pageNo++;
					displayText(path, pageNo);
				}

			}
		}

		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			// TODO Auto-generated method stub
			return gestureDetector.onTouchEvent(event);
		}
	}

	
	// Method called to play the text
	private void speak() {
		removeSpan(start, length);
		start = 0;
		// Get the length of text
		length = txtdisplay.getText().toString().length();
		// Remove special characters from string

		if (sb != null) {

			String result = sb.toString();

			// Log.d(TAG, result);

			sc2 = new Scanner(result);
			if (sc2.hasNextLine()) {
				// Get the next line
				tempStr = sc2.nextLine();
				orignalStr = tempStr;
				// remove special characters
				tempStr = tempStr.replaceAll(expression, "");
				length = orignalStr.length();
				map = new HashMap<String, String>();
				map.put(TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS,
						"true");
				// Set Utterance ID for OnUtteranceProgressListner
				map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
						"SOME MESSAGE");
				tts = new TextToSpeech(PdfTextActivity.this,
						(OnInitListener) this);

				tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

					@Override
					public void onStart(String utteranceId) {
						// TODO Auto-gene)rated method stub
						Log.d("TAG", "onStart");
					}

					@Override
					public void onError(String utteranceId) {
						// TODO Auto-generated method stub
						Log.d("TAG", "onError");

					}

					// Called when tts is done
					@Override
					public void onDone(String utteranceId) {
						// TODO Auto-generated method stub
						Log.d("TAG", "OnDone");
						removeSpan(start, length);

						if (sc2.hasNextLine()) {
							// play the next line
							tempStr = sc2.nextLine();
							orignalStr = tempStr;
							tempStr = tempStr.replaceAll(expression, "");
							start = length + 1;
							length = orignalStr.length() + start;
							Log.d("TAG", String.valueOf(orignalStr.length()));
							Log.d("Start", String.valueOf(start));
							Log.d("Length", String.valueOf(length));
							// highlight the line
							str.setSpan(new BackgroundColorSpan(bgColor),
									start, length,
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {

									txtdisplay.setText(str);

								}
							});
							// txtdisplay.setText(str);
							Log.d("TAG", tempStr);
							tts.speak(tempStr, TextToSpeech.QUEUE_FLUSH, map);
						} else {
							isStarted = false;
							runOnUiThread(new Runnable() {
								@Override
								public void run() {

									invalidateOptionsMenu();

								}
							});
							if (sc2 != null) {
								sc2.close();
							}
							if (tts != null) {
								tts.stop();
								tts.shutdown();
							}
						}
					}

				});

			} else {
				isStarted = false;
				// Toggle the play button and set it to default at end of text
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						invalidateOptionsMenu();

					}
				});
				if (sc2 != null) {
					sc2.close();
				}
				if (tts != null) {
					tts.stop();
					tts.shutdown();
				}
			}
		}
	}

	void resume() {

		tts = new TextToSpeech(PdfTextActivity.this, (OnInitListener) this);

		tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

			@Override
			public void onStart(String utteranceId) {
				// TODO Auto-gene)rated method stub
				Log.d("TAG", "onStart");
			}

			@Override
			public void onError(String utteranceId) {
				// TODO Auto-generated method stub
				Log.d("TAG", "onError");

			}

			// Called when tts is done
			@Override
			public void onDone(String utteranceId) {
				// TODO Auto-generated method stub
				Log.d("TAG", "OnDone");
				removeSpan(start, length);

				if (sc2.hasNextLine()) {
					// play the next line
					tempStr = sc2.nextLine();
					orignalStr = tempStr;
					tempStr = tempStr.replaceAll(expression, "");
					start = length + 1;
					length = orignalStr.length() + start;
					Log.d("TAG", String.valueOf(orignalStr.length()));
					Log.d("Start", String.valueOf(start));
					Log.d("Length", String.valueOf(length));
					// highlight the line
					str.setSpan(new BackgroundColorSpan(bgColor), start,
							length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

							txtdisplay.setText(str);

						}
					});
					// txtdisplay.setText(str);
					Log.d("TAG", tempStr);
					tts.speak(tempStr, TextToSpeech.QUEUE_FLUSH, map);
				} else {
					isStarted = false;
					isPaused = false;
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

							invalidateOptionsMenu();

						}
					});
					if (sc2 != null) {
						sc2.close();
					}
					if (tts != null) {
						tts.stop();
						tts.shutdown();
					}
				}
			}
		});
	}

	// One time call when text to speech engine is started
	@Override
	public void onInit(int status) {
		tts.setLanguage(Locale.US);

		tts.setSpeechRate(speechRate);
		Log.d(TAG, "Speech RATE " + speechRate);
		// tts.setSpeechRate(0.5f);
		Log.d("TAG", "onInit");
		Log.d("TAG", tempStr);

		Log.d("onInitStart", String.valueOf(start));
		Log.d("onInitStart", String.valueOf(length));
		str.setSpan(new BackgroundColorSpan(bgColor), start, length,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		txtdisplay.setText(str);
		tts.speak(tempStr, TextToSpeech.QUEUE_FLUSH, map);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.removeItem(R.id.action_stop);
		getMenuInflater().inflate(R.menu.pdf_text, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_help:
			Intent helpIntent = new Intent(PdfTextActivity.this,HelpScreen.class);
			startActivity(helpIntent);
			return true;
			
		case R.id.action_play:
			isStarted = true;
			invalidateOptionsMenu();
			if (!isPaused) {
				speak();

			} else {
				removeSpan(start, length);
				resume();

			}
			return true;
		case R.id.action_stop:
			isStarted = false;
			isPaused = true;
			// Calls onPrepareOptionsMenu
			invalidateOptionsMenu();
			if (tts != null) {
				tts.stop();
				tts.shutdown();
			}

			return true;

		case R.id.action_completeStop:
			isStarted = false;
			isPaused = false;
			removeSpan(start, length);
			// Calls onPrepareOptionsMenu
			invalidateOptionsMenu();
			if (tts != null) {
				tts.stop();
				tts.shutdown();
			}

			return true;

		case R.id.action_settings:
			isStarted = false;
			isPaused = false;
			removeSpan(start, length);
			// Calls onPrepareOptionsMenu
			invalidateOptionsMenu();
			if (tts != null) {
				tts.stop();
				tts.shutdown();
			}
			Intent intent = new Intent(PdfTextActivity.this,
					SettingsActivity.class);
			startActivity(intent);
			return true;

		case R.id.action_goto_page:
			isStarted = false;
			isPaused = false;
			removeSpan(start, length);
			// Calls onPrepareOptionsMenu
			invalidateOptionsMenu();
			if (tts != null) {
				tts.stop();
				tts.shutdown();
			}
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Enter page number");
			alert.setMessage("please enter page number you wish to jump!!");

			// Set an EditText view to get user input

			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
			alert.setView(input);

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							if (!input.getText().toString().equals("")) {
								int tempPageNo = Integer.parseInt(input.getText()
										.toString());
								// Do something with value!

								if ((tempPageNo >= 1 && tempPageNo <= reader
										.getNumberOfPages())) {
									try {
										pageNo = tempPageNo;
										displayText(path, pageNo);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} else {
									//pageNo =1;
									Toast.makeText(PdfTextActivity.this,
											"Invalid Page Number",
											Toast.LENGTH_SHORT).show();
								}

							}
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});

			alert.show();
			return true;

		case R.id.action_ViewBookMarks:
			isStarted = false;
			isPaused = false;
			removeSpan(start, length);
			// Calls onPrepareOptionsMenu
			invalidateOptionsMenu();
			if (tts != null) {
				tts.stop();
				tts.shutdown();
			}
			AlertDialog.Builder alert1 = new AlertDialog.Builder(this);

			alert1.setTitle("Select BookMark");
			alert1.setMessage("Please select page number!");

			// Set an EditText view to get user input
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(this);

			String strBook = pref.getString(path, "");
			ArrayList<Integer> pageNos = new ArrayList<Integer>();

			if (!strBook.equals("")) {
				System.out.println("old tokens" + strBook.toString());
				StringTokenizer st = new StringTokenizer(strBook, ",");
				while (st.hasMoreTokens()) {
					int temp = Integer.parseInt(st.nextToken());
					pageNos.add(temp);
				}
			}
			final Spinner list0fBookmarks = new Spinner(this);
			// list0fBookmarks = (Spinner) findViewById(R.id.spinner1);
			// Create an ArrayAdapter using the string array and a default
			// spinner
			// layout
			ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
					android.R.layout.simple_spinner_dropdown_item, pageNos);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			list0fBookmarks.setAdapter(adapter);
			// String strPageNo;
			list0fBookmarks
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View arg1, int pos, long id) {
							// TODO Auto-generated method stub
							strPageNo = parent.getItemAtPosition(pos)
									.toString();
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});
			alert1.setView(list0fBookmarks);

			alert1.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							if (strPageNo!=null) {
								pageNo = Integer.parseInt(strPageNo);
								// Do something with value!
								if ((pageNo >= 1 && pageNo <= reader
										.getNumberOfPages())) {
									try {
										displayText(path, pageNo);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} 
							}

						}
					});

			alert1.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});

			alert1.show();
			return true;

		case R.id.action_addBookMark:
			isStarted = false;
			isPaused = false;
			removeSpan(start, length);
			// Calls onPrepareOptionsMenu
			invalidateOptionsMenu();
			if (tts != null) {
				tts.stop();
				tts.shutdown();
			}

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);

			String strBookmarks = prefs.getString(path, "");
			ArrayList<Integer> bookmarks = new ArrayList<Integer>();

			if (!strBookmarks.equals("")) {
				System.out.println("old tokens" + strBookmarks.toString());
				StringTokenizer st = new StringTokenizer(strBookmarks, ",");
				while (st.hasMoreTokens()) {
					int temp = Integer.parseInt(st.nextToken());
					bookmarks.add(temp);
				}
			}
			if (!bookmarks.contains(pageNo))
			{
				bookmarks.add(pageNo);
				
			}
			Toast.makeText(PdfTextActivity.this, "Bookmark added Successfully", Toast.LENGTH_SHORT).show();
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < bookmarks.size(); i++) {
				str.append(bookmarks.get(i)).append(",");
			}
			prefs.edit().putString(path, str.toString()).commit();
			System.out.println(str.toString());

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	// Called to update the menu by method invalidateOptionsMenu
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// return super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.action_play).setVisible(!isStarted);
		menu.findItem(R.id.action_stop).setVisible(isStarted);
		return true;

	}

	@Override
	protected void onResume() {
		super.onResume();
		removeSpan(start, length);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		speechRate = prefs.getFloat("SpeechRate", 1.0f);

		String tempColor = prefs.getString("color", "CYAN");
		if (tempColor.equals("BLUE"))
			bgColor = Color.BLUE;
		else if (tempColor.equals("RED"))
			bgColor = Color.RED;
		else if (tempColor.equals("GREEN"))
			bgColor = Color.GREEN;
		else if (tempColor.equals("YELLOW"))
			bgColor = Color.YELLOW;
		else
			bgColor = Color.CYAN;

		Log.d(TAG, tempColor);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (tts != null) {
			this.tts.stop();
			this.tts.shutdown();
		}
		if (sc2 != null)
			sc2.close();

	}

}
