////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: DictionariesFacade.java     //
////////////////////////////////////////

package ru.snegir.snegirlingua.database.facade;

import android.app.Activity;

import ru.snegir.snegirlingua.database.Database;
import ru.snegir.snegirlingua.entity.Word;

// All methods should be called in a dedicated thread
public class WordsFacade
{
	public static Word getById(Activity activity, int id)
	{
		return Database.get(activity).words().getById(id);
	}
}
