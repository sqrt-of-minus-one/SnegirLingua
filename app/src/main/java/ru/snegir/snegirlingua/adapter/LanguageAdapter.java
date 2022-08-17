    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: LanguageAdapter.java        //
////////////////////////////////////////

package ru.snegir.snegirlingua.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import ru.snegir.snegirlingua.entity.Language;

public class LanguageAdapter extends ArrayAdapter<Language>
{
	private TextView[] codeTVs;
	private TextView[] langTVs;
	private ImageButton[] deleteIBs;
	
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
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_language, null);
		}
		codeTVs[position] = convertView.findViewById(R.id.a_language_codeTV);
		langTVs[position] = convertView.findViewById(R.id.a_language_langTV);
		deleteIBs[position] = convertView.findViewById(R.id.a_language_deleteIB);
		
		codeTVs[position].setText(getItem(position).getCode());
		langTVs[position].setText(getItem(position).getName());
		
		convertView.setOnClickListener(v ->
		{
			View editLangDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_lang, null);
			final TextView codeTV = editLangDialog.findViewById(R.id.d_edit_lang_codeTV);
			final EditText nameET = editLangDialog.findViewById(R.id.d_edit_lang_nameET);
			codeTV.setText(getItem(position).getCode());
			nameET.setText(getItem(position).getName());
			
			new AlertDialog.Builder(getContext())
					.setTitle(R.string.edit_lang_edit_lang)
					.setView(editLangDialog)
					.setPositiveButton(R.string.ok, (dialog, which) ->
					{
						final String code = codeTV.getText().toString();
						final String name = nameET.getText().toString();
						
						// Code cannot be changed
						if (!name.isEmpty())
						{
							activity.editLang(new Language(code, name));
						}
						else
						{
							Toast.makeText(getContext(), R.string.edit_lang_enter_name, Toast.LENGTH_LONG).show();
						}
					})
					.setNegativeButton(R.string.cancel, null)
					.create()
					.show();
		});
		deleteIBs[position].setOnClickListener(v -> new AlertDialog.Builder(getContext())
				.setMessage(R.string.edit_lang_delete_sure)
				.setPositiveButton(R.string.delete, (dialog, which) -> activity.deleteLang(getItem(position)))
				.setNegativeButton(R.string.cancel, null)
				.create()
				.show());
		
		return convertView;
	}
}
