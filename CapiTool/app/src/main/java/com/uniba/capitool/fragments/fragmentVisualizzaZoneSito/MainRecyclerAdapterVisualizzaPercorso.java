package com.uniba.capitool.fragments.fragmentVisualizzaZoneSito;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uniba.capitool.R;
import com.uniba.capitool.activities.VisualizzaOpera;
import com.uniba.capitool.activities.VisualizzaPercorso;
import com.uniba.capitool.activities.VisualizzaZoneSito;

import java.util.List;

public class MainRecyclerAdapterVisualizzaPercorso extends RecyclerView.Adapter<MainRecyclerAdapterVisualizzaPercorso.MainViewHolder>  implements ItemOperaZonaRecyclerAdapterVisualizzaPercorso.OnOperaListener {
    private Context context;
    private List<AllZona> allZoneList;


    /***
     * Metodo costruttore
     * @param context
     * @param allZoneList
     */
    public MainRecyclerAdapterVisualizzaPercorso(Context context, List<AllZona> allZoneList) {
        this.context = context;
        this.allZoneList = allZoneList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.item_zona, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

        holder.nomeZona.setText(allZoneList.get(position).getNomeZona());
       // holder.nomeZona.setTextColor("#2e2e2e");
        setOpereZonaRecycler(holder.opereRecycler, allZoneList.get(position).getListaOpereZona());

        /***
         * Se listaOpereZona è null significa che quella zona è stata creata ma che non ha nessun opera ancora al suo interno
         * Mostro un messaggio all'utente all'interno della recycler view
         */
        if(allZoneList.get(position).getListaOpereZona()==null){
           holder.emptyZona.setVisibility(View.VISIBLE);
       }


        /*
        holder.nomeZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("RECYCLER", ""+allZoneList.get(holder.getAdapterPosition()).getNomeZona());

                Intent visualizzaZona = new Intent(v.getContext(), VisualizzaZona.class);
                Bundle dati = new Bundle();
                dati.putSerializable("sito", ((VisualizzaZoneSito)v.getContext()).getSito());
                dati.putSerializable("utente", ((VisualizzaZoneSito)v.getContext()).getUtente());
                dati.putSerializable("allZone", (Serializable) allZoneList.get(holder.getAdapterPosition()));
                visualizzaZona.putExtras(dati);
                ((VisualizzaZoneSito)v.getContext()).startActivity(visualizzaZona);
            }
        });
        */

    }

    @Override
    public int getItemCount() {
        return allZoneList.size();
    }


    public static final class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nomeZona;
        RecyclerView opereRecycler;
        TextView emptyZona;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            nomeZona=itemView.findViewById(R.id.nomeZona);
            opereRecycler=itemView.findViewById(R.id.opere_recycler);
            emptyZona=itemView.findViewById(R.id.textViewEmptyZona);

            /*
            emptyZona.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("nomeZona", ""+getAdapterPosition());

                }
            });
            */
        }

        /***
         * Listner del click quando clicco il nome della zona o il layout del item Zona
         * @param v
         */
        @Override
        public void onClick(View v) {
        //    Log.e("RECYCLER", ""+getAdapterPosition()+""+nomeZona.getText());

//            Intent visualizzaZona = new Intent(v.getContext(), VisualizzaZona.class);
//            Bundle dati = new Bundle();
//            dati.putSerializable("sito", ((VisualizzaZoneSito)v.getContext()).getSito());
//            dati.putSerializable("utente", ((VisualizzaZoneSito)v.getContext()).getUtente());
//            dati.putString("nomeZona", nomeZona.getText().toString());
//            visualizzaZona.putExtras(dati);
//
//
//
//            ((VisualizzaZoneSito)v.getContext()).startActivity(visualizzaZona);

        }



    }

    private void setOpereZonaRecycler(RecyclerView recyclerView, List<ItemOperaZona> listaOpereZona){

        ItemOperaZonaRecyclerAdapterVisualizzaPercorso itemOperaZonaRecyclerAdapter = new ItemOperaZonaRecyclerAdapterVisualizzaPercorso(context, listaOpereZona,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(itemOperaZonaRecyclerAdapter);

    }


    @Override
    public void onOperaClick(int posizioneOpera, String idZona, View v) {


        int posizioneZona=getIndexZona(allZoneList, idZona);

        //Log.e("CLICCATO", ""+allZoneList.get(posizioneZona).getListaOpereZona().get(posizioneOpera).getDescrizione());

        ItemOperaZona operaCliccata=allZoneList.get(posizioneZona).getListaOpereZona().get(posizioneOpera);
        Intent visualizzaOpera = new Intent(v.getContext(), VisualizzaOpera.class);
        Bundle itemSelected = new Bundle();
        itemSelected.putSerializable("opera", operaCliccata);
        visualizzaOpera.putExtras(itemSelected);
        Bundle transazione= ActivityOptions.makeSceneTransitionAnimation((VisualizzaPercorso)v.getContext()).toBundle();

        ((VisualizzaPercorso)v.getContext()).startActivity(visualizzaOpera, transazione);


    }

    /***
     * Cerca la posizione (int) corrispondente all'idZona
     * @param allZoneList
     * @param idZona
     * @return
     */
    public int getIndexZona(List<AllZona> allZoneList, String idZona){
        int index=-1;

        for(int i=0; i<allZoneList.size(); i++){
            if(allZoneList.get(i).getId().equals(idZona)){
                index=i;
                break;
            }
        }
        return index;
    }
}
