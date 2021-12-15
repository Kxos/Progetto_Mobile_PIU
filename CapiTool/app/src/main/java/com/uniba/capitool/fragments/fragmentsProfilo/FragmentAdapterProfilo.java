package com.uniba.capitool.fragments.fragmentsProfilo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapterProfilo extends FragmentStateAdapter {

    public FragmentAdapterProfilo(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //Log.e( "createFragment:", String.valueOf(position));
        switch (position){
            case 0: return new FragmentDatiPersonali();
            case 1: return new FragmentSicurezza();
        }
        return new FragmentGestioneAccount();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
