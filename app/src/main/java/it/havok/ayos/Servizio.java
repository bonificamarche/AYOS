package it.havok.ayos;

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;


public class Servizio extends Service
{
    String __logTag = "service";
    String __message;
    String __speakServiceMessage;
    String __language = "it-IT";
    Parla __parla = new Parla();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId)
    {
        startForeground(0, null);
        __message = "start";
        Log.i(__logTag, __message);
        Toast.makeText(getApplicationContext(), __logTag + " " + __message, Toast.LENGTH_LONG).show();

        /*
        __speakServiceMessage = "ciao, sono Alessia";
        __parla.Parla(getApplicationContext(), __speakServiceMessage);
        */

        return startId;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}