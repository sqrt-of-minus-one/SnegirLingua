////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: DictionaryActivity.java     //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.adapter.DictionaryWordAdapter;
import ru.snegir.snegirlingua.adapter.WordDictionaryAdapter;
import ru.snegir.snegirlingua.database.facade.DictionariesFacade;
import ru.snegir.snegirlingua.database.facade.TranslationsFacade;
import ru.snegir.snegirlingua.database.facade.WordsFacade;
import ru.snegir.snegirlingua.entity.Dictionary;
import ru.snegir.snegirlingua.entity.Translation;

public class DictionaryActivity extends Activity
{
	// The intent is supposed to have:
	//     two language codes (LANG_1 and LANG_2), which should be sorted
	//     IS_NEW: whether a new dictionary is being added
	//     DICTIONARY_ID: id of the dictionary which is going to be edited, if the dictionary isn't new
	public static final String LANG_1 = "lang_1";
	public static final String LANG_2 = "lang_2";
	public static final String IS_NEW = "is_new";
	public static final String DICTIONARY_ID = "dictionary_id";
	
	private ImageButton colorIB;
	private EditText nameET;
	private RadioGroup sortRG;
	private RadioButton sortLang1RB, sortLang2RB;
	private Button cancelBT, saveBT;
	private ProgressBar loadPB;
	private ListView listLV;
	
	private Pair<String, String> langs;
	private boolean isNew; // Is the dictionary new or it already exists in database (add dictionary or edit dictionary)
	private Dictionary dictionary;
	private DictionaryWordAdapter adapter;
	@ColorInt private int color;
	public HashMap<Integer, Boolean> originallyAdded;
	public HashMap<Integer, Boolean> added; // Which translation was added to the dictionary
	private Translation[] translationsLang1; // Sorted by lang 1
	private Translation[] translationsLang2; // Sorted by lang 2
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dictionary);
		
		colorIB = findViewById(R.id.dictionary_colorIB);
		nameET = findViewById(R.id.dictionary_nameET);
		sortRG = findViewById(R.id.dictionary_sortRG);
		sortLang1RB = findViewById(R.id.dictionary_sortLang1RB);
		sortLang2RB = findViewById(R.id.dictionary_sortLang2RB);
		cancelBT = findViewById(R.id.dictionary_cancelBT);
		saveBT = findViewById(R.id.dictionary_saveBT);
		loadPB = findViewById(R.id.dictionary_loadPB);
		listLV = findViewById(R.id.dictionary_listLV);
		
		langs = new Pair<>(getIntent().getStringExtra(LANG_1), getIntent().getStringExtra(LANG_2));
		isNew = getIntent().getBooleanExtra(IS_NEW, true);
		
		sortLang1RB.setText(getString(R.string.sort, langs.first));
		sortLang2RB.setText(getString(R.string.sort, langs.second));
		
		load();
	}
	
	private void load()
	{
		setPBVisibility(true);
		new Thread(() ->
		{
			List<Translation> translations1 = TranslationsFacade.getForLangs(DictionaryActivity.this, langs, false);
			List<Translation> translations2 = TranslationsFacade.getForLangs(DictionaryActivity.this, langs, true);
			translationsLang1 = new Translation[translations1.size()];
			translationsLang2 = new Translation[translations2.size()];
			added = new HashMap<>(translations1.size());
			translations1.toArray(translationsLang1);
			translations2.toArray(translationsLang2);
			
			if (isNew)
			{
				for (Translation i : translationsLang1)
				{
					added.put(i.getId(), false);
				}
				
				DictionaryActivity.this.runOnUiThread(() ->
				{
					cancelBT.setText(R.string.cancel);
					cancelBT.setOnClickListener(v -> finish());
					saveBT.setOnClickListener(v ->
					{
						setPBVisibility(true);
						new Thread(() ->
						{
							// Which translations should be added to the dictionary
							LinkedList<Integer> translationsList = new LinkedList<>(); // Translations ID's
							for (Translation i : translationsLang1)
							{
								if (added.get(i))
								{
									translationsList.add(i.getId());
								}
							}
							if (DictionariesFacade.insert(DictionaryActivity.this, langs, nameET.getText().toString(), colorIB.getBackground(), translationsList);
														  (WordActivity.this, langs,
									new Pair<>(word1ET.getText().toString(), word2ET.getText().toString()), dictList))
							{
								WordActivity.this.runOnUiThread(WordActivity.this::finish);
							}
							setPBVisibility(false);
						}).start();
					});
					color = 0x00ffff00;
				});
			}
			else
			{
				for (Translation i : translationsLang1)
				{
					added.put(i.getId(), DictionariesFacade.containsTranslation(DictionaryActivity.this,
							dictionary.getId(), i.getId()));
				}
				
				DictionaryActivity.this.runOnUiThread(() ->
				{
					cancelBT.setText(R.string.delete);
					cancelBT.setOnClickListener(v ->
					{
						// Todo: delete
					});
					saveBT.setOnClickListener(v ->
					{
						// Todo: save
					});
				});
			}
			originallyAdded = new HashMap<>(added.size());
			originallyAdded.putAll(added);
			
			DictionaryActivity.this.runOnUiThread(() ->
			{
				sortRG.setOnCheckedChangeListener((group, checkedId) -> loadList());
				sortLang1RB.setChecked(true); // Also calls loadList()
			});
		}).start();
	}
	
	private void loadList()
	{
		setPBVisibility(true);
		new Thread(() ->
		{
			if (sortLang1RB.isChecked())
			{
				adapter = new DictionaryWordAdapter(DictionaryActivity.this, translationsLang1);
			}
			else
			{
				adapter = new DictionaryWordAdapter(DictionaryActivity.this, translationsLang2);
			}
			DictionaryActivity.this.runOnUiThread(() ->
			{
				listLV.setAdapter(adapter);
				cancelBT.setOnClickListener(v -> finish());
			});
		}).start();
	}
	
	public void setPBVisibility(boolean visible)
	{
		runOnUiThread(() ->
		{
			loadPB.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
			colorIB.setEnabled(!visible);
			nameET.setEnabled(!visible);
			sortRG.setEnabled(!visible);
			sortLang1RB.setEnabled(!visible);
			sortLang2RB.setEnabled(!visible);
			cancelBT.setEnabled(!visible);
			saveBT.setEnabled(!visible);
			listLV.setEnabled(!visible);
		});
	}
}
