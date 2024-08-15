////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: DictionaryWordAdapter.java  //
////////////////////////////////////////

package ru.snegir.snegirlingua.adapter;

import android.app.Activity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.HashMap;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.activity.DictionaryActivity;
import ru.snegir.snegirlingua.database.facade.TranslationsFacade;
import ru.snegir.snegirlingua.database.facade.WordsFacade;
import ru.snegir.snegirlingua.entity.Dictionary;
import ru.snegir.snegirlingua.entity.Translation;
import ru.snegir.snegirlingua.entity.Word;

public class DictionaryWordAdapter extends ArrayAdapter<Translation>
{
	private CheckBox[] addedCBs;
	private TextView[] word1TVs;
	private TextView[] word2TVs;
	
	private DictionaryActivity activity;
	
	public DictionaryWordAdapter(@NonNull Activity activity, Translation[] array)
	{
		super(activity, R.layout.adapter_dictionary_word, array);
		addedCBs = new CheckBox[array.length];
		word1TVs = new TextView[array.length];
		word2TVs = new TextView[array.length];
		
		this.activity = (DictionaryActivity)activity;
	}
	
	@Override
	@NonNull
	public View getView(final int position, View convertView, @NonNull ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_dictionary_word, null);
		}
		addedCBs[position] = convertView.findViewById(R.id.a_dictionary_word_addedCB);
		word1TVs[position] = convertView.findViewById(R.id.a_dictionary_word_word1TV);
		word2TVs[position] = convertView.findViewById(R.id.a_dictionary_word_word2TV);
		
		Translation translation = getItem(position);
		
		new Thread(() ->
		{
			Word word1 = WordsFacade.getById(activity, translation.getWord1());
			Word word2 = WordsFacade.getById(activity, translation.getWord2());
			activity.runOnUiThread(() ->
			{
				word1TVs[position].setText(word1.getWord());
				word2TVs[position].setText(word2.getWord());
			});
		}).start();
		
		addedCBs[position].setChecked(activity.added.get(translation.getId()));
		addedCBs[position].setOnCheckedChangeListener((compoundButton, b) ->
		{
			for (int i = 0; i < addedCBs.length; i++)
			{
				if (addedCBs[i] == compoundButton)
				{
					activity.added.put(getItem(i).getId(), b);
					break;
				}
			}
		});
		
		return convertView;
	}
}
