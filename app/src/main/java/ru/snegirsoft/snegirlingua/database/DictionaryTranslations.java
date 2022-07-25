    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: DictionaryTranslations.java //
////////////////////////////////////////

package ru.snegirsoft.snegirlingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ru.snegirsoft.snegirlingua.entity.DictionaryTranslation;

@Dao
public interface DictionaryTranslations
{
	@Query("SELECT * FROM dictionary_translations WHERE id = :id")
	DictionaryTranslation getById(int id);

	@Insert
	void insert(DictionaryTranslation... dictionaryTranslations);

	@Delete
	void delete(DictionaryTranslation... dictionaryTranslations);

	@Update
	void update(DictionaryTranslation... dictionaryTranslations);
}
