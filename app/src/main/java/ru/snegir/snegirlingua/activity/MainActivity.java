////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: MainActivity.java           //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.SettingsManager;
import ru.snegir.snegirlingua.database.facade.LanguagesFacade;
import ru.snegir.snegirlingua.entity.Language;

public class MainActivity extends AppCompatActivity
{
	private ProgressBar loadPB;
	private Spinner lang1SP, lang2SP;
	private ImageButton proceedIB, editLangIB, settingsIB;
	
	// If true, the language list needs to be updated
	private boolean needsToBeReloaded;
	
	@Override
	public void onResume()
	{
		super.onResume();
		// Reload if necessary
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
		
		// Load settings from file
		SettingsManager.load(MainActivity.this);
		
		needsToBeReloaded = false;
		
		loadPB = findViewById(R.id.main_loadPB);
		lang1SP = findViewById(R.id.main_lang1SP);
		lang2SP = findViewById(R.id.main_lang2SP);
		proceedIB = findViewById(R.id.main_proceedIB);
		editLangIB = findViewById(R.id.main_editLangIB);
		settingsIB = findViewById(R.id.main_settingsIB);
		
		loadLangs();
		
		proceedIB.setOnClickListener(v ->
		{
			Pair<String, String> langs = new Pair<>(
					lang1SP.getSelectedItem() == null ? "" : lang1SP.getSelectedItem().toString(),
					lang2SP.getSelectedItem() == null ? "" : lang2SP.getSelectedItem().toString());
			if (!langs.first.isEmpty() && !langs.second.isEmpty() && !langs.first.equals(langs.second))
			{
				// The first lang must be less (lexicographically) than the second one
				if (langs.first.compareTo(langs.second) > 0)
				{
					langs = new Pair<>(langs.second, langs.first);
				}
				Intent menuI = new Intent(MainActivity.this, MenuActivity.class);
				menuI.putExtra(MenuActivity.LANG_1, langs.first);
				menuI.putExtra(MenuActivity.LANG_2, langs.second);
				startActivity(menuI);
			}
			else
			{
				Toast.makeText(MainActivity.this, R.string.error_lang_same_selected, Toast.LENGTH_LONG).show();
			}
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
	
	// Reload the languages list
	private void loadLangs()
	{
		setPBVisibility(true);
		needsToBeReloaded = false;
		new Thread(() ->
		{
			final List<Language> languages = LanguagesFacade.getAll(MainActivity.this);
			String[] langCodes = new String[languages.size()]; // Array with codes of languages
			for (int i = 0; i < languages.size(); i++)
			{
				langCodes[i] = languages.get(i).getCode();
			}
			final ArrayAdapter<String> adapter =
					new ArrayAdapter<>(MainActivity.this,
									   android.R.layout.simple_spinner_item, langCodes);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			runOnUiThread(() ->
			{
				lang1SP.setAdapter(adapter);
				lang2SP.setAdapter(adapter);
				setPBVisibility(false);
			});
		}).start();
	}
	
	public void setPBVisibility(boolean visible)
	{
		runOnUiThread(() ->
		{
			loadPB.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
			lang1SP.setEnabled(!visible);
			lang2SP.setEnabled(!visible);
			proceedIB.setEnabled(!visible);
			editLangIB.setEnabled(!visible);
			settingsIB.setEnabled(!visible);
		});
	}
}