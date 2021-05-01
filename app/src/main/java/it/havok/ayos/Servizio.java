package it.havok.ayos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class Servizio extends Service
{
    int counter = 0;
    SecondsFromLastAnswer secondsFromLastAnswer = new SecondsFromLastAnswer();
    Timer timer;
    TimerTask timerTask;
    String TAG = "ascolto";
    String __message;
    String __speakServiceMessage;
    String WAKEUP_WORD = "alessia";
    Intent recognizerIntent;
    SpeechRecognizer speechRecognizer;

    @Override
    public void onCreate() {
        super.onCreate();


        __message = "onCreate";
        Log.i(TAG, __message);
        Toast.makeText(getApplicationContext(), TAG + " " + __message, Toast.LENGTH_LONG).show();

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
        Log.i(TAG, __message);
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

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
                Log.i("Count", "=========  " + (counter++));
                /*
                if(secondsFromLastAnswer.secondsPassed > secondsFromLastAnswer.secondsFromLastAnswer){
                    secondsFromLastAnswer.secondsPassed = 0;
                }else {
                    secondsFromLastAnswer.secondsPassed++;
                }
                */

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
        Log.i(TAG, __speakServiceMessage);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplication().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ITALY.toString());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.ITALY.toString());

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                __speakServiceMessage = "in ascolto: " + params.toString();
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                Log.i(TAG, __speakServiceMessage);
            }

            @Override
            public void onBeginningOfSpeech() {
                __speakServiceMessage = "inizio parlato";
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                Log.i(TAG, __speakServiceMessage);
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
                __speakServiceMessage = "fine parlato";
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                Log.i(TAG, __speakServiceMessage);
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
                Log.i(TAG, __speakServiceMessage);


                AvviaAscolto();
            }

            @Override
            public void onResults(Bundle results) {

                String frase = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
                if(!frase.isEmpty() || frase != null){
                    ControlloAscolto(frase);
                }
                __speakServiceMessage = "onResults: " + frase;
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                //Log.i(__logTag, __speakServiceMessage);
                AvviaAscolto();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

                __speakServiceMessage = "dialogo parziale: " + partialResults.toString();
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                Log.i(TAG, __speakServiceMessage);
            }

            @Override
            public void onEvent(int eventType, Bundle params) {

                __speakServiceMessage = "evento: " + eventType + " - " + params.toString();
                //__parla.Parla(getApplicationContext(), __speakServiceMessage);
                Log.i(TAG, __speakServiceMessage);
            }
        });
    }

    public void AvviaAscolto() {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        AudioManager manager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        if(manager.getMode() == AudioManager.MODE_IN_COMMUNICATION || manager.getMode() == AudioManager.MODE_RINGTONE){
        //if(telephonyManager.getCallState() != TelephonyManager.CALL_STATE_OFFHOOK) {
            __message = "Microfono Impegnato";
            AvviaAscolto();
        }else{
            __message = "Microfono Disponibile";
            speechRecognizer.startListening(recognizerIntent);
        }
        Log.i(TAG, __message + " " + manager.getMode());
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {

        super.onStartCommand(intent, flag, startId);;

        __message = "onStartCommand";
        Log.i(TAG, __message);

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
        String intento = frase;

        Date now = new Date();

        secondsFromLastAnswer.secondsPassed = (now.getTime() - secondsFromLastAnswer.timeOfLastAnswer.getTime()) / 1000;
        Log.i(TAG, "secondsPassed:" + secondsFromLastAnswer.secondsPassed);

        if(secondsFromLastAnswer.secondsPassed <= secondsFromLastAnswer.secondsFromLastAnswer && secondsFromLastAnswer.firstIntentPassed) {
            wakeupWord = true;
        }else{
            intento = "";
            for (int i = 0; i < parole.length; i++) {
                String parola = parole[i];
                if (parola.equalsIgnoreCase(WAKEUP_WORD)) {
                    wakeupWord = true;
                } else {
                    intento += parola + " ";
                }
            }
            intento = intento.trim();
        }
        secondsFromLastAnswer.timeOfLastAnswer = new Date();

        if(!secondsFromLastAnswer.firstIntentPassed)secondsFromLastAnswer.firstIntentPassed = true;

        TryAnswer tryAnswer = new TryAnswer(getApplicationContext());
        Log.i(TAG, frase);

        if (wakeupWord) {
            Log.i(TAG, "wakeupWordFound");
            tryAnswer.Send(intento);
        }else {
            Log.i(TAG, "wakeupWordNotFound");
            tryAnswer.RAW(frase);
        }

    }
}