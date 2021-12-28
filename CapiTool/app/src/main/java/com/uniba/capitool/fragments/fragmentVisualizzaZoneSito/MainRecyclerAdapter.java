package com.uniba.capitool.fragments.fragmentVisualizzaZoneSito;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uniba.capitool.R;

import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {
    private Context context;
    private List<AllZone> allZoneList;

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

    public static final class MainViewHolder extends RecyclerView.ViewHolder{

        TextView nomeZona;
        RecyclerView opereRecycler;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeZona=itemView.findViewById(R.id.nomeZona);
            opereRecycler=itemView.findViewById(R.id.opere_recycler);
        }
    }

    private void setOpereZonaRecycler(RecyclerView recyclerView, List<ItemOperaZona> listaOpereZona){

        ItemOperaZonaRecyclerAdapter itemOperaZonaRecyclerAdapter = new ItemOperaZonaRecyclerAdapter(context, listaOpereZona);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(itemOperaZonaRecyclerAdapter);
    }
}
