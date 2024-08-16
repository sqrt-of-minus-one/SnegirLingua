////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: Translation.java            //
////////////////////////////////////////

package ru.snegir.snegirlingua.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Locale;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "translations",
		foreignKeys = {
			@ForeignKey(entity = Word.class, parentColumns = "id", childColumns = "word1",
						onDelete = CASCADE, onUpdate = CASCADE),
			@ForeignKey(entity = Word.class, parentColumns = "id", childColumns = "word2",
						onDelete = CASCADE, onUpdate = CASCADE)
		})
public class Translation
{
	// The translation ID
	@PrimaryKey
	private int id;

	// IDs of the words in the first and the second languages
	// Words are supposed to be sorted by the language code
	private int word1, word2;
	
	// The comments on the words
	private String comment1, comment2;

	// Whether the translation is learned in the first and the second languages
	private boolean learned1, learned2;

	public Translation(int id, int word1, int word2, String comment1, String comment2, boolean learned1, boolean learned2)
	{
		this.id = id;
		this.word1 = word1;
		this.word2 = word2;
		this.comment1 = comment1;
		this.comment2 = comment2;
		this.learned1 = learned1;
		this.learned2 = learned2;
	}

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}

	public int getWord1()
	{
		return word1;
	}
	public void setWord1(int word1)
	{
		this.word1 = word1;
	}

	public int getWord2()
	{
		return word2;
	}
	public void setWord2(int word2)
	{
		this.word2 = word2;
	}
	
	public String getComment1()
	{
		return comment1;
	}
	public void setComment1(String comment1)
	{
		this.comment1 = comment1;
	}
	
	public String getComment2()
	{
		return comment2;
	}
	public void setComment2(String comment2)
	{
		this.comment2 = comment2;
	}

	public boolean getLearned1()
	{
		return learned1;
	}
	public void setLearned1(boolean learned1)
	{
		this.learned1 = learned1;
	}

	public boolean getLearned2()
	{
		return learned2;
	}
	public void setLearned2(boolean learned2)
	{
		this.learned2 = learned2;
	}

	@Override
	@NonNull
	public String toString()
	{
		return String.format(Locale.getDefault(),
				"(%b) %d : %d (%b)", learned1, word1, word2, learned2);
	}
}
