////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: LanguageAdapter.java        //
////////////////////////////////////////

package ru.snegir.snegirlingua.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.activity.EditLangActivity;
import ru.snegir.snegirlingua.database.facade.LanguagesFacade;
import ru.snegir.snegirlingua.entity.Language;

public class LanguageAdapter extends ArrayAdapter<Language>
{
	final private TextView[] codeTVs;
	final private TextView[] langTVs;
	final private ImageButton[] deleteIBs;
	
	EditLangActivity activity;
	
	public LanguageAdapter(@NonNull Activity activity, Language[] array)
	{
		super(activity, R.layout.adapter_language, array);
		codeTVs = new TextView[array.length];
		langTVs = new TextView[array.length];
		deleteIBs = new ImageButton[array.length];
		
		this.activity = (EditLangActivity)activity;
	}
	
	@Override
	@NonNull
	public View getView(final int position, View convertView, @NonNull ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_language, parent, false);
		}
		codeTVs[position] = convertView.findViewById(R.id.a_language_codeTV);
		langTVs[position] = convertView.findViewById(R.id.a_language_langTV);
		deleteIBs[position] = convertView.findViewById(R.id.a_language_deleteIB);
		
		codeTVs[position].setText(getItem(position).getCode());
		langTVs[position].setText(getItem(position).getName());
		
		convertView.setOnClickListener(v ->
		{
			// Open dialog with the language editing
			View editLangDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_lang, null);
			final TextView codeTV = editLangDialog.findViewById(R.id.d_editLang_codeTV);
			final EditText nameET = editLangDialog.findViewById(R.id.d_editLang_nameET);
			codeTV.setText(codeTVs[position].getText());
			nameET.setText(langTVs[position].getText());
			
			new AlertDialog.Builder(getContext())
					.setTitle(R.string.a_editLang_editLang)
					.setView(editLangDialog)
					.setPositiveButton(R.string.save, (dialog, which) ->
					{
						activity.setPBVisibility(true);
						new Thread(() ->
						{
							LanguagesFacade.update(activity, codeTV.getText().toString(), nameET.getText().toString());
							activity.runOnUiThread(activity::loadLangs);
							// Progress bar becomes invisible in loadLangs
						}).start();
					})
					.setNegativeButton(R.string.cancel, null)
					.create()
					.show();
		});
		convertView.setOnLongClickListener(v ->
		{
			Toast.makeText(activity, R.string.a_editLang_editLang_hint, Toast.LENGTH_LONG).show();
			return true;
		});
		deleteIBs[position].setOnClickListener(v -> new AlertDialog.Builder(getContext())
				.setMessage(R.string.sure_delete_lang)
				.setPositiveButton(R.string.delete, (dialog, which) ->
				{
					activity.setPBVisibility(true);
					new Thread(() ->
					{
						LanguagesFacade.delete(activity, codeTVs[position].getText().toString());
						activity.runOnUiThread(activity::loadLangs);
						// Progress bar becomes invisible in loadLangs
					}).start();
				})
				.setNegativeButton(R.string.cancel, null)
				.create()
				.show());
		deleteIBs[position].setOnLongClickListener(v ->
		{
			Toast.makeText(activity, R.string.a_editLang_delete_hint, Toast.LENGTH_LONG).show();
			return true;
		});
		
		return convertView;
	}
}
