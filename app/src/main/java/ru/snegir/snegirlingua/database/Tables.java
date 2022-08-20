////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: Tables.java                 //
////////////////////////////////////////

package ru.snegir.snegirlingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import ru.snegir.snegirlingua.entity.Table;

@Dao
public interface Tables
{
	@Query("SELECT * FROM tables WHERE id = :id")
	Table getById(int id);

	@Insert
	void insert(Table... tables);

	@Delete
	void delete(Table... tables);

	@Update
	void update(Table... tables);
}
