////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: WordsActivity.java          //
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
	private ImageButton infoIB;
	private ProgressBar loadPB;
	private Button addBT;
	private ListView list1LV, list2LV;
	
	private WordAdapter adapter1, adapter2;
	
	private Pair<String, String> langs;
	
	// If true, the word list should be reloaded from database
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
		
		needsToBeReloaded = true;
		
		sortRG = findViewById(R.id.words_sortRG);
		sortLang1RB = findViewById(R.id.words_sortLang1RB);
		sortLang2RB = findViewById(R.id.words_sortLang2RB);
		infoIB = findViewById(R.id.words_infoIB);
		loadPB = findViewById(R.id.words_loadPB);
		addBT = findViewById(R.id.words_addBT);
		list1LV = findViewById(R.id.words_list1LV);
		list2LV = findViewById(R.id.words_list2LV);
		
		// Languages are supposed to be sorted
		langs = new Pair<>(getIntent().getStringExtra(LANG_1), getIntent().getStringExtra(LANG_2));
		
		sortLang1RB.setText(getString(R.string.sort, langs.first));
		sortLang2RB.setText(getString(R.string.sort, langs.second));
		
		sortRG.setOnCheckedChangeListener((group, checkedId) -> loadWords());
		addBT.setOnClickListener(v ->
		{
			Intent wordI = new Intent(WordsActivity.this, WordActivity.class);
			wordI.putExtra(WordActivity.LANG_1, langs.first);
			wordI.putExtra(WordActivity.LANG_2, langs.second);
			wordI.putExtra(WordActivity.IS_NEW, true);
			needsToBeReloaded = true;
			startActivity(wordI);
		});
		sortLang1RB.setChecked(true); // Also calls loadWords()
		
		infoIB.setOnClickListener(v -> new AlertDialog.Builder(WordsActivity.this)
				.setTitle(R.string.a_words_help_title)
				.setMessage(R.string.a_words_help)
				.setPositiveButton(R.string.ok, null)
				.create()
				.show());
		infoIB.setOnLongClickListener(v ->
		{
			Toast.makeText(WordsActivity.this, R.string.help, Toast.LENGTH_LONG).show();
			return true;
		});
	}
	
	public void loadWords()
	{
		new Thread(() ->
		{
			if (needsToBeReloaded)
			{
				setPBVisibility(true);
				needsToBeReloaded = false;
				List<Translation> translations1 = TranslationsFacade.getForLangs(WordsActivity.this, langs, false);
				List<Translation> translations2 = TranslationsFacade.getForLangs(WordsActivity.this, langs, true);
				adapter1 = new WordAdapter(WordsActivity.this, translations1.toArray(new Translation[0]), langs);
				adapter2 = new WordAdapter(WordsActivity.this, translations2.toArray(new Translation[0]), langs);
				WordsActivity.this.runOnUiThread(() ->
				{
					list1LV.setAdapter(adapter1);
					list2LV.setAdapter(adapter2);
					setPBVisibility(false);
					if (sortLang1RB.isChecked())
					{
						list2LV.setVisibility(View.GONE);
						list1LV.setVisibility(View.VISIBLE);
					}
					else
					{
						list1LV.setVisibility(View.GONE);
						list2LV.setVisibility(View.VISIBLE);
					}
				});
			}
			else
			{
				WordsActivity.this.runOnUiThread(() ->
				{
					if (sortLang1RB.isChecked())
					{
						list2LV.setVisibility(View.GONE);
						list1LV.setVisibility(View.VISIBLE);
						adapter1.updateAll();
					}
					else
					{
						list1LV.setVisibility(View.GONE);
						list2LV.setVisibility(View.VISIBLE);
						adapter2.updateAll();
					}
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
			addBT.setEnabled(!visible);
			list1LV.setEnabled(!visible);
			list2LV.setEnabled(!visible);
		});
	}
}
