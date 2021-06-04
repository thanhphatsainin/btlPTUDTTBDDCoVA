package com.example.namtcshop.Adapter;

import android.content.Context;
import android.text.TextUtils;
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
import java.util.Locale;

public class DienThoaiAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SanPham> arrayDienThoai;
    private ArrayList<SanPham> arrayList;


    public DienThoaiAdapter(Context context, ArrayList<SanPham> arraydienthoai) {
        this.context = context;
        this.arrayDienThoai = arraydienthoai;
        this.arrayList=new ArrayList<SanPham>();
        this.arrayList.addAll(arraydienthoai);
    }

    @Override
    public int getCount() {
        return arrayDienThoai.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayDienThoai.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        private ImageView imageViewDienThoai;
        private TextView textViewDienThoai, textViewGia, textViewMoTa;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.dong_dienthoai,null);
            viewHolder.imageViewDienThoai =(ImageView)convertView.findViewById(R.id.imgDienThoai);
            viewHolder.textViewDienThoai =(TextView) convertView.findViewById(R.id.txtTenDienThoai);
            viewHolder.textViewGia =(TextView)convertView.findViewById(R.id.txtGiaDienThoai);
            viewHolder.textViewMoTa =(TextView)convertView.findViewById(R.id.txtMoTaDienThoai);
           convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        SanPham sanpham= (SanPham) getItem(position);
        viewHolder.textViewDienThoai.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        viewHolder.textViewGia.setText("Gía: "+decimalFormat.format(sanpham.getGiasanpham())+" đ");
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.imglcocal)
                .error(R.drawable.errors)
                .into(viewHolder.imageViewDienThoai);
        viewHolder.textViewMoTa.setMaxLines(2);
        viewHolder.textViewMoTa.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.textViewMoTa.setText(sanpham.getMotasanpham());

        return convertView;
    }
    //Filter Class
    public void  filter(String charText){
        charText=charText.toLowerCase(Locale.getDefault());
        arrayDienThoai.clear();
        if(charText.length() == 0){
            arrayDienThoai.addAll(arrayList);
        }else{
            for(SanPham sanPham : arrayList){
                if(sanPham.getTensanpham().toLowerCase(Locale.getDefault()).contains(charText)){
                    arrayDienThoai.add(sanPham);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void TheoGiaSanPham(Integer gia){
        arrayDienThoai.clear();
        if(gia==1){
            for(SanPham sanPham : arrayList){
                if(sanPham.getGiasanpham() <=2000000){
                    arrayDienThoai.add(sanPham);
                }
            }

        }else if(gia==2){
            for(SanPham sanPham : arrayList){
                if(sanPham.getGiasanpham() >=2000000 && sanPham.getGiasanpham()<=5000000){
                    arrayDienThoai.add(sanPham);
                }
            }

        }else if(gia==3){
            for(SanPham sanPham : arrayList){
                if(sanPham.getGiasanpham() >5000000 && sanPham.getGiasanpham()<=10000000){
                    arrayDienThoai.add(sanPham);
                }
            }


        }else if(gia==4){
            for(SanPham sanPham : arrayList){
                if(sanPham.getGiasanpham() >10000000){
                    arrayDienThoai.add(sanPham);
                }
            }

        }else if(gia==5){
            arrayDienThoai.addAll(arrayList);
        }
        notifyDataSetChanged();
    }
}
