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
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class Ascolta extends RecognitionService {


    int counter = 0;
    Timer timer;
    TimerTask timerTask;
    String __logTag = "ascolto";
    String __message;
    String __speakServiceMessage;
    Intent recognizerIntent;
    String __language = "it-IT";
    Parla __parla = new Parla();
    SpeechRecognizer speechRecognizer;
    String ACTIVATION_WORD = "ALESSIA";
    String API_KK = "8d5e957f297893487bd98fa830fa6413";
    String API_KS = "8d5e957f297893487bd98fa830fa6413";


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

    public void IstanziaRecognizer()
    {
        recognizerIntent = new Intent(SERVICE_INTERFACE);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationInfo().name);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getBaseContext());
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

                __speakServiceMessage = "errore " + error;
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                Log.i(__logTag, __speakServiceMessage);

                if (error == 7) {

                }
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

        super.onStartCommand(intent, flag, startId);

        __message = "onStartCommand";
        Log.i(__logTag, __message);

        startTimer();
        IstanziaRecognizer();
        AvviaAscolto();

        return START_STICKY;
    }



    @Override
    protected void onCancel(Callback listener) {
        __speakServiceMessage = "onCancel";
        Log.i(__logTag, __speakServiceMessage);

    }

    @Nullable
    @Override
    public void onDestroy() {
        __speakServiceMessage = "onDestroy";
        Log.i(__logTag, __speakServiceMessage);

        super.onDestroy();

        stoptimertask();

        speechRecognizer.destroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
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

    @Override
    protected void onStopListening(Callback listener) {

        __speakServiceMessage = "onStopListening";
        Log.i(__logTag, __speakServiceMessage);

        AvviaAscolto();
    }

    @Override
    protected void onStartListening(Intent recognizerIntent, Callback listener) {

        __speakServiceMessage = "onStartListening";
        Log.i(__logTag, __speakServiceMessage);

    }

    public void ControlloAscolto(String frase)
    {
        boolean activationKey = false;

        String[] parole = frase.split(" ");

        /*
        for(int i = 0; i < parole.length; i++)
        {
            if(parole[i].equalsIgnoreCase(ACTIVATION_WORD))activationKey = true;
        }
        */

        if (activationKey) {
            __speakServiceMessage = frase;
            __parla.Parla(getApplicationContext(), __speakServiceMessage);
            //Log.i(__logTag, __speakServiceMessage);
        }

        __speakServiceMessage = frase;
        __parla.Parla(getApplicationContext(), __speakServiceMessage);
    }
}


