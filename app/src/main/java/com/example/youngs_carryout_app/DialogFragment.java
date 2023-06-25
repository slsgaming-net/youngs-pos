package com.example.youngs_carryout_app;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

import static android.app.AlertDialog.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogFragment extends AppCompatDialogFragment {

    public DialogFragment() {
        // Required empty public constructor
    }

    ArrayList<String> item_list;
    ArrayAdapter<String> adapter;

    GridView gridView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Builder builder = new Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_dialog, null);


        adapter = new ArrayAdapter<>(getActivity(), R.layout.listview_layout);

        gridView = view.findViewById(R.id.gridView);

        gridView.setAdapter(adapter);
        builder.setTitle("Extra Addons");
        return builder.create();
    }

}
