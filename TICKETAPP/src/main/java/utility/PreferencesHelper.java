package utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user2 on 01/07/2015.
 */
public class PreferencesHelper {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferencesHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit(); }

    public String GetPreferences(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void SavePreferences(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }
}