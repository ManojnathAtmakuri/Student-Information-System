package com.example.manu.finalpro;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class Achievements extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        Bundle data = getIntent().getExtras();
        String semrank=data.getString("semrank");
        String examinfo=data.getString("examinfo");
        ScrollView sv=(ScrollView)findViewById(R.id.scrollView2);

        String year=data.getString("year");
        int yr=Integer.parseInt(year);
        String sem=data.getString("sem");
        int seme=Integer.parseInt(sem);
        TextView sr=(TextView)findViewById(R.id.semesterRank);
        sr.setText("You have achieved rank "+semrank+" in this semester");
        //makeText(this,semrank,Toast.LENGTH_SHORT).show();

        try
        {
            JSONArray jsonArray=new JSONArray(examinfo);
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String ystr=jsonObject.getString("year_of_pursue");
                String sstr=jsonObject.getString("semester");
                if(Integer.parseInt(ystr)==yr && Integer.parseInt(sstr) ==seme)
                {
                    Boolean supple=jsonObject.getBoolean("supple");
                    if(supple==false)
                    {
                        JSONArray results=jsonObject.getJSONArray("result");
                        TableRow tr= new TableRow(this);
                        TableLayout tb=new TableLayout(this);
                        sv.addView(tb);
                        for(int k=0;k<2;k++)
                        {
                            String a[]={"subject","rank"};
                            TextView tv=new TextView(this);
                            tv.setText(a[k]);
                            tr.addView(tv);

                        }
                        tb.addView(tr);
                        for(int j=0;j<results.length();j++)
                        {
                            JSONObject jsonObject1=results.getJSONObject(j);
                            JSONObject sub=jsonObject1.getJSONObject("subjects");
                            String subname=sub.getString("name");
                            JSONArray jsonArray1=jsonObject1.getJSONArray("ach_res");
                            if(jsonArray1.getJSONObject(0).getString("rank")!=null) {
                              //  Toast.makeText(this, subname, Toast.LENGTH_SHORT).show();
                               // Toast.makeText(this, jsonArray1.getJSONObject(0).getString("rank"), Toast.LENGTH_SHORT).show();


                                TableRow tr1 = new TableRow(this);
                                TextView tv1 = new TextView(this);
                                tv1.setText(subname);
                                TextView tv2 = new TextView(this);
                                tv2.setText(jsonArray1.getJSONObject(0).getString("rank"));
                                tr1.addView(tv1);
                                tr1.addView(tv2);
                                tb.addView(tr1);
                                if (j % 2 == 0) {
                                    tr1.setBackgroundColor(Color.GRAY);
                                }

                            }
                        }
                    }
                }

            }
        }catch(JSONException e){}

    }
}
