package com.example.namtcshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.namtcshop.R;

import com.example.namtcshop.Model.SanPhamDaDat;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SanPhamDaDatAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SanPhamDaDat> arraySanPhamDaDat;


    public SanPhamDaDatAdapter(Context context, ArrayList<SanPhamDaDat> arraydienthoai) {
        this.context = context;
        this.arraySanPhamDaDat = arraydienthoai;
    }

    @Override
    public int getCount() {
        return arraySanPhamDaDat.size();
    }

    @Override
    public Object getItem(int position) {
        return arraySanPhamDaDat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        private ImageView imageViewSanPhamDaDat;
        private TextView textViewTenSanPhamDaDat, textViewGiaSanPhamDaDat, textViewSoLuongSanPhamDaDat;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.dong_san_pham_da_dat,null);
            viewHolder.imageViewSanPhamDaDat =(ImageView)convertView.findViewById(R.id.imgHinhDienThoaiDaDat);
            viewHolder.textViewTenSanPhamDaDat =(TextView) convertView.findViewById(R.id.txtTenDienThoaiDaDat);
            viewHolder.textViewGiaSanPhamDaDat =(TextView)convertView.findViewById(R.id.txtGiaDienThoaiDaDat);
            viewHolder.textViewSoLuongSanPhamDaDat =(TextView)convertView.findViewById(R.id.txtSoLuongDienThoaiDaDat);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        SanPhamDaDat sanPhamDaDat= (SanPhamDaDat) getItem(position);
        viewHolder.textViewTenSanPhamDaDat.setText(sanPhamDaDat.getTensp());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        viewHolder.textViewGiaSanPhamDaDat.setText("Gía: "+decimalFormat.format(sanPhamDaDat.getGiasp())+" đ");
        Picasso.with(context).load(sanPhamDaDat.getHinhanh())
                .placeholder(R.drawable.imglcocal)
                .error(R.drawable.errors)
                .into(viewHolder.imageViewSanPhamDaDat);
        viewHolder.textViewSoLuongSanPhamDaDat.setText("Số Lượng: "+sanPhamDaDat.getSl()+"");


        return convertView;
    }

}
