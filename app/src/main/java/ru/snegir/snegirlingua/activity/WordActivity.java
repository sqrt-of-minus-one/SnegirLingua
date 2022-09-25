////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: WordActivity.java           //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.adapter.WordDictionaryAdapter;
import ru.snegir.snegirlingua.database.Database;
import ru.snegir.snegirlingua.database.facade.DictionariesFacade;
import ru.snegir.snegirlingua.database.facade.TranslationsFacade;
import ru.snegir.snegirlingua.database.facade.WordsFacade;
import ru.snegir.snegirlingua.entity.Dictionary;
import ru.snegir.snegirlingua.entity.DictionaryTranslation;
import ru.snegir.snegirlingua.entity.Translation;
import ru.snegir.snegirlingua.entity.Word;

public class WordActivity extends Activity
{
	public static final String LANG1 = "lang_1";
	public static final String LANG2 = "lang_2";
	public static final String IS_NEW = "is_new";
	public static final String TRANSLATION_ID = "translation_id";
	
	private TextView lang1TV, lang2TV;
	private EditText word1ET, word2ET;
	private Button cancelBT, saveBT;
	private ProgressBar loadPB;
	private ListView listLV;
	
	private Pair<String, String> langs;
	private boolean isNew; // Is the translation new or it already exists in database (add word or edit word)
	private Translation translation;
	private WordDictionaryAdapter adapter;
	private Database database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word);
		
		lang1TV = findViewById(R.id.word_lang1TV);
		lang2TV = findViewById(R.id.word_lang2TV);
		word1ET = findViewById(R.id.word_word1ET);
		word2ET = findViewById(R.id.word_word2ET);
		cancelBT = findViewById(R.id.word_cancelBT);
		saveBT = findViewById(R.id.word_saveBT);
		loadPB = findViewById(R.id.word_loadPB);
		listLV = findViewById(R.id.word_listLV);
		
		langs = new Pair<>(getIntent().getStringExtra(LANG1), getIntent().getStringExtra(LANG2));
		isNew = getIntent().getBooleanExtra(IS_NEW, true);
		
		lang1TV.setText(langs.first);
		lang2TV.setText(langs.second);
		
		if (isNew)
		{
			cancelBT.setText(R.string.cancel);
		}
		else
		{
			cancelBT.setText(R.string.delete);
		}
		
		loadUi();
	}
	
	/*private void create(String word1, String word2)
	{
		new Thread(() ->
		{
			List<Word> wl1 = database.words().getWord(word1, langs.first);
			List<Word> wl2 = database.words().getWord(word2, langs.second);
			Word w1, w2;
			
			if (wl1.isEmpty())
			{
				w1 = new Word(database.words().getLastId(), word1, langs.first);
				database.words().insert(w1);
			}
			else
			{
				w1 = wl1.get(0);
			}
			
			if (wl2.isEmpty())
			{
				w2 = new Word(database.words().getLastId(), word2, langs.first);
				database.words().insert(w2);
			}
			else
			{
				w2 = wl2.get(0);
			}
			
			List<Translation> tr = database.translations().getTranslation(w1.getId(), w2.getId());
			if (tr.isEmpty())
			{
				translation = new Translation(database.translations().getLastId(), w1.getId(), w2.getId(), false, false);
				database.translations().insert(translation);
				
				boolean[] added = adapter.getChecked();
				for (int i = 0; i < added.length; i++)
				{
					if (added[i])
					{
						DictionaryTranslation dictionaryTranslation =
								new DictionaryTranslation(database.dictionaryTranslations().getLastId(), adapter.getItem(i).getId(), translation.getId());
						database.dictionaryTranslations().insert(dictionaryTranslation);
					}
				}
			}
			else
			{
				WordActivity.this.runOnUiThread(() -> Toast.makeText(WordActivity.this, R.string.word_already_exists, Toast.LENGTH_LONG).show());
			}
		}).start();
	}
	
	private void delete()
	{
		database.translations().delete(translation);
		
		if (database.translations().getWithWord(translation.getWord1()).isEmpty())
		{
			database.words().delete(database.words().getById(translation.getWord1()));
		}
		if (database.translations().getWithWord(translation.getWord2()).isEmpty())
		{
			database.words().delete(database.words().getById(translation.getWord2()));
		}
	}
	
	private void update(String word1, String word2)
	{
		int wi1 = translation.getWord1();
		int wi2 = translation.getWord2();
		
		List<Word> wl1 = database.words().getWord(word1, langs.first);
		List<Word> wl2 = database.words().getWord(word2, langs.second);
		Word w1, w2;
		
		if (wl1.isEmpty())
		{
			w1 = new Word(database.words().getLastId(), word1, langs.first);
			database.words().insert(w1);
		}
		else
		{
			w1 = wl1.get(0);
		}
		
		if (wl2.isEmpty())
		{
			w2 = new Word(database.words().getLastId(), word2, langs.first);
			database.words().insert(w2);
		}
		else
		{
			w2 = wl2.get(0);
		}
		
		List<Translation> tr = database.translations().getTranslation(w1.getId(), w2.getId());
		if (tr.isEmpty())
		{
			translation = new Translation(database.translations().getLastId(), w1.getId(), w2.getId(), false, false);
			database.translations().insert(translation);
			
			boolean[] added = adapter.getChecked();
			for (int i = 0; i < added.length; i++)
			{
				if (added[i])
				{
					DictionaryTranslation dictionaryTranslation =
							new DictionaryTranslation(database.dictionaryTranslations().getLastId(), adapter.getItem(i).getId(), translation.getId());
					database.dictionaryTranslations().insert(dictionaryTranslation);
				}
			}
		}
		else
		{
			WordActivity.this.runOnUiThread(() -> Toast.makeText(WordActivity.this, R.string.word_already_exists, Toast.LENGTH_LONG).show());
		}
	}*/
	
	private void loadUi()
	{
		setPBVisibility(true);
		new Thread(() ->
		{
			database = Database.get(WordActivity.this);
		
			List<Dictionary> dictionaries = DictionariesFacade.getForLangs(WordActivity.this, langs);
			Dictionary[] array = new Dictionary[dictionaries.size()];
			dictionaries.toArray(array);
			boolean[] added = new boolean[dictionaries.size()];
			
			if (isNew)
			{
				adapter = new WordDictionaryAdapter(WordActivity.this, array, added);
				WordActivity.this.runOnUiThread(() ->
				{
					listLV.setAdapter(adapter);
					cancelBT.setOnClickListener(v -> finish());
					saveBT.setOnClickListener(v ->
					{
						setPBVisibility(true);
						new Thread(() ->
						{
							// Which dictionaries the translation should be added to
							boolean[] checked = adapter.getChecked();
							LinkedList<Integer> dictList = new LinkedList<>();
							for (int i = 0; i < checked.length; i++)
							{
								if (checked[i])
								{
									dictList.add(array[i].getId());
								}
							}
							if (TranslationsFacade.insert(WordActivity.this, langs,
									new Pair<>(word1ET.getText().toString(), word2ET.getText().toString()), dictList))
							{
								WordActivity.this.runOnUiThread(WordActivity.this::finish);
							}
							setPBVisibility(false);
						}).start();
					});
					setPBVisibility(false);
				});
			}
			else
			{
				translation = TranslationsFacade.getById(WordActivity.this, getIntent().getIntExtra(TRANSLATION_ID, 0));
				adapter = new WordDictionaryAdapter(WordActivity.this, array, added);
				for (int i = 0; i < added.length; i++)
				{
					added[i] = DictionariesFacade.containsTranslation(WordActivity.this, array[i].getId(), translation.getId());
				}
				String str1 = WordsFacade.getById(WordActivity.this, translation.getWord1()).getWord();
				String str2 = WordsFacade.getById(WordActivity.this, translation.getWord2()).getWord();
				WordActivity.this.runOnUiThread(() ->
				{
					word1ET.setText(str2);
					word2ET.setText(str1);
					listLV.setAdapter(adapter);
					cancelBT.setOnClickListener(v -> new AlertDialog.Builder(WordActivity.this)
								.setMessage(R.string.sure_delete_word)
								.setPositiveButton(R.string.delete, (dialog, which) ->
								{
									setPBVisibility(true);
									new Thread(() ->
									{
										TranslationsFacade.delete(WordActivity.this, translation.getId());
										WordActivity.this.runOnUiThread(WordActivity.this::finish);
									}).start();
								})
								.setNegativeButton(R.string.cancel, null)
								.create()
								.show());
					saveBT.setOnClickListener(v ->
					{
						new Thread(() ->
						{
							TranslationsFacade.update(WordActivity.this, translation.getId(),
									new Pair<>(word1ET.getText().toString(), word2ET.getText().toString()));
							// Todo: update dictionaries
							WordActivity.this.runOnUiThread(WordActivity.this::finish);
						}).start();
					});
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
			lang1TV.setEnabled(!visible);
			lang2TV.setEnabled(!visible);
			word1ET.setEnabled(!visible);
			word2ET.setEnabled(!visible);
			cancelBT.setEnabled(!visible);
			saveBT.setEnabled(!visible);
			listLV.setEnabled(!visible);
		});
	}
}
