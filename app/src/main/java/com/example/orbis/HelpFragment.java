package com.example.orbis;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;


public class HelpFragment extends Fragment {
    MainActivity main; //store main activity

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        main = ((MainActivity) getActivity());

        //toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbarHelp);
        toolbar.setTitle(R.string.settings_help);
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material); //set back arrow
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.goToLastFragment();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
