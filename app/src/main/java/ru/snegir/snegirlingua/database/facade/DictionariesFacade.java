////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: DictionariesFacade.java     //
////////////////////////////////////////

package ru.snegir.snegirlingua.database.facade;

import android.app.Activity;
import android.util.Pair;

import java.util.List;

import ru.snegir.snegirlingua.database.Database;
import ru.snegir.snegirlingua.entity.Dictionary;

// All methods should be called in a dedicated thread
public class DictionariesFacade
{
	public static List<Dictionary> getForLangs(Activity activity, Pair<String, String> langs)
	{
		return Database.get(activity).dictionaries().getForLangs(langs.first, langs.second);
	}
	
	public static boolean containsTranslation(Activity activity, int dictionary, int translation)
	{
		return !Database.get(activity).dictionaryTranslations().getByContent(dictionary, translation).isEmpty();
	}
}
