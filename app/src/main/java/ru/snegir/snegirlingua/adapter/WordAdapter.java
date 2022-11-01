////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: WordAdapter.java            //
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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.activity.WordActivity;
import ru.snegir.snegirlingua.activity.WordsActivity;
import ru.snegir.snegirlingua.database.facade.TranslationsFacade;
import ru.snegir.snegirlingua.database.facade.WordsFacade;
import ru.snegir.snegirlingua.entity.Translation;

public class WordAdapter extends ArrayAdapter<Translation>
{
	private CheckBox[] learned1CBs;
	private CheckBox[] learned2CBs;
	private TextView[] word1TVs;
	private TextView[] word2TVs;
	private ImageButton[] deleteIBs;
	
	private Pair<String, String> langs;
	private WordsActivity activity;
	
	public WordAdapter(@NonNull Activity activity, Translation[] array, Pair<String, String> langs)
	{
		super(activity, R.layout.adapter_word, array);
		learned1CBs = new CheckBox[array.length];
		learned2CBs = new CheckBox[array.length];
		word1TVs = new TextView[array.length];
		word2TVs = new TextView[array.length];
		deleteIBs = new ImageButton[array.length];
		this.activity = (WordsActivity)activity;
		this.langs = langs;
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
		new Thread(() ->
		{
			String str1 = WordsFacade.getById(activity, translation.getWord1()).getWord();
			String str2 = WordsFacade.getById(activity, translation.getWord2()).getWord();
			activity.runOnUiThread(() ->
			{
				word1TVs[position].setText(str1);
				word2TVs[position].setText(str2);
			});
		}).start();
		
		learned1CBs[position].setOnCheckedChangeListener((buttonView, isChecked) ->
		{
			// If the checkbox is switched by user
			if (buttonView.isPressed())
			{
				activity.setPBVisibility(true);
				new Thread(() ->
				{
					TranslationsFacade.setLearned(activity, getItem(position).getId(), false, isChecked);
					activity.runOnUiThread(activity::loadWords);
					// Progress bar becomes invisible in loadWords
				}).start();
			}
		});
		learned2CBs[position].setOnCheckedChangeListener((buttonView, isChecked) ->
		{
			// If the checkbox is switched by user
			if (buttonView.isPressed())
			{
				activity.setPBVisibility(true);
				new Thread(() ->
				{
					TranslationsFacade.setLearned(activity, getItem(position).getId(), true, isChecked);
					activity.runOnUiThread(activity::loadWords);
					// Progress bar becomes invisible in loadWords
				}).start();
			}
		});
		convertView.setOnClickListener(v ->
		{
			Intent wordI = new Intent(activity, WordActivity.class);
			wordI.putExtra(WordActivity.LANG_1, langs.first);
			wordI.putExtra(WordActivity.LANG_2, langs.second);
			wordI.putExtra(WordActivity.IS_NEW, false);
			wordI.putExtra(WordActivity.TRANSLATION_ID, getItem(position).getId());
			activity.needsToBeReloaded = true;
			activity.startActivity(wordI);
		});
		deleteIBs[position].setOnClickListener(v -> new AlertDialog.Builder(activity)
				.setMessage(R.string.sure_delete_word)
				.setPositiveButton(R.string.delete, (dialog, which) ->
				{
					activity.setPBVisibility(true);
					new Thread(() ->
					{
						TranslationsFacade.delete(activity, translation.getId());
						activity.runOnUiThread(activity::loadWords);
						// Progress bar becomes invisible in loadWords
					}).start();
				})
				.setNegativeButton(R.string.cancel, null)
				.create()
				.show());
		
		return convertView;
	}
}
