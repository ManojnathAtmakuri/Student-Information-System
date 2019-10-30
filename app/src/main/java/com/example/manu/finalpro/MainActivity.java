package com.example.manu.finalpro;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends AppCompatActivity {
    EditText usernameregister, emailregister, passwordregister, mobileregister;
    CheckBox isstudentregister;
    Button registerbutton;
    TextView tologinregister;

    String url;
    int isstudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameregister = (EditText) findViewById(R.id.userNameRegister);
        emailregister = (EditText) findViewById(R.id.emailRegister);
        passwordregister = (EditText) findViewById(R.id.passwordRegister);
        mobileregister = (EditText) findViewById(R.id.mobileRegister);
        isstudentregister = (CheckBox) findViewById(R.id.studentRegister);
        registerbutton = (Button) findViewById(R.id.registerButton);
        tologinregister = (TextView) findViewById(R.id.toLoginRegister);

        url = "http://192.168.43.69/api/register/";
    }

    public void register(View v) {

        RequestQueue queue= Volley.newRequestQueue(this);

        String username=usernameregister.getText().toString();
        String email=emailregister.getText().toString();
        String mobile=mobileregister.getText().toString();
        String password=passwordregister.getText().toString();
        if(isstudentregister.isChecked())
        {
            isstudent =1;
        }
        else {
            isstudent=0;
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("username",username);
        params.put("email",email);
        params.put("mobile",mobile);
        params.put("password",password);
        params.put("is_student",""+isstudent);

        JSONObject jsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

// Add the request to the RequestQueue.
        queue.add(jsonObjRequest);

    }
    public void registertologin(View v)
    {
        startActivity(new Intent(this,Login.class));
    }

}