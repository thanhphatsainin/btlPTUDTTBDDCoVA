package com.example.namtcshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import com.example.namtcshop.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.namtcshop.Connect.Server;


import java.util.HashMap;
import java.util.Map;

public class DangKyActivity extends AppCompatActivity {
    private Toolbar toolbarDangKy;
    private EditText editTextHo, editTextTen, editTextEmail, editTextMatkhau, editTextSDT, editTextDiaChi;
    private RadioButton radioButtonnam, radioButtonnu;
    private Button btndangky;
    private TextView textViewBaoLoi;
    private String gioitinh ="Nam";
    private String formgmail ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        toolbarDangKy=(Toolbar)findViewById(R.id.toolbarDangKy);
        ActionToolbarDangKy();
        AnhXa();
        if(radioButtonnu.isChecked()){
            gioitinh ="Nữ";
        }
        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextHo.length()==0 || editTextTen.length()==0 || editTextEmail.length()==0 || editTextMatkhau.length()==0|| editTextSDT.length()==0 || editTextDiaChi.length()==0 ){
                    textViewBaoLoi.setText("Vui lòng điền đầy đủ thông tin!");

                }
                else if(editTextMatkhau.length()<=6 || editTextEmail.length()<=6){
                    String t = editTextEmail.getText().toString();
                    formgmail = t.substring(t.length() - 10);
                    textViewBaoLoi.setText("Email hoặc mặt khẩu phải từ 6 ký tự!");

                }else if(editTextSDT.length()<=9){
                    textViewBaoLoi.setText("Số Điên Thoại Không Hợp Lệ!");

                }
                else if(formgmail.equals("@gmail.com")) {
                    textViewBaoLoi.setText("Email Không hợp lệ!");
                }else {
                    ThemTaiKhoanMoi();
                }

            }
        });
    }
    public void ThemTaiKhoanMoi(){
        final RequestQueue requestQueue=Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.dangkitaikhoan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")) {
                    Toast.makeText(DangKyActivity.this, "Đã Thêm", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
                }else if(response.trim().equals("Tài Khoản Đã Tồn Tại!")){
                    Toast.makeText(DangKyActivity.this,response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DangKyActivity.this,"Lỗi Xảy Ra!", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("ho", editTextHo.getText().toString().trim());
                params.put("ten", editTextTen.getText().toString().trim());
                params.put("email", editTextEmail.getText().toString().trim());
                params.put("matkhau", editTextMatkhau.getText().toString().trim());
                params.put("sdt", editTextSDT.getText().toString().trim());
                params.put("gioitinh", gioitinh);
                params.put("diachi", editTextDiaChi.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void ActionToolbarDangKy() {
        setSupportActionBar(toolbarDangKy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDangKy.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void AnhXa(){
        editTextSDT =(EditText)findViewById(R.id.editTextSDTDangKy);
        editTextDiaChi =(EditText)findViewById(R.id.editTextDiaChiDangKy);
        textViewBaoLoi =(TextView)findViewById(R.id.txtCanhBao);
        editTextHo =(EditText)findViewById(R.id.editTextHoDangKy);
        editTextTen =(EditText)findViewById(R.id.editTextTenDangKy);
        editTextEmail =(EditText)findViewById(R.id.editTextEmailDangKy);
        editTextMatkhau =(EditText)findViewById(R.id.editTextMatKhauDangKy);
        btndangky =(Button)findViewById(R.id.btnDangKy);
        radioButtonnam =(RadioButton)findViewById(R.id.radioButtonNam);
        radioButtonnu =(RadioButton)findViewById(R.id.radioButtonNu);

    }

}
