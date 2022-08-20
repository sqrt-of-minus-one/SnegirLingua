////////////////////////////////////////////
/////     SnegirLingua by SnegirSoft     //
////                                    //
///  File: Settings.java               //
////////////////////////////////////////

package ru.snegir.snegirlingua;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SettingsManager
{
	public static class Settings
	{
		public boolean test = false;
	}
	
	public static final String FILE_NAME = "settings.dat";
	public static Settings settings = new Settings();
	
	public static void load(Context context)
	{
		FileInputStream fis = null;
		try
		{
			// Read from file and load to Settings object using GSON
			fis = context.openFileInput(FILE_NAME);
			byte[] bytes = new byte[fis.available()];
			fis.read(bytes);
			settings = new GsonBuilder().create().fromJson(new String(bytes), Settings.class);
		}
		catch (IOException ignored)
		{}
		finally
		{
			if (settings == null) // settings must not be null
			{
				settings = new Settings();
			}
			try
			{
				if (fis != null)
				{
					fis.close();
				}
			}
			catch (IOException ignored)
			{}
		}
	}
	
	public static void save(Context context)
	{
		FileOutputStream fos = null;
		try
		{
			// write settings to file
			fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			fos.write(new GsonBuilder().create().toJson(settings).getBytes());
		}
		catch (IOException exception)
		{
			Toast.makeText(context, R.string.error_settings_save, Toast.LENGTH_LONG).show();
		}
		finally
		{
			try
			{
				if (fos != null)
				{
					fos.close();
				}
			}
			catch (IOException ignored)
			{}
		}
	}
}
