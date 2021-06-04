package com.example.namtcshop.Activity;

import android.os.Bundle;
import com.example.namtcshop.R;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.namtcshop.Adapter.SanPhamDaDatAdapter;
import com.example.namtcshop.Model.SanPhamDaDat;
import com.example.namtcshop.Connect.CheckConnection;
import com.example.namtcshop.Connect.Server;

import androidx.appcompat.widget.Toolbar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SanPhamDaDatActivity extends AppCompatActivity {
    private Toolbar toolbarSanPhamDaDat;
    private ListView listViewSanPham;
    private Integer TongTien=0;
    private TextView textViewTongTien,textViewHangTrong;
    private SanPhamDaDatAdapter sanPhamDaDatAdapter;
    private ArrayList<SanPhamDaDat> mangdadat = new ArrayList<SanPhamDaDat>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham_da_dat);
        AnhXa();
        textViewHangTrong.setVisibility(View.INVISIBLE);
        if(CheckConnection.NetworkConnection(getApplicationContext())) {
            ActiontoolbarSanPhamDaDat();
            LayDuLieuSanPhamDaDat();

        }else{
            CheckConnection.ShowToastThongBao(getApplicationContext(),"Lỗi kết nói");

        }

    }

    public void AnhXa(){
        mangdadat=new ArrayList<>();
        sanPhamDaDatAdapter=new SanPhamDaDatAdapter(getApplicationContext(),mangdadat);
        listViewSanPham=(ListView)findViewById(R.id.listviewsSanPhamDaDat);
        listViewSanPham.setAdapter(sanPhamDaDatAdapter);
        toolbarSanPhamDaDat =(Toolbar)findViewById(R.id.toolbarSanPhamDaDat);
        textViewTongTien=(TextView)findViewById(R.id.txtTongTienSanPhamDaDat);
        textViewHangTrong=(TextView)findViewById(R.id.SanPhamDaDatTrongRong);
    }

    public void ActiontoolbarSanPhamDaDat() {
        setSupportActionBar(toolbarSanPhamDaDat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarSanPhamDaDat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //get data da dat
    private void LayDuLieuSanPhamDaDat() {
        TongTien=0;
       final DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
       RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
       StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.laysanphamdadat, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               if(response!=null && response.length() != 2){
                   try {
                       JSONArray jsonArray=new JSONArray(response);
                       for(int i=0;i<jsonArray.length();i++){
                           JSONObject object=jsonArray.getJSONObject(i);
                           mangdadat.add(new SanPhamDaDat(object.getString("tensp"),
                                   object.getString("hinhanhsp"),
                                   object.getInt("giasp"),
                                   object.getInt("soluongsanpham"),
                                   object.getInt("tientungsanpham")));
                           TongTien+= object.getInt("tientungsanpham");
                           sanPhamDaDatAdapter.notifyDataSetChanged();
                       }
                       textViewTongTien.setText(decimalFormat.format(TongTien)+" đ");
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }else{
                   textViewHangTrong.setVisibility(View.VISIBLE);
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               HashMap<String, String> map=new HashMap<>();
               map.put("idkhachhang", String.valueOf(DangNhapActivity.id));
               return map;
           }
       };
        requestQueue.add(stringRequest);

    }



}
