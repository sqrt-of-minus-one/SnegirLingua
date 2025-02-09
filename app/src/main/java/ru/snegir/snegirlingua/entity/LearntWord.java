package ru.snegir.snegirlingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// The existence of this relation between the word and the language means that the translation
// of the word to this language is learnt
@Entity(tableName = "learnt_words")
public class LearntWord
{
	@PrimaryKey
	private int id;
	
	private int word;
	
	@NonNull
	private String language;
	
	public LearntWord(int id, int word, @NonNull String language)
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
	
	public int getWord()
	{
		return word;
	}
	public void setWord(int word)
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
}
