package com.tanzee.findmyparkingapp.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tanzee.findmyparkingapp.R;
import com.tanzee.findmyparkingapp.databinding.RowMySpotsBinding;
import com.tanzee.findmyparkingapp.db.MySpotsFrequency;

import java.util.List;


public class MySpotsAdapters extends BaseAdapter {

    private Context context;
    private List<MySpotsFrequency> history;

    public MySpotsAdapters(Context context, List<MySpotsFrequency> history){
        this.context = context;
        this.history = history;
    }

    @Override
    public int getCount(){
        return history.size();
    }

    @Override
    public Object getItem(int i){
        return history.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){

        Holder holder;

        if(view == null){
            holder = new Holder(context);
            view = holder.binding.getRoot();
            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }

        holder.binding.tvId.setText((history.get(position).getFrequency() + 1) + "");
        holder.binding.tvName.setText(history.get(position).getName());
        holder.binding.tvDate.setText(history.get(position).getDate());

        return view;
    }

    class Holder{

        RowMySpotsBinding binding;

        public Holder(Context context){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_my_spots, null, true);
        }
    }

}
