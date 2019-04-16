package com.example.myfirstapp;


import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity {
    public boolean waitingResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    // registers the input data in the DB
    public void register(View view) {
        TextView textView = findViewById(R.id.registerResult);
        EditText firstNameText = findViewById(R.id.firstNameText);
        EditText surnameText = findViewById(R.id.surnameText);
        EditText passwordText = findViewById(R.id.passwordText);
        EditText emailText = findViewById(R.id.emailText);
        EditText phoneText = findViewById(R.id.phoneText);

        boolean params = checkParams(firstNameText, surnameText, passwordText, emailText, phoneText);

        if (!params) {
            setResponse("Completa todos los parametros");
            registerDialog();
        } else {
            JSONObject obj = convertToJSONObj(emailText, firstNameText, surnameText, phoneText, passwordText);
            postRequest("signup", textView, obj);
        }
    }

    // change to the login activity
    public void toLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }


    // change to the gets activity
    public void toGets(View view) {
        Intent intent = new Intent(this, GetRequests.class);
        startActivity(intent);
    }


    // change to the update user activity
    public void updateUser(View view) {
        Intent intent = new Intent(this, UpdateUser.class);
        startActivity(intent);
    }


    // converts the params to a JSON object and returns it
    private JSONObject convertToJSONObj(EditText email, EditText name, EditText surname, EditText phone, EditText password) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("email", email.getText());
            obj.put("firstname", name.getText());
            obj.put("surname", surname.getText());
            obj.put("phone", phone.getText());
            obj.put("password", password.getText());
        } catch (JSONException problem) {
            problem.printStackTrace();
        }
        return obj;
    }


    // sends the postRequest
    public void postRequest(final String param, final TextView textView, final JSONObject json) {
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
                        setResponse(response.toString());
                        textView.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                waitingResponse = false;
                setResponse(error.toString());
                registerDialog();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    // checks if all the parameters have been filled
    public boolean checkParams(EditText param1, EditText param2, EditText param3, EditText param4, EditText param5) {
        return param1.getText().length() > 0
                && param2.getText().length() > 0
                && param3.getText().length() > 0
                && param4.getText().length() > 0
                && param5.getText().length() > 0;
    }


    public void registerDialog()
    {
        DialogFragment newFragment = new CustomDialogFragment();
        newFragment.show(getSupportFragmentManager(), "loginFragment");
    }
}
