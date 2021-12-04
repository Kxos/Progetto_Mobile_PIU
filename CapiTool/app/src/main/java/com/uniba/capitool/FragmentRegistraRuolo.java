package com.uniba.capitool;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class FragmentRegistraRuolo extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();

        if(bundle != null){
            // handle your code here.
            Log.d( "onCreateView: ", bundle.get("email").toString());
        }




        // Inflate the layout for this fragment
         View v=inflater.inflate(R.layout.fragment_registra_ruolo, container, false);

        Button avanti=v.findViewById(R.id.avanti2);

        RadioGroup radioGroup= v.findViewById(R.id.radioGroup2);
        RadioButton visitatore = v.findViewById(R.id.visitatore);
        RadioButton curatore = v.findViewById(R.id.curatore);
        visitatore.setChecked(true);

        avanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectedRadioButton = (RadioButton)findViewById(selectedId);
                int radioId=radioGroup.getCheckedRadioButtonId();
                RadioButton ruolo = (RadioButton) v.findViewById(radioId);
               // String ruoloSelezionato= (String) ruolo.getText();

                if(visitatore.isChecked()){
                    Log.d( "onClick ruolo:" , "visitatore");
                }else if(curatore.isChecked()){
                    Log.d( "onClick ruolo:" , "curatore");
                }else{
                    Log.d( "onClick ruolo:" , "guida");
                }
                Log.d( "radioId:" , String.valueOf(radioId));
              //  Log.d( "onClick:" , ruoloSelezionato);

               FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, new FragmentRegistraDatiPersonali() );
                fragmentTransaction.commit();
            }
        });
        return v;
    }

}