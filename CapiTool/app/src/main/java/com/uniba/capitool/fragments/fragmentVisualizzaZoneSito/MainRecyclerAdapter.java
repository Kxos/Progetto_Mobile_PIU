package com.uniba.capitool.fragments.fragmentVisualizzaZoneSito;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uniba.capitool.R;

import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>  implements ItemOperaZonaRecyclerAdapter.OnOperaListener {
    private Context context;
    private List<AllZone> allZoneList;


    /***
     * Metodo costruttore
     * @param context
     * @param allZoneList
     */
    public MainRecyclerAdapter(Context context, List<AllZone> allZoneList) {
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
        setOpereZonaRecycler(holder.opereRecycler, allZoneList.get(position).getListaOpereZona());

    }

    @Override
    public int getItemCount() {
        return allZoneList.size();
    }


    public static final class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nomeZona;
        RecyclerView opereRecycler;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            nomeZona=itemView.findViewById(R.id.nomeZona);
            opereRecycler=itemView.findViewById(R.id.opere_recycler);

//            nomeZona.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("nomeZona", ""+getAdapterPosition());
//
//                }
//            });
        }

        /***
         * Listner del click quando clicco il nome della zona o il layout del item Zona
         * @param v
         */
        @Override
        public void onClick(View v) {
            Log.e("RECYCLER", ""+getAdapterPosition());

        }

    }

    private void setOpereZonaRecycler(RecyclerView recyclerView, List<ItemOperaZona> listaOpereZona){

        ItemOperaZonaRecyclerAdapter itemOperaZonaRecyclerAdapter = new ItemOperaZonaRecyclerAdapter(context, listaOpereZona,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(itemOperaZonaRecyclerAdapter);

    }


    @Override
    public void onOperaClick(int posizioneOpera, String idZona) {

        int posizioneZona=getIndexZona(allZoneList, idZona);
        Log.e("CLICCATO", ""+allZoneList.get(posizioneZona).getListaOpereZona().get(posizioneOpera).getDescrizione());

    }

    /***
     * Cerca la posizione (int) corrispondente all'idZona
     * @param allZoneList
     * @param idZona
     * @return
     */
    public int getIndexZona(List<AllZone> allZoneList, String idZona){
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
