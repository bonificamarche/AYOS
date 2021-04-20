package it.havok.ayos;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class Parla {

    TextToSpeech __tts;
    public void Parla(Context contesto, String messaggio){
        __tts = new TextToSpeech(contesto, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                String toastMessage = "";
                switch (status) {
                    case TextToSpeech.SUCCESS:
                        int result = __tts.setLanguage(Locale.ITALY);

                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            toastMessage = "Language not supported";
                        } else {
                            toastMessage = "TTS works!";
                            __tts.speak(messaggio, TextToSpeech.QUEUE_FLUSH, null);
                        }
                        break;

                    case TextToSpeech.ERROR:
                        toastMessage = "Initialization failed";
                        break;

                    default:
                        toastMessage = "Unknow error";
                        break;
                }
                Log.i("TTS", toastMessage);
                Toast.makeText(contesto, toastMessage + " " + status, Toast.LENGTH_LONG).show();
            }
        });
    }
}