////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: LanguagesFacade.java        //
////////////////////////////////////////

package ru.snegir.snegirlingua.database.facade;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import java.util.List;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.database.Database;
import ru.snegir.snegirlingua.entity.Language;

// All methods should be called in a dedicated thread
public class LanguagesFacade
{
	public static List<Language> getAll(Activity activity)
	{
		return Database.get(activity).languages().getAll();
	}
	
	public static void insert(Activity activity, String code, String name)
	{
		// Both code and name should be filled
		if (code == null || name == null || code.isEmpty() || name.isEmpty())
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_lang_add_noCodeName, Toast.LENGTH_LONG).show());
			return;
		}
		
		// There shouldn't be another language with the same code
		if (Database.get(activity).languages().get(code) != null)
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_lang_add_exists, Toast.LENGTH_LONG).show());
			return;
		}
		
		try
		{
			Database.get(activity).languages().insert(new Language(code, name));
		}
		catch (SQLiteException e)
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_lang_add, Toast.LENGTH_LONG).show());
		}
	}
	
	public static void update(Activity activity, String code, String name)
	{
		// Both the code and the name should be filled
		if (code == null || name == null || code.isEmpty() || name.isEmpty())
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_lang_edit_noName, Toast.LENGTH_LONG).show());
			return;
		}
		
		Language language = Database.get(activity).languages().get(code);
		// The language with the passed code should exist
		if (language == null)
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_lang_edit_notExist, Toast.LENGTH_LONG).show());
			return;
		}
		
		language.setName(name);
		try
		{
			Database.get(activity).languages().update(language);
		}
		catch (SQLiteException e)
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_lang_edit, Toast.LENGTH_LONG).show());
		}
	}
	
	public static void delete(Activity activity, String code)
	{
		Language language = Database.get(activity).languages().get(code);
		// The language with the passed code should exist
		if (language == null)
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_lang_delete_notExist, Toast.LENGTH_LONG).show());
			return;
		}
		
		try
		{
			Database.get(activity).languages().delete(language);
		}
		catch (SQLiteException e)
		{
			activity.runOnUiThread(() ->
					Toast.makeText(activity, R.string.error_lang_delete, Toast.LENGTH_LONG).show());
		}
	}
}
