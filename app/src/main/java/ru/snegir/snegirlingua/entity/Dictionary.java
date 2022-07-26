    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: Dictionary.java             //
////////////////////////////////////////

package ru.snegir.snegirlingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "dictionaries",
		foreignKeys = {
			@ForeignKey(entity = Language.class, parentColumns = "code", childColumns = "lang1",
						onDelete = CASCADE, onUpdate = CASCADE),
			@ForeignKey(entity = Language.class, parentColumns = "code", childColumns = "lang2",
						onDelete = CASCADE, onUpdate = CASCADE)
		})
public class Dictionary
{
	@PrimaryKey
	private int id;

	@NonNull
	private String name;

	@NonNull
	private String lang1;

	@NonNull
	private String lang2;

	public Dictionary(int id, @NonNull String name, @NonNull String lang1, @NonNull String lang2)
	{
		this.id = id;
		this.name = name;
		this.lang1 = lang1;
		this.lang2 = lang2;
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
	public String getLang1()
	{
		return lang1;
	}

	public void setLang1(@NonNull String lang1)
	{
		this.lang1 = lang1;
	}

	@NonNull
	public String getLang2()
	{
		return lang2;
	}

	public void setLang2(@NonNull String lang2)
	{
		this.lang2 = lang2;
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

	@Override
	@NonNull
	public String toString()
	{
		return String.format("%s (%s, %s)", name, lang1, lang2);
	}
}
