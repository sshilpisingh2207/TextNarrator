package com.scu.textnarrator;
import android.R.color;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.scu.textnarrator.R;

public class ColorPreference extends DialogPreference implements OnItemSelectedListener {

	Spinner spinner;

	String currentColor;
	String defaultColor = "CYAN";
	Context context;
	
	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);

		spinner = (Spinner) view.findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				context, R.array.colors_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setSelection(adapter.getPosition(currentColor));
		spinner.setOnItemSelectedListener(this);
		
		
		
	}

	public ColorPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDialogLayoutResource(R.layout.color);
		this.context=context;
		
	}
	
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue,
			Object defaultValue) {
		// TODO Auto-generated method stub
		if (restorePersistedValue) {
			// Restore existing state
			currentColor = this.getPersistedString(defaultColor);
			//Log.i("ColorPreference", currentColor);
			
		} else {
			// Set default state from the XML attribute
			currentColor = (String) defaultValue;
			persistString(currentColor);
		
		}
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			// save to SharedPrefs, etc
			boolean isDone = persistString(currentColor);
			SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
			
			preferences.edit().putString("color", currentColor).commit();
			//Log.i(TAG, "saved? "+ isDone);
		}
	}
	
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		currentColor = a.getString(index);
		return currentColor;
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		// TODO Auto-generated method stub
		//Log.d("COLOR", currentColor);
		currentColor = parent.getItemAtPosition(pos).toString();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
