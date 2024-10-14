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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
	private Button addBT;
	private ImageButton infoIB;
	private ListView listLV;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_lang);
		
		loadPB = findViewById(R.id.editLang_loadPB);
		addBT = findViewById(R.id.editLang_addBT);
		infoIB = findViewById(R.id.editLang_infoIB);
		listLV = findViewById(R.id.editLang_listLV);
		
		addBT.setOnClickListener(v ->
		{
			final View addLangDialog = LayoutInflater.from(EditLangActivity.this).inflate(R.layout.dialog_add_lang, null);
			new AlertDialog.Builder(EditLangActivity.this)
					.setTitle(R.string.a_editLang_addLang)
					.setView(addLangDialog)
					.setPositiveButton(R.string.add, (dialog, which) ->
					{
						EditText codeET = addLangDialog.findViewById(R.id.d_addLang_codeET);
						EditText nameET = addLangDialog.findViewById(R.id.d_addLang_nameET);
						
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
		infoIB.setOnClickListener(v -> new AlertDialog.Builder(EditLangActivity.this)
				.setTitle(R.string.a_editLang_help_title)
				.setMessage(R.string.a_editLang_help)
				.setPositiveButton(R.string.ok, null)
				.create()
				.show());
		infoIB.setOnLongClickListener(v ->
		{
			Toast.makeText(EditLangActivity.this, R.string.help, Toast.LENGTH_LONG).show();
			return true;
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
			addBT.setEnabled(!visible);
		});
	}
}
