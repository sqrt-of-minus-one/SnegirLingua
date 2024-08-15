////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: DictionariesFacade.java     //
////////////////////////////////////////

package ru.snegir.snegirlingua.database.facade;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.util.Pair;
import android.widget.Toast;

import java.util.List;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.database.Database;
import ru.snegir.snegirlingua.entity.Dictionary;
import ru.snegir.snegirlingua.entity.DictionaryTranslation;
import ru.snegir.snegirlingua.entity.Language;

// All methods should be called in a dedicated thread
public class DictionariesFacade
{
	public static void insert(Activity activity, Pair<String, String> langs, String name, int color, List<Integer> translations)
	{
		// The name should be filled
		if (name != null && !name.isEmpty())
		{
			try
			{
				Database database = Database.get(activity);
				database.dictionaries().insert(new Dictionary(database.dictionaries().getLastId() + 1, name, langs.first, langs.second, color));
				for (Integer i : translations)
				{
					includeTranslation(activity, id, i);
				}
			}
			catch (SQLiteException e)
			{
				activity.runOnUiThread(() ->
						Toast.makeText(activity, R.string.error_lang_add, Toast.LENGTH_LONG).show());
			}
		}
		else
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_dictionary_no_name, Toast.LENGTH_LONG).show());
		}
	}
	
	public static void update(Activity activity, int id, String name, int color)
	{
		// The name should be filled
		if (name != null && !name.isEmpty())
		{
			Dictionary dictionary = Database.get(activity).dictionaries().getById(id);
			// The language with the passed code should exist
			if (dictionary != null)
			{
				dictionary.setName(name);
				dictionary.setColor(color);
				try
				{
					Database.get(activity).dictionaries().update(dictionary);
				}
				catch (SQLiteException e)
				{
					activity.runOnUiThread(() ->
							Toast.makeText(activity, R.string.error_dictionary_edit, Toast.LENGTH_LONG).show());
				}
			}
			else
			{
				activity.runOnUiThread(() ->
						Toast.makeText(activity, R.string.error_dictionary_edit, Toast.LENGTH_LONG).show());
			}
		}
		else
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_dictionary_no_name, Toast.LENGTH_LONG).show());
		}
	}
	
	public static void delete(Activity activity, int dictionary)
	{
		Dictionary dictionaryObj = Database.get(activity).dictionaries().getById(dictionary);
		if (dictionaryObj != null)
		{
			try
			{
				Database.get(activity).dictionaries().delete(dictionaryObj);
			}
			catch (SQLiteException e)
			{
				activity.runOnUiThread(() ->
						Toast.makeText(activity, R.string.error_dictionary_delete, Toast.LENGTH_LONG).show());
			}
		}
		else
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_dictionary_delete, Toast.LENGTH_LONG).show());
		}
	}
	
	public static List<Dictionary> getForLangs(Activity activity, Pair<String, String> langs)
	{
		return Database.get(activity).dictionaries().getForLangs(langs.first, langs.second);
	}
	
	public static boolean containsTranslation(Activity activity, int dictionary, int translation)
	{
		return !Database.get(activity).dictionaryTranslations().getByContent(dictionary, translation).isEmpty();
	}
	
	public static int countTranslations(Activity activity, int dictionary)
	{
		return Database.get(activity).dictionaryTranslations().getForDictionary(dictionary).size();
	}
	
	public static void includeTranslation(Activity activity, int dictionary, int translation)
	{
		Database database = Database.get(activity);
		if (database.dictionaryTranslations().getByContent(dictionary, translation).isEmpty())
		{
			DictionaryTranslation dictionaryTranslation =
					new DictionaryTranslation(database.dictionaryTranslations().getLastId() + 1, dictionary, translation);
			database.dictionaryTranslations().insert(dictionaryTranslation);
		}
	}
	
	public static void excludeTranslation(Activity activity, int dictionary, int translation)
	{
		Database database = Database.get(activity);
		List<DictionaryTranslation> dictionaryTranslations =
				database.dictionaryTranslations().getByContent(dictionary, translation);
		if (!dictionaryTranslations.isEmpty())
		{
			database.dictionaryTranslations().delete(dictionaryTranslations.get(0));
		}
	}
}
