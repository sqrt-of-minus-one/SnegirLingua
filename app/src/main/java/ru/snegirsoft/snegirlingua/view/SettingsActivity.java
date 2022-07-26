    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: SettingsActivity.java       //
////////////////////////////////////////

package ru.snegirsoft.snegirlingua.view;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import ru.snegirsoft.snegirlingua.R;
import ru.snegirsoft.snegirlingua.SettingsManager;

	public class SettingsActivity extends AppCompatActivity
{
	private CheckBox testCB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		testCB = findViewById(R.id.settings_testCB);
		
		testCB.setChecked(SettingsManager.settings.test);
		
		testCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				SettingsManager.settings.test = isChecked;
				SettingsManager.save(SettingsActivity.this);
			}
		});
	}
}
