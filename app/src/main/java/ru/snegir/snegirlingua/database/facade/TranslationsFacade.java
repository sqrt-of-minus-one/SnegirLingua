////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: TranslationsFacade.java     //
////////////////////////////////////////

package ru.snegir.snegirlingua.database.facade;

import android.app.Activity;
import android.util.Pair;

import java.util.List;

import ru.snegir.snegirlingua.database.Database;
import ru.snegir.snegirlingua.entity.Translation;

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
}
