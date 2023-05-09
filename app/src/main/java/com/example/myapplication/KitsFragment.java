package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.myapplication.kit.KitsModel;
import com.example.myapplication.kit.KitsExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KitsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KitsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    View view33;

    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> medCollection;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    Button btnAddCat, btnDeleteCat;

    public KitsFragment() {
        // Required empty public constructor
    }


    public static KitsFragment newInstance(String param1, String param2) {
        KitsFragment fragment = new KitsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view33 = inflater.inflate(R.layout.fragment_kits, container, false);
        KitsModel.initialize();
        groupList = KitsModel.groupList;
        childList = KitsModel.childList;
        medCollection = KitsModel.medCollection;
        btnAddCat = view33.findViewById(R.id.btnAddCat);
        btnDeleteCat = view33.findViewById(R.id.btnDeleteCat);
        btnAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View view1 = getLayoutInflater().inflate(R.layout.get_category_name, null);
                AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
                alertDialog.setTitle("Добавление аптечки");
                alertDialog.setCancelable(false);
                //alertDialog.setMessage("Your Message Here");


                final EditText etComments = (EditText) view1.findViewById(R.id.etComments);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        groupList.add(etComments.getText().toString());
                        medCollection.put(etComments.getText().toString(), new ArrayList<>());
                        expandableListAdapter = new KitsExpandableListAdapter(getContext(), groupList, medCollection);
                        expandableListView.setAdapter(expandableListAdapter);
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(view1);
                alertDialog.show();
            }
        });

        btnDeleteCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item, groupList);
                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setTitle("Выберите медикамент")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String toDel = groupList.get(which);
                                groupList.remove(toDel);
                                medCollection.remove(toDel);
                                expandableListAdapter = new KitsExpandableListAdapter(getContext(), groupList, medCollection);
                                expandableListView.setAdapter(expandableListAdapter);
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        expandableListView = view33.findViewById(R.id.elvMobiles);
        expandableListAdapter = new KitsExpandableListAdapter(getContext(), groupList, medCollection);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;

            @Override
            public void onGroupExpand(int i) {
                if (lastExpandedPosition != -1 && i != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selected = expandableListAdapter.getChild(i, i1).toString();
                //Toast.makeText(getApplicationContext(), "Selected: " + selected, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return view33;
    }

}