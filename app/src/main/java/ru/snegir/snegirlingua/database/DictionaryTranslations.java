////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: DictionaryTranslations.java //
////////////////////////////////////////

package ru.snegir.snegirlingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.snegir.snegirlingua.entity.DictionaryTranslation;

@Dao
public interface DictionaryTranslations
{
	@Query("SELECT * FROM dictionary_translations WHERE id = :id")
	DictionaryTranslation getById(int id);
	
	@Query("SELECT * FROM dictionary_translations WHERE dictionary = :dictionary AND translation = :translation")
	List<DictionaryTranslation> getByContent(int dictionary, int translation);
	
	@Query("SELECT MAX(id) FROM dictionary_translations")
	int getLastId();

	@Insert
	void insert(DictionaryTranslation... dictionaryTranslations);

	@Delete
	void delete(DictionaryTranslation... dictionaryTranslations);

	@Update
	void update(DictionaryTranslation... dictionaryTranslations);
}
