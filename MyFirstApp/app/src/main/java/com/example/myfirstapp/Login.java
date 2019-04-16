package com.example.myfirstapp;


import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.*;

import static com.example.myfirstapp.CustomDialogFragment.setResponse;


public class Login extends AppCompatActivity {
    public boolean waitingResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void callPOSTRequest(View view) {
        EditText email = findViewById(R.id.emailText);
        EditText password = findViewById(R.id.passwordText);

        TextView textView = findViewById(R.id.loginTextView);

        JSONObject json = convertToJSONObj(email, password);
        if (email.length() > 0 && password.length() > 0) {
            if (!waitingResponse) {
            postRequest("login", textView, json);
            }
        } else {
            setResponse("Introduce un email y contraseña");
            loginDialog();
        }
    }


    private void postRequest(final String param, final TextView textView, final JSONObject json) {
        waitingResponse = true;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.124:3000/user/" + param;
        // casa 192.168.1.104
        // uni 192.168.1.124

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        waitingResponse = false;
                        textView.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        waitingResponse = false;
                        setResponse(error.toString());
                        loginDialog();
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    // converts the params to a JSON object and returns it
    private JSONObject convertToJSONObj(EditText email, EditText password) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("email", email.getText());
            obj.put("password", password.getText());
        } catch (JSONException error) {
            error.printStackTrace();
        }
        return obj;
    }


    public void loginDialog() {
        DialogFragment newFragment = new CustomDialogFragment();
        newFragment.show(getSupportFragmentManager(), "loginFragment");
    }
}
