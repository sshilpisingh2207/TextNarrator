package com.scu.textnarrator;

import java.text.DecimalFormat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SeekbarPreference extends DialogPreference implements
		OnSeekBarChangeListener {

	private static final String TAG = SeekbarPreference.class.getSimpleName();
	private SeekBar mSeekbar;
	float mCurrentValue = 1.0f;
	private float DEFAULT_VALUE = 1.0f;
	private TextView txtRate;
	private DecimalFormat df = new DecimalFormat("#.#");

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);

		mSeekbar = (SeekBar) view.findViewById(R.id.seekBar2);
		mSeekbar.setProgress((int) (mCurrentValue * 100));
		mSeekbar.setOnSeekBarChangeListener(this);
		txtRate = (TextView) view.findViewById(R.id.txtRate);
		txtRate.setText(df.format(mCurrentValue));
	}

	public SeekbarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDialogLayoutResource(R.layout.seekbar);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue,
			Object defaultValue) {
		// TODO Auto-generated method stub
		if (restorePersistedValue) {
			// Restore existing state
			mCurrentValue = this.getPersistedFloat(DEFAULT_VALUE);
		} else {
			// Set default state from the XML attribute
			mCurrentValue = (Float) defaultValue;
			persistFloat(mCurrentValue);

		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getFloat(index, DEFAULT_VALUE);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			// save to SharedPrefs, etc
			boolean isDone = persistFloat(mCurrentValue);
			SharedPreferences preferences = getPreferenceManager()
					.getSharedPreferences();
			preferences.edit().putFloat("SpeechRate", mCurrentValue).commit();
			Log.i(TAG, "saved? " + isDone);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekbar, int progress,
			boolean fromUser) {
		mCurrentValue = (progress / 10.0f) + 0.2f;
		txtRate.setText(df.format(mCurrentValue));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

}