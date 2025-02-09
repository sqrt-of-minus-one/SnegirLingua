////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: WordDictionaryAdapter.java  //
////////////////////////////////////////

package ru.snegir.snegirlingua.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.entity.Dictionary;

public class WordDictionaryAdapter extends ArrayAdapter<Dictionary>
{
	private CheckBox[] addedCBs;
	private boolean[] added;
	
	public WordDictionaryAdapter(@NonNull Activity activity, Dictionary[] array, boolean[] added)
	{
		super(activity, R.layout.adapter_word_dictionary, array);
		addedCBs = new CheckBox[array.length];
		this.added = added;
	}
	
	@Override
	@NonNull
	public View getView(final int position, View convertView, @NonNull ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_word_dictionary, parent, false);
		}
		addedCBs[position] = convertView.findViewById(R.id.a_word_dictionary_addedCB);
		View colorVW = convertView.findViewById(R.id.a_word_dictionary_colorVW);
		colorVW.setBackgroundColor(getItem(position).getColor());
		
		addedCBs[position].setText(getItem(position).getName());
		addedCBs[position].setChecked(added[position]);
		addedCBs[position].setOnLongClickListener(v ->
		{
			Toast.makeText(getContext(), R.string.a_word_dict_hint, Toast.LENGTH_LONG).show();
			return true;
		});
		
		return convertView;
	}
	
	public Boolean[] getChecked()
	{
		return Arrays.stream(addedCBs)
				.map(CheckBox::isChecked)
				.toArray(Boolean[]::new);
	}
	
	public List<Integer> getCheckedDictionariesId()
	{
		List<Integer> dictionaries = new LinkedList<>();
		for (int i = 0; i < addedCBs.length; i++)
		{
			if (addedCBs[i].isChecked())
			{
				dictionaries.add(getItem(i).getId());
			}
		}
		return dictionaries;
	}
}
