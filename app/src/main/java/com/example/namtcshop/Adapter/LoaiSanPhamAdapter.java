package com.example.namtcshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.namtcshop.R;
import com.example.namtcshop.Model.LoaiSanPham;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LoaiSanPhamAdapter extends BaseAdapter {
    public ArrayList<LoaiSanPham> arrayListLoaiSanPham;
    public Context context;

    public LoaiSanPhamAdapter(ArrayList<LoaiSanPham> arrayListLoaiSanPham, Context context) {
        this.arrayListLoaiSanPham = arrayListLoaiSanPham;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayListLoaiSanPham.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListLoaiSanPham.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class Viewhorder{
        TextView textViewTenLoaiSanPham;
        ImageView imageViewLoaiSanPham;
    }
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        Viewhorder ViewHorder;
        if(view == null) {
            ViewHorder=new Viewhorder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.dong_listview_loaisp,null);
            ViewHorder.imageViewLoaiSanPham =(ImageView)view.findViewById(R.id.imgHinhLoaiSanPham);
            ViewHorder.textViewTenLoaiSanPham=(TextView)view.findViewById(R.id.txtTenLoaiSanPham);
            view.setTag(ViewHorder);
        }else{
            ViewHorder= (Viewhorder) view.getTag();
        }
        LoaiSanPham loaiSanPham =(LoaiSanPham)getItem(i);
        ViewHorder.textViewTenLoaiSanPham.setText(loaiSanPham.getTenLoaisp());
        Picasso.with(context).load(loaiSanPham.getHinhanhloaisp())
                .placeholder(R.drawable.imglcocal)
                .error(R.drawable.errors)
                .into(ViewHorder.imageViewLoaiSanPham);

        return view;
    }
}
