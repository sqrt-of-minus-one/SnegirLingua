    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: WordsActivity.java          //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import android.app.Activity;
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
import ru.snegir.snegirlingua.database.Database;
import ru.snegir.snegirlingua.entity.Translation;

	public class WordsActivity extends Activity
{
	public static final String LANG1 = "lang_1";
	public static final String LANG2 = "lang_2";
	
	private RadioGroup sortRG;
	private RadioButton sortLang1RB, sortLang2RB;
	private ProgressBar loadPB;
	private ListView listLV;
	private FloatingActionButton addFB;
	
	private Pair<String, String> langs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_words);
		
		sortRG = findViewById(R.id.words_sortRG);
		sortLang1RB = findViewById(R.id.words_sortLang1RB);
		sortLang2RB = findViewById(R.id.words_sortLang2RB);
		loadPB = findViewById(R.id.words_loadPB);
		listLV = findViewById(R.id.words_listLV);
		addFB = findViewById(R.id.words_addFB);
		
		langs = new Pair<>(getIntent().getStringExtra(LANG1), getIntent().getStringExtra(LANG2));
		
		sortLang1RB.setText(langs.first + " ↓");
		sortLang2RB.setText(langs.second + " ↓");
		
		sortRG.setOnCheckedChangeListener((group, checkedId) ->
		{
			load();
		});
		addFB.setOnClickListener(v ->
		{
			// Todo: add word
		});
		sortLang1RB.setChecked(true);
	}
	
	private void load()
	{
		loadPB.setVisibility(View.VISIBLE);
		new Thread(() ->
		{
			List<Translation> translations = Database.get(WordsActivity.this).translations().getForLang(langs.first, langs.second);
			Translation[] array = new Translation[translations.size()];
			translations.toArray(array);
			WordAdapter adapter = new WordAdapter(WordsActivity.this, array);
			WordsActivity.this.runOnUiThread(() ->
			{
				listLV.setAdapter(adapter);
				loadPB.setVisibility(View.INVISIBLE);
			});
		}).start();
	}
}
