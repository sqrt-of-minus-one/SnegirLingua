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

import androidx.annotation.NonNull;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.entity.Dictionary;

public class WordDictionaryAdapter extends ArrayAdapter<Dictionary>
{
	private CheckBox[] addedCB;
	private boolean[] added;
	
	public WordDictionaryAdapter(@NonNull Activity activity, Dictionary[] array, boolean[] added)
	{
		super(activity, R.layout.adapter_word_dictionary, array);
		addedCB = new CheckBox[array.length];
		this.added = added;
	}
	
	@Override
	@NonNull
	public View getView(final int position, View convertView, @NonNull ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_word_dictionary, null);
		}
		addedCB[position] = convertView.findViewById(R.id.a_word_dictionary_addedCB);
		View colorVW = convertView.findViewById(R.id.a_word_dictionary_colorVW);
		colorVW.setBackgroundColor(getItem(position).getColor());
		
		addedCB[position].setChecked(added[position]);
		
		return convertView;
	}
	
	public boolean[] getChecked()
	{
		boolean[] checked = new boolean[addedCB.length];
		for (int i = 0; i < checked.length; i++)
		{
			checked[i] = addedCB[i].isChecked();
		}
		return checked;
	}
}
