package com.example.myapplication.med;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class AddNewMed extends BottomSheetDialogFragment {

    private DatePickerDialog datePickerDialog;
    private Button dateBtn;
    private EditText editTextNumber;

    public static final String TAG = "ActionBottomDialog";
    private EditText newMedText;
    private Button newMedSaveButton;
    private Button closeNewMedBtn;

    private DatabaseHandler db;

    public static AddNewMed newInstance() {
        return new AddNewMed();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_med, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newMedText = requireView().findViewById(R.id.newTaskText);
        newMedSaveButton = getView().findViewById(R.id.newTaskButton);
        closeNewMedBtn = getView().findViewById(R.id.button9);
        editTextNumber = getView().findViewById(R.id.editTextNumber);
        initDatePicker();
        dateBtn = requireView().findViewById(R.id.btPickDate);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        dateBtn.setText(getDate());

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String med = bundle.getString("med");
            newMedText.setText(med);
            int amount = bundle.getInt("amount");
            editTextNumber.setText(String.valueOf(amount));
            String date = bundle.getString("date");
            dateBtn.setText(date);
            assert med != null;
            if (med.length() > 0)
                newMedSaveButton.setTextColor(Color.GREEN);
        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        newMedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newMedSaveButton.setEnabled(false);
                    newMedSaveButton.setTextColor(Color.GRAY);
                } else {
                    newMedSaveButton.setEnabled(true);
                    newMedSaveButton.setTextColor(Color.GREEN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        final boolean finalIsUpdate = isUpdate;
        newMedSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String text = newMedText.getText().toString();
                    String stringAmount = editTextNumber.getText().toString();
                    if (text.trim().length() < 1 || stringAmount.length() < 1) {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                        builder.setTitle("Ошибка при добавлении лекарства");
                        builder.setMessage("Заполните название лекарства и его количество," +
                                " чтобы добавить его в базу");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else if (text.length() > 100) {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                        builder.setTitle("Ошибка при добавлении лекарства");
                        builder.setMessage("Превышен лимит количества символов в названии лекарства." +
                                " (максимальное возможное количество символов в названии - 100)");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else if (stringAmount.length() > 7) {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                        builder.setTitle("Ошибка при добавлении лекарства");
                        builder.setMessage("Превышен лимит количества лекарства." +
                                " (максимальное возможное количество лекарства - 9999999)");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        int amount = 0;
                        amount = Integer.parseInt(stringAmount);
                        String date = dateBtn.getText().toString();
                        if (finalIsUpdate) {
                            db.updateTask(bundle.getInt("id"), text, date, amount);
                        } else {
                            MedModel task = new MedModel();
                            task.setMed(text);
                            task.setAmount(amount);
                            task.setDate(date);
                            db.insertTask(task);
                        }
                        dismiss();
                    }
                } catch (Exception e) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                    builder.setTitle("Ошибка при добавлении лекарства");
                    builder.setMessage("Проверьте корректность заполнения полей");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        closeNewMedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener)
            ((DialogCloseListener) activity).handleDialogClose(dialog);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                dateBtn.setText(date);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this.getContext(), dateListener, year, month, day);

    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day + "/" + month + "/" + year;
    }
}
