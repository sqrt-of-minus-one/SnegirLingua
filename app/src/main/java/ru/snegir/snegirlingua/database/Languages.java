////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: Languages.java              //
////////////////////////////////////////

package ru.snegir.snegirlingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.snegir.snegirlingua.entity.Language;

@Dao
public interface Languages
{
	@Query("SELECT * FROM languages WHERE code = :code")
	Language get(String code);

	@Query("SELECT * FROM languages ORDER BY code")
	List<Language> getAll();

	@Insert
	void insert(Language... languages);

	@Delete
	void delete(Language... languages);

	@Update
	void update(Language... languages);
}