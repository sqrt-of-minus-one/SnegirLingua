////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: WordsActivity.java          //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.adapter.WordAdapter;
import ru.snegir.snegirlingua.database.facade.TranslationsFacade;
import ru.snegir.snegirlingua.entity.Translation;

public class WordsActivity extends Activity
{
	// The intent is supposed to have two language codes (LANG_1 and LANG_2), which should be sorted
	public static final String LANG_1 = "lang_1";
	public static final String LANG_2 = "lang_2";
	
	private RadioGroup sortRG;
	private RadioButton sortLang1RB, sortLang2RB;
	private ProgressBar loadPB;
	private ListView listLV;
	private FloatingActionButton addFB;
	
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
			loadWords();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_words);
		
		needsToBeReloaded = false;
		
		sortRG = findViewById(R.id.words_sortRG);
		sortLang1RB = findViewById(R.id.words_sortLang1RB);
		sortLang2RB = findViewById(R.id.words_sortLang2RB);
		loadPB = findViewById(R.id.words_loadPB);
		listLV = findViewById(R.id.words_listLV);
		addFB = findViewById(R.id.words_addFB);
		
		// Languages are supposed to be sorted
		langs = new Pair<>(getIntent().getStringExtra(LANG_1), getIntent().getStringExtra(LANG_2));
		
		sortLang1RB.setText(getString(R.string.sort, langs.first));
		sortLang2RB.setText(getString(R.string.sort, langs.second));
		
		sortRG.setOnCheckedChangeListener((group, checkedId) -> loadWords());
		addFB.setOnClickListener(v ->
		{
			Intent wordI = new Intent(WordsActivity.this, WordActivity.class);
			wordI.putExtra(WordActivity.LANG_1, langs.first);
			wordI.putExtra(WordActivity.LANG_2, langs.second);
			wordI.putExtra(WordActivity.IS_NEW, true);
			needsToBeReloaded = true;
			startActivity(wordI);
		});
		sortLang1RB.setChecked(true);
	}
	
	public void loadWords()
	{
		needsToBeReloaded = false;
		new Thread(() ->
		{
			synchronized (WordsActivity.this)
			{
				WordsActivity.this.runOnUiThread(() -> setPBVisibility(true));
				List<Translation> translations = TranslationsFacade.getForLangs(WordsActivity.this, langs, sortLang2RB.isChecked());
				Translation[] array = new Translation[translations.size()];
				translations.toArray(array);
				WordAdapter adapter = new WordAdapter(WordsActivity.this, array, langs);
				WordsActivity.this.runOnUiThread(() ->
				{
					listLV.setAdapter(adapter);
					setPBVisibility(false);
				});
			}
		}).start();
	}
	
	public void setPBVisibility(boolean visible)
	{
		runOnUiThread(() ->
		{
			loadPB.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
			sortRG.setEnabled(!visible);
			sortLang1RB.setEnabled(!visible);
			sortLang2RB.setEnabled(!visible);
			listLV.setEnabled(!visible);
			addFB.setEnabled(!visible);
		});
	}
}
