package com.example.namtcshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import com.example.namtcshop.R;
import com.example.namtcshop.Model.GioHang;
import com.example.namtcshop.Model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ChiTiecSanPhamActivity extends AppCompatActivity {
    private TextView textViewTen, textViewGia, textViewMota;
    private ImageView imageHinh;
    private Toolbar toolbarChiTiet;
    private ImageButton btnthem;
    private Spinner spinner;
    public int idSP =0;
    public String TenChiTiecSP ="";
    public int GiaChiTietSP =0;
    public String HinhAnhChiTiecSP ="";
    public String MoTaChiTiecSP ="";
    public int IdSanPham =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiec_san_pham);
        AnhXa();
        ActiontoolbarChiTietSanPham();
        LayDuLieu();
        BatSuKienSpiner();
        ClickButtonGioHang();

    }



    // khoi tạo dữ liệu spiner
    private void BatSuKienSpiner() {
        Integer[] soluong=new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> arrayAdapter=new ArrayAdapter<Integer>(this,R.layout.support_simple_spinner_dropdown_item,soluong);
        spinner.setAdapter(arrayAdapter);
    }

    // lay du lieu san pham tu mainactivity push sang
    private void LayDuLieu() {
        SanPham sanpham= (SanPham) getIntent().getSerializableExtra("thongtinsanpham");
        idSP =sanpham.getID();
        TenChiTiecSP =sanpham.getTensanpham();
        GiaChiTietSP =sanpham.getGiasanpham();
        HinhAnhChiTiecSP =sanpham.getHinhanhsanpham();
        MoTaChiTiecSP =sanpham.getMotasanpham();
        IdSanPham =sanpham.getIDsanpham();
        int gia=sanpham.getGiasanpham();
        textViewTen.setText(sanpham.getTensanpham());
        textViewMota.setText(sanpham.getMotasanpham());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        textViewGia.setText("Gía "+decimalFormat.format(gia)+" đ");
        Picasso.with(getApplicationContext()).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.imglcocal)
                .error(R.drawable.errors)
                .into(imageHinh);
    }

    // click button them vao gio hang
    private void ClickButtonGioHang() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mangGioHang.size()>0){  // neu mang da co san pham
                    int sl= Integer.parseInt(spinner.getSelectedItem().toString());
                    boolean exits=false;   // check xem san pham do co trong gio hang chua
                    for(int i = 0; i<MainActivity.mangGioHang.size(); i++){
                        if(MainActivity.mangGioHang.get(i).getIdsp() == idSP){      // neu san pham da co trong gio hang
                            MainActivity.mangGioHang.get(i).setSoluongsp(MainActivity.mangGioHang.get(i).getSoluongsp()+sl);  // cap nhat lai so luong
                            if(MainActivity.mangGioHang.get(i).getSoluongsp()>10){
                                MainActivity.mangGioHang.get(i).setSoluongsp(10);
                            }
                            MainActivity.mangGioHang.get(i).setGiasp(GiaChiTietSP *MainActivity.mangGioHang.get(i).getSoluongsp()); // cap nhat lai tong tien cua san pham do, lay so luong san pham * gia san pham
                            exits=true;
                        }
                    }
                    if(exits==false){   // neu san pham k co trong gio hang
                        int soluong= Integer.parseInt(spinner.getSelectedItem().toString());
                        long giamoi1=soluong* GiaChiTietSP;
                        MainActivity.mangGioHang.add(new GioHang(idSP, TenChiTiecSP,giamoi1, HinhAnhChiTiecSP,soluong));
                    }
                }else{  // neu mang chua co san pham nao
                    int soluongmoi= Integer.parseInt(spinner.getSelectedItem().toString());
                    long giacamoi=soluongmoi* GiaChiTietSP;
                    MainActivity.mangGioHang.add(new GioHang(idSP, TenChiTiecSP,giacamoi, HinhAnhChiTiecSP,soluongmoi));
                }
                Intent intent=new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
            }

        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trangchu,menu);
        return true;
    }

    // click vào menu giỏ hàng
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Carts:
                Intent intent=new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void ActiontoolbarChiTietSanPham() {
        setSupportActionBar(toolbarChiTiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarChiTiet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            } // quay tro lai trang truoc
        });
    }

    private void AnhXa() {
        spinner=(Spinner)findViewById(R.id.spinnerSoLuong);
        toolbarChiTiet=findViewById(R.id.toolbarChiTietSanPham);
        textViewGia =(TextView)findViewById(R.id.txtGiaChiTietSanPham);
        textViewMota =(TextView)findViewById(R.id.txtMoTaChiTietSanPham);
        textViewTen =(TextView)findViewById(R.id.txtTenChiTietSanPham);
        imageHinh =(ImageView)findViewById(R.id.imgAnhChiTietSanPham);
        btnthem =(ImageButton) findViewById(R.id.btnGioHangChiTietSanPham);
    }
}
