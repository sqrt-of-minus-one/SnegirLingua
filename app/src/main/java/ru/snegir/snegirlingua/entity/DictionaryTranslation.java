////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: DictionaryTranslation.java  //
////////////////////////////////////////

package ru.snegir.snegirlingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "dictionary_translations",
		foreignKeys = {
			@ForeignKey(entity = Dictionary.class, parentColumns = "id", childColumns = "dictionary",
						onDelete = CASCADE, onUpdate = CASCADE),
			@ForeignKey(entity = Translation.class, parentColumns = "id", childColumns = "translation",
						onDelete = CASCADE, onUpdate = CASCADE)
		})
public class DictionaryTranslation
{
	@PrimaryKey
	private int id;

	private int dictionary;

	private int translation;

	public DictionaryTranslation(int id, int dictionary, int translation)
	{
		this.id = id;
		this.dictionary = dictionary;
		this.translation = translation;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getDictionary()
	{
		return dictionary;
	}

	public void setDictionary(int dictionary)
	{
		this.dictionary = dictionary;
	}

	public int getTranslation()
	{
		return translation;
	}

	public void setTranslation(int translation)
	{
		this.translation = translation;
	}

	@Override
	@NonNull
	public String toString()
	{
		return String.format("%d in %d", translation, dictionary);
	}
}
