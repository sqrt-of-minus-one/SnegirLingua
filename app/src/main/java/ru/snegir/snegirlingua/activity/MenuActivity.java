////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: MenuActivity.java           //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ru.snegir.snegirlingua.R;

public class MenuActivity extends Activity
{
	// The intent is supposed to have two language codes (LANG_1 and LANG_2), which should be sorted
	public static final String LANG_1 = "lang_1";
	public static final String LANG_2 = "lang_2";
	
	private TextView lang1TV, lang2TV;
	private ImageButton infoIB, dictionariesIB, wordsIB, tablesIB, trainingIB;
	
	private Pair<String, String> langs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		lang1TV = findViewById(R.id.menu_lang1TV);
		lang2TV = findViewById(R.id.menu_lang2TV);
		infoIB = findViewById(R.id.menu_infoIB);
		dictionariesIB = findViewById(R.id.menu_dictionariesIB);
		wordsIB = findViewById(R.id.menu_wordsIB);
		tablesIB = findViewById(R.id.menu_tablesIB);
		trainingIB = findViewById(R.id.menu_trainingIB);
		
		// Languages are supposed to be sorted
		langs = new Pair<>(getIntent().getStringExtra(LANG_1), getIntent().getStringExtra(LANG_2));
		lang1TV.setText(langs.first);
		lang2TV.setText(langs.second);
		
		dictionariesIB.setOnClickListener(v ->
		{
			Intent dictionariesI = new Intent(MenuActivity.this, DictionariesActivity.class);
			dictionariesI.putExtra(DictionariesActivity.LANG_1, langs.first);
			dictionariesI.putExtra(DictionariesActivity.LANG_2, langs.second);
			startActivity(dictionariesI);
		});
		dictionariesIB.setOnLongClickListener(v ->
		{
			Toast.makeText(MenuActivity.this, R.string.a_menu_dictionaries_hint, Toast.LENGTH_LONG).show();
			return true;
		});
		
		wordsIB.setOnClickListener(v ->
		{
			Intent wordsI = new Intent(MenuActivity.this, WordsActivity.class);
			wordsI.putExtra(WordsActivity.LANG_1, langs.first);
			wordsI.putExtra(WordsActivity.LANG_2, langs.second);
			startActivity(wordsI);
		});
		wordsIB.setOnLongClickListener(v ->
		{
			Toast.makeText(MenuActivity.this, R.string.a_menu_words_hint, Toast.LENGTH_LONG).show();
			return true;
		});
		
		tablesIB.setOnClickListener(v ->
		{
			// Todo: start TablesActivity
		});
		tablesIB.setOnLongClickListener(v ->
		{
			Toast.makeText(MenuActivity.this, R.string.a_menu_tables_hint, Toast.LENGTH_LONG).show();
			return true;
		});
		
		trainingIB.setOnClickListener(v ->
		{
			// Todo: start TrainingActivity
		});
		trainingIB.setOnLongClickListener(v ->
		{
			Toast.makeText(MenuActivity.this, R.string.a_menu_training_hint, Toast.LENGTH_LONG).show();
			return true;
		});
		
		infoIB.setOnClickListener(v -> new AlertDialog.Builder(MenuActivity.this)
				.setTitle(R.string.a_menu_help_title)
				.setMessage(R.string.a_menu_help)
				.setPositiveButton(R.string.ok, null)
				.create()
				.show());
		infoIB.setOnLongClickListener(v ->
		{
			Toast.makeText(MenuActivity.this, R.string.help, Toast.LENGTH_LONG).show();
			return true;
		});
	}
}
