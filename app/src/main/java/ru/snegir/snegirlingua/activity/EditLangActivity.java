    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: EditLangActivity.java       //
////////////////////////////////////////

package ru.snegir.snegirlingua.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ru.snegir.snegirlingua.R;
import ru.snegir.snegirlingua.adapter.LanguageAdapter;
import ru.snegir.snegirlingua.database.Database;
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
						
						String code = codeET.getText().toString();
						String name = nameET.getText().toString();
						
						if (!code.isEmpty() && !name.isEmpty())
						{
							addLang(code, name);
						}
						else
						{
							Toast.makeText(EditLangActivity.this, R.string.edit_lang_enter_code_name, Toast.LENGTH_SHORT).show();
						}
					})
					.setNegativeButton(R.string.cancel, null)
					.create()
					.show();
		});
		loadLangs();
	}
	
	private void addLang(@NonNull String code, @NonNull String name)
	{
		loadPB.setVisibility(View.VISIBLE);
		new Thread(() ->
		{
			try
			{
				Database.get(EditLangActivity.this).languages().insert(new Language(code, name));
			}
			catch (SQLiteConstraintException e)
			{
				EditLangActivity.this.runOnUiThread(() ->
						Toast.makeText(EditLangActivity.this, R.string.edit_lang_couldnt_add, Toast.LENGTH_LONG).show());
			}
			finally
			{
				EditLangActivity.this.runOnUiThread(this::loadLangs);
			}
		}).start();
	}
	
	public void editLang(Language language)
	{
		loadPB.setVisibility(View.VISIBLE);
		new Thread(() ->
		{
			try
			{
				Database.get(EditLangActivity.this).languages().update(language);
			}
			catch (SQLiteConstraintException e)
			{
				EditLangActivity.this.runOnUiThread(() ->
						Toast.makeText(EditLangActivity.this, R.string.edit_lang_couldnt_edit, Toast.LENGTH_LONG).show());
			}
			finally
			{
				EditLangActivity.this.runOnUiThread(this::loadLangs);
			}
		}).start();
	}
	
	public void deleteLang(Language language)
	{
		loadPB.setVisibility(View.VISIBLE);
		new Thread(() ->
		{
			try
			{
				Database.get(EditLangActivity.this).languages().delete(language);
			}
			catch (SQLiteConstraintException e)
			{
				EditLangActivity.this.runOnUiThread(() ->
						Toast.makeText(EditLangActivity.this, R.string.edit_lang_couldnt_delete, Toast.LENGTH_LONG).show());
			}
			finally
			{
				EditLangActivity.this.runOnUiThread(this::loadLangs);
			}
		}).start();
	}
	
	public void loadLangs()
	{
		loadPB.setVisibility(View.VISIBLE);
		new Thread(() ->
		{
			List<Language> list = Database.get(EditLangActivity.this).languages().getAll();
			Language[] array = new Language[list.size()];
			list.toArray(array);
			LanguageAdapter adapter = new LanguageAdapter(EditLangActivity.this, array);
			EditLangActivity.this.runOnUiThread(() ->
			{
				listLV.setAdapter(adapter);
				loadPB.setVisibility(View.INVISIBLE);
			});
		}).start();
	}
}
