    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: MainActivity.java           //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.SettingsManager;
import ru.snegir.snegirlingua.database.Database;
import ru.snegir.snegirlingua.entity.Language;

public class MainActivity extends AppCompatActivity
{
	private ProgressBar loadPB;
	private Spinner lang1SP, lang2SP;
	private ImageButton proceedIB, editLangIB, settingsIB;
	private TextView proceedTV, editLangTV, settingsTV;
	
	// If true, the languages list needs to be updated
	private boolean needsToBeReloaded;
	
	@Override
	public void onResume()
	{
		super.onResume();
		if (needsToBeReloaded)
		{
			loadLangs();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SettingsManager.load(MainActivity.this);
		
		needsToBeReloaded = false;
		
		loadPB = findViewById(R.id.main_loadPB);
		lang1SP = findViewById(R.id.main_lang1SP);
		lang2SP = findViewById(R.id.main_lang2SP);
		proceedIB = findViewById(R.id.main_proceedIB);
		editLangIB = findViewById(R.id.main_editLangIB);
		settingsIB = findViewById(R.id.main_settingsIB);
		proceedTV = findViewById(R.id.main_proceedTV);
		editLangTV = findViewById(R.id.main_editLangTV);
		settingsTV = findViewById(R.id.main_settingsTV);
		
		loadLangs();
		
		proceedIB.setOnClickListener(v ->
		{
			// Todo: start MenuActivity
		});
		proceedIB.setOnLongClickListener(v ->
		{
			Toast.makeText(MainActivity.this, R.string.main_proceed_info, Toast.LENGTH_LONG).show();
			return true;
		});
		
		editLangIB.setOnClickListener(v ->
		{
			Intent editLangI = new Intent(MainActivity.this, EditLangActivity.class);
			startActivity(editLangI);
		});
		editLangIB.setOnLongClickListener(v ->
		{
			Toast.makeText(MainActivity.this, R.string.main_edit_lang_info, Toast.LENGTH_LONG).show();
			return true;
		});
		
		settingsIB.setOnClickListener(v ->
		{
			Intent settingsI = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(settingsI);
		});
		settingsIB.setOnLongClickListener(v ->
		{
			Toast.makeText(MainActivity.this, R.string.main_settings_info, Toast.LENGTH_LONG).show();
			return true;
		});
	}
	
	// Update the languages list
	private void loadLangs()
	{
		loadPB.setVisibility(View.VISIBLE);
		needsToBeReloaded = false;
		new Thread(() ->
		{
			final List<Language> languages = Database.get(MainActivity.this).languages().getAll();
			String[] langs = new String[languages.size()]; // Array with codes of languages
			for (int i = 0; i < languages.size(); i++)
			{
				langs[i] = languages.get(i).getCode();
			}
			final ArrayAdapter<String> adapter =
					new ArrayAdapter<>(MainActivity.this,
									   android.R.layout.simple_spinner_item, langs);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			runOnUiThread(() ->
			{
				lang1SP.setAdapter(adapter);
				lang2SP.setAdapter(adapter);
				loadPB.setVisibility(View.INVISIBLE);
			});
		}).start();
	}
}