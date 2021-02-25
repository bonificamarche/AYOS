package it.havok.ayos;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    String TAG = "voiceRecognitionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonRekognition = findViewById(R.id.buttonRecognition);
        Button buttonSinthesys = findViewById(R.id.buttonSinthesys);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;

            // If the user previously denied this permission then show a message explaining why
            // this permission is needed
            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
        else{
            startService(new Intent(this, Ascolta.class));
        }


        buttonRekognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        buttonSinthesys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messaggio = "hai premuto il pulsante di test";
                Parla parla = new Parla();
                parla.Parla(getApplicationContext(), messaggio);
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        startService(new Intent(this, Ascolta.class));
    }
}