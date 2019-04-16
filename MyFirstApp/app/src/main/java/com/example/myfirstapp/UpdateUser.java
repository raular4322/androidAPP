package com.example.myfirstapp;


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

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.myfirstapp.CustomDialogFragment.setResponse;


public class UpdateUser extends AppCompatActivity {
    public boolean waitingResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
    }


    public void updateUserData(View view) {
        TextView textView = findViewById(R.id.updateResult);
        EditText id = findViewById(R.id.idText);
        EditText firstNameText = findViewById(R.id.firstNameText);
        EditText surnameText = findViewById(R.id.surnameText);
        EditText passwordText = findViewById(R.id.passwordText);
        EditText emailText = findViewById(R.id.emailText);
        EditText phoneText = findViewById(R.id.phoneText);

        JSONObject obj = convertToJSONObj(emailText, firstNameText, surnameText, phoneText, passwordText);
        putRequest("update/" + id.getText(), textView, obj);
        waitingResponse = true;
    }


    public void putRequest(final String param, final TextView textView, final JSONObject json) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.124:3000/user/" + param;
        // casa 192.168.1.104
        // uni 192.168.1.124

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        waitingResponse = false;
                        setResponse(response.toString());
                        updateDialog();
                        // Display the first 500 characters of the response string.
                        textView.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                waitingResponse = false;
                setResponse(error.toString());
                updateDialog();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


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


    public void updateDialog() {
        DialogFragment newFragment = new CustomDialogFragment();
        newFragment.show(getSupportFragmentManager(), "loginFragment");
    }
}
