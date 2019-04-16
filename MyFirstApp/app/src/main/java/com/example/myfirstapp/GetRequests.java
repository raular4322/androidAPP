package com.example.myfirstapp;


import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.example.myfirstapp.CustomDialogFragment.setResponse;


public class GetRequests extends AppCompatActivity {
    public String[] myDataSet;
    public JSONArray jsonArray;
    private boolean waitingResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_requests);

        // Se busca y oculta la progressBar hasta que sea necesaria
        ProgressBar progressBar = findViewById(R.id.progressBarGet);
        progressBar.setVisibility(View.INVISIBLE);
    }


    public void callGetRequestID(View view) {
        // Se comprueba que la app no esta esperando otra respuesta
        if (!waitingResponse) {
            TextView userIDText = findViewById(R.id.userIDText);

            // Si se introduce una ID se hace la peticion, si no se le pide al usuario una
            if (userIDText.getText().toString().length() > 0) {
                String userID = userIDText.getText().toString();
                getRequest(userID);
            } else {
                setResponse("Introduce una ID");
                getDialog();
            }
        }
    }


    public void callGetRequestALL(View view) {
        // Se comprueba que la app no esta esperando otra respuesta
        if (!waitingResponse) {
            getRequest("all");
            waitingResponse = true;
        }
    }


    private void getRequest(String URLParam) {
        waitingResponse = true;

        // Se activa la progressBar hasta la llegada de la respuesta
        final ProgressBar progressBar = findViewById(R.id.progressBarGet);
        progressBar.setVisibility(View.VISIBLE);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.124:3000/user/" + URLParam;
        // casa 192.168.1.104
        // uni 192.168.1.124

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        waitingResponse = false;

                        showResponse(response);

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                waitingResponse = false;

                setResponse(error.toString());
                getDialog();

                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public void showResponse(String response) {
        try {
            jsonArray = new JSONArray(response);
        } catch (JSONException error) {
            error.printStackTrace();
        }

        ArrayList<String> listData = new ArrayList();

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    listData.add(jsonArray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        myDataSet = listData.toArray(new String[listData.size()]);
        userList();
    }


    public void getDialog() {
        DialogFragment newFragment = new CustomDialogFragment();
        newFragment.show(getSupportFragmentManager(), "loginFragment");
    }


    // Poblar el recyclerView con los usuarios
    public void userList() {
        RecyclerView recyclerView = findViewById(R.id.recycler_get_all);

        // Improve performance
        recyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new MyAdapter(myDataSet));
    }
}
