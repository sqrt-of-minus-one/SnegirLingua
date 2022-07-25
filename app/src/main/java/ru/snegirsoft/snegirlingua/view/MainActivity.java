    ////////////////////////////////////////
   //     SnegirLingua by SnegirSoft     //
  //                                    //
 //  File: MainActivity.java           //
////////////////////////////////////////

package ru.snegirsoft.snegirlingua.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import ru.snegirsoft.snegirlingua.R;

public class MainActivity extends AppCompatActivity
{
	private ProgressBar loadPB;
	private Spinner lang1SP, lang2SP;
	private ImageButton proceedIB;
	private Button editLangBT, settingsBT;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}