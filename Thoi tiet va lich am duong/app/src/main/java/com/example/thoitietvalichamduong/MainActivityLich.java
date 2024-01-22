package com.example.thoitietvalichamduong;


import static java.lang.Math.PI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivityLich extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText, LichAmTV;
    private RecyclerView LichRecyclerview;
    private LocalDate selectDate;
    int dd;
    int mm;
    int yy;
    String ngayAm;
    String thangAm;
    String namAm;
    String namAmLich;
    double k; double timeZone;
    double jdn;
    double a11;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lich);
        initWigets();
        selectDate = LocalDate.now();
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(monthYearFormDate(selectDate));
        ArrayList<String> dayInMonth = dayInMonthArray(selectDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(dayInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        LichRecyclerview.setLayoutManager(layoutManager);
        LichRecyclerview.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> dayInMonthArray(LocalDate date) {
        ArrayList<String> dayInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int dayInMoth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i =2 ; i<=42; i++)
        {
            if(i <= dayOfWeek || i > dayInMoth + dayOfWeek)
            {
                dayInMonthArray.add("");
            }
            else
            {
                dayInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  dayInMonthArray;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String monthYearFormDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String monthFormDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String YearFormDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return date.format(formatter);
    }

    private void initWigets() {
        LichRecyclerview = findViewById(R.id.LichRecyclerview);
        monthYearText = findViewById(R.id.thangNamTV);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int positson, String dayText) {
        if (dayText.equals(""))
        {
            String messge = "Chọn thời gian" + dayText +" "+ monthYearFormDate(selectDate);
            Toast.makeText(this, messge, Toast.LENGTH_LONG).show();
        }else
        {
            String messge = "Chọn thời gian " + dayText +" "+ monthYearFormDate(selectDate);
            //Toast.makeText(this, messge, Toast.LENGTH_LONG).show();
            String lichDuong = "" + dayText +" "+ monthFormDate(selectDate)+" "+YearFormDate(selectDate);
            int dd = Integer.parseInt(dayText);
            int mm = Integer.parseInt(monthFormDate(selectDate));
            int yy = Integer.parseInt(YearFormDate(selectDate));
            LichAmTV = findViewById(R.id.LichAmTV);
            jdFromDate(dd, mm, yy);
            getNewMoonDay(k, timeZone);
            getSunLongitude(jdn, (int) timeZone);
            getSunLongitude2((int) jdn);
            getLunarMonth11( yy, (int) timeZone);
            getLeapMonthOffset(a11, (int) timeZone);
            convertSolar2Lunar(dd, mm, yy, (int) timeZone);
            doiNam();
            String lichAm = ""+ngayAm+" "+thangAm+" "+namAmLich;
            LichAmTV.setText(lichAm);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ThangSau(View view) {
        selectDate = selectDate.plusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ThangTruoc(View view) {
        selectDate = selectDate.minusMonths(1);
        setMonthView();
    }

    //Doi Lich am
    public int jdFromDate(int dd, int mm, int yy) {
        double a = Math.floor((14 - mm) / 12);
        double y = yy + 4800 - a;
        double m = mm + 12 * a - 3;
        double jd = dd + Math.floor((153 * m + 2) / 5) + 365 * y + Math.floor(y / 4) - Math.floor(y / 100)
                + Math.floor(y / 400) - 32045;
        if (jd < 2299161) {
            jd = dd + Math.floor((153 * m + 2) / 5) + 365 * y + Math.floor(y / 4) - 32083;
        }
        return (int) jd;
    }

    public double getNewMoonDay(double k, double timeZone) {
        double T, T2, T3, dr, Jd1, M, Mpr, F, C1, deltat, JdNew;
        T = k / 1236.85; // Time in Julian centuries from 1900 January 0.5
        T2 = T * T;
        T3 = T2 * T;
        dr = Math.PI / 180;
        Jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * T2 - 0.000000155 * T3;
        Jd1 = Jd1 + 0.00033 * Math.sin((166.56 + 132.87 * T - 0.009173 * T2) * dr); // Mean new moon
        M = 359.2242 + 29.10535608 * k - 0.0000333 * T2 - 0.00000347 * T3; // Sun"s mean anomaly
        Mpr = 306.0253 + 385.81691806 * k + 0.0107306 * T2 + 0.00001236 * T3; // Moon"s mean anomaly
        F = 21.2964 + 390.67050646 * k - 0.0016528 * T2 - 0.00000239 * T3; // Moon"s argument of latitude
        C1 = (0.1734 - 0.000393 * T) * Math.sin(M * dr) + 0.0021 * Math.sin(2 * dr * M);
        C1 = C1 - 0.4068 * Math.sin(Mpr * dr) + 0.0161 * Math.sin(dr * 2 * Mpr);
        C1 = C1 - 0.0004 * Math.sin(dr * 3 * Mpr);
        C1 = C1 + 0.0104 * Math.sin(dr * 2 * F) - 0.0051 * Math.sin(dr * (M + Mpr));
        C1 = C1 - 0.0074 * Math.sin(dr * (M - Mpr)) + 0.0004 * Math.sin(dr * (2 * F + M));
        C1 = C1 - 0.0004 * Math.sin(dr * (2 * F - M)) - 0.0006 * Math.sin(dr * (2 * F + Mpr));
        C1 = C1 + 0.0010 * Math.sin(dr * (2 * F - Mpr)) + 0.0005 * Math.sin(dr * (2 * Mpr + M));
        if (T < -11) {
            deltat = 0.001 + 0.000839 * T + 0.0002261 * T2 - 0.00000845 * T3 - 0.000000081 * T * T3;
        } else {
            deltat = -0.000278 + 0.000265 * T + 0.000262 * T2;
        }
        ;
        JdNew = Jd1 + C1 - deltat;
        return Math.floor(JdNew + 0.5 + timeZone / 24);
    }

    public double getSunLongitude(double jdn, int timeZone) {
        double T, T2, dr, M, L0, DL, L;
        T = (jdn - 2451545.5 - timeZone / 24) / 36525; // Time in Julian centuries from 2000-01-01 12:00:00 GMT
        T2 = T * T;
        dr = Math.PI / 180; // degree to radian
        M = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2; // mean anomaly, degree
        L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2; // mean longitude, degree
        DL = (1.914600 - 0.004817 * T - 0.000014 * T2) * Math.sin(dr * M);
        DL = DL + (0.019993 - 0.000101 * T) * Math.sin(dr * 2 * M) + 0.000290 * Math.sin(dr * 3 * M);
        L = L0 + DL; // true longitude, degree
        double omega = 125.04 - 1934.136 * T;
        L = L - 0.00569 - 0.00478 * Math.sin(omega * dr);
        L = L * dr;
        L = L - Math.PI * 2 * (Math.floor(L / (Math.PI * 2))); // Normalize to (0, 2*PI)
        return Math.floor(L / Math.PI * 6);
    }

    public double getSunLongitude2(int jdn) {
        double T, T2, dr, M, L0, DL, lambda, theta, omega;
        T = (jdn - 2451545.0) / 36525; // Time in Julian centuries from 2000-01-01 12:00:00 GMT
        T2 = T * T;
        dr = Math.PI / 180; // degree to radian
        M = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2; // mean anomaly, degree
        L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2; // mean longitude, degree
        DL = (1.914600 - 0.004817 * T - 0.000014 * T2) * Math.sin(dr * M);
        DL = DL + (0.019993 - 0.000101 * T) * Math.sin(dr * 2 * M) + 0.000290 * Math.sin(dr * 3 * M);
        theta = L0 + DL; // true longitude, degree
        // obtain apparent longitude by correcting for nutation and aberration
        omega = 125.04 - 1934.136 * T;
        lambda = theta - 0.00569 - 0.00478 * Math.sin(omega * dr);
        // Convert to radians
        lambda = lambda * dr;
        lambda = lambda - Math.PI * 2 * (Math.floor(lambda / (Math.PI * 2))); // Normalize to (0, 2*PI)
        return lambda;
    }

    public double getLunarMonth11(int yy, int timeZone) {
        int off = jdFromDate(31, 12, yy) - 2415021;
        int k = (int) Math.floor(off / 29.530588853);
        double nm = getNewMoonDay(k, timeZone);
        double sunLong = getSunLongitude(nm, timeZone); // sun longitude at local midnight
        if (sunLong >= 9) {
            nm = getNewMoonDay(k - 1, timeZone);
        }
        return nm;
    }

    public int getLeapMonthOffset(double a11, int timeZone) {
        int k = (int) Math.floor((a11 - 2415021.076998695) / 29.530588853 + 0.5);
        double last = 0;
        int i = 1; // We start with the month following lunar month 11
        double arc = getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone);
        do {
            last = arc;
            i = i + 1;
            arc = getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone);
        } while (arc != last && i < 14);
        return i - 1;
    }

    public Date convertSolar2Lunar(int dd, int mm, int yy, int timeZone) {
        int dayNumber = jdFromDate(dd, mm, yy);
        int k = (int) Math.floor((dayNumber - 2415021.076998695) / 29.530588853);
        double monthStart = getNewMoonDay(k + 1, timeZone);
        if (monthStart > dayNumber) {
            monthStart = getNewMoonDay(k, timeZone);
        }
        double a11 = getLunarMonth11(yy, timeZone);
        double b11 = a11;
        int lunarYear = 0;
        if (a11 >= monthStart) {
            lunarYear = yy;
            a11 = getLunarMonth11(yy - 1, timeZone);
        } else {
            lunarYear = yy + 1;
            b11 = getLunarMonth11(yy + 1, timeZone);
        }
        int lunarDay = (int) (dayNumber - monthStart + 1);
        int diff = (int) Math.floor((monthStart - a11) / 29);
        int lunarLeap = 0;
        int lunarMonth = diff + 11;
        if (b11 - a11 > 365) {
            int leapMonthDiff = getLeapMonthOffset(a11, timeZone);
            if (diff >= leapMonthDiff) {
                lunarMonth = diff + 10;
                if (diff == leapMonthDiff) {
                    lunarLeap = 1;
                }
            }
        }
        if (lunarMonth > 12) {
            lunarMonth = lunarMonth - 12;
        }
        if (lunarMonth >= 11 && diff < 4) {
            lunarYear -= 1;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
             date = simpleDateFormat.parse(lunarYear + "-" + lunarMonth + "-" + lunarDay);
             ngayAm = String.valueOf(lunarDay);
             thangAm = String.valueOf(lunarMonth);
             namAm =  String.valueOf(lunarYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public void doiNam()
    {
        String can = "";
        String chi = "";
        int nam = Integer.parseInt(namAm);
        switch ((nam+6) % 10)
        {
            case 0:can = "Giáp"; break;
            case 1:can = "Ất"; break;
            case 2:can = "Bính"; break;
            case 3:can = "Đinh"; break;
            case 4:can = "Mậu"; break;
            case 5:can = "Kỷ"; break;
            case 6:can = "Canh"; break;
            case 7:can = "Tân"; break;
            case 8:can = "Nhâm"; break;
            case 9:can = "Quí"; break;
        }
        switch ((nam+8) % 12)
        {
            case 0:chi = "Tý"; break;
            case 1:chi = "Sửu"; break;
            case 2:chi = "Dần"; break;
            case 3:chi = "Mão"; break;
            case 4:chi = "Thìn"; break;
            case 5:chi = "Tỵ"; break;
            case 6:chi = "Ngọ"; break;
            case 7:chi = "Mùi"; break;
            case 8:chi = "Thân"; break;
            case 9:chi = "Dậu"; break;
            case 10:chi = "Tuất"; break;
            case 11:chi = "Hợi"; break;
        }
        namAmLich = ""+can+" "+chi;
    }

}