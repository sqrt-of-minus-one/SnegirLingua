////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: WordAdapter.java            //
////////////////////////////////////////

package ru.snegir.snegirlingua.adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.activity.WordActivity;
import ru.snegir.snegirlingua.activity.WordsActivity;
import ru.snegir.snegirlingua.database.facade.TranslationsFacade;
import ru.snegir.snegirlingua.database.facade.WordsFacade;
import ru.snegir.snegirlingua.entity.Translation;

class ItemData
{
	public int position;
	public final CheckBox learned1CB;
	public final CheckBox learned2CB;
	public final TextView word1TV;
	public final TextView word2TV;
	public final ImageButton deleteIB;
	public final ProgressBar loadPB;
	
	public ItemData(int position, CheckBox learned1CB, CheckBox learned2CB, TextView word1TV, TextView word2TV,
					ImageButton deleteIB, ProgressBar loadPB)
	{
		this.position = position;
		this.learned1CB = learned1CB;
		this.learned2CB = learned2CB;
		this.word1TV = word1TV;
		this.word2TV = word2TV;
		this.deleteIB = deleteIB;
		this.loadPB = loadPB;
	}
}

public class WordAdapter extends ArrayAdapter<Translation>
{
	private Pair<String, String> langs;
	private WordsActivity activity;
	
	private Map<View, ItemData> itemData;
	
	public WordAdapter(@NonNull WordsActivity activity, Translation[] array, Pair<String, String> langs)
	{
		super(activity, R.layout.adapter_word, array);
		this.activity = activity;
		this.langs = langs;
		itemData = new HashMap<>();
	}
	
	@Override
	@NonNull
	public View getView(final int position, View convertView, @NonNull ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_word, parent, false);
		}
		ItemData data = itemData.get(convertView);
		if (data == null)
		{
			data = new ItemData(position,
					convertView.findViewById(R.id.a_word_learned1CB),
					convertView.findViewById(R.id.a_word_learned2CB),
					convertView.findViewById(R.id.a_word_word1TV),
					convertView.findViewById(R.id.a_word_word2TV),
					convertView.findViewById(R.id.a_word_deleteIB),
					convertView.findViewById(R.id.a_word_loadPB));
			itemData.put(convertView, data);
		}
		else
		{
			data.position = position;
		}
		
		updateWord(data);
		
		final Translation translation = getItem(position);
		
		data.learned1CB.setOnCheckedChangeListener((buttonView, isChecked) ->
		{
			// If the checkbox is switched by user
			if (buttonView.isPressed())
			{
				new Thread(() -> TranslationsFacade.setLearned(activity, getItem(position).getId(), false, isChecked)).start();
				setCheckboxes(isChecked, false, getItem(position).getWord1());
			}
		});
		data.learned1CB.setOnLongClickListener(v ->
		{
			Toast.makeText(activity, R.string.a_words_learned_hint, Toast.LENGTH_LONG).show();
			return true;
		});
		data.learned2CB.setOnCheckedChangeListener((buttonView, isChecked) ->
		{
			// If the checkbox is switched by user
			if (buttonView.isPressed())
			{
				new Thread(() -> TranslationsFacade.setLearned(activity, getItem(position).getId(), true, isChecked)).start();
				setCheckboxes(isChecked, true, getItem(position).getWord2());
			}
		});
		data.learned2CB.setOnLongClickListener(v ->
		{
			Toast.makeText(activity, R.string.a_words_learned_hint, Toast.LENGTH_LONG).show();
			return true;
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
		convertView.setOnLongClickListener(v ->
		{
			Toast.makeText(activity, R.string.a_words_editWord_hint, Toast.LENGTH_LONG).show();
			return true;
		});
		data.deleteIB.setOnClickListener(v -> new AlertDialog.Builder(activity)
				.setMessage(R.string.sure_delete_word)
				.setPositiveButton(R.string.delete, (dialog, which) ->
				{
					activity.setPBVisibility(true);
					new Thread(() ->
					{
						TranslationsFacade.delete(activity, translation.getId());
						activity.needsToBeReloaded = true;
						activity.runOnUiThread(activity::loadWords);
						// Progress bar becomes invisible in loadWords
					}).start();
				})
				.setNegativeButton(R.string.cancel, null)
				.create()
				.show());
		
		return convertView;
	}
	
	private void setCheckboxes(boolean state, boolean isSecondLanguage, int wordId)
	{
		itemData.values().stream()
				.map(data -> new Pair<>(
						isSecondLanguage ? getItem(data.position).getWord2() : getItem(data.position).getWord1(),
						isSecondLanguage ? data.learned2CB : data.learned1CB))
				.filter(pair -> pair.first == wordId)
				.forEach(pair -> pair.second.setChecked(state));
	}
	
	public void updateAll()
	{
		itemData.values().forEach(this::updateWord);
	}
	
	private void updateWord(ItemData data)
	{
		activity.runOnUiThread(() ->
		{
			data.learned1CB.setEnabled(false);
			data.learned2CB.setEnabled(false);
			data.word1TV.setText("");
			data.word2TV.setText("");
			data.deleteIB.setEnabled(false);
			data.loadPB.setVisibility(View.VISIBLE);
		});
		
		Translation translation = getItem(data.position);
		new Thread(() ->
		{
			boolean learned1 = TranslationsFacade.isLearned(activity, translation.getWord1(), langs.second);
			boolean learned2 = TranslationsFacade.isLearned(activity, translation.getWord2(), langs.first);
			String str1 = WordsFacade.getById(activity, translation.getWord1()).getWord();
			String str2 = WordsFacade.getById(activity, translation.getWord2()).getWord();
			activity.runOnUiThread(() ->
			{
				data.learned1CB.setChecked(learned1);
				data.learned1CB.jumpDrawablesToCurrentState();
				data.learned2CB.setChecked(learned2);
				data.learned2CB.jumpDrawablesToCurrentState();
				data.word1TV.setText(str1);
				data.word2TV.setText(str2);
				data.learned1CB.setEnabled(true);
				data.learned2CB.setEnabled(true);
				data.loadPB.setVisibility(View.INVISIBLE);
			});
		}).start();
	}
}
