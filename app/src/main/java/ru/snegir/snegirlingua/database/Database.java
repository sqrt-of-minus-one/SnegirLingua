////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: Database.java               //
////////////////////////////////////////

package ru.snegir.snegirlingua.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.snegir.snegirlingua.entity.*;

@androidx.room.Database(entities = { Language.class, Word.class, Translation.class, Dictionary.class,
									 DictionaryTranslation.class, Table.class, Row.class, Entry.class, RowEntry.class },
						version = 1)
public abstract class Database extends RoomDatabase
{
	public abstract Languages languages();
	public abstract Words words();
	public abstract Translations translations();
	public abstract Dictionaries dictionaries();
	public abstract DictionaryTranslations dictionaryTranslations();
	public abstract Tables tables();
	public abstract Rows rows();
	public abstract Entries entries();
	public abstract RowEntries rowEntries();

	private static final String DB_NAME = "snegirlingua.db";
	private static volatile Database INSTANCE = null;

	public synchronized static Database get(Context context)
	{
		if (INSTANCE == null)
		{
			INSTANCE = create(context, false);
		}
		return INSTANCE;
	}

	private static Database create(Context context, boolean memoryOnly)
	{
		RoomDatabase.Builder<Database> builder;
		if (memoryOnly)
		{
			builder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), Database.class);
		}
		else
		{
			builder = Room.databaseBuilder(context.getApplicationContext(), Database.class, DB_NAME);
		}
		return builder.build();
	}
}
