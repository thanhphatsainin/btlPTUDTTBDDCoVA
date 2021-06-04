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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.namtcshop.Adapter.DienThoaiAdapter;
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

public class DienThoaiActivity extends AppCompatActivity {
    private Toolbar toolbarDienThoai;
    private ListView listViewDienThoai;
    private DienThoaiAdapter DienThoaiAdapter;
    private ArrayList<SanPham> mangDienThoai;
    private int idDienThoai =0;
    private int pages =1;
    private boolean isLoadings =false;
    private boolean islimitdatas =false;
    private View footerviews;
    private mHandler msHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        AnhXa();
        if(CheckConnection.NetworkConnection(getApplicationContext())) {
            GetIdloaisp();
            Actiontoolbar();
            GetDataSanPham(pages);
            LoadmoreData();
        }else {
            CheckConnection.ShowToastThongBao(getApplicationContext(),"lỗi");
        }
    }

    private void GetIdloaisp() {
        idDienThoai =getIntent().getIntExtra("idloaisanpham",-1);
//        Toast.makeText(this, idDienThoai +"", Toast.LENGTH_SHORT).show();
    }


    // lay danh sach san pham
    private void GetDataSanPham(int page) {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        String duongdan= Server.dienthoailaptop+ String.valueOf(page);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null && response.length() != 2){           // response : [key : value]
                    listViewDienThoai.removeFooterView(footerviews); //có dữ liệu thì sẽ mất biểu tưởng load
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            mangDienThoai.add(new SanPham(jsonObject.getInt("id")
                                    ,jsonObject.getString("tensp")
                                    ,jsonObject.getInt("giasp")
                                    ,jsonObject.getString("hinhanhsp")
                                    ,jsonObject.getString("motasp")
                                    ,jsonObject.getInt("idsanpham")));
                            // DienThoaiAdapter.notifyDataSetChanged();

                        }
                        DienThoaiAdapter =new DienThoaiAdapter(getApplicationContext(), mangDienThoai);
                        listViewDienThoai.setAdapter(DienThoaiAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }else{
                    islimitdatas =true;   // neu du lieu do ra het thi xoa cai progress
                    listViewDienThoai.removeFooterView(footerviews);
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
                param.put("idsanpham", String.valueOf(idDienThoai));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    // click vao san pham chuyen sang giao dien chi tiet san pham va load them du lieu
    private void LoadmoreData() {
        listViewDienThoai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), ChiTiecSanPhamActivity.class);
                intent.putExtra("thongtinsanpham", mangDienThoai.get(position));
                startActivity(intent);
            }
        });
        listViewDienThoai.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override       // firstVisibleItem : dong dau tien trong listview, visibleItemCount: cac gia tri item co the nhin thay, totalItemCount : tong so luong item trong listview
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem +visibleItemCount == totalItemCount && totalItemCount != 0 && isLoadings ==false && islimitdatas ==false){ // neu dang loading thi k dc keo tiep
                    isLoadings =true;                                // firstVisibleItem +visibleItemCount == totalItemCount : da o vi tri cuoi cung
                    ThreadData threadData=new ThreadData();
                    threadData.start();
                }
            }
        });
    }


    // seach theo ten dien thoai
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
                DienThoaiAdapter.filter(s.trim());
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
                break;
            case R.id.Dollars:
                DialogTheoGiaSanPham();
                   break;
        }
        return super.onOptionsItemSelected(item);
    }
    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    listViewDienThoai.addFooterView(footerviews); // hien thi dang load
                    break;
                case 1:
                    GetDataSanPham(++pages);
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
            Message message= msHandler.obtainMessage(1);  // obtainMessage : lien ket lien tuc giua Handler vs Thread
            msHandler.sendMessage(message);
            super.run();
        }
    }
    private void DialogTheoGiaSanPham(){
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.tim_kiem_theo_gia);
        RadioGroup group=(RadioGroup)dialog.findViewById(R.id.radioGroupPhanGia);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonDuoi2:
                        DienThoaiAdapter.TheoGiaSanPham(1);
                        dialog.dismiss();
                        break;
                    case R.id.radioButtonTu2Den5:
                        DienThoaiAdapter.TheoGiaSanPham(2);
                        dialog.dismiss();
                        break;
                    case R.id.radioButtonTu5Den10:
                        DienThoaiAdapter.TheoGiaSanPham(3);
                        dialog.dismiss();
                        break;
                    case R.id.radioButtonTren10:
                        DienThoaiAdapter.TheoGiaSanPham(4);
                        dialog.dismiss();
                        break;
                    case R.id.radioButtonAll:
                        DienThoaiAdapter.TheoGiaSanPham(5);
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog.show();

    }

    public void Actiontoolbar() {
        setSupportActionBar(toolbarDienThoai);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDienThoai.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void AnhXa(){
        toolbarDienThoai =(Toolbar)findViewById(R.id.toolbarDienTHoai);
        listViewDienThoai =(ListView)findViewById(R.id.listviewDienTHoai);

        mangDienThoai =new ArrayList<>();
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerviews =inflater.inflate(R.layout.progrebar,null);
        msHandler =new mHandler();

    }

}
