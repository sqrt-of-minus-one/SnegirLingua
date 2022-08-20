////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: Language.java               //
////////////////////////////////////////

package ru.snegir.snegirlingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "languages",
		indices = { @Index(value = "name", unique = true) })
public class Language
{
	// The language code
	@PrimaryKey
	@NonNull
	private String code;

	// The language name
	@NonNull
	private String name;

	public Language(@NonNull String code, @NonNull String name)
	{
		this.code = code;
		this.name = name;
	}

	@NonNull
	public String getCode()
	{
		return code;
	}

	public void setCode(@NonNull String code)
	{
		this.code = code;
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
		return String.format("%s: %s", code, name);
	}
}
