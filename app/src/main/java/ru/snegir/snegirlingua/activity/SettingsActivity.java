////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: SettingsActivity.java       //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

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
		testCB.setOnLongClickListener(v ->
		{
			Toast.makeText(SettingsActivity.this, R.string.a_settings_test_hint, Toast.LENGTH_LONG).show();
			return true;
		});
	}
}
