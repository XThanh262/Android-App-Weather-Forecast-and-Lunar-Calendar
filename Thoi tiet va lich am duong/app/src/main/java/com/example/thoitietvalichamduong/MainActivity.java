package com.example.thoitietvalichamduong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText editTimKiem;
    Button BottomTimKiem,bottom7Ngay,bottomLich;
    TextView TextTenTp,TextTenNc,textNhietDo,textTrangThai,textDoAm,textMay,textSucGio,textNgay;
    ImageView imageIcon;
    String tp = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        LayDuLieuThoiTiet("Ha Noi");
        BottomTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Tp = editTimKiem.getText().toString();
                if (Tp.equals(""))
                {
                    tp = "Ha Noi";
                    LayDuLieuThoiTiet(tp);
                }else
                    tp = Tp;
                LayDuLieuThoiTiet(tp);
            }
        });
        bottom7Ngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Tp = editTimKiem.getText().toString();
                Intent intent = new Intent(MainActivity.this,MainActivity7ngay.class);
                intent.putExtra("name",Tp);
                startActivity(intent);
            }
        });
        bottomLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainActivityLich.class);
                startActivity(intent);
            }
        });
    }
    public void  LayDuLieuThoiTiet(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=1a5b7408a0cc12088bfefa4019ef6594";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String ngay = jsonObject.getString("dt");
                            String Tp = jsonObject.getString("name");
                            TextTenTp.setText("Tên thành phố :"+Tp);

                            Long l = Long.valueOf(ngay);
                            Date date = new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                            String Ngay = simpleDateFormat.format(date);
                            SimpleDateFormat simpleDateFormatThu = new SimpleDateFormat("EEE");
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
                            String ThuNgay = ""+thu+" "+Ngay;
                            textNgay.setText(ThuNgay);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String trangthai = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.with(MainActivity.this).load("https://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageIcon);
                            String Trangthai = "";
                            switch (trangthai) {
                                case  "Clear": Trangthai="Trời quang"; break;
                                case "Clouds": Trangthai="Có mây"; break;
                                case "Rain": Trangthai="Có mưa"; break;
                                case "Thunderstorm": Trangthai="Có Sấm"; break;
                                case  "Snow": Trangthai="Có Tuyết"; break;
                                case  "Mist": Trangthai="Có sương mù"; break;
                            }
                            textTrangThai.setText(Trangthai);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");

                            Double a = Double.valueOf(nhietdo);
                            String Nhietdo = String.valueOf(a.intValue());
                            textNhietDo.setText(Nhietdo+"°C");
                            textDoAm.setText(doam+"%");

                            JSONObject jsonObjectwind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectwind.getString("speed");
                            textSucGio.setText(gio+"m/s");

                            JSONObject jsonObjectmay = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectmay.getString("all");
                            textMay.setText(may+"%");

                            JSONObject jsonObjectSys =jsonObject.getJSONObject("sys");
                            String nc = jsonObjectSys.getString("country");
                            TextTenNc.setText("Quốc gia: "+nc);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.d("Ketqua", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
    private  void  Anhxa(){
        editTimKiem = (EditText) findViewById(R.id.editTimkiem);
        BottomTimKiem = (Button) findViewById(R.id.BottomTimkiem);
        bottom7Ngay = (Button) findViewById(R.id.bottom7Ngay);
        bottomLich = (Button)  findViewById(R.id.bottomLich);
        TextTenTp = (TextView) findViewById(R.id.TextTenTp);
        TextTenNc = (TextView) findViewById(R.id.TextTenNc);
        textNhietDo = (TextView) findViewById(R.id.textNhietDo);
        textTrangThai = (TextView) findViewById(R.id.textTrangThai);
        textDoAm = (TextView) findViewById(R.id.textDoAm);
        textMay = (TextView) findViewById(R.id.textMay);
        textSucGio = (TextView) findViewById(R.id.textSucGio);
        textNgay = (TextView) findViewById(R.id.textNgay);
        imageIcon = (ImageView) findViewById(R.id.imageIcon);
    }
}