package com.uniba.capitool.fragments.fragmentsNavDrawnBar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.uniba.capitool.R;

/**
 */
public class FragmentProfilo extends Fragment {

    TabLayout tabsLayout;
    ViewPager viewPager;

    public FragmentProfilo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profilo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabsLayout=view.findViewById(R.id.tab_layout);
        viewPager=view.findViewById(R.id.pager);

        FragmentManager fragmentManager= getActivity().getSupportFragmentManager();

        /*PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return tabsLayout.get(index).constructFragment();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return false;
            }
        };*/
    }
}