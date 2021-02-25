package it.havok.ayos;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class Ascolta extends RecognitionService {


    String __logTag = "service";
    String __message;
    String __speakServiceMessage;
    String __language = "it-IT";
    Parla __parla = new Parla();
    SpeechRecognizer speechRecognizer;

    @Override
    public int onStartCommand(Intent intent, int flag, int startId)
    {
        __message = "start";
        Log.i(__logTag, __message);
        Toast.makeText(getApplicationContext(), __logTag + " " + __message, Toast.LENGTH_LONG).show();

        __speakServiceMessage = "ciao, sono Alessia";
        __parla.Parla(getApplicationContext(), __speakServiceMessage);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                __speakServiceMessage = "in ascolto";
                __parla.Parla(getApplicationContext(), __speakServiceMessage);
            }

            @Override
            public void onBeginningOfSpeech() {
                __speakServiceMessage = "inizio ascolto";
                __parla.Parla(getApplicationContext(), __speakServiceMessage);
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                __speakServiceMessage = "rms cambiato";
                __parla.Parla(getApplicationContext(), __speakServiceMessage);
            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                __speakServiceMessage = "ascolto terminato";
                __parla.Parla(getApplicationContext(), __speakServiceMessage);
            }

            @Override
            public void onError(int error) {

                __speakServiceMessage = "errore " + error;
                __parla.Parla(getApplicationContext(), __speakServiceMessage);
            }

            @Override
            public void onResults(Bundle results) {

                __speakServiceMessage = "dialogo disponibile";
                __parla.Parla(getApplicationContext(), __speakServiceMessage);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

                __speakServiceMessage = "dialogo parziale";
                __parla.Parla(getApplicationContext(), __speakServiceMessage);
            }

            @Override
            public void onEvent(int eventType, Bundle params) {

                __speakServiceMessage = "evento";
                __parla.Parla(getApplicationContext(), __speakServiceMessage);
            }
        });
        speechRecognizer.startListening(intent);

        return Service.START_NOT_STICKY;
    }


    @Override
    protected void onCancel(Callback listener) {

    }

    @Override
    protected void onStopListening(Callback listener) {

        speechRecognizer.stopListening();
        __speakServiceMessage = "rilevato dialogo";
        __parla.Parla(getApplicationContext(), __speakServiceMessage);
    }

    @Override
    protected void onStartListening(Intent recognizerIntent, Callback listener) {

        __speakServiceMessage = "sono in ascolto";
        __parla.Parla(getApplicationContext(), __speakServiceMessage);

        // A real recognizer would probably utilize a lot of the other listener callback
        // methods. But we'll just skip all that and pretend we've got a result.
        ArrayList<String> results = new ArrayList<String>();

        SharedPreferences prefs = getSharedPreferences(
                VoiceRecognitionSettings.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);

        String resultType = prefs.getString(
                VoiceRecognitionSettings.PREF_KEY_RESULTS_TYPE,
                String.valueOf(VoiceRecognitionSettings.RESULT_TYPE_LETTERS));
        int resultTypeInt = Integer.parseInt(resultType);

        if (resultTypeInt == VoiceRecognitionSettings.RESULT_TYPE_LETTERS) {
            results.add("a");
            results.add("b");
            results.add("c");
        } else if (resultTypeInt == VoiceRecognitionSettings.RESULT_TYPE_NUMBERS) {
            results.add("1");
            results.add("2");
            results.add("3");
        }

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION, results);

        try {
            listener.results(bundle);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


}


