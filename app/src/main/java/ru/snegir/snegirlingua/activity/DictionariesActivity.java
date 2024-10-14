////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: DictionariesActivity.java   //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.adapter.DictionaryAdapter;
import ru.snegir.snegirlingua.database.facade.DictionariesFacade;
import ru.snegir.snegirlingua.entity.Dictionary;

public class DictionariesActivity extends Activity
{
	// The intent is supposed to have two language codes (LANG_1 and LANG_2), which should be sorted
	public static final String LANG_1 = "lang_1";
	public static final String LANG_2 = "lang_2";
	
	private Button addBT;
	private ImageButton infoIB;
	private ProgressBar loadPB;
	private ListView listLV;
	
	private Pair<String, String> langs;
	
	// If true, the word list will be reloaded in onResume method
	public boolean needsToBeReloaded;
	
	@Override
	protected void onResume()
	{
		super.onResume();
		// Reload words if necessary
		if (needsToBeReloaded)
		{
			loadDictionaries();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dictionaries);
		
		needsToBeReloaded = true;
		
		addBT = findViewById(R.id.dictionaries_addBT);
		infoIB = findViewById(R.id.dictionaries_infoIB);
		loadPB = findViewById(R.id.dictionaries_loadPB);
		listLV = findViewById(R.id.dictionaries_listLV);
		
		// Languages are supposed to be sorted
		langs = new Pair<>(getIntent().getStringExtra(LANG_1), getIntent().getStringExtra(LANG_2));
		
		addBT.setOnClickListener(v ->
		{
			Intent wordI = new Intent(DictionariesActivity.this, DictionaryActivity.class);
			wordI.putExtra(DictionaryActivity.LANG_1, langs.first);
			wordI.putExtra(DictionaryActivity.LANG_2, langs.second);
			wordI.putExtra(DictionaryActivity.IS_NEW, true);
			needsToBeReloaded = true;
			startActivity(wordI);
		});
		addBT.setOnLongClickListener(v ->
		{
			Toast.makeText(DictionariesActivity.this, R.string.a_dictionaries_addDictionary_hint, Toast.LENGTH_LONG).show();
			return true;
		});
		
		infoIB.setOnClickListener(v -> new AlertDialog.Builder(DictionariesActivity.this)
				.setTitle(R.string.a_dictionaries_help_title)
				.setMessage(R.string.a_dictionaries_help)
				.setPositiveButton(R.string.ok, null)
				.create()
				.show());
		infoIB.setOnLongClickListener(v ->
		{
			Toast.makeText(DictionariesActivity.this, R.string.help, Toast.LENGTH_LONG).show();
			return true;
		});
		
		loadDictionaries();
	}
	
	public void loadDictionaries()
	{
		setPBVisibility(true);
		needsToBeReloaded = false;
		new Thread(() ->
		{
			List<Dictionary> dictionaries = DictionariesFacade.getForLangs(DictionariesActivity.this, langs);
			Dictionary[] array = new Dictionary[dictionaries.size()];
			dictionaries.toArray(array);
			DictionaryAdapter adapter = new DictionaryAdapter(DictionariesActivity.this, array, langs);
			DictionariesActivity.this.runOnUiThread(() ->
			{
				listLV.setAdapter(adapter);
				setPBVisibility(false);
			});
		}).start();
	}
	
	public void setPBVisibility(boolean visible)
	{
		runOnUiThread(() ->
		{
			loadPB.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
			addBT.setEnabled(!visible);
			listLV.setEnabled(!visible);
		});
	}
}
