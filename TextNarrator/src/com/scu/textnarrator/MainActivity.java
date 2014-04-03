package com.scu.textnarrator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.qoppa.android.pdf.PDFException;
import com.qoppa.android.pdfProcess.PDFDocument;
import com.qoppa.android.pdfProcess.PDFPage;
import com.qoppa.android.pdfViewer.fonts.StandardFontTF;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.MediaColumns;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView txtfilename;
	ListView listNames;
	ArrayList<String> title = new ArrayList<String>();
	ArrayList<String> paths = new ArrayList<String>();
	ArrayList<Bitmap> thumbnails = new ArrayList<Bitmap>();
	CustomAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtfilename = (TextView) findViewById(R.id.textView1);
		listNames = (ListView) findViewById(R.id.listView1);
		//Get list of pdf name and type
		getPdfs();
		// If pdf files present in External Storage
		if (!title.isEmpty() && !paths.isEmpty()) {
			try {
				getThumbnails();
			} catch (PDFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// TODO
		//Display list
		adapter = new CustomAdapter(this, R.layout.activity_customicon, title,
				thumbnails, paths);
		listNames.setAdapter(adapter);
		registerForContextMenu(listNames);
		

		listNames.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				String path = paths.get((int) id);
				File f = new File(path);
				//If file is of zero bytes 
				if (f.length() == 0) {
					Toast.makeText(MainActivity.this, "File is empty",
							Toast.LENGTH_SHORT).show();
				} else {
					//Display text in new activity if file > zero bytes
					Intent intent = new Intent(MainActivity.this, PdfTextActivity.class);
					intent.putExtra("Id", id);
					intent.putExtra("Path", path);
					Log.d("TAG", String.valueOf(id));
					startActivity(intent);
				}
			}
		});

	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	  int id = info.position;
	  switch (item.getItemId()) {
	  case R.id.text:
		  displayText((long)id);
		  return true;
	  case R.id.pdf:
	    displayPdf((long)id);
	    return true;
	  default:
	    return super.onContextItemSelected(item);
	  }
	}
	
	private void displayPdf(long id) {
		// TODO Auto-generated method stub
		String path = paths.get((int) id);
		File f = new File(path);
		//If file is of zero bytes 
		if (f.length() == 0) {
			Toast.makeText(MainActivity.this, "File is empty",
					Toast.LENGTH_SHORT).show();
		} else {
			//Display text in new activity if file > zero bytes
			Intent intent = new Intent(MainActivity.this, DisplayPdf.class);
			intent.putExtra("Id", id);
			intent.putExtra("Path", path);
			Log.d("TAG", String.valueOf(id));
			startActivity(intent);
		}
	}
	void displayText(long id)
	{
		String path = paths.get((int) id);
		File f = new File(path);
		//If file is of zero bytes 
		if (f.length() == 0) {
			Toast.makeText(MainActivity.this, "File is empty",
					Toast.LENGTH_SHORT).show();
		} else {
			//Display text in new activity if file > zero bytes
			Intent intent = new Intent(MainActivity.this, PdfTextActivity.class);
			intent.putExtra("Id", id);
			intent.putExtra("Path", path);
			Log.d("TAG", String.valueOf(id));
			startActivity(intent);
		}
	}
	
	private void getThumbnails() throws PDFException {
		// TODO Auto-generated method stub
		//Reference : http://www.qoppa.com/files/android/pdfsdk/guide/javadoc/
		
		StandardFontTF.mAssetMgr = getAssets();
		PDFDocument pdf;
		PDFPage page;
		int width;
		int height;
		File f;
		for (String path : paths) {
			f = new File(path);
			//Add default thumbnail if file < 0 bytes
			if (f.length() == 0) {
				thumbnails.add(null);
				continue;
			}
			//Get pdfDocument from this path
			pdf = new PDFDocument(path, null);
			//Get first page of the pdf
			page = pdf.getPage(0);
			width = (int) Math.ceil(page.getDisplayWidth());
			height = (int) Math.ceil(page.getDisplayHeight());
			//Add thumbnail
			thumbnails.add(page.getBitmap(width / 10, height / 10, false));
		}

	}

	
	private void getPdfs() {
		// TODO Auto-generated method stub
		ContentResolver cr = this.getContentResolver();
		Uri uri = MediaStore.Files.getContentUri("external");
		String sortOrder = null;
		String[] mProjection = {
				// MediaColumns.MIME_TYPE,
				// MediaStore.MediaColumns.MIME_TYPE,
				MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns.DATA };

		String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE
				+ "=?";
		//Get files with extension or mime type pdf
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
				"pdf");
		String[] selectionArgsPdf = new String[] { mimeType };
		Cursor allPdfFilesCr = cr.query(uri, mProjection, selectionMimeType,
				selectionArgsPdf, sortOrder);
		//StringBuffer str = new StringBuffer();
		File tempfile;
		String name;
		String path;
		Log.d("TAG", String.valueOf(allPdfFilesCr.getCount()));
		if (allPdfFilesCr.getCount()<=0) {
			txtfilename.setText("No pdf files found");
			Log.d("TAG", "empty cursor");
		} else {
			while (allPdfFilesCr.moveToNext()) {
				name = allPdfFilesCr.getString(0);
				path = allPdfFilesCr.getString(1);
				//if file exists add to the list
				tempfile = new File(path);
				if (tempfile.exists()) {
					Log.d("NAME", name);
					Log.d("PATH", path);
					title.add(name);
					paths.add(path);
				} else {
					Log.e("FILE NOT", name + " doesn't exist");
				}
				// txtfilename.append("\n"+allPdfFiles.getString(0));
				// txtfilename.append("  "+allPdfFiles.getString(1));
			}
		}
		allPdfFilesCr.close();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (!thumbnails.isEmpty()) {
			//Delete the thumbnails to avoid out0fMemmory
			for (Bitmap bm : thumbnails) {
				if (bm != null)
					bm.recycle();

			}

		}
		thumbnails = null;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_help:
			Intent helpIntent = new Intent(MainActivity.this,HelpScreen.class);
			startActivity(helpIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
