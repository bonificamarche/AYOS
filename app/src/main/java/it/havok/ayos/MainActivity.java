package it.havok.ayos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    String TAG = "voiceRecognitionActivity";
    Parla parla = new Parla();
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    Intent __intent;
    Servizio __servizio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonRekognition = findViewById(R.id.buttonRecognition);
        Button buttonSinthesys = findViewById(R.id.buttonSinthesys);
        Log.i(TAG, "onCreate");

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        buttonRekognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        buttonSinthesys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messaggio = "hai premuto il pulsante di test";
                parla.Parla(getApplicationContext(), messaggio);
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    private void AvviaServizio() {

        Log.i(TAG, "AvviaServizio");

        __servizio = new Servizio();
        __intent = new Intent(this, __servizio.getClass());
        if (!isMyServiceRunning(__servizio.getClass())) {
            startService(__intent);
        }
    }

    @Override
    protected void onDestroy() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

        AvviaServizio();
    }
}