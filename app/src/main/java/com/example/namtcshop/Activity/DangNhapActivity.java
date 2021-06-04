package com.example.namtcshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import com.example.namtcshop.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.namtcshop.ultil.Database;
import com.example.namtcshop.Connect.Server;


import java.util.HashMap;
import java.util.Map;

public class DangNhapActivity extends AppCompatActivity {
    Button bntDangNhap, btnDangKy;
    EditText editTextTenDangNhap, editTextMatKhau;
    private TextView textViewCanhBao;
    public static int id =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        AnhXa();
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DangKyActivity.class));
            }
        });

        bntDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tendn= editTextMatKhau.getText().toString().trim();
                String mk= editTextTenDangNhap.getText().toString().trim();
                if(tendn.length()>0 && mk.length()>0){
                    KiemTraTaiKhoanDangNhap();
                }else{

                    textViewCanhBao.setText("!!! Vui Lòng Điền Đầy Đủ Thông Tin");
                }
            }
        });


    }
    public  void KiemTraTaiKhoanDangNhap(){
        final RequestQueue requestQueue=Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.kiemtrataikhoan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("error")) {
                    textViewCanhBao.setText("!!! Mật Khẩu Hoặc Tài Khoản Không Đúng");
                }else{
                    if (response == null){
                        Toast.makeText(DangNhapActivity.this, "null", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        System.out.println(response.trim());

                        int value = Integer.parseInt(response.trim());  // ma tk
//                                Integer.parseInt(response);
//                        int value= 15;
                        id =value;
                        startActivity(new Intent(DangNhapActivity.this, MainActivity.class));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DangNhapActivity.this,"Lỗi Xảy Ra", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("taikhoan", editTextTenDangNhap.getText().toString().trim());
                params.put("matkhau", editTextMatKhau.getText().toString().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
    public void AnhXa(){
        textViewCanhBao =(TextView)findViewById(R.id.txtCanhBapDangNhap);
        btnDangKy =(Button)findViewById(R.id.btnDangKyDangNhap);
        bntDangNhap =(Button)findViewById(R.id.btnDangNhap);
        editTextMatKhau =(EditText)findViewById(R.id.editTextMatKhauDangNhap);
        editTextTenDangNhap =(EditText)findViewById(R.id.editTextEmailSDTDangNhap);
    }
}
