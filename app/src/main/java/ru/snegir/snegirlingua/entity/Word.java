////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: Word.java                   //
////////////////////////////////////////

package ru.snegir.snegirlingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "words",
		foreignKeys = @ForeignKey(entity = Language.class,
								  parentColumns = "code", childColumns = "language",
								  onDelete = CASCADE, onUpdate = CASCADE))
public class Word
{
	// The word ID
	@PrimaryKey
	private int id;

	// The word
	@NonNull
	private String word;

	// The language code of the word
	@NonNull
	private String language;

	public Word(int id, @NonNull String word, @NonNull String language)
	{
		this.id = id;
		this.word = word;
		this.language = language;
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
	public String getWord()
	{
		return word;
	}

	public void setWord(@NonNull String word)
	{
		this.word = word;
	}

	@NonNull
	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(@NonNull String language)
	{
		this.language = language;
	}

	@Override
	@NonNull
	public String toString()
	{
		return String.format("%s (%s)", word, language);
	}
}
