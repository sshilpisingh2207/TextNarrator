package com.scu.textnarrator;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class HelpScreen extends Activity {

	TextView txtHelp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_screen);
		txtHelp = (TextView) findViewById(R.id.txtHelp);
		txtHelp.setText("User Flow of the project:\n "
				+ "1.Click on any item in the list\n"
				+ "2.To read the pdf: Long press on any list item\n"
				+ "->Select option View as Pdf to open the pdf reader.\n\n"
				+ "->Select option View as Text to open the pdf for narration.\n\n"
				+ "3.After opening the pdf scroll through the pdf using left/right swipe.\n\n"
				+ "4.The menu gives the following options:\n"
				+ "->Go to Page: Enter a valid page number you want to go to.\n\n"
				+ "->Bookmark: Opens a submenu with the following options:\n"
				+ "a)View Bookmarks:\n"
				+ "Select the page you want to go to from the list of saved bookmarks.\n\n"
				+ "b)BookMark this page :\n" + "Bookmarks the current page.\n\n" +

				"->Settings:\n" +

				"a)Set speech rate: \n"
				+ "Set the speech rate for narration.\n\n"
				+ "b)Set Focus Color:-\n"
				+ "Select the color for highlight of the narrated text.");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help_screen, menu);
		return true;
	}

}
