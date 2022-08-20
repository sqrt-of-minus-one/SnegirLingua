////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: SettingsActivity.java       //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import android.os.Bundle;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.SettingsManager;

public class SettingsActivity extends AppCompatActivity
{
	private CheckBox testCB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		testCB = findViewById(R.id.settings_testCB);
		
		// Settings have already been loaded in MainActivity
		
		toUi();
		setupOnUpdate();
	}
	
	// Apply values from settings to the UI elements
	private void toUi()
	{
		testCB.setChecked(SettingsManager.settings.test);
	}
	
	// Set listeners to update settings when UI elements are changed
	private void setupOnUpdate()
	{
		testCB.setOnCheckedChangeListener((buttonView, isChecked) ->
		{
			SettingsManager.settings.test = isChecked;
			SettingsManager.save(SettingsActivity.this);
		});
	}
}
