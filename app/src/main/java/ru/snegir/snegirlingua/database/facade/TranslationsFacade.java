////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: TranslationsFacade.java     //
////////////////////////////////////////

package ru.snegir.snegirlingua.database.facade;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.util.Pair;
import android.widget.Toast;

import java.util.List;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.database.Database;
import ru.snegir.snegirlingua.entity.DictionaryTranslation;
import ru.snegir.snegirlingua.entity.Translation;
import ru.snegir.snegirlingua.entity.Word;

// All methods should be called in a dedicated thread
public class TranslationsFacade
{
	public static Translation getById(Activity activity, int id)
	{
		return Database.get(activity).translations().getById(id);
	}
	
	public static List<Translation> getForLangs(Activity activity, Pair<String, String> langs, boolean sortBySecond)
	{
		if (sortBySecond)
		{
			return Database.get(activity).translations().getForLangOrderBySecond(langs.first, langs.second);
		}
		else
		{
			return Database.get(activity).translations().getForLangOrderByFirst(langs.first, langs.second);
		}
	}
	
	public static void setLearned(Activity activity, int translation, boolean isSecond, boolean isLearned)
	{
		// Todo: set learned
	}
	
	// Returns true if the insertion has been performed
	public static boolean insert(Activity activity, Pair<String, String> langs, Pair<String, String> words, List<Integer> dictionaries)
	{
		try
		{
			Database database = Database.get(activity);
			// Both words should be filled
			if (words.first != null && words.second != null && !words.first.isEmpty() && !words.second.isEmpty())
			{
				Pair<List<Word>, List<Word>> wordLists = new Pair<>(database.words().getWord(words.first, langs.first), database.words().getWord(words.second, langs.second));
				Word word1, word2;
				
				// If the database doesn't contain the word, add it, otherwise use existing word
				if (wordLists.first.isEmpty())
				{
					word1 = new Word(database.words().getLastId() + 1, words.first, langs.first);
					database.words().insert(word1);
				}
				else
				{
					word1 = wordLists.first.get(0);
				}
				// Do the same with the second word
				if (wordLists.second.isEmpty())
				{
					word2 = new Word(database.words().getLastId() + 1, words.second, langs.second);
					database.words().insert(word2);
				}
				else
				{
					word2 = wordLists.second.get(0);
				}
				
				if (database.translations().getTranslation(word1.getId(), word2.getId()).isEmpty())
				{
					Translation translation = new Translation(database.translations().getLastId() + 1,
															  word1.getId(), word2.getId(), false, false);
					database.translations().insert(translation);
					// Add the translation into the dictionaries
					for (Integer i : dictionaries)
					{
						DictionaryTranslation dictionaryTranslation =
								new DictionaryTranslation(database.dictionaryTranslations().getLastId() + 1, i, translation.getId());
						database.dictionaryTranslations().insert(dictionaryTranslation);
					}
					return true;
				}
				else
				{
					activity.runOnUiThread(() -> Toast.makeText(activity, R.string.error_word_already_exists, Toast.LENGTH_LONG).show());
					return false;
				}
			}
			else
			{
				activity.runOnUiThread(() -> Toast.makeText(activity, R.string.error_word_no_word, Toast.LENGTH_LONG).show());
				return false;
			}
		}
		catch (SQLiteException e)
		{
			activity.runOnUiThread(() -> Toast.makeText(activity, R.string.error_word_add, Toast.LENGTH_LONG).show());
			return false;
		}
	}
	
	public static void update(Activity activity, int translation, Pair<String, String> words)
	{
		// Todo: update translation
	}
	
	public static void delete(Activity activity, int translation)
	{
		Database database = Database.get(activity);
		Translation translationObj = database.translations().getById(translation);
		if (translationObj != null)
		{
			try
			{
				Pair<Word, Word> words = new Pair<>(
						database.words().getById(translationObj.getWord1()),
						database.words().getById(translationObj.getWord2()));
				database.translations().delete(translationObj);
				if (database.translations().getWithWord(words.first.getId()).isEmpty())
				{
					database.words().delete(words.first);
				}
				if (database.translations().getWithWord(words.second.getId()).isEmpty())
				{
					database.words().delete(words.second);
				}
			}
			catch (SQLiteException e)
			{
				activity.runOnUiThread(() -> Toast.makeText(activity, R.string.error_word_delete, Toast.LENGTH_LONG).show());
			}
		}
		else
		{
			activity.runOnUiThread(() -> Toast.makeText(activity, R.string.error_word_delete, Toast.LENGTH_LONG).show());
		}
	}
}
