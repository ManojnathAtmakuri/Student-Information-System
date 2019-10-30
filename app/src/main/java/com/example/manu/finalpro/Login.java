package com.example.manu.finalpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText usernamelogin,passwordlogin;
    Button loginbutton;
    String url,token;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userlogin;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernamelogin =(EditText)findViewById(R.id.userNameLogin);
        passwordlogin=(EditText)findViewById(R.id.passwordLogin);
        loginbutton=(Button)findViewById(R.id.loginButton);

        url = "http://192.168.43.69/auth/login/";
        session= new UserSessionManager(this);
    }
    public void login(View v)
    {
        RequestQueue queue= Volley.newRequestQueue(this);

        String username = usernamelogin.getText().toString();
        String password=passwordlogin.getText().toString();

        Map<String, String> params = new HashMap<String, String>();
        params.put("username",username);
        params.put("password",password);

        JSONObject jsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                        if(response.has("auth_token"))
                        {
                            try {
                                token = response.getString("auth_token");
                                session.createUserLoginSession(token);


                            }catch(JSONException e)
                            {}
                            Intent i =new Intent(Login.this,Result.class) ;
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("token",token);
                            startActivity(i);
                           // Toast.makeText(Login.this,"successfully logged",Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        }

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
    public void toRegister(View v)
    {
        startActivity(new Intent(this,MainActivity.class));
    }
}
