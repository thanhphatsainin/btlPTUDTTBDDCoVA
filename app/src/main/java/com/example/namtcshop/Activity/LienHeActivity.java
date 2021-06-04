package com.example.namtcshop.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.example.namtcshop.R;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.namtcshop.Connect.CheckConnection;
import com.example.namtcshop.Connect.Server;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.Map;

public class LienHeActivity extends AppCompatActivity {
    private Toolbar toolbarLienHe;
    private Button buttonCall, buttonEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lien_he);
        toolbarLienHe =(Toolbar)findViewById(R.id.toolbarlienhe);
        Actiontoolbar();
        buttonCall =(Button) findViewById(R.id.btnCall);
        buttonEmail =(Button)findViewById(R.id.btnEmail);
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGrantedMobile()){
                    CallAdmin();
                }
            }
        });
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendEmailAdmin();
            }
        });
    }

    public  boolean isPermissionGrantedMobile() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission is granted!", Toast.LENGTH_SHORT).show();
                return true;
            } else {

//                Toast.makeText(this, "Permission is revoked!", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
//            Toast.makeText(this, "Permission is granted!", Toast.LENGTH_SHORT).show();
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted!", Toast.LENGTH_SHORT).show();
                    CallAdmin();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void CallAdmin(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:0946131591" ));
        startActivity(callIntent);
    }

    public void SendEmailAdmin(){
        final RequestQueue requestQueue=Volley.newRequestQueue(this); // lấy email
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.layemail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("error")) {
                    CheckConnection.ShowToastThongBao(LienHeActivity.this,"lỗi");

                }else{

                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, response);
                    intent.setData(Uri.parse("mailto:"+"namtc101099@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                    try {

                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.d("Email error:",e.toString());
//                        Toast.makeText(LienHeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("MaTaiKhoan", String.valueOf(DangNhapActivity.id));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void Actiontoolbar() {
        setSupportActionBar(toolbarLienHe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLienHe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
