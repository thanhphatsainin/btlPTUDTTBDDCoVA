package com.example.namtcshop.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.example.namtcshop.R;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.namtcshop.Adapter.LapTopAdapter;
import com.example.namtcshop.Model.SanPham;
import com.example.namtcshop.Connect.CheckConnection;
import com.example.namtcshop.Connect.Server;

import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LapTopActivity extends AppCompatActivity {
    private Toolbar toolbarLaptop;
    private ListView listViewLaptop;
    private LapTopAdapter adapterLaptop;
    private ArrayList<SanPham> mangLaptop;
    private int idLaptop =0;
    private int pages =1;
    private boolean isLoadings =false;
    private boolean islimitdatas =false;
    private View footerviews;
    private mHandler msHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lap_top);
        AnhXa();
        if(CheckConnection.NetworkConnection(getApplicationContext())) {
            LayIDLoaiSanPham();
            Actiontoolbar();
            LayDuLieuSanPham(pages);
            XuLyListView();
        }else {
            CheckConnection.ShowToastThongBao(getApplicationContext(),"lỗi");
        }
    }

    private void LayIDLoaiSanPham() {
        idLaptop =getIntent().getIntExtra("idloaisanpham",-1);
        Toast.makeText(this, idLaptop +"", Toast.LENGTH_SHORT).show();
    }

    private void LayDuLieuSanPham(int page) {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        String duongDamUrl = Server.dienthoailaptop+ String.valueOf(page);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, duongDamUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null && response.length() != 2){
                    listViewLaptop.removeFooterView(footerviews); //có dữ liệu thì sẽ mất biểu tưởng load
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            mangLaptop.add(new SanPham(jsonObject.getInt("id")
                                    ,jsonObject.getString("tensp")
                                    ,jsonObject.getInt("giasp")
                                    ,jsonObject.getString("hinhanhsp")
                                    ,jsonObject.getString("motasp")
                                    ,jsonObject.getInt("idsanpham")));


                        }
                        adapterLaptop =new LapTopAdapter(getApplicationContext(), mangLaptop);
                        listViewLaptop.setAdapter(adapterLaptop);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }else{
                    islimitdatas =true;
                    listViewLaptop.removeFooterView(footerviews);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("idsanpham", String.valueOf(idLaptop));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void XuLyListView() {
        listViewLaptop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), ChiTiecSanPhamActivity.class);
                intent.putExtra("thongtinsanpham", mangLaptop.get(position));
                startActivity(intent);
            }
        });
        listViewLaptop.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem +visibleItemCount == totalItemCount && totalItemCount != 0 && isLoadings ==false && islimitdatas ==false){
                    isLoadings =true;
                    ThreadData threadData=new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    // menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        SearchView searchView= (SearchView) menu.findItem(R.id.Search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterLaptop.filter(s.trim());
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Carts:
                Intent intent=new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
            case R.id.Dollars:
                DialogTheoGiaSanPham();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void DialogTheoGiaSanPham() {
        final Dialog dialogls = new Dialog(this);
        dialogls.setContentView(R.layout.tim_kiem_theo_gia);
        RadioGroup radioGroup = (RadioGroup) dialogls.findViewById(R.id.radioGroupPhanGia);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonDuoi2:
                        adapterLaptop.TheoGiaSanPham(1);
                        dialogls.dismiss();
                        break;
                    case R.id.radioButtonTu2Den5:
                        adapterLaptop.TheoGiaSanPham(2);
                        dialogls.dismiss();
                        break;
                    case R.id.radioButtonTu5Den10:
                        adapterLaptop.TheoGiaSanPham(3);
                        dialogls.dismiss();
                        break;
                    case R.id.radioButtonTren10:
                        adapterLaptop.TheoGiaSanPham(4);
                        dialogls.dismiss();
                        break;
                    case R.id.radioButtonAll:
                        adapterLaptop.TheoGiaSanPham(5);
                        dialogls.dismiss();
                        break;
                }
            }
        });
        dialogls.show();
    }


    // xu ly hien thi moi lan 5 sp
    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    listViewLaptop.addFooterView(footerviews);
                    break;
                case 1:
                    LayDuLieuSanPham(++pages);
                    isLoadings =false;
                    break;
            }
            super.handleMessage(msg);
        }
    }
    public class ThreadData extends Thread {
        public void run(){
            msHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message= msHandler.obtainMessage(1);
            msHandler.sendMessage(message);
            super.run();
        }
    }



    public void Actiontoolbar() {
        setSupportActionBar(toolbarLaptop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLaptop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    public void AnhXa(){
        toolbarLaptop =(Toolbar)findViewById(R.id.toolbarLapTop);
        listViewLaptop =(ListView)findViewById(R.id.listviewLapTop);
        mangLaptop =new ArrayList<>();
        adapterLaptop =new LapTopAdapter(getApplicationContext(), mangLaptop);
        listViewLaptop.setAdapter(adapterLaptop);
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerviews =inflater.inflate(R.layout.progrebar,null);
        msHandler =new mHandler();

    }

}
