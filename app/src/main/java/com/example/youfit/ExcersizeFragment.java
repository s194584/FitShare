package com.example.youfit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;

public class ExcersizeFragment extends Fragment {

    private String workoutSelected;
    private String excersizeSelected;
    protected String[] excersizes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_excersize, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutSelected = getArguments().getString("excersize");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView excersizeSelectedTextView = view.findViewById(R.id.excersizeSelectedTextView);
        excersizeSelectedTextView.setText(workoutSelected);

        this.excersizes = ArrayUtils.concat(getResources().getStringArray(R.array.rep_excersizes),
                getResources().getStringArray(R.array.time_excersizes));

        Button addExcersizeBtn = view.findViewById(R.id.addExerciseBtn);

        addExcersizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add excersize");
                // inflate view
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.popup_input_excersize, (ViewGroup) getView(), false);
                // Set up the input
                final AutoCompleteTextView inputExcersize = viewInflated.findViewById(R.id.inputExcersize);

                ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,excersizes);
                inputExcersize.setAdapter(autocompleteAdapter);


                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        excersizeSelected = inputExcersize.getText().toString();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


    }
}