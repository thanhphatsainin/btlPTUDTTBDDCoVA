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

public class LapTopAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SanPham> arrayLapTop;
    private ArrayList<SanPham> arrayList;

    public LapTopAdapter(Context context, ArrayList<SanPham> arraylaptop) {
        this.context = context;
        this.arrayLapTop = arraylaptop;
        arrayList=new ArrayList<SanPham>();
        this.arrayList.addAll(arraylaptop);
    }

    @Override
    public int getCount() {

        return arrayLapTop.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayLapTop.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        private ImageView imageViewHinhLapTop;
        private TextView textViewLapTop, textViewGiaLapTop, textViewMoTaLapTop;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.dong_laptop,null);
            viewHolder.imageViewHinhLapTop =(ImageView)convertView.findViewById(R.id.imgHinhSanPhamLapTop);
            viewHolder.textViewLapTop =(TextView) convertView.findViewById(R.id.txtTenSanPhamLapTop);
            viewHolder.textViewGiaLapTop =(TextView)convertView.findViewById(R.id.txtGiaSanPhamLapTop);
            viewHolder.textViewMoTaLapTop =(TextView)convertView.findViewById(R.id.txtMoTaSanPhamLapTop);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        SanPham sanpham= (SanPham) getItem(position);
        viewHolder.textViewLapTop.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        viewHolder.textViewGiaLapTop.setText("Gía: "+decimalFormat.format(sanpham.getGiasanpham())+" đ");
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.imglcocal)
                .error(R.drawable.errors)
                .into(viewHolder.imageViewHinhLapTop);
        viewHolder.textViewMoTaLapTop.setMaxLines(2);
        viewHolder.textViewMoTaLapTop.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.textViewMoTaLapTop.setText(sanpham.getMotasanpham());

        return convertView;
    }
    //Filter Class : search laptop theo ten
    public void  filter(String charTexts){
        charTexts = charTexts.toLowerCase(Locale.getDefault());
        arrayLapTop.clear();
        if(charTexts.length() == 0){
            arrayLapTop.addAll(arrayList);
        }else{
            for(SanPham sanPham : arrayList){
                if(sanPham.Tensanpham.toLowerCase(Locale.getDefault()).contains(charTexts)){
                    arrayLapTop .add(sanPham);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void TheoGiaSanPham(Integer gia){
        arrayLapTop.clear();
        if(gia==1){
            for(SanPham sanPham : arrayList){
                if(sanPham.getGiasanpham() <=2000000){
                    arrayLapTop.add(sanPham);
                }
            }

        }else if(gia==2){
            for(SanPham sanPham : arrayList){
                if(sanPham.getGiasanpham() >=2000000 && sanPham.getGiasanpham()<=5000000){
                    arrayLapTop.add(sanPham);
                }
            }

        }else if(gia==3){
            for(SanPham sanPham : arrayList){
                if(sanPham.getGiasanpham() >5000000 && sanPham.getGiasanpham()<=10000000){
                    arrayLapTop.add(sanPham);
                }
            }


        }else if(gia==4){
            for(SanPham sanPham : arrayList){
                if(sanPham.getGiasanpham() >10000000){
                    arrayLapTop.add(sanPham);
                }
            }

        }else if(gia==5){
            arrayLapTop.addAll(arrayList);
        }
        notifyDataSetChanged();
    }
}
