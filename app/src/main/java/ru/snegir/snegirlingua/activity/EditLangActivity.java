////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: EditLangActivity.java       //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.adapter.LanguageAdapter;
import ru.snegir.snegirlingua.database.facade.LanguagesFacade;
import ru.snegir.snegirlingua.entity.Language;

public class EditLangActivity extends AppCompatActivity
{
	private ProgressBar loadPB;
	private ListView listLV;
	private FloatingActionButton addFB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_lang);
		
		loadPB = findViewById(R.id.edit_lang_loadPB);
		listLV = findViewById(R.id.edit_lang_listLV);
		addFB = findViewById(R.id.edit_lang_addFB);
		
		addFB.setOnClickListener(v ->
		{
			final View addLangDialog = LayoutInflater.from(EditLangActivity.this).inflate(R.layout.dialog_add_lang, null);
			new AlertDialog.Builder(EditLangActivity.this)
					.setTitle(R.string.edit_lang_add_lang)
					.setView(addLangDialog)
					.setPositiveButton(R.string.add, (dialog, which) ->
					{
						EditText codeET = addLangDialog.findViewById(R.id.d_add_lang_codeET);
						EditText nameET = addLangDialog.findViewById(R.id.d_add_lang_nameET);
						
						setPBVisibility(true);
						new Thread(() ->
						{
							LanguagesFacade.insert(EditLangActivity.this, codeET.getText().toString(), nameET.getText().toString());
							EditLangActivity.this.runOnUiThread(this::loadLangs);
							// Progress bar becomes invisible in loadLangs
						}).start();
					})
					.setNegativeButton(R.string.cancel, null)
					.create()
					.show();
		});
		loadLangs();
	}
	
	// Load languages to the list view
	public void loadLangs()
	{
		setPBVisibility(true);
		new Thread(() ->
		{
			List<Language> list = LanguagesFacade.getAll(EditLangActivity.this);
			Language[] array = new Language[list.size()];
			list.toArray(array);
			LanguageAdapter adapter = new LanguageAdapter(EditLangActivity.this, array);
			EditLangActivity.this.runOnUiThread(() ->
			{
				listLV.setAdapter(adapter);
				setPBVisibility(false);
			});
		}).start();
	}
	
	public void setPBVisibility(boolean visible)
	{
		runOnUiThread(() ->
		{
			loadPB.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
			listLV.setEnabled(!visible);
			addFB.setEnabled(!visible);
		});
	}
}
