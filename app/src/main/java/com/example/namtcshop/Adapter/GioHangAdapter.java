package com.example.namtcshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.namtcshop.R;
import com.example.namtcshop.Activity.GioHangActivity;
import com.example.namtcshop.Activity.MainActivity;
import com.example.namtcshop.Model.GioHang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GioHangAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GioHang> mangGioHang;

    public GioHangAdapter(Context context, ArrayList<GioHang> manggiohang) {
        this.context = context;
        this.mangGioHang = manggiohang;
    }

    @Override
    public int getCount() {
        return mangGioHang.size();
    }

    @Override
    public Object getItem(int position) {
        return mangGioHang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class  ViewHolder{
        public static TextView textViewTenGioHang, textViewGiaGioHang;
        public ImageView imageViewGioHang;
        public static Button buttonPlus, buttonValues, buttonMins;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(viewHolder==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.dong_giohang,null);
            viewHolder.textViewTenGioHang =(TextView)convertView.findViewById(R.id.txtTenSanPhamGioHang);
            viewHolder.textViewGiaGioHang =(TextView)convertView.findViewById(R.id.txtGiaSanPhamGioHang);
            viewHolder.imageViewGioHang =(ImageView)convertView.findViewById(R.id.imgAnhSanPhamGioHang);
            viewHolder.buttonValues =(Button)convertView.findViewById(R.id.btnValuesGioHang);
            viewHolder.buttonMins =(Button)convertView.findViewById(R.id.btnTruGioHang);
            viewHolder.buttonPlus =(Button)convertView.findViewById(R.id.btnCongGioHang);

        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        GioHang gioHang= (GioHang) getItem(position);
        viewHolder.textViewTenGioHang.setText(gioHang.getTensp());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        viewHolder.textViewGiaGioHang.setText("Gía : "+decimalFormat.format(gioHang.giasp)+" đ");
        Picasso.with(context)
                .load(gioHang.getHinhanhsp())
                .placeholder(R.drawable.imglcocal)
                .error(R.drawable.errors)
                .into(viewHolder.imageViewGioHang);
        viewHolder.buttonValues.setText(gioHang.getSoluongsp()+"");
        final int sl= Integer.parseInt(viewHolder.buttonValues.getText().toString());
        if (sl>=10) {
            viewHolder.buttonPlus.setVisibility(View.INVISIBLE);

        }else if(sl<1){
            viewHolder.buttonMins.setVisibility(View.INVISIBLE);

        }else {
            viewHolder.buttonMins.setVisibility(View.VISIBLE);
            viewHolder.buttonPlus.setVisibility(View.VISIBLE);
        }

        viewHolder.buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soluongmoinhat = Integer.parseInt(ViewHolder.buttonValues.getText().toString())+1;
                int soluonght = MainActivity.mangGioHang.get(position).soluongsp;
                long giaht=MainActivity.mangGioHang.get(position).giasp;
                MainActivity.mangGioHang.get(position).setSoluongsp(soluongmoinhat);
                long giaMoiNhat =(giaht* soluongmoinhat)/ soluonght;
                MainActivity.mangGioHang.get(position).setGiasp(giaMoiNhat);
                DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
                ViewHolder.textViewGiaGioHang.setText("Giá : "+decimalFormat.format(giaMoiNhat)+" đ");
                GioHangActivity.TinhTongTien();
                if(soluongmoinhat >9){
                    ViewHolder.buttonValues.setText(String.valueOf(soluongmoinhat));
                    ViewHolder.buttonMins.setVisibility(View.VISIBLE);
                    ViewHolder.buttonPlus.setVisibility(View.INVISIBLE);
                }else{
                    ViewHolder.buttonValues.setText(String.valueOf(soluongmoinhat));
                    ViewHolder.buttonMins.setVisibility(View.VISIBLE);
                    ViewHolder.buttonPlus.setVisibility(View.VISIBLE);
                }
            }
        });
        ViewHolder.buttonMins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soluongmoinhat = Integer.parseInt(ViewHolder.buttonValues.getText().toString())-1;
                int soluonght =MainActivity.mangGioHang.get(position).soluongsp;
                long giaht=MainActivity.mangGioHang.get(position).giasp;
                MainActivity.mangGioHang.get(position).setSoluongsp(soluongmoinhat);
                long giamoinhat=(giaht* soluongmoinhat)/ soluonght;
                MainActivity.mangGioHang.get(position).setGiasp(giamoinhat);
                DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
                ViewHolder.textViewGiaGioHang.setText("Giá : "+decimalFormat.format(giamoinhat)+" đ");
                GioHangActivity.TinhTongTien();
                if(soluongmoinhat <2){
                    ViewHolder.buttonValues.setText(String.valueOf(soluongmoinhat));
                    ViewHolder.buttonMins.setVisibility(View.INVISIBLE);
                    ViewHolder.buttonPlus.setVisibility(View.VISIBLE);
                }else{
                    ViewHolder.buttonValues.setText(String.valueOf(soluongmoinhat));
                    ViewHolder.buttonMins.setVisibility(View.VISIBLE);
                    ViewHolder.buttonPlus.setVisibility(View.VISIBLE);
                }
            }
        });


        return convertView;
    }
}
