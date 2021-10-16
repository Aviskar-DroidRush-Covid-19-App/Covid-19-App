package com.ee.droidrush.covid_19_app.data_container;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.ee.droidrush.covid_19_app.R;

import java.util.ArrayList;


public class CenterListAdpater extends RecyclerView.Adapter<CenterListAdpater.ViewHolder> {
    ArrayList<Center> center = new ArrayList<>();
    int orange,green,red;
    Drawable empty_drwable;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        orange=context.getColor(R.color.orange);
        green=context.getColor(R.color.green);
        red=context.getColor(R.color.red);
        empty_drwable= AppCompatResources.getDrawable(context,R.drawable.ic_baseline_close_24);
        LayoutInflater inflator = LayoutInflater.from(context);
        View VideoView = inflator.inflate(R.layout.item_slot_search_result,parent,false);
        return new ViewHolder(VideoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Center mcenter = center.get(position);

        holder.address.setText(mcenter.address);
        holder.fee_type.setText(mcenter.fee_type);
        holder.age_limit.setText(mcenter.min_age_limit);
        holder.dose1.setText(String.valueOf(mcenter.dose1));
        holder.name.setText(mcenter.name);
        if(mcenter.dose2>5)
        {
            holder.dose2.setText(String.valueOf(mcenter.dose2));
            holder.dose2.setTextColor(green);
        }else if(mcenter.dose2>0)
        {
            holder.dose2.setText(String.valueOf(mcenter.dose2));
            holder.dose2.setTextColor(orange);
        }else {
            holder.dose2.setText("0");
            holder.dose2.setTextColor(red);
            //holder.dose2.setCompoundDrawables(empty_drwable,null,null,null);
        }

        if(mcenter.dose1>5)
        {
            holder.dose1.setText(String.valueOf(mcenter.dose1));
            holder.dose1.setTextColor(green);
        }else if(mcenter.dose1>0)
        {
            holder.dose1.setText(String.valueOf(mcenter.dose1));
            holder.dose1.setTextColor(orange);
        }else {
            holder.dose1.setText("0");
            holder.dose1.setTextColor(red);
            //holder.dose1.(empty_drwable,null,null,null);
        }
    }

    @Override
    public int getItemCount() {
        return center.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
       public  TextView name,address,vaccine,fee_type,age_limit,dose1,dose2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.center_name);
            address=itemView.findViewById(R.id.cenetr_address);
            vaccine=itemView.findViewById(R.id.vaccine);
            fee_type=itemView.findViewById(R.id.fee_type);
            age_limit=itemView.findViewById(R.id.age_limit);
            dose1=itemView.findViewById(R.id.dose1);
            dose2=itemView.findViewById(R.id.dose2);
        }
    }

    public void setCenter(ArrayList<Center> center) {
        this.center.clear();
        this.center.addAll(center);
        notifyDataSetChanged();
    }
}
