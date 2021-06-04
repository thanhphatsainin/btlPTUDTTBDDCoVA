package com.example.namtcshop.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.example.namtcshop.R;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.namtcshop.Adapter.GioHangAdapter;
import com.example.namtcshop.Connect.CheckConnection;
import com.example.namtcshop.Connect.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class GioHangActivity extends AppCompatActivity {
    private ListView listViewGioHang;
    private TextView textViewThongBao;
    public static TextView textViewTongTien;
    public  static EditText editTextDiaChi;
    private static long tongtienGioHang =0;  //tong tien cua ca gio hang
    private Button buttonThanhToan, buttonTiepTucMua;
    private Toolbar toolbarGioHang;
    private GioHangAdapter adapterGioHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        AnhXa();
        Actiontoolbar();
        CheckDataTrongGioHang();             // check du lieu trong gio hang
        TinhTongTien();             // lay tong tien trong gio hang
        BatSuKienClickItemListView();   // bat su kien an giu dong trong list gio hang để xóa
        ClickButtonMua();           // click nút tiếp tục mua
        buttonThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDiaChiKhachHang();
            }
        });
    }

    // check du lieu trong gio hang
    private void CheckDataTrongGioHang() {
        if(MainActivity.mangGioHang.size() <= 0){
            adapterGioHang.notifyDataSetChanged();
            textViewThongBao.setVisibility(View.VISIBLE);     // hien thi thong bao gio hang trong va k hien thi listview
            listViewGioHang.setVisibility(View.INVISIBLE);
        }else{
            adapterGioHang.notifyDataSetChanged();
            textViewThongBao.setVisibility(View.INVISIBLE);  // nguoc lai
            listViewGioHang.setVisibility(View.VISIBLE);
        }
    }

    // lay tong tien trong gio hang
    public static void TinhTongTien() {
        tongtienGioHang =0;
        for(int i = 0; i<MainActivity.mangGioHang.size(); i++){
            tongtienGioHang +=MainActivity.mangGioHang.get(i).getGiasp();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###"); // định dạng số
        textViewTongTien.setText("Gía : "+decimalFormat.format(tongtienGioHang)+" đ");
    }


    // bat su kien an giu dong trong list gio hang để xóa
    private void BatSuKienClickItemListView() {
        listViewGioHang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builders =new AlertDialog.Builder(GioHangActivity.this);
                builders.setTitle("Xác Nhận xóa sản phẩm");
                builders.setMessage("Bạn có chắc xóa sản phẩm này ");
                builders.setPositiveButton("có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(MainActivity.mangGioHang.size()<=0){
                            textViewThongBao.setVisibility(View.VISIBLE);
                        }else{
                            MainActivity.mangGioHang.remove(position);
                            adapterGioHang.notifyDataSetChanged();
                            TinhTongTien();            // cap nha lai tong tien trong gio hang
                            if(MainActivity.mangGioHang.size()<=0){     // neu xoa xong ma gio hang trong
                                textViewThongBao.setVisibility(View.VISIBLE);
                            }else{
                                textViewThongBao.setVisibility(View.INVISIBLE);
                                adapterGioHang.notifyDataSetChanged();
                                TinhTongTien();
                            }

                        }
                    }
                });
                builders.setNegativeButton("Không ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapterGioHang.notifyDataSetChanged();
                        TinhTongTien();
                    }
                });
                builders.show();
                return true;
            }
        });
    }

    // click nút tiếp tục mua
    private void ClickButtonMua() {
        buttonTiepTucMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }

    // goi DialogDiaChi
    private void DialogDiaChiKhachHang(){
        final Dialog dialogls =new Dialog(this);
        dialogls.setContentView(R.layout.dialog_diachi);
        editTextDiaChi =(EditText) dialogls.findViewById(R.id.editTextDialogDiaChi);
        Button buttonXacNhan =(Button) dialogls.findViewById(R.id.btnXacNhanDialogDiaChi);
        Button buttonThoat=(Button) dialogls.findViewById(R.id.btnThoatDialogDiaChi);
        LayDiaChiKhachHang();        // lay dia chi cua khach hang ròi xét vào editTextDiaChi
        buttonXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CapNhatDiaChiKhachHang();    // cap nhật lại địa chỉ khi nguoi dung edit lai dia chi
                ThanhToanDonHang();
            }
        });
        buttonThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogls.dismiss();
            }
        });
        dialogls.show();

    }

    // lay dia chi cua khach hang ròi xét vào editTextDiaChi
    private void LayDiaChiKhachHang(){
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.laydiachi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                editTextDiaChi.setText(response.trim());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                return param;
            }
        };
        requestQueue.add(stringRequest);

    }

    // cap nhật lại địa chỉ
    private void CapNhatDiaChiKhachHang(){
        final RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.capnhapdiachi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                if (response.trim().equals("success")){
//                    Toast.makeText(GioHang.this, "success", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(GioHang.this, "error", Toast.LENGTH_SHORT).show();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param=new HashMap<String, String>();
                param.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                param.put("diachi", editTextDiaChi.getText().toString().trim());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void ThanhToanDonHang(){
        AlertDialog.Builder builders =new AlertDialog.Builder(GioHangActivity.this);
        builders.setTitle("Xác Nhận Thanh Toán");
        builders.setMessage("Bạn Có chắc Muốn Đặt Giỏ Hàng Này !");
        builders.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    final RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext()); // thêm đơn hàng vào csdl -> mã đơn hàng
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.donhang, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String madonhang) {
                            Log.d("mahoadon",madonhang.trim()); // log
                            if(Integer.parseInt(madonhang.trim())>0){ // neu them thanh cong
                                RequestQueue queue=Volley.newRequestQueue(getApplicationContext()); // thêm chi tiết đơn hàng -> success/ error
                                StringRequest  stringRequest1=new StringRequest(Request.Method.POST, Server.chitiecdonhang, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("machitiec",response);
//                                        Toast.makeText(GioHang.this,"respon : "+ response, Toast.LENGTH_SHORT).show();//
                                        System.out.println("respon : "+ response);
//                                        response = "success"; ///sua code
                                        if(response.trim().equals("success")){
                                            MainActivity.mangGioHang.clear();
                                            CheckConnection.ShowToastThongBao(getApplicationContext(),"Bạn đã thêm giỏ hàng thành công");
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            CheckConnection.ShowToastThongBao(getApplicationContext(),"Mời bạn tiếp tục mua sản phẩm"); // da nhay vao day
                                        }else{
                                            CheckConnection.ShowToastThongBao(getApplicationContext(),"dữ liệu của bạn đã bị lỗi");
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError { // gui gio hang len server
                                        JSONArray jsonArray=new JSONArray();

                                        for(int i = 0; i<MainActivity.mangGioHang.size(); i++){  // them tat ca vao json roi gui 1 lan de tranh mat mat giu lieu
                                            JSONObject object=new JSONObject();
                                            try {
                                                object.put("madonhang",madonhang.trim());
                                                object.put("masanpham",MainActivity.mangGioHang.get(i).getIdsp());
                                                object.put("soluongsanpham",MainActivity.mangGioHang.get(i).getSoluongsp());
                                                object.put("tientungsanpham",MainActivity.mangGioHang.get(i).getGiasp()); // dong 60 cHI TIET SAN PHAM
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            jsonArray.put(object);

                                        }
                                        HashMap<String, String> hashMap=new HashMap<String, String>();
                                        hashMap.put("json",jsonArray.toString()); // push json
                                        return hashMap;
                                    }
                                };
                                queue.add(stringRequest1);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap=new HashMap<String, String>();
                            hashMap.put("idkhachhang", String.valueOf(DangNhapActivity.id));
                            hashMap.put("tongtien", String.valueOf(tongtienGioHang));
                            return hashMap;   // them moi don hang push idkhachhang, tong tien con tg thi tu sinh ra trong file php
                        }
                    };
                    requestQueue.add(stringRequest);


            }
        });
        builders.setNegativeButton("Không ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              startActivity(new Intent(getApplicationContext(), GioHangActivity.class));
            }
        });
        builders.show();

    }



    private void Actiontoolbar() {
        setSupportActionBar(toolbarGioHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarGioHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void AnhXa() {
        listViewGioHang =(ListView)findViewById(R.id.listviewGioHang);
        textViewTongTien =(TextView)findViewById(R.id.txtGiaTriGioHang);
        textViewThongBao =(TextView)findViewById(R.id.txtThongBaoGioHang);
        buttonThanhToan =(Button)findViewById(R.id.buttonthanhtoanngiohang);
        buttonTiepTucMua =(Button)findViewById(R.id.buttontieptucmuahang);
        toolbarGioHang =findViewById(R.id.toolbarGioHang);
        adapterGioHang =new GioHangAdapter(GioHangActivity.this,MainActivity.mangGioHang);
        listViewGioHang.setAdapter(adapterGioHang);

    }
}
