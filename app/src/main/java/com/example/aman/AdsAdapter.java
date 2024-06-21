package com.example.aman;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.MyHolder> {

ArrayList<String> data;
public AdsAdapter(ArrayList<String> data){
    this.data=data;
}
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_card_item,parent,false);
    return new MyHolder(view);
}

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
holder.tv.setText(data.get(position));
    }



    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

    TextView tv ;
        public MyHolder(@NonNull View itemView) {

            super(itemView);
            tv=itemView.findViewById(R.id.tv);
        }
    }
}
