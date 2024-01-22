package com.example.thoitietvalichamduong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity7ngay extends AppCompatActivity {
    String tenthanhpho = "";
    ImageView imageBack;
    TextView TextTenTp;
    ListView ListView;
    CustomAdapter customAdapter;
    ArrayList<ThoiTiet> mangthoitiet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity7ngay);
        Anhxa();
        Intent intent = getIntent();
        String Tp = intent.getStringExtra("name");
        Log.d("KetQua:","DuLieu:"+ Tp);
        if (Tp.equals(""))
        {
            tenthanhpho = "Ha Noi";
            LayDuLieu7Ngay(tenthanhpho);
        }else {
            tenthanhpho = Tp;
            LayDuLieu7Ngay(tenthanhpho);
        }
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void Anhxa() {
        imageBack = (ImageView)  findViewById(R.id.imageBack);
        TextTenTp = (TextView) findViewById(R.id.TextTenTp);
        ListView = (ListView) findViewById(R.id.ListView);
        mangthoitiet = new ArrayList<ThoiTiet>();
        customAdapter = new CustomAdapter(MainActivity7ngay.this,mangthoitiet);
        ListView.setAdapter(customAdapter);
    }


    private void  LayDuLieu7Ngay(String data){
        String url = "https://api.openweathermap.org/data/2.5/forecast/daily?q="+data+"&units=metric&cnt=7&appid=53fbf527d52d4d773e828243b90c1f8e";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity7ngay.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*Log.d("Ket qua:","Json:" + response);*/
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectCity =jsonObject.getJSONObject("city");
                            String name = jsonObjectCity.getString("name");
                            TextTenTp.setText(name);

                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            for(int i=0; i<jsonArrayList.length();i++){
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String ngay = jsonObjectList.getString("dt");

                                Long l = Long.valueOf(ngay);
                                Date date = new Date(l*1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                SimpleDateFormat simpleDateFormatThu = new SimpleDateFormat("EEE");
                                String Ngay = simpleDateFormat.format(date);
                                String Thu = simpleDateFormatThu.format(date);
                                String thu = "";
                                switch (Thu) {
                                    case "Mon": thu ="Thứ Hai"; break;
                                    case "Tue": thu = "Thứ Ba"; break;
                                    case "Wed": thu = "Thứ Tư"; break;
                                    case "Thu": thu = "Thứ Năm"; break;
                                    case "Fri": thu = "Thứ Sáu"; break;
                                    case "Sat": thu = "Thứ Bảy"; break;
                                    case "Sun": thu = "Chủ nhật"; break;
                                };
                                String day = ""+thu+" "+Ngay;

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                                String max = jsonObjectTemp.getString("max");
                                String min = jsonObjectTemp.getString("min");
                                Double a = Double.valueOf(max);
                                Double b = Double.valueOf(min);
                                String maxTemp = String.valueOf(a.intValue());
                                String minTemp = String.valueOf(b.intValue());

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String trangthai = jsonObjectWeather.getString("main");
                                String status = "";
                                switch (trangthai) {
                                    case  "Clear": status="Trời quang"; break;
                                    case "Clouds": status="Có mây"; break;
                                    case "Rain": status="Có mưa"; break;
                                    case "Thunderstorm": status="Có Sấm"; break;
                                    case  "Snow": status="Có Tuyết"; break;
                                    case  "Mist": status="Có sương mù"; break;
                                }
                                String image = jsonObjectWeather.getString("icon");

                                mangthoitiet.add(new ThoiTiet(day,status,image,maxTemp,minTemp));
                                Log.d("DuLieu 7 ngay","DuLieu"+day);
                            }
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
}