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
	// The class keeping all the settings
	public static class Settings
	{
		public boolean test = false;
	}
	
	public static final String FILE_NAME = "settings.dat";
	public static Settings settings = new Settings();
	
	public static void load(Context context)
	{
		try (FileInputStream fis = context.openFileInput(FILE_NAME))
		{
			// Read from file and load to Settings object using GSON
			byte[] bytes = new byte[fis.available()];
			fis.read(bytes);
			settings = new GsonBuilder().create().fromJson(new String(bytes), Settings.class);
		}
		catch (IOException ignored)
		{
		}
		finally
		{
			if (settings == null) // settings must not be null
			{
				settings = new Settings();
			}
		}
	}
	
	public static void save(Context context)
	{
		try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE))
		{
			// write settings to file
			fos.write(new GsonBuilder().create().toJson(settings).getBytes());
		}
		catch (IOException exception)
		{
			Toast.makeText(context, R.string.error_settings_save, Toast.LENGTH_LONG).show();
		}
	}
}
