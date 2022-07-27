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
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.entity.Language;

public class LanguageAdapter extends ArrayAdapter<Language>
{
	private TextView[] codeTVs;
	private TextView[] langTVs;
	private ImageButton[] deleteIBs;
	
	public LanguageAdapter(@NonNull Activity activity, Language[] array)
	{
		super(activity, R.layout.adapter_language, array);
		codeTVs = new TextView[array.length];
		langTVs = new TextView[array.length];
		deleteIBs = new ImageButton[array.length];
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
		
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Todo: edit language
			}
		});
		deleteIBs[position].setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new AlertDialog.Builder(getContext())
						.setMessage(R.string.a_language_delete_sure)
						.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								// Todo: delete language
							}
						})
						.setNegativeButton(R.string.cancel, null)
						.create()
						.show();
			}
		});
		
		return convertView;
	}
}
