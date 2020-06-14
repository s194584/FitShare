package com.example.youfit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Stream;

public class ExcersizeFragment extends Fragment {

    private String workoutSelected;
    private String excersizeSelected;
    private LinearLayout parentLinearLayout;
    protected HashMap<String,String[]> excersizeStrings = new HashMap<>();
    protected String[] excersizeStringsArray = new String[]{};

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

        this.parentLinearLayout = view.findViewById(R.id.excersizeLinearLayout);

        this.excersizeStrings.put("time",getResources().getStringArray(R.array.rep_excersizes));
        this.excersizeStrings.put("rep",getResources().getStringArray(R.array.time_excersizes));

        excersizeStringsArray = ArrayUtils.concat(this.excersizeStrings.get("time"),
                this.excersizeStrings.get("rep"));

        Button addExcersizeBtn = view.findViewById(R.id.addExerciseBtn);

        addExcersizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add excersize");
                // inflate view
                View viewInflated = LayoutInflater.from(getContext()).
                        inflate(R.layout.popup_input_excersize,
                                (ViewGroup) getView(), false);
                // Set up the input
                final AutoCompleteTextView inputExcersize = viewInflated.findViewById(R.id.inputExcersize);

                //set up autocomplete adapter
                ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<String>(getContext(),
                        R.layout.support_simple_spinner_dropdown_item, excersizeStringsArray);
                inputExcersize.setAdapter(autocompleteAdapter);


                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        excersizeSelected = inputExcersize.getText().toString();

                        LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View rowView=inflater.inflate(R.layout.list_of_excersizes_detail, null);
                        ((TextView) rowView.findViewById(R.id.excersizeNameTextView)).setText(excersizeSelected);

                        // Add the new row before the add field button.
                        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

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