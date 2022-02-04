package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.uniba.capitool.R;
import com.uniba.capitool.fragments.fragmentsProfilo.FragmentAdapterProfilo;

public class FragmentProfilo extends Fragment {

    TabLayout tabsLayout;
    ViewPager2 viewPager2;
    FragmentAdapterProfilo adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_profilo, container, false);
        // Inflate the layout for this fragment
        tabsLayout=view.findViewById(R.id.tab_layout);
        viewPager2=view.findViewById(R.id.pager);


        FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
        adapter= new FragmentAdapterProfilo(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);

        //nomi che compaiono per ogni scheda sulla barra in alto
        tabsLayout.addTab(tabsLayout.newTab().setText(getString(R.string.personalData)));
        tabsLayout.addTab(tabsLayout.newTab().setText("Sicurezza"));
        tabsLayout.addTab(tabsLayout.newTab().setText("Gestione Account"));

        tabsLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Log.e("onTabSelected: ", String.valueOf(tab.getPosition()));
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabsLayout.selectTab(tabsLayout.getTabAt(position));
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}