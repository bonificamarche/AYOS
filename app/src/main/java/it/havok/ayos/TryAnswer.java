package it.havok.ayos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TryAnswer {

    Context CONTEXT;
    String TAG = "TryAnswer";
    String TAG_MSG = "";

    String __message;
    String __logTag = "tryAnswer";

    String URL = "https://webserv.havok.it/ayos/";
    String WS_RECORDS = "qa.php";
    String WS_AAA = "aaa.php";
    String PARAMS_KK = "8d5e957f297893487bd98fa830fa6413";
    String PARAMS_KS = "8d5e957f297893487bd98fa830fa6413";
    String PARAMS_S = "17";
    String PARAMS_U = "24943";
    //String PARAMS_U = "304";

    public TryAnswer(Context context) {
        CONTEXT = context;

    }

    public void Send(String frase) {

        //EditText editTextQuery = (EditText)findViewById(R.id.editTextQuery);

        RequestQueue queue = Volley.newRequestQueue(CONTEXT);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                URL + WS_RECORDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String esito = jsonObject.getString("esito");
                            Log.i(TAG, esito);

                            Parla parla = new Parla();
                            parla.Parla(CONTEXT, esito);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("f", frase);
                params.put("kk", PARAMS_KK);
                params.put("ks", PARAMS_KS);
                params.put("u", PARAMS_U);
                params.put("_s", PARAMS_S);

                Log.d("tryAnswer", params.toString());

                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }
    public void RAW(String frase) {

        RequestQueue queue = Volley.newRequestQueue(CONTEXT);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                URL + WS_AAA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String esito = jsonObject.getString("esito");
                            Log.i(TAG, esito);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("f", frase);
                params.put("kk", PARAMS_KK);
                params.put("ks", PARAMS_KS);

                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
