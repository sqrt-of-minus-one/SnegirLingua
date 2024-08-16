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
		if (langs.first.compareTo(langs.second) > 0)
		{
			langs = new Pair<>(langs.second, langs.first);
		}
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
		Database database = Database.get(activity);
		Translation translationObj = database.translations().getById(translation);
		// The translation must exist
		if (translationObj == null)
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_translation_set_learned_not_exist, Toast.LENGTH_LONG).show());
			return;
		}
		
		// The words of the translation
		Pair<Word, Word> words = new Pair<>(
				database.words().getById(translationObj.getWord1()),
				database.words().getById(translationObj.getWord2()));
		// Both words must exist
		if (words.first == null || words.second == null)
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_translation_set_learned_invalid_word, Toast.LENGTH_LONG).show());
			return;
		}
		// Which word is about to be marked
		int word = isSecond ? words.second.getId() : words.first.getId();
		// Set learned for all translations with the word
		List<Translation> translations = database.translations().getWithWordForLangs(word, words.first.getLanguage(), words.second.getLanguage());
		for (Translation i : translations)
		{
			if (i.getWord1() == word)
			{
				i.setLearned1(isLearned);
			}
			else
			{
				i.setLearned2(isLearned);
			}
			try
			{
				database.translations().update(i);
			}
			catch (SQLiteException e)
			{
				activity.runOnUiThread(() ->
						Toast.makeText(activity, R.string.error_translation_set_learned, Toast.LENGTH_LONG).show());
				return;
			}
		}
	}
	
	// Returns true if the insertion has been performed
	public static boolean insert(Activity activity, Pair<String, String> langs, Pair<String, String> words, Pair<String, String> comments, List<Integer> dictionaries)
	{
		if (langs.first.compareTo(langs.second) > 0)
		{
			langs = new Pair<>(langs.second, langs.first);
			words = new Pair<>(words.second, words.first);
			comments = new Pair<>(comments.second, comments.first);
		}
		try
		{
			Database database = Database.get(activity);
			// Both words should be filled
			if (words.first == null || words.second == null || words.first.isEmpty() || !words.second.isEmpty())
			{
				activity.runOnUiThread(() ->
						Toast.makeText(activity, R.string.error_translation_add_no_word, Toast.LENGTH_LONG).show());
				return false;
			}
			// Each list is supposed to contain <=1 element
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
			
			if (!database.translations().getTranslation(word1.getId(), word2.getId()).isEmpty())
			{
				activity.runOnUiThread(() ->
						Toast.makeText(activity, R.string.error_translation_add_exists, Toast.LENGTH_LONG).show());
				return false;
			}
			Translation translation = new Translation(database.translations().getLastId() + 1,
													  word1.getId(), word2.getId(), comments.first, comments.second, false, false);
			database.translations().insert(translation);
			// Add the translation into the dictionaries
			for (Integer i : dictionaries)
			{
				DictionariesFacade.includeTranslation(activity, i, translation.getId());
			}
			return true;
		}
		catch (SQLiteException e)
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_translation_add, Toast.LENGTH_LONG).show());
			return false;
		}
	}
	
	public static void update(Activity activity, int translation, Pair<String, String> words, Pair<String, String> comments)
	{
		if (words.first == null || words.second == null || words.first.isEmpty() || words.second.isEmpty())
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_translation_edit_no_word, Toast.LENGTH_LONG).show());
			return;
		}
		try
		{
			Database database = Database.get(activity);
			Translation translationObj = database.translations().getById(translation);
			if (translationObj == null)
			{
				activity.runOnUiThread(() -> Toast.makeText(activity, R.string.error_translation_edit_not_exist, Toast.LENGTH_LONG).show());
				return;
			}
			Pair<Word, Word> oldWords = new Pair<>(database.words().getById(translationObj.getWord1()), database.words().getById(translationObj.getWord2()));
			boolean deleteFirst = false, deleteSecond = false; // Should we delete the old words
					// (we can't delete them too early because this would cause a cascade deletion of the translation)
			if (!oldWords.first.getWord().equals(words.first)) // If the first word has been changed
			{
				List<Translation> translationsWithWord = database.translations().getWithWord(oldWords.first.getId()); // The translations with the old word (contains at least this translation)
				List<Word> newWord = database.words().getWord(words.first, oldWords.first.getLanguage()); // The new word if it already exists
				if (newWord.size() > 0) // If the new word already exists
				{
					translationObj.setWord1(newWord.get(0).getId());
					if (translationsWithWord.size() == 1 && translationsWithWord.get(0).getId() == translationObj.getId()) // If the old word is only used by this translation, delete it
					{
						deleteFirst = true;
					}
				}
				else
				{
					if (translationsWithWord.size() == 1 && translationsWithWord.get(0).getId() == translationObj.getId()) // If the old word is only used by this translation, we can simply change the word
					{
						database.words().update(new Word(oldWords.first.getId(), words.first, oldWords.first.getLanguage()));
					}
					else
					{
						int id = database.words().getLastId() + 1;
						database.words().insert(new Word(id, words.first, oldWords.first.getLanguage()));
						translationObj.setWord1(id);
					}
				}
			}
			if (!oldWords.second.getWord().equals(words.second)) // If the second word has been changed
			{
				List<Translation> translationsWithWord = database.translations().getWithWord(oldWords.second.getId()); // The translations with the old word (contains at least this translation)
				List<Word> newWord = database.words().getWord(words.second, oldWords.second.getLanguage()); // The new word if it already exists
				if (newWord.size() > 0) // If the new word already exists
				{
					translationObj.setWord1(newWord.get(0).getId());
					if (translationsWithWord.size() == 1 && translationsWithWord.get(0).getId() == translationObj.getId()) // If the old word is only used by this translation, delete it
					{
						deleteSecond = true;
					}
				}
				else
				{
					if (translationsWithWord.size() == 1 && translationsWithWord.get(0).getId() == translationObj.getId()) // If the old word is only used by this translation, we can simply change the word
					{
						database.words().update(new Word(oldWords.second.getId(), words.second, oldWords.second.getLanguage()));
					}
					else
					{
						int id = database.words().getLastId() + 1;
						database.words().insert(new Word(id, words.second, oldWords.second.getLanguage()));
						translationObj.setWord1(id);
					}
				}
			}
			translationObj.setComment1(comments.first);
			translationObj.setComment2(comments.second);
			database.translations().update(translationObj);
			if (deleteFirst)
			{
				database.words().delete(oldWords.first);
			}
			if (deleteSecond)
			{
				database.words().delete(oldWords.second);
			}
		}
		catch (SQLiteException e)
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_translation_edit, Toast.LENGTH_LONG).show());
		}
	}
	
	public static void delete(Activity activity, int translation)
	{
		Database database = Database.get(activity);
		Translation translationObj = database.translations().getById(translation);
		if (translationObj == null)
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_translation_delete_not_exist, Toast.LENGTH_LONG).show());
			return;
		}
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
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_translation_delete, Toast.LENGTH_LONG).show());
		}
	}
}
