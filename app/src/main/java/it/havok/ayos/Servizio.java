package it.havok.ayos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;


public class Servizio extends Service
{
    int counter = 0;
    Timer timer;
    TimerTask timerTask;
    String __logTag = "ascolto";
    String __message;
    String __speakServiceMessage;
    String WAKEUP_WORD = "alessia";
    Intent recognizerIntent;
    SpeechRecognizer speechRecognizer;

    @Override
    public void onCreate() {
        super.onCreate();

        __message = "onCreate";
        Log.i(__logTag, __message);
        Toast.makeText(getApplicationContext(), __logTag + " " + __message, Toast.LENGTH_LONG).show();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        //__speakServiceMessage = "sono pronta";
        //__parla.Parla(getApplicationContext(), __speakServiceMessage);


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        __message = "startMyOwnForeground";
        Log.i(__logTag, __message);
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                Log.i("Count", "=========  "+ (counter++));
            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }



    public void IstanziaRecognizer()
    {
        __speakServiceMessage = "IstanziaRecognizer";
        Log.i(__logTag, __speakServiceMessage);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationInfo().name);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                __speakServiceMessage = "in ascolto: " + params.toString();
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                Log.i(__logTag, __speakServiceMessage);
            }

            @Override
            public void onBeginningOfSpeech() {
                __speakServiceMessage = "inizio ascolto";
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                Log.i(__logTag, __speakServiceMessage);
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                __message = "rms " + rmsdB;
                //Log.i(__logTag, __message);
                    /*
                    __speakServiceMessage = "rms cambiato " ;
                    __parla.Parla(getApplicationContext(), __speakServiceMessage);
                    */
            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                __speakServiceMessage = "ascolto terminato";
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                Log.i(__logTag, __speakServiceMessage);
            }

            @Override
            public void onError(int error) {

                String message = "";

                if(error == SpeechRecognizer.ERROR_AUDIO)                           message = "audio";
                else if(error == SpeechRecognizer.ERROR_CLIENT)                     message = "client";
                else if(error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS)   message = "insufficient permissions";
                else if(error == SpeechRecognizer.ERROR_NETWORK)                    message = "network";
                else if(error == SpeechRecognizer.ERROR_NETWORK_TIMEOUT)            message = "network timeout";
                else if(error == SpeechRecognizer.ERROR_NO_MATCH)                   message = "no match found";
                else if(error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY)            message = "recognizer busy";
                else if(error == SpeechRecognizer.ERROR_SERVER)                     message = "server";
                else if(error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT)             message = "speech timeout";

                __speakServiceMessage = "errore " + message;
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                Log.i(__logTag, __speakServiceMessage);


                AvviaAscolto();
            }

            @Override
            public void onResults(Bundle results) {

                String frase = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
                ControlloAscolto(frase);
                __speakServiceMessage = "onResults: " + frase;
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                //Log.i(__logTag, __speakServiceMessage);
                AvviaAscolto();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

                __speakServiceMessage = "dialogo parziale: " + partialResults.toString();
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                Log.i(__logTag, __speakServiceMessage);
            }

            @Override
            public void onEvent(int eventType, Bundle params) {

                __speakServiceMessage = "evento: " + eventType + " - " + params.toString();
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                Log.i(__logTag, __speakServiceMessage);
            }
        });
    }

    public void AvviaAscolto() {
        __message = "AvviaAscolto";
        Log.i(__logTag, __message);
        speechRecognizer.startListening(recognizerIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {

        super.onStartCommand(intent, flag, startId);;

        __message = "onStartCommand";
        Log.i(__logTag, __message);

        startTimer();
        IstanziaRecognizer();
        AvviaAscolto();

        return START_STICKY;

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        stoptimertask();

        speechRecognizer.destroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    public void ControlloAscolto(String frase)
    {
        boolean wakeupWord = false;

        String[] parole = frase.split(" ");

        for(int i = 0; i < parole.length; i++)
        {
            if(parole[i].equalsIgnoreCase(WAKEUP_WORD))wakeupWord = true;
        }

        if (wakeupWord) {
            Log.i("wakeupWord", "found");

            TryAnswer tryAnswer = new TryAnswer(getApplicationContext());
            tryAnswer.Send(frase);
        }

    }

}