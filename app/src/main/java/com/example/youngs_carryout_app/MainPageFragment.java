package com.example.youngs_carryout_app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.Collections;

public class MainPageFragment extends Fragment {

    private ArrayList<String> categories = new ArrayList<>();

    public MainPageFragment() {
        // Required empty public constructor
    }

    public void initCategories(String[] category) {

        Collections.addAll(categories, category);

    }

    LoadMainPageFragment loadMainPageFragment;

    // interface for loading categories from main page fragment
    public interface LoadMainPageFragment {
        void loadCategory(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;

        try {
            loadMainPageFragment = (LoadMainPageFragment) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement LoadPageFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        final GridView mainPageGridView = view.findViewById(R.id.main_gv);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.listview_layout, categories);
        mainPageGridView.setAdapter(adapter);


        mainPageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                loadMainPageFragment.loadCategory(position);

            }
        });

        return view;
    }
}
