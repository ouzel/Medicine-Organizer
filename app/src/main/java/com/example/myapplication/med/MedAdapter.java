package com.example.myapplication.med;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.List;

public class MedAdapter extends RecyclerView.Adapter<MedAdapter.ViewHolder> {

    private List<MedModel> medsList;
    private DatabaseHandler db;
    private MainActivity activity;

    public MedAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.med_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final MedModel item = medsList.get(position);
        holder.med.setText(item.getMed());
        holder.amount.setText(String.valueOf(item.getAmount()) + " шт/мл");
        holder.date.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return medsList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<MedModel> todoList) {
        this.medsList = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        MedModel item = medsList.get(position);
        db.deleteMed(item.getId());
        medsList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        MedModel item = medsList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("med", item.getMed());
        bundle.putString("date", item.getDate());
        bundle.putInt("amount", item.getAmount());
        AddNewMed fragment = new AddNewMed();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewMed.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView med;
        TextView date;
        TextView amount;

        ViewHolder(View view) {
            super(view);
            med = view.findViewById(R.id.medTextView);
            date = view.findViewById(R.id.medDate);
            amount = view.findViewById(R.id.medAmount);
        }
    }
}
