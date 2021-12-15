package com.uniba.capitool.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uniba.capitool.R;

import java.util.ArrayList;

public class CardSitoCulturaleAdapter extends RecyclerView.Adapter<CardSitoCulturaleAdapter.ViewHolder>{

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.card_sito_culturale, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Get the data model based on position
        CardSitoCulturale cardSitoCulturale = sitiCulturali.get(position);

        // Set item views based on your views and data model
        TextView cardNomeSito = holder.nome;
        cardNomeSito.setText(cardSitoCulturale.getNome());

        TextView cardIndirizzoSito = holder.indirizzo;
        cardIndirizzoSito.setText(cardSitoCulturale.getIndirizzo());

        TextView cardOrarioAperturaSito = holder.orarioApertura;
        cardOrarioAperturaSito.setText(cardSitoCulturale.getOrarioApertura());

        TextView cardOrarioChiusuraSito = holder.orarioChiusura;
        cardOrarioChiusuraSito.setText(cardSitoCulturale.getOrarioChiusura());

        TextView cardCostoBigliettoSito = holder.costoBiglietto;
        cardCostoBigliettoSito.setText(cardSitoCulturale.getCostoBiglietto().toString());

        TextView cardCittaSito = holder.citta;
        cardCittaSito.setText(cardSitoCulturale.getCitta());

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return sitiCulturali.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView foto;
        public TextView nome;
        public TextView indirizzo;
        public TextView orarioApertura;
        public TextView orarioChiusura;
        public TextView costoBiglietto;
        public TextView citta;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            foto = (ImageView) itemView.findViewById(R.id.itemImmagineSito);
            nome = (TextView) itemView.findViewById(R.id.itemNomeSito);
            indirizzo = (TextView) itemView.findViewById(R.id.itemIndirizzoSito);
            orarioApertura = (TextView) itemView.findViewById(R.id.itemOrarioAperturaSito);
            orarioChiusura = (TextView) itemView.findViewById(R.id.itemOrarioChiusuraSito);
            costoBiglietto = (TextView) itemView.findViewById(R.id.itemCostoBigliettoSito);
            citta = (TextView) itemView.findViewById(R.id.itemCittaSito);

        }
    }

    // Store a member variable
    private ArrayList<CardSitoCulturale> sitiCulturali;

    // Pass in the array into the constructor
    public CardSitoCulturaleAdapter(ArrayList<CardSitoCulturale> sitiCulturali) {
        this.sitiCulturali = sitiCulturali;
    }

}
