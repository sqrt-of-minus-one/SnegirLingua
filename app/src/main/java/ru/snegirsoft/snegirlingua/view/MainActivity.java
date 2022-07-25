    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: MainActivity.java           //
////////////////////////////////////////

package ru.snegirsoft.snegirlingua.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.snegirsoft.snegirlingua.R;
import ru.snegirsoft.snegirlingua.database.Database;
import ru.snegirsoft.snegirlingua.entity.Language;

public class MainActivity extends AppCompatActivity
{
	private ProgressBar loadPB;
	private Spinner lang1SP, lang2SP;
	private ImageButton proceedIB, editLangIB, settingsIB;
	private TextView proceedTV, editLangTV, settingsTV;
	
	private boolean needsToBeReloaded;
	
	@Override
	public void onResume()
	{
		super.onResume();
		if (needsToBeReloaded)
		{
			loadLangs();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		needsToBeReloaded = false;
		
		loadPB = findViewById(R.id.main_loadPB);
		lang1SP = findViewById(R.id.main_lang1SP);
		lang2SP = findViewById(R.id.main_lang2SP);
		proceedIB = findViewById(R.id.main_proceedIB);
		editLangIB = findViewById(R.id.main_editLangIB);
		settingsIB = findViewById(R.id.main_settingsIB);
		proceedTV = findViewById(R.id.main_proceedTV);
		editLangTV = findViewById(R.id.main_editLangTV);
		settingsTV = findViewById(R.id.main_settingsTV);
		
		loadLangs();
		
		proceedIB.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Todo: start MenuActivity
			}
		});
		proceedIB.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				Toast.makeText(MainActivity.this, R.string.main_proceed_info, Toast.LENGTH_LONG).show();
				return true;
			}
		});
		
		editLangIB.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Todo: start EditLangActivity
			}
		});
		editLangIB.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				Toast.makeText(MainActivity.this, R.string.main_edit_lang_info, Toast.LENGTH_LONG).show();
				return true;
			}
		});
		
		settingsIB.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Todo: start SettingsActivity
			}
		});
		settingsIB.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				Toast.makeText(MainActivity.this, R.string.main_settings_info, Toast.LENGTH_LONG).show();
				return true;
			}
		});
	}
	
	private void loadLangs()
	{
		loadPB.setVisibility(View.VISIBLE);
		needsToBeReloaded = false;
		new Thread()
		{
			@Override
			public void run()
			{
				List<Language> languages = Database.get(MainActivity.this).languages().getAll();
				String[] langs = new String[languages.size()];
				for (int i = 0; i < languages.size(); i++)
				{
					langs[i] = languages.get(i).getCode();
				}
				final ArrayAdapter<String> adapter =
						new ArrayAdapter<>(MainActivity.this,
										   android.R.layout.simple_spinner_item, langs);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						lang1SP.setAdapter(adapter);
						lang2SP.setAdapter(adapter);
						loadPB.setVisibility(View.INVISIBLE);
					}
				});
			}
		}.start();
	}
}