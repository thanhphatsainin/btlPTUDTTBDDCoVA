package com.example.namtcshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.namtcshop.R;
import com.example.namtcshop.Model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GirdViewSanPham extends BaseAdapter {
    private Context context;
    private ArrayList<SanPham> arraySanPham;

    public GirdViewSanPham(Context context, ArrayList<SanPham> arraysanpham) {
        this.context = context;
        this.arraySanPham = arraysanpham;
    }

    @Override
    public int getCount() {
        return arraySanPham.size();
    }

    @Override
    public Object getItem(int position) {
        return arraySanPham.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class Viewhorder{
        TextView textViewTenSanPham, textViewGiaSanPham;
        ImageView imageViewSanPham;
    }
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        Viewhorder ViewHorder;
        if(view == null) {
            ViewHorder=new Viewhorder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.girdview_sanpham,null);
            ViewHorder.imageViewSanPham =(ImageView)view.findViewById(R.id.imgGripViewSanPham);
            ViewHorder.textViewTenSanPham =(TextView)view.findViewById(R.id.txtTenGripViewSanPham);
            ViewHorder.textViewGiaSanPham =(TextView)view.findViewById(R.id.txtGiaGripViewSanPham);
            view.setTag(ViewHorder);
        }else{
            ViewHorder= (Viewhorder) view.getTag();
        }
        SanPham sanpham=(SanPham) getItem(i);
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        ViewHorder.textViewTenSanPham.setText(sanpham.getTensanpham());
        ViewHorder.textViewGiaSanPham.setText("Gía : "+decimalFormat.format(sanpham.getGiasanpham())+" đ");
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.imglcocal)
                .error(R.drawable.errors)
                .into(ViewHorder.imageViewSanPham);
        return view;
    }
}
