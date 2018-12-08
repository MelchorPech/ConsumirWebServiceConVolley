package com.example.m_p_g.consumirwebserviceconvolley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnConsumirWeb, btnParsear;
    EditText edtURL, edtRespuesta;
    String respuestaServidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtURL = (EditText) findViewById(R.id.edtURL);
        edtRespuesta = (EditText) findViewById(R.id.edtResultado);
        btnConsumirWeb = (Button) findViewById(R.id.btn);
        btnParsear = (Button) findViewById(R.id.button);
        btnParsear.setOnClickListener(this);
        btnConsumirWeb.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn ) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = edtURL.getText().toString();

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        edtRespuesta.setText("Respuesta es: " + response);
                        respuestaServidor = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                edtRespuesta.setText("NO funciona el web Service LUL");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    else{ //btnParsear
        parsearJSon(respuestaServidor);
    }

}



    public void parsearJSon(String respuestaJSon){
        if (respuestaJSon != null) {
            try {
                JSONArray arregloJson = new JSONArray(respuestaJSon);

                edtRespuesta.setText("");

                // looping through All Contacts
                for (int i = 0; i < arregloJson.length(); i++) {
                    JSONObject c = arregloJson.getJSONObject(i);
                    String codigo = c.getString("Código");
                    String nombre = c.getString("Nombre");
                    String direccion = c.getString("Dirección");
                    String foto = c.getString("Foto");

                    //¿Qué hacer con cada valor de los atributos?
                    edtRespuesta.setText(edtRespuesta.getText() + "\nCódigo: " + codigo
                            + "\nnombre: " + nombre
                            + "\ndirección: " + direccion
                            + "\nfoto: " + foto + "\n");

                }
            } catch (final JSONException e) {
                Log.e("ParsearJSON", "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Error al parsear Json: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }

        } else {
            Log.e("ParsearJSON", "No se obtuvo respuesta JSON del servidor");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Error al solicitar JSON del servidor. Revise el LogCat por posibles errores!",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}



