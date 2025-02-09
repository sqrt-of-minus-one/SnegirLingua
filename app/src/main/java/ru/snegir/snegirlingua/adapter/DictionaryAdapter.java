////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: DictionaryAdapter.java      //
////////////////////////////////////////

package ru.snegir.snegirlingua.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.activity.DictionariesActivity;
import ru.snegir.snegirlingua.activity.DictionaryActivity;
import ru.snegir.snegirlingua.database.facade.DictionariesFacade;
import ru.snegir.snegirlingua.entity.Dictionary;

public class DictionaryAdapter extends ArrayAdapter<Dictionary>
{
	private View[] colorVWs;
	private TextView[] nameTVs;
	private TextView[] wordsTVs;
	private ImageButton[] deleteIBs;
	
	private Pair<String, String> langs;
	private DictionariesActivity activity;
	
	public DictionaryAdapter(@NonNull Activity activity, Dictionary[] array, Pair<String, String> langs)
	{
		super(activity, R.layout.adapter_word, array);
		colorVWs = new View[array.length];
		nameTVs = new TextView[array.length];
		wordsTVs = new TextView[array.length];
		deleteIBs = new ImageButton[array.length];
		this.activity = (DictionariesActivity)activity;
		this.langs = langs;
	}
	
	@Override
	@NonNull
	public View getView(final int position, View convertView, @NonNull ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_dictionary, parent, false);
		}
		colorVWs[position] = convertView.findViewById(R.id.a_dictionary_colorVW);
		nameTVs[position] = convertView.findViewById(R.id.a_dictionary_nameTV);
		wordsTVs[position] = convertView.findViewById(R.id.a_dictionary_wordsTV);
		deleteIBs[position] = convertView.findViewById(R.id.a_dictionary_deleteIB);
		
		final Dictionary dictionary = getItem(position);
		
		colorVWs[position].setBackgroundColor(dictionary.getColor());
		nameTVs[position].setText(dictionary.getName());
		new Thread(() ->
		{
			int words = DictionariesFacade.countTranslations(activity, dictionary.getId());
			activity.runOnUiThread(() -> wordsTVs[position].setText(activity.getString(R.string.a_dictionaries_words, words)));
		}).start();
		
		convertView.setOnClickListener(v ->
		{
			Intent dictionaryI = new Intent(activity, DictionaryActivity.class);
			dictionaryI.putExtra(DictionaryActivity.LANG_1, langs.first);
			dictionaryI.putExtra(DictionaryActivity.LANG_2, langs.second);
			dictionaryI.putExtra(DictionaryActivity.IS_NEW, false);
			dictionaryI.putExtra(DictionaryActivity.DICTIONARY_ID, getItem(position).getId());
			activity.needsToBeReloaded = true;
			activity.startActivity(dictionaryI);
		});
		convertView.setOnLongClickListener(v ->
		{
			Toast.makeText(activity, R.string.a_dictionaries_editDictionary_hint, Toast.LENGTH_LONG).show();
			return true;
		});
		deleteIBs[position].setOnClickListener(v -> new AlertDialog.Builder(activity)
				.setMessage(R.string.sure_delete_dictionary)
				.setPositiveButton(R.string.delete, (dialog, which) ->
				{
					activity.setPBVisibility(true);
					new Thread(() ->
					{
						DictionariesFacade.delete(activity, dictionary.getId());
						activity.needsToBeReloaded = true;
						activity.runOnUiThread(activity::loadDictionaries);
						// Progress bar becomes invisible in loadDictionaries
					}).start();
				})
				.setNegativeButton(R.string.cancel, null)
				.create()
				.show());
		
		return convertView;
	}
}
