package com.example.thoitietvalichamduong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<ThoiTiet> arrayList;

    public CustomAdapter(Context context, ArrayList<ThoiTiet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dong_listview,null);

        ThoiTiet thoiTiet = arrayList.get(i);
        TextView textDay = (TextView) view.findViewById(R.id.textViewNgay);
        TextView textStatus = (TextView) view.findViewById(R.id.textViewTrangThai);
        TextView textMaxTemp = (TextView) view.findViewById(R.id.textViewNhietDoLon);
        TextView textMinTemp = (TextView) view.findViewById(R.id.textViewNhietDoNho);
        ImageView ImageStatus = (ImageView) view.findViewById(R.id.imageViewTrangthai);

        textDay.setText(thoiTiet.Day);
        textStatus.setText(thoiTiet.Status);
        textMaxTemp.setText(thoiTiet.MaxTemp+"°C");
        textMinTemp.setText(thoiTiet.MinTemp+"°C");

        Picasso.with(context).load("https://openweathermap.org/img/wn/"+thoiTiet.Image+"@2x.png").into(ImageStatus);
        return view;
    }
}
