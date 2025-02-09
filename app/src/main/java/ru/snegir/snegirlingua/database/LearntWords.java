package ru.snegir.snegirlingua.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.snegir.snegirlingua.entity.LearntWord;

@Dao
public interface LearntWords
{
	@Query("SELECT * FROM learnt_words WHERE id = :id")
	LearntWord getById(int id);
	
	@Query("SELECT * FROM learnt_words WHERE word = :word AND language = :language")
	List<LearntWord> getByWordAndLanguage(int word, String language);
	
	@Query("SELECT MAX(id) FROM learnt_words")
	int getLastId();
	
	@Query("SELECT COUNT(*) FROM learnt_words")
	int count();
	
	@Insert
	void insert(LearntWord... learntWords);
	
	@Delete
	void delete(LearntWord... learntWords);
	
	@Update
	void update(LearntWord... learntWords);
}
