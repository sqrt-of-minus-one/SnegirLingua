    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: SettingsActivity.java       //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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
		
		// Settings are loaded in MainActivity
		testCB.setChecked(SettingsManager.settings.test);
		
		testCB.setOnCheckedChangeListener((buttonView, isChecked) ->
		{
			SettingsManager.settings.test = isChecked;
			SettingsManager.save(SettingsActivity.this);
		});
	}
}
