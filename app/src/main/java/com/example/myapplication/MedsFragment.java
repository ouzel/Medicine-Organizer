package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.med.AddNewMed;
import com.example.myapplication.med.DatabaseHandler;
import com.example.myapplication.med.DialogCloseListener;
import com.example.myapplication.med.RecyclerItemTouchHelper;
import com.example.myapplication.med.MedAdapter;
import com.example.myapplication.med.MedModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedsFragment extends Fragment implements DialogCloseListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View view22;

    private DatabaseHandler db;

    private RecyclerView tasksRecyclerView;
    private MedAdapter tasksAdapter;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    private static int sorting = 1;
    private List<MedModel> taskList;


    public MedsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedsFragment.
     */
    public static MedsFragment newInstance(String param1, String param2) {
        MedsFragment fragment = new MedsFragment();
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

        view22 = inflater.inflate(R.layout.fragment_meds, container, false);

        db = new DatabaseHandler(view22.getContext());
        db.openDatabase();

        tasksRecyclerView = view22.findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(view22.getContext()));
        tasksAdapter = new MedAdapter(db, (MainActivity) getActivity());
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = view22.findViewById(R.id.fab);
        fab2 = view22.findViewById(R.id.fab2);

        taskList = db.getAllMeds();

        if (sorting == 1) {
            taskList.sort(new AlphabetComparator());
        } else {
            taskList.sort(new DateComparator());
        }
        tasksAdapter.setTasks(taskList);
        return view22;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewMed.newInstance().show(((AppCompatActivity) getActivity()).getSupportFragmentManager(),
                        AddNewMed.TAG);
                tasksAdapter.notifyDataSetChanged();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Сортировать лекарства");
                builder.setMessage("Выберите сортировку");
                builder.setPositiveButton("По названию", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sorting = 1;
                        taskList.sort(new AlphabetComparator());
                        tasksAdapter.setTasks(taskList);
                    }
                });
                builder.setNegativeButton("По сроку\nгодности", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sorting = 2;
                        taskList.sort(new DateComparator());
                        tasksAdapter.setTasks(taskList);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                tasksAdapter.notifyDataSetChanged();
            }
        });

        tasksAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllMeds();
        if (sorting == 1) {
            taskList.sort(new AlphabetComparator());
        } else {
            taskList.sort(new DateComparator());
        }
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    static class AlphabetComparator implements Comparator<MedModel> {
        public int compare(MedModel o1, MedModel o2) {
            return o1.getMed().compareTo(o2.getMed());
        }
    }

    static class DateComparator implements Comparator<MedModel> {
        public int compare(MedModel o1, MedModel o2) {
            String stringDate1 = o1.getDate();
            String stringDate2 = o2.getDate();
            String[] date1 = stringDate1.split("/");
            String[] date2 = stringDate2.split("/");
            if (date1[1].length() == 1) {
                date1[1] = "0" + date1[1];
            }
            if (date2[1].length() == 1) {
                date2[1] = "0" + date2[1];
            }
            if (date1[0].length() == 1) {
                date1[0] = "0" + date1[0];
            }
            if (date2[0].length() == 1) {
                date2[0] = "0" + date2[0];
            }
            String dateToSort1 = date1[2] + date1[1] + date1[0];
            String dateToSort2 = date2[2] + date2[1] + date2[0];
            return dateToSort1.compareTo(dateToSort2);
        }
    }
}