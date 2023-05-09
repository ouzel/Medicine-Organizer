package com.example.myapplication.kit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.med.DatabaseHandler;
import com.example.myapplication.med.MedModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KitsExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    public Map<String, List<String>> mobileCollection;
    public List<String> groupList;

    private DatabaseHandler db;

    public KitsExpandableListAdapter(Context context, List<String> groupList,
                                     Map<String, List<String>> mobileCollection) {
        this.context = context;
        this.mobileCollection = mobileCollection;
        this.groupList = groupList;
    }

    @Override
    public int getGroupCount() {
        return mobileCollection.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mobileCollection.get(groupList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mobileCollection.get(groupList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        db = new DatabaseHandler(context);
        db.openDatabase();
        String mobileName = getGroup(i).toString();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.group_item, null);
        }
        TextView item = view.findViewById(R.id.nameOfCategory);
        ImageView img = view.findViewById(R.id.imageViewCategory);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(mobileName);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_dropdown_item, getMedsNames());
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("Выберите медикамент")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<String> child = mobileCollection.get(groupList.get(i));
                                child.add(getMedsNames().get(which));
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        String model = getChild(i, i1).toString();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_item, null);
        }
        TextView item = view.findViewById(R.id.model);
        ImageView delete = view.findViewById(R.id.delete);
        item.setText(model);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Удалить?");
                builder.setCancelable(true);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        List<String> child = mobileCollection.get(groupList.get(i));
                        child.remove(i1);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public List<String> getMedsNames() {
        List<MedModel> medsList = db.getAllMeds();
        List<String> medNamesList = new ArrayList<>();
        for (MedModel mm : medsList) {
            medNamesList.add(mm.getMed());
        }
        return medNamesList;
    }

}
