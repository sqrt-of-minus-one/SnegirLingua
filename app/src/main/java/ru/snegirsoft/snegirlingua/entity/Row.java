    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: Row.java                    //
////////////////////////////////////////

package ru.snegirsoft.snegirlingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "rows",
		foreignKeys = @ForeignKey(entity = Table.class, parentColumns = "id", childColumns = "table",
			onDelete = CASCADE, onUpdate = CASCADE))
public class Row
{
	@PrimaryKey
	private int id;

	@NonNull
	private String name;

	private int table;

	private int position;

	public Row(int id, @NonNull String name, int table, int position)
	{
		this.id = id;
		this.name = name;
		this.table = table;
		this.position = position;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@NonNull
	public String getName()
	{
		return name;
	}

	public void setName(@NonNull String name)
	{
		this.name = name;
	}

	public int getTable()
	{
		return table;
	}

	public void setTable(int table)
	{
		this.table = table;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

	@Override
	@NonNull
	public String toString()
	{
		return String.format("%s (#%d in %d)", name, position, table);
	}
}
