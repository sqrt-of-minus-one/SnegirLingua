    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: WordAdapter.java            //
////////////////////////////////////////

package ru.snegir.snegirlingua.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.database.Database;
import ru.snegir.snegirlingua.entity.Translation;

public class WordAdapter extends ArrayAdapter<Translation>
{
	private CheckBox[] learned1CBs;
	private CheckBox[] learned2CBs;
	private TextView[] word1TVs;
	private TextView[] word2TVs;
	private ImageButton[] deleteIBs;
	
	public WordAdapter(@NonNull Activity activity, Translation[] array)
	{
		super(activity, R.layout.adapter_word, array);
		learned1CBs = new CheckBox[array.length];
		learned2CBs = new CheckBox[array.length];
		word1TVs = new TextView[array.length];
		word2TVs = new TextView[array.length];
		deleteIBs = new ImageButton[array.length];
	}
	
	@Override
	@NonNull
	public View getView(final int position, View convertView, @NonNull ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_word, null);
		}
		learned1CBs[position] = convertView.findViewById(R.id.a_word_learned1CB);
		learned2CBs[position] = convertView.findViewById(R.id.a_word_learned2CB);
		word1TVs[position] = convertView.findViewById(R.id.a_word_word1TV);
		word2TVs[position] = convertView.findViewById(R.id.a_word_word2TV);
		deleteIBs[position] = convertView.findViewById(R.id.a_word_deleteIB);
		
		final Translation translation = getItem(position);
		
		learned1CBs[position].setChecked(translation.getLearned1());
		learned2CBs[position].setChecked(translation.getLearned2());
		word1TVs[position].setText(Database.get(getContext()).words().getById(translation.getWord1()).getWord());
		word2TVs[position].setText(Database.get(getContext()).words().getById(translation.getWord2()).getWord());
		
		convertView.setOnClickListener(v ->
		{
			// Todo: edit word
		});
		return convertView;
	}
}
