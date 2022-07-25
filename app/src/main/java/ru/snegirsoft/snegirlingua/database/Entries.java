    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: Entries.java                //
////////////////////////////////////////

package ru.snegirsoft.snegirlingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ru.snegirsoft.snegirlingua.entity.Entry;

@Dao
public interface Entries
{
	@Query("SELECT * FROM entries WHERE id = :id")
	Entry getById(int id);

	@Insert
	void insert(Entry... entries);

	@Delete
	void delete(Entry... entries);

	@Update
	void update(Entry... entries);
}
