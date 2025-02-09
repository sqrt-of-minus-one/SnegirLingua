////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: DictionaryWordAdapter.java  //
////////////////////////////////////////

package ru.snegir.snegirlingua.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.activity.DictionaryActivity;
import ru.snegir.snegirlingua.database.facade.WordsFacade;
import ru.snegir.snegirlingua.entity.Translation;
import ru.snegir.snegirlingua.entity.Word;

public class DictionaryWordAdapter extends ArrayAdapter<Translation>
{
	private CheckBox[] addedCBs;
	private TextView[] word1TVs;
	private TextView[] word2TVs;
	
	private DictionaryActivity activity;
	private boolean[] added;
	
	public DictionaryWordAdapter(@NonNull DictionaryActivity activity, Translation[] array, boolean[] added)
	{
		super(activity, R.layout.adapter_dictionary_word, array);
		addedCBs = new CheckBox[array.length];
		word1TVs = new TextView[array.length];
		word2TVs = new TextView[array.length];
		
		this.activity = activity;
		this.added = added;
	}
	
	@Override
	@NonNull
	public View getView(final int position, View convertView, @NonNull ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_dictionary_word, parent, false);
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
		
		addedCBs[position].setChecked(added[position]);
		addedCBs[position].setOnLongClickListener(v ->
		{
			Toast.makeText(activity, R.string.a_dictionary_word_hint, Toast.LENGTH_LONG).show();
			return true;
		});
		return convertView;
	}
}
