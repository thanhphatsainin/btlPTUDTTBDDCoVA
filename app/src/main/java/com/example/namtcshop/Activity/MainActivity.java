package com.example.namtcshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

import com.example.namtcshop.R;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.namtcshop.Adapter.LoaiSanPhamAdapter;
import com.example.namtcshop.Adapter.GirdViewSanPham;
import com.example.namtcshop.Model.GioHang;
import com.example.namtcshop.Model.LoaiSanPham;
import com.example.namtcshop.Model.SanPham;
import com.example.namtcshop.Connect.CheckConnection;
import com.example.namtcshop.Connect.Server;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbarMain; // cai toolbarMain tren dau
    private ViewFlipper viewFlipperQuangCao;  // hien thi quang cao
    private GridView gridViewSanPhamMoiNhat; // gripview san pham ms nhat
    private NavigationView navigationViewLeft;  // chua anh logo va menu 6 dong
    private ListView listViewMenu;  // chua menu 6 dong
    private DrawerLayout drawerLayouts;
    private ArrayList<LoaiSanPham> mangLoaiSanPham;  //mang chua doi tuong cho menu
    private LoaiSanPhamAdapter adapterLoaiSanPham;   // adapter mangLoaiSanPham
    private int id=0;                 // id, tenloaisp, hinhanhloaisp cua loaisp
    private String tenLoaiSanPham="";
    private String hinhLoaiSanPham="";
    private ArrayList<SanPham> mangSanPham;  //  mang san pham moi nhat
    private GirdViewSanPham girdViewAdapterSanPhamMoiNhat;  // adapter gridViewSanPhamMoiNhat mangsanpham
    public static ArrayList<GioHang> mangGioHang;   //mang chua cac san pham khi them vao gio hang

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        if(CheckConnection.NetworkConnection(getApplicationContext())){
            ActionBarMain();    // xet icon cho toolbarMain 3 suoc
            ViewFlippersQuangCao(); // xét dữ liệu cho cái ViewFlipper quảng cáo
            LayDuLieuLoaiSanPham(); // set dữ liệu cho cái menu
            LayMangSanPhamMoiNhat();  // lay ra mang san pham moi nhat
            ClickVaoTungDongMenu(); // xet su kien click vao cac dong trong menu
            ChuyenSangGiaoDienChiTietSanPham(); // khi click vao 1 san pham se chuyen sang giao dien chi tiet san pham
        }else{
            CheckConnection.ShowToastThongBao(getApplicationContext(),"Bạn Hãy Kiểm Tra Lại Kết Nối Internet or Wifi!");
            finish();
        }

    }

    // xét dữ liệu cho cái ViewFlipper quảng cáo
    private void ViewFlippersQuangCao() {
        ArrayList<String> mangQuangCao=new ArrayList<>();
        mangQuangCao.add("https://cellphones.com.vn/sforum/wp-content/uploads/2019/09/vivo-v17-pro-1.jpg");
        mangQuangCao.add("https://thaycamungcantho.com.vn/wp-content/uploads/2020/11/iphone-12-chinh-thuc-mo-ban-iphone-12-pro-max-len-ngoi-thumbnail.jpg");
        mangQuangCao.add("https://salt.tikicdn.com/ts/categoryblock/55/be/7d/1ac99e78bc71f8ffc61fe8b1649e3997.jpg");
        mangQuangCao.add("https://photo-cms-sggp.zadn.vn/Uploaded/2021/yfsgf/2020_09_24/hinh11_mzad.jpg");
        mangQuangCao.add("https://cellphones.com.vn/sforum/wp-content/uploads/2019/05/Honor-20-Pro-lo-anh-quang-cao-1.jpg");
        mangQuangCao.add("http://cafefcdn.com/2017/photo-1-1488674784324.png");
        mangQuangCao.add("https://channel.mediacdn.vn/2019/1/10/photo-1-15470930389662024470690.jpg");
        mangQuangCao.add("https://genk.mediacdn.vn/2019/4/23/photo-1-15560365277871628784452.jpg");
        mangQuangCao.add("https://www.vlance.vn/uploads/portfolio/view/6828d289507c1d9cab1a9a8b6869801d0d18d4be1.jpg");
        mangQuangCao.add("https://cdn.tgdd.vn/Files/2021/01/05/1318051/xiaomi-mi-11-ra-mat-voi-loat-tinh-nang-cao-cap-voi-gia-tu-14-trieu-1.jpg");
        for(int i=0;i<mangQuangCao.size();i++){
            ImageView imageView=new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipperQuangCao.addView(imageView);
            viewFlipperQuangCao.setAutoStart(true);
            Animation animation_slide_in=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in);
            Animation animation_slide_out=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out);
            viewFlipperQuangCao.setInAnimation(animation_slide_in);
            viewFlipperQuangCao.setOutAnimation(animation_slide_out);
        }
    }

    // set dữ liệu cho cái menu
    private void LayDuLieuLoaiSanPham() {
        final RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Server.layloaisanpham, new Response.Listener<JSONArray>() { // lấy ra 2 trong menu cái điện thoại và laptop

            @Override
            public void onResponse(JSONArray response) {
                if(response !=null){
                    for(int i= 0 ;i<response.length();i++){
                        try {
                            JSONObject jsonObject=response.getJSONObject(i);
                            id=jsonObject.getInt("id");
                            tenLoaiSanPham=jsonObject.getString("tenLoaisp");
                            hinhLoaiSanPham=jsonObject.getString("hinhanhloaisp");
                            mangLoaiSanPham.add(new LoaiSanPham(id,tenLoaiSanPham,hinhLoaiSanPham));
                            adapterLoaiSanPham.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    mangLoaiSanPham.add(3,new LoaiSanPham(0,"Liên Hệ","https://images.techhive.com/images/article/2016/06/ios-mail-icon-100669537-large.jpg"));
                    mangLoaiSanPham.add(4,new LoaiSanPham(0,"Thông Tin","https://st2.depositphotos.com/3369547/11386/v/950/depositphotos_113864336-stock-illustration-avatar-man-icon-people-design.jpg"));
                    mangLoaiSanPham.add(5,new LoaiSanPham(0,"Thanh Toán","https://previews.123rf.com/images/yupiramos/yupiramos1709/yupiramos170930979/87003099-manos-humanas-que-pagan-dinero-aislado-icono-vector-ilustraci%C3%B3n-dise%C3%B1o.jpg"));
//                    mangLoaiSanPham.add(6,new Loaisp(0,"Thông Tin khách hàng","https://gemdigital.vn/wp-content/uploads/2019/10/moi-quan-he-giua-customer-va-consumer.jpg"));
                    adapterLoaiSanPham.notifyDataSetChanged();

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.ShowToastThongBao(getApplicationContext(),error.toString());
                finish();
            }
        });
        requestQueue.add(jsonArrayRequest);

    }

    // lay ra mang san pham moi nhat
    private void LayMangSanPhamMoiNhat() {
        final RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Server.laysanphammoinhat, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                if(response !=null){
                    int ID=0;
                    String Tensanpham="";
                    Integer Giasanpham=0;
                    String Hinhanhsanpham="";
                    String Motasanpham="";
                    int IDsanpham=0;
                    for(int i = 0;i<response.length();i++){
                        try {
                            JSONObject jsonObject=response.getJSONObject(i);
                            ID=jsonObject.getInt("id");
                            Tensanpham=jsonObject.getString("tensp");
                            Giasanpham=jsonObject.getInt("giasp");
                            Hinhanhsanpham=jsonObject.getString("hinhanhsp");
                            Motasanpham=jsonObject.getString("motasp");
                            IDsanpham=jsonObject.getInt("idsanpham");
                            mangSanPham.add(new SanPham(ID,Tensanpham,Giasanpham,Hinhanhsanpham,Motasanpham,IDsanpham));
                            girdViewAdapterSanPhamMoiNhat.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.ShowToastThongBao(getApplicationContext(),error.toString());
                finish();
            }
        });
        requestQueue.add(jsonArrayRequest);

    }

    // menu trang chu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trangchu,menu);
        return true;
    }

    // bat su kien khi click vào menu gio hang
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Carts:
                Intent intent=new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // xet su kien click vao cac dong trong menu
    private void ClickVaoTungDongMenu() {
        listViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if(CheckConnection.NetworkConnection(getApplicationContext())){
                            Intent intent=new Intent(MainActivity.this,MainActivity.class);
                        }else{
                            CheckConnection.ShowToastThongBao(getApplicationContext(),"Lỗi");
                        }
                        drawerLayouts.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        if(CheckConnection.NetworkConnection(getApplicationContext())){
                            Intent intent=new Intent(MainActivity.this,DienThoaiActivity.class);
                            intent.putExtra("idloaisanpham", mangLoaiSanPham.get(position).getId()); // id = 1;
                            startActivity(intent);
                        }else{
                            CheckConnection.ShowToastThongBao(getApplicationContext(),"Lỗi");
                        }
                        drawerLayouts.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        if(CheckConnection.NetworkConnection(getApplicationContext())){
                            Intent intent=new Intent(MainActivity.this,LapTopActivity.class);
                            intent.putExtra("idloaisanpham", mangLoaiSanPham.get(position).getId()); // id = 2;
                            startActivity(intent);
                        }else{
                            CheckConnection.ShowToastThongBao(getApplicationContext(),"Lỗi");
                        }
                        drawerLayouts.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        if(CheckConnection.NetworkConnection(getApplicationContext())){
                            Intent intent=new Intent(MainActivity.this,LienHeActivity.class);
                            startActivity(intent);
                        }else{
                            CheckConnection.ShowToastThongBao(getApplicationContext(),"Lỗi");
                        }
                        drawerLayouts.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        if(CheckConnection.NetworkConnection(getApplicationContext())){
                            Intent intent=new Intent(MainActivity.this,ThongTinActivity.class);
                            startActivity(intent);
                        }else{
                            CheckConnection.ShowToastThongBao(getApplicationContext(),"Lỗi");
                        }
                        drawerLayouts.closeDrawer(GravityCompat.START);
                        break;
                    case 5:
                        if(CheckConnection.NetworkConnection(getApplicationContext())){
                            Intent intent=new Intent(MainActivity.this, SanPhamDaDatActivity.class);
                            startActivity(intent);
                        }else{
                            CheckConnection.ShowToastThongBao(getApplicationContext(),"Lỗi");
                        }
                        drawerLayouts.closeDrawer(GravityCompat.START);
                        break;

//                    case 6:
//                        if(CheckConnection.NetworkConnection(getApplicationContext())){
//                            Intent intent=new Intent(MainActivity.this,ThongTinKhachHang.class);
//                            startActivity(intent);
//                        }else{
//                            CheckConnection.ShowToastThongBao(getApplicationContext(),"Lỗi");
//                        }
//                        drawerLayouts.closeDrawer(GravityCompat.START);
//                        break;

                }
            }
        });
    }

    // khi click vao 1 san pham se chuyen sang giao dien chi tiet san pham
    private void ChuyenSangGiaoDienChiTietSanPham() {
        gridViewSanPhamMoiNhat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), ChiTiecSanPhamActivity.class);
                intent.putExtra("thongtinsanpham", mangSanPham.get(position));   // put doi tuong san pham sang
                startActivity(intent);
            }
        });
    }

    // xet icon cho toolbarMain 3 suoc menu
    private void ActionBarMain() {
        setSupportActionBar(toolbarMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarMain.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayouts.openDrawer(GravityCompat.START);   // mo menu
            }
        });
    }


    private void Anhxa(){
        toolbarMain = (Toolbar) findViewById(R.id.toobarmanhinhchinh);
        viewFlipperQuangCao =(ViewFlipper)findViewById(R.id.viewlipQuangCao);
        navigationViewLeft =(NavigationView)findViewById(R.id.navigationview);
        listViewMenu =(ListView)findViewById(R.id.listviewTrangChu);
        drawerLayouts =(DrawerLayout)findViewById(R.id.DrawbleTrangChu);
        mangLoaiSanPham =new ArrayList<>();
        // set dong trang chu tren menu
        mangLoaiSanPham.add(0,new LoaiSanPham(0,"Trang Chính","https://www.clipartmax.com/png/middle/72-724499_42487-house-with-garden-icon-house-emoji-transparent.png"));
        adapterLoaiSanPham =new LoaiSanPhamAdapter(mangLoaiSanPham,getApplicationContext());
        listViewMenu.setAdapter(adapterLoaiSanPham);
        mangSanPham =new ArrayList<>();
        girdViewAdapterSanPhamMoiNhat=new GirdViewSanPham(getApplicationContext(), mangSanPham);
        gridViewSanPhamMoiNhat =(GridView)findViewById(R.id.girdviewSanPham);
        gridViewSanPhamMoiNhat.setAdapter(girdViewAdapterSanPhamMoiNhat);

        if(mangGioHang !=null){ // neu mang da co san pham thi k can khoi tao
//            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        }else{ // neu chua co j thi khoi tao moi
            mangGioHang =new ArrayList<>();
            //Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        }
    }
}
