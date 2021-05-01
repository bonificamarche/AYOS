package it.havok.ayos;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Parla {

    TextToSpeech TTS;
    public void Parla(Context contesto, String messaggio){
        TTS = new TextToSpeech(contesto, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                String toastMessage = "";
                //TTS.setPitch(0.8f);
                TTS.setSpeechRate(0.8f);
                switch (status) {
                    case TextToSpeech.SUCCESS:
                        int result = TTS.setLanguage(Locale.ITALY);

                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            toastMessage = "Lingua non supportata";
                        } else {
                            toastMessage = "TTS pronto!";
                            TTS.speak(messaggio, TextToSpeech.QUEUE_FLUSH, null);
                        }
                        break;

                    case TextToSpeech.ERROR:
                        toastMessage = "Initializazione fallita";
                        break;

                    default:
                        toastMessage = "Errore sconosciuto";
                        break;
                }
                Log.i("TTS", toastMessage);
                //Toast.makeText(contesto, toastMessage + " " + status, Toast.LENGTH_LONG).show();
            }
        });
    }
}
