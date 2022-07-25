    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: Dictionaries.java           //
////////////////////////////////////////

package ru.snegirsoft.snegirlingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.snegirsoft.snegirlingua.entity.Dictionary;

@Dao
public interface Dictionaries
{
	@Query("SELECT * FROM dictionaries WHERE id = :id")
	Dictionary getById(int id);

	@Query("SELECT * FROM dictionaries WHERE name = :name AND ((lang1 = :lang1 AND lang2 = :lang2) OR (lang1 = :lang2 AND lang2 = :lang1))")
	List<Dictionary> getDictionary(String name, String lang1, String lang2);

	@Insert
	void insert(Dictionary... languages);

	@Delete
	void delete(Dictionary... languages);

	@Update
	void update(Dictionary... languages);
}
