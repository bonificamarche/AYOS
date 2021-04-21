package it.havok.ayos;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TryAnswer {

    Context __context;
    String TAG = "TryAnswer";
    String TAG_MSG = "";

    public TryAnswer(Context context){
        __context = context;
    }

    public void Send(String frase) {

        String __message;
        String __logTag = "tryAnswer";

        String url = "https://webserv.havok.it/ayos/records.php";
        String kk = "8d5e957f297893487bd98fa830fa6413";
        String ks = "8d5e957f297893487bd98fa830fa6413";
        String u = "304";

        RequestQueue queue = Volley.newRequestQueue(__context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String esito = jsonObject.getString("esito");
                            Log.i(TAG, esito);

                            Parla parla = new Parla();
                            parla.Parla(__context, esito);

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
                params.put("kk", kk);
                params.put("ks", ks);
                params.put("u", u);

                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }
}
