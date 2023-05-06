package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

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
    Map<String, List<String>> mobileCollection;
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
        setHasOptionsMenu(true);
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
        mobileCollection = KitsModel.mobileCollection;
        btnAddCat = view33.findViewById(R.id.btnAddCat);
        btnDeleteCat = view33.findViewById(R.id.btnDeleteCat);
        btnAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupList.add("categorie");
                mobileCollection.put("categorie", new ArrayList<>());
                expandableListAdapter = new MyExpandableListAdapter(getContext(), groupList, mobileCollection);
                expandableListView.setAdapter(expandableListAdapter);
            }
        });

        btnDeleteCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupList.remove("categorie");
                mobileCollection.remove("categorie");
                expandableListAdapter = new MyExpandableListAdapter(getContext(), groupList, mobileCollection);
                expandableListView.setAdapter(expandableListAdapter);
            }
        });
        expandableListView = view33.findViewById(R.id.elvMobiles);
        expandableListAdapter = new MyExpandableListAdapter(getContext(), groupList, mobileCollection);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selected = expandableListAdapter.getChild(i,i1).toString();
                //Toast.makeText(getApplicationContext(), "Selected: " + selected, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return view33;
    }

}