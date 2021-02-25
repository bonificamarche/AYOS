package it.havok.ayos;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

public class Avvio extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1)
    {
        Intent startupBootIntent = new Intent(context, MainActivity.class);
        startupBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startupBootIntent);

    }
}
