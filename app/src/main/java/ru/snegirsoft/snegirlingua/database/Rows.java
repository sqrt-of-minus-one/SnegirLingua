    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: Rows.java                   //
////////////////////////////////////////

package ru.snegirsoft.snegirlingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ru.snegirsoft.snegirlingua.entity.Row;

@Dao
public interface Rows
{
	@Query("SELECT * FROM rows WHERE id = :id")
	Row getById(int id);

	@Insert
	void insert(Row... rows);

	@Delete
	void delete(Row... rows);

	@Update
	void update(Row... rows);
}
