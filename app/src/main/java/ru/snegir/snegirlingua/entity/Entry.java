    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: Entry.java                  //
////////////////////////////////////////

package ru.snegir.snegirlingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "entries",
		foreignKeys = @ForeignKey(entity = Table.class, parentColumns = "id", childColumns = "table",
			onDelete = CASCADE, onUpdate = CASCADE))
public class Entry
{
	@PrimaryKey
	private int id;

	private int table;

	public Entry(int id, int table)
	{
		this.id = id;
		this.table = table;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getTable()
	{
		return table;
	}

	public void setTable(int table)
	{
		this.table = table;
	}

	@Override
	@NonNull
	public String toString()
	{
		return String.format("%d in %d", id, table);
	}
}
