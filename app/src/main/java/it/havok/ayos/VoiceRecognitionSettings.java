package it.havok.ayos;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class VoiceRecognitionSettings extends PreferenceActivity {
    // The name of the SharedPreferences file we'll store preferences in.
    public static final String SHARED_PREFERENCES_NAME = "VoiceRecognitionService";

    // The key to the preference for the type of results to show (letters or numbers).
    // Identical to the value specified in res/values/strings.xml.
    public static final String PREF_KEY_RESULTS_TYPE = "results_type";

    // The values of the preferences for the type of results to show (letters or numbers).
    // Identical to the values specified in res/values/strings.xml.
    public static final int RESULT_TYPE_LETTERS = 0;
    public static final int RESULT_TYPE_NUMBERS = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
