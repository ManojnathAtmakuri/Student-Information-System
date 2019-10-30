package com.example.manu.finalpro;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result extends AppCompatActivity {
    UserSessionManager session;
    String url, auth, month, yr, name,semrank;
    ScrollView sv;
    int i, b, j, y = 0, k,e;
    int a = 0;
    List<Integer> sem;
    TableRow.LayoutParams lp;
    TableLayout tb1, tb2, tb3;
    JSONArray examinfo,result,achsemarray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        session=new UserSessionManager(this);
        if(session.checkLogin())
            finish();

        HashMap<String,String> user =session.getUserDetails();
        auth=user.get(UserSessionManager.TOKEN);
       // Toast.makeText(this,session.isUserLoggedIn()+"",Toast.LENGTH_SHORT).show();
        tb1 = new TableLayout(Result.this);
        sv = (ScrollView) findViewById(R.id.scrollView);
        tb2 = new TableLayout(Result.this);
        sv.addView(tb2);
        final LinearLayout studlay=(LinearLayout)findViewById(R.id.studentButton);

        Bundle data = getIntent().getExtras();
        //  auth = data.getString("token");

        url = "http://192.168.43.69/api/results/";

        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("user");

                            Boolean user = jsonObject.getBoolean("is_student");
                            if (user == true) {

                                JSONObject jsonObject1 = response.getJSONObject("students");
                                name = jsonObject1.getString("name");
                                String ht=jsonObject1.getString("hall_ticket");
                                LinearLayout studlay=(LinearLayout)findViewById(R.id.studentButton);
                                TableRow tr2 = new TableRow(Result.this);
                                TextView textView = new TextView(Result.this);
                                textView.setText(name+"-"+ht);
                                textView.setTypeface(null, Typeface.BOLD);
                                textView.setTextColor(Color.BLACK);
                                tr2.setGravity(Gravity.CENTER_HORIZONTAL);
                                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                studlay.setGravity(Gravity.CENTER_HORIZONTAL);
                                studlay.addView(textView);

                                LinearLayout yearLayout = (LinearLayout) findViewById(R.id.yearButton);
                                examinfo = jsonObject1.getJSONArray("examinfo");
                                int x = examinfo.length();
                                String yearbuttons = examinfo.getJSONObject(x - 1).getString("year_of_pursue");
                                int ybut = Integer.parseInt(yearbuttons);
                                // Toast.makeText(Result.this,ybut+"",Toast.LENGTH_LONG).show();
                                TableLayout tb = new TableLayout(Result.this);
                                TableRow tr = new TableRow(Result.this);
                                lp = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                                yearLayout.addView(tb);
                                for (i = 1; i <= ybut; i++) {
                                    Button b = new Button(Result.this);
                                    b.setId(i);
                                    b.setText( "year "+i);
                                    b.setLayoutParams(lp);
                                    tr.addView(b);
                                    b.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            tb1.removeAllViews();
                                            a = v.getId();
                                            for (j = 0; j <= examinfo.length(); j++) {
                                                try {
                                                    int x = Integer.parseInt(examinfo.getJSONObject(j).getString("year_of_pursue"));

                                                    if (x == a) {
                                                        String semstring = examinfo.getJSONObject(j).getString("semester");
                                                        sem = new ArrayList<Integer>();
                                                        sem.add(Integer.parseInt(semstring));

                                                    }
                                                } catch (JSONException e) {
                                                }
                                            }
                                            Collections.sort(sem);
                                            int max = sem.get(sem.size() - 1);
                                            // Toast.makeText(Result.this, max + "", Toast.LENGTH_LONG).show();
                                            LinearLayout sembut = (LinearLayout) findViewById(R.id.semButton);

                                            TableRow tr1 = new TableRow(Result.this);
                                            //LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                            sembut.removeView(tb1);
                                            sembut.addView(tb1);
                                            for (k = 1; k <= max; k++) {
                                                Button b1 = new Button(Result.this);
                                                b1.setId(k);
                                                b1.setText("sem "+k);
                                                b1.setWidth(330);
                                                tr1.addView(b1);
                                                b1.setOnClickListener(sembtnListener);

                                            }
                                            tb1.addView(tr1);


                                        }
                                    });

                                }
                                tb.addView(tr);

                            }
                            else
                            {
                                final JSONArray studentslist = response.getJSONArray("students");
                                for( e =0;e<studentslist.length();e++)
                                {
                                    String halltick=studentslist.getJSONObject(e).getString("hall_ticket");
                                    final String stuname=studentslist.getJSONObject(e).getString("name");
                                    Button studb= new Button(Result.this);
                                    studb.setText(halltick);
                                    studb.setId(e);
                                    // studb.setLayoutParams(lp);

                                    studb.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int x=v.getId();
                                            Toast.makeText(Result.this,stuname,Toast.LENGTH_SHORT).show();
                                            //Toast.makeText(Result.this,x+"",Toast.LENGTH_SHORT).show();

                                            try {
                                                JSONObject detail = studentslist.getJSONObject(x);
                                                // Toast.makeText(Result.this,detail.toString(),Toast.LENGTH_SHORT).show();
                                                LinearLayout yearLayout = (LinearLayout) findViewById(R.id.yearButton);
                                                examinfo = detail.getJSONArray("examinfo");
                                                int p = examinfo.length();
                                                String yearbuttons = examinfo.getJSONObject(p - 1).getString("year_of_pursue");
                                                int ybut = Integer.parseInt(yearbuttons);
                                                //Toast.makeText(Result.this,ybut+"",Toast.LENGTH_LONG).show();
                                                TableLayout tb = new TableLayout(Result.this);
                                                TableRow tr = new TableRow(Result.this);
                                                lp = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2.0f);
                                                yearLayout.addView(tb);
                                                for (i = 1; i <= ybut; i++) {
                                                    Button b = new Button(Result.this);
                                                    b.setId(i);
                                                    b.setText( "year "+i);
                                                    b.setLayoutParams(lp);
                                                    tr.addView(b);
                                                    b.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            tb1.removeAllViews();
                                                            a = v.getId();
                                                            for (j = 0; j <= examinfo.length(); j++) {
                                                                try {
                                                                    int x = Integer.parseInt(examinfo.getJSONObject(j).getString("year_of_pursue"));

                                                                    if (x == a) {
                                                                        String semstring = examinfo.getJSONObject(j).getString("semester");
                                                                        sem = new ArrayList<Integer>();
                                                                        sem.add(Integer.parseInt(semstring));

                                                                    }
                                                                } catch (JSONException e) {
                                                                }
                                                            }
                                                            Collections.sort(sem);
                                                            int max = sem.get(sem.size() - 1);
                                                            // Toast.makeText(Result.this, max + "", Toast.LENGTH_LONG).show();
                                                            LinearLayout sembut = (LinearLayout) findViewById(R.id.semButton);

                                                            TableRow tr1 = new TableRow(Result.this);
                                                            //LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                                            sembut.removeView(tb1);
                                                            sembut.addView(tb1);
                                                            for (k = 1; k <= max; k++) {
                                                                Button b1 = new Button(Result.this);
                                                                b1.setId(k);
                                                                b1.setText("sem"+k);
                                                                b1.setWidth(330);
                                                                // b1.setBackgroundColor(Color.parseColor("#A4A370"));
                                                                b1.setLayoutParams(lp);
                                                                //b1.setGravity(Gravity.CENTER);
                                                                tr1.addView(b1);
                                                                b1.setOnClickListener(sembtnListener);

                                                            }
                                                            tb1.addView(tr1);


                                                        }
                                                    });

                                                }
                                                tb.addView(tr);

                                            }catch(JSONException e)
                                            {

                                            }


                                        }
                                    });
                                    studlay.addView(studb);
                                }

                            }

                        } catch (JSONException e) {

                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Result.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "token " + auth);
                return headers;
            }
        };


        queue.add(jsonObjRequest);

    }

    View.OnClickListener sembtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tb2.removeAllViews();
            b = v.getId();
            final int d=b;
            final int c = a;



            // Toast.makeText(Result.this,name,Toast.LENGTH_SHORT).show();


            for (int l = 0; l < examinfo.length(); l++) {
                try {
                    final Boolean supple = examinfo.getJSONObject(l).getBoolean("supple");
                    String ystr = examinfo.getJSONObject(l).getString("year_of_pursue");
                    String sstr = examinfo.getJSONObject(l).getString("semester");
                    month = examinfo.getJSONObject(l).getString("month_of_year");
                    yr = examinfo.getJSONObject(l).getString("year_of_calendar");



                    if (Integer.parseInt(ystr) == c && Integer.parseInt(sstr) == b) {
                        JSONObject results = examinfo.getJSONObject(l);
                        result = results.getJSONArray("result");

                        // final String semrank=achsemarray.getJSONObject(0).getString("rank");
                        tb3=new TableLayout(Result.this);
                        TableRow tr5 = new TableRow(Result.this);
                        TextView textView1 = new TextView(Result.this);
                        textView1.setText("Held on : "+month+""+yr+"\n");
                        textView1.setTypeface(null, Typeface.BOLD);
                        textView1.setTextColor(Color.BLACK);
                        tr5.setGravity(Gravity.CENTER_HORIZONTAL);
                        tr5.addView(textView1);
                        tb3.addView(tr5);
                        tb2.addView(tb3);

                        String a[] = {"Subject ", "Internal ", "External ", "Result ", "Credit"};
                        TableRow tr3 = new TableRow(Result.this);
                        for (int e = 0; e < a.length; e++) {
                            TextView textView2 = new TextView(Result.this);
                            textView2.setText(a[e]);
                            textView2.setTextColor(Color.BLACK);
                            textView2.setTypeface(Typeface.DEFAULT_BOLD);
                            tr3.addView(textView2);
                        }
                        tb2.addView(tr3);
                        for(int q=0;q<result.length();q++)
                        {
                            TableRow tr4=new TableRow(Result.this);
                            JSONObject insideres=result.getJSONObject(q);
                            JSONObject subname=insideres.getJSONObject("subjects");
                            String sname=subname.getString("name");
                            String internal = insideres.getString("internal_marks");
                            String external = insideres.getString("external_marks");
                            String credits = insideres.getString("credits");
                            String res = insideres.getString("results");
                            TextView tv = new TextView(Result.this);
                            tv.setText(sname+"");
                            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                            tv.setMaxWidth(270);
                            tv.setPadding(5,5,5,5);
                            TextView tv1 = new TextView(Result.this);
                            tv1.setText(internal+"");

                            tv.setPadding(5,5,5,5);
                            TextView tv2 = new TextView(Result.this);
                            tv2.setText(external+"");

                            tv.setPadding(5,5,5,5);
                            TextView tv3 = new TextView(Result.this);
                            tv3.setText(res+"");

                            tv.setPadding(5,5,5,5);
                            TextView tv4 = new TextView(Result.this);
                            tv4.setText(credits+"");

                            tv.setPadding(5,5,5,5);
                            tr4.addView(tv);
                            tr4.addView(tv1);
                            tr4.addView(tv2);
                            tr4.addView(tv3);
                            tr4.addView(tv4);
                            tb2.addView(tr4);
                            if(  q%2==0)
                            {
                                tr4.setBackgroundColor(Color.GRAY);
                            }


                        }

                        if(supple == false) {
                            achsemarray = results.getJSONArray("examinfo");
                            Button achbut = (Button) findViewById(R.id.achButton);
                            achbut.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        // Toast.makeText(Result.this, achsemarray.toString(), Toast.LENGTH_SHORT).show();
                                        semrank = achsemarray.getJSONObject(0).getString("rank");
                                    }catch(JSONException e){}
                                    Intent intent = new Intent(Result.this,Achievements.class);
                                    intent.putExtra("semrank",semrank);
                                    intent.putExtra("examinfo",examinfo.toString());
                                    intent.putExtra("year",c+"");
                                    intent.putExtra("sem",d+"");
                                    startActivity(intent);

                                }
                            });
                        }



                    }
                } catch (JSONException e) {

                }
            }
            // Toast.makeText(Result.this, result.toString(),Toast.LENGTH_SHORT).show();
        }
    };
    public void logout(View v)
    {
        session.logoutUser();
    }

}