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
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_word_dictionary, null);
		}
		addedCBs[position] = convertView.findViewById(R.id.a_word_dictionary_addedCB);
		View colorVW = convertView.findViewById(R.id.a_word_dictionary_colorVW);
		colorVW.setBackgroundColor(getItem(position).getColor());
		
		addedCBs[position].setText(getItem(position).getName());
		addedCBs[position].setChecked(added[position]);
		
		return convertView;
	}
	
	public boolean[] getChecked()
	{
		boolean[] checked = new boolean[addedCBs.length];
		for (int i = 0; i < checked.length; i++)
		{
			checked[i] = addedCBs[i].isChecked();
		}
		return checked;
	}
}
