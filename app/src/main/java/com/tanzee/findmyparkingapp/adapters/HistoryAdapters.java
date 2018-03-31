package com.tanzee.findmyparkingapp.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tanzee.findmyparkingapp.R;
import com.tanzee.findmyparkingapp.databinding.RowHistoryBinding;
import com.tanzee.findmyparkingapp.db.MySpots;

import java.util.List;


public class HistoryAdapters extends BaseAdapter {

    private Context context;
    private List<MySpots> history;

    public HistoryAdapters(Context context, List<MySpots> history){
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

        holder.binding.tvId.setText(position + "");
        holder.binding.tvName.setText(history.get(position).getName());
        holder.binding.tvDate.setText(history.get(position).getDate());

        return view;
    }

    class Holder{

        RowHistoryBinding binding;

        public Holder(Context context){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_history, null, true);
        }
    }

}
