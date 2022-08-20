////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: RowEntry.java               //
////////////////////////////////////////

package ru.snegir.snegirlingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "row_entries",
		foreignKeys = {
				@ForeignKey(entity = Row.class, parentColumns = "id", childColumns = "row",
						onDelete = CASCADE, onUpdate = CASCADE),
				@ForeignKey(entity = Entry.class, parentColumns = "id", childColumns = "entry",
						onDelete = CASCADE, onUpdate = CASCADE)
		})
public class RowEntry
{
	@PrimaryKey
	private int id;

	private int row;

	private int entry;
	
	private boolean learned;

	public RowEntry(int id, int row, int entry, boolean learned)
	{
		this.id = id;
		this.row = row;
		this.entry = entry;
		this.learned = learned;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getRow()
	{
		return row;
	}

	public void setRow(int row)
	{
		this.row = row;
	}

	public int getEntry()
	{
		return entry;
	}

	public void setEntry(int entry)
	{
		this.entry = entry;
	}
	
	public boolean isLearned()
	{
		return learned;
	}
	
	public void setLearned(boolean learned)
	{
		this.learned = learned;
	}

	@Override
	@NonNull
	public String toString()
	{
		return String.format("%d in %d", entry, row);
	}
}
