package com.uniba.capitool.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uniba.capitool.R;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;

import java.util.List;

public class OrdinaZoneAdapter extends RecyclerView.Adapter<OrdinaZoneAdapter.OrdinaZoneViewHolder> {

    private Context context;
    private List<AllZona> allZoneList;

    public OrdinaZoneAdapter(Context context, List<AllZona> allZoneList) {
        this.context = context;
        this.allZoneList = allZoneList;
    }

    @NonNull
    @Override
    public OrdinaZoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdinaZoneViewHolder(LayoutInflater.from(context).inflate(R.layout.card_zona_ordinamento, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdinaZoneViewHolder holder, int position) {
        holder.nomeZona.setText(allZoneList.get(position).getNomeZona());

    }

    @Override
    public int getItemCount() {
        return allZoneList.size();
    }

    public class OrdinaZoneViewHolder extends RecyclerView.ViewHolder {

        TextView nomeZona;
        TextView posizione;

        public OrdinaZoneViewHolder(@NonNull View itemView) {
            super(itemView);


            nomeZona=itemView.findViewById(R.id.itemNomeZona);
            posizione=itemView.findViewById(R.id.posizioneZona);

        }
    }


}
