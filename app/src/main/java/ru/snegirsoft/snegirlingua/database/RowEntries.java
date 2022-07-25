    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: RowEntries.java             //
////////////////////////////////////////

package ru.snegirsoft.snegirlingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ru.snegirsoft.snegirlingua.entity.RowEntry;

@Dao
public interface RowEntries
{
	@Query("SELECT * FROM row_entries WHERE id = :id")
	RowEntry getById(int id);

	@Insert
	void insert(RowEntry... rowEntries);

	@Delete
	void delete(RowEntry... rowEntries);

	@Update
	void update(RowEntry... rowEntries);
}
