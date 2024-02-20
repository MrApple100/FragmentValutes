package com.example.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterValute extends RecyclerView.Adapter<AdapterValute.ViewHolder> {

    ArrayList<Valute> valutes = new ArrayList<>();

    public AdapterValute(ArrayList<Valute> valutes) {
        this.valutes = valutes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.onevalute,parent,false);
        return new AdapterValute.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Valute valute = valutes.get(position);
        holder.name.setText(valute.name);
        holder.value.setText(valute.value);
        holder.picture.setImageBitmap(valute.picture);

    }

    @Override
    public int getItemCount() {
        return valutes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView value,name;
        ImageView picture;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            value = itemView.findViewById(R.id.value);
            name = itemView.findViewById(R.id.name);
            picture = itemView.findViewById(R.id.picture);

        }
    }
}
