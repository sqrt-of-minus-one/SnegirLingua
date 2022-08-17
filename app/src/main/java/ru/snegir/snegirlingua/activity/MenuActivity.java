    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: SettingsActivity.java       //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ru.snegir.snegirlingua.R;

public class MenuActivity extends Activity
{
	public static final String LANG_1 = "lang_1";
	public static final String LANG_2 = "lang_2";
	
	private TextView lang1TV, lang2TV;
	private ImageButton dictionariesIB, wordsIB, tablesIB, trainingIB;
	
	private Pair<String, String> langs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		lang1TV = findViewById(R.id.menu_lang1TV);
		lang2TV = findViewById(R.id.menu_lang2TV);
		dictionariesIB = findViewById(R.id.menu_dictionariesIB);
		wordsIB = findViewById(R.id.menu_wordsIB);
		tablesIB = findViewById(R.id.menu_tablesIB);
		trainingIB = findViewById(R.id.menu_trainingIB);
		
		langs = new Pair<>(getIntent().getStringExtra(LANG_1), getIntent().getStringExtra(LANG_2));
		lang1TV.setText(langs.first);
		lang2TV.setText(langs.second);
		
		dictionariesIB.setOnClickListener(v ->
		{
			// Todo: start DictionariesActivity
		});
		dictionariesIB.setOnLongClickListener(v ->
		{
			Toast.makeText(MenuActivity.this, R.string.menu_dictionaries_info, Toast.LENGTH_LONG).show();
			return true;
		});
		
		wordsIB.setOnClickListener(v ->
		{
			// Todo: start WordsActivity
		});
		wordsIB.setOnLongClickListener(v ->
		{
			Toast.makeText(MenuActivity.this, R.string.menu_words_info, Toast.LENGTH_LONG).show();
			return true;
		});
		
		tablesIB.setOnClickListener(v ->
		{
			// Todo: start TablesActivity
		});
		tablesIB.setOnLongClickListener(v ->
		{
			Toast.makeText(MenuActivity.this, R.string.menu_tables_info, Toast.LENGTH_LONG).show();
			return true;
		});
		
		trainingIB.setOnClickListener(v ->
		{
			// Todo: start TrainingActivity
		});
		trainingIB.setOnLongClickListener(v ->
		{
			Toast.makeText(MenuActivity.this, R.string.menu_training_info, Toast.LENGTH_LONG).show();
			return true;
		});
	}
}
