package com.uniba.capitool.classes;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniba.capitool.R;

import java.util.ArrayList;

public class CardPercorsoAdapter extends RecyclerView.Adapter<CardPercorsoAdapter.ViewHolder>{

    // Store a member variable
    private ArrayList<CardPercorso> listaPercorsi;
    private String fragment;
    private CardPercorsoAdapter.OnEventClickListener mListener;

    public interface OnEventClickListener{
        void onEventClick (int position);
    }

    public void setOnEventClickListener(OnEventClickListener listener){
        mListener=listener;
    }

    // Pass in the array into the constructor
    public CardPercorsoAdapter(ArrayList<CardPercorso> listaPercorsi) {
        this.listaPercorsi = listaPercorsi;
        this.fragment = "";
    }

    public CardPercorsoAdapter(ArrayList<CardPercorso> listaPercorsi, String fragment) {
        this.listaPercorsi = listaPercorsi;
        this.fragment = fragment;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.card_percorso, parent, false);

        // Return a new holder instance
        CardPercorsoAdapter.ViewHolder viewHolder = new CardPercorsoAdapter.ViewHolder(contactView);
        return viewHolder;

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Get the data model based on position
        CardPercorso cardPercorso = listaPercorsi.get(position);

        // Set item views based on your views and data model
        TextView cardIdPercorso = holder.id;
        cardIdPercorso.setText(cardPercorso.getId());

        ImageView cardFotoSito = holder.foto;
        setImmagineSitoFromDB(cardPercorso.getIdSitoAssociato(), holder.itemView.getContext(), cardFotoSito);

        TextView cardNomePercorso = holder.nome;
        cardNomePercorso.setText(cardPercorso.getNome());

        TextView cardIdSitoAssociato = holder.idSitoAssociato;
        cardIdSitoAssociato.setText(cardPercorso.getIdSitoAssociato());

        TextView cardNomeSitoAssociato = holder.nomeSitoAssociato;
        cardNomeSitoAssociato.setText(cardPercorso.getNomeSitoAssociato());

        TextView cardCittaSitoAssociato = holder.cittaSitoAssociato;
        cardCittaSitoAssociato.setText(cardPercorso.getCittaSitoAssociato());

        TextView cardDescrizionePercorso = holder.descrizione;
        cardDescrizionePercorso.setText(cardPercorso.getDescrizione());

        TextView cardStatoPubblico = holder.pubblico;
        cardStatoPubblico.setText(""+cardPercorso.isPubblico());

        if(cardPercorso.isPubblico()){
            ImageView itemVisibilityTruePercorso = holder.itemVisibilityTruePercorso;
            itemVisibilityTruePercorso.setVisibility(View.VISIBLE);

            ImageView itemVisibilityFalsePercorso = holder.itemVisibilityFalsePercorso;
            itemVisibilityFalsePercorso.setVisibility(View.INVISIBLE);
        }else{
            ImageView itemVisibilityTruePercorso = holder.itemVisibilityTruePercorso;
            itemVisibilityTruePercorso.setVisibility(View.INVISIBLE);

            ImageView itemVisibilityFalsePercorso = holder.itemVisibilityFalsePercorso;
            itemVisibilityFalsePercorso.setVisibility(View.VISIBLE);

        }

        // Verifico che mi trovo in FragmentConsigliati
        if(fragment.equals("Consigliati")){
            ImageView itemFavouriteBorder = holder.itemFavouriteBorder;
            itemFavouriteBorder.setVisibility(View.VISIBLE);
        }else{
            ImageView itemFavouriteBorder = holder.itemFavouriteBorder;
            itemFavouriteBorder.setVisibility(View.INVISIBLE);

            ImageView itemFavourite = holder.itemFavourite;
            itemFavourite.setVisibility(View.INVISIBLE);
        }

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return listaPercorsi.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView foto;
        public TextView id;
        public TextView nome;
        public TextView idSitoAssociato;
        public TextView nomeSitoAssociato;
        public TextView cittaSitoAssociato;
        public TextView descrizione;
        public TextView pubblico;
        public ImageView itemVisibilityTruePercorso;
        public ImageView itemVisibilityFalsePercorso;
        public ImageView itemFavouriteBorder;
        public ImageView itemFavourite;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            foto = (ImageView) itemView.findViewById(R.id.itemImmaginePercorso);
            id = (TextView) itemView.findViewById(R.id.itemIdPercorso);
            nome = (TextView) itemView.findViewById(R.id.itemNomePercorso);
            descrizione = (TextView) itemView.findViewById(R.id.itemDescrizionePercorso);
            idSitoAssociato = (TextView) itemView.findViewById(R.id.itemIdSitoAssociato);
            nomeSitoAssociato = (TextView) itemView.findViewById(R.id.itemNomeSitoAssociato);
            cittaSitoAssociato = (TextView) itemView.findViewById(R.id.itemCittaSitoAssociato);
            pubblico = (TextView) itemView.findViewById(R.id.itemStatoPubblico);
            itemVisibilityTruePercorso = (ImageView)itemView.findViewById(R.id.itemVisibilityTruePercorso);
            itemVisibilityFalsePercorso = (ImageView)itemView.findViewById(R.id.itemVisibilityFalsePercorso);
            itemFavouriteBorder = (ImageView)itemView.findViewById(R.id.itemFavouriteBorder);
            itemFavourite = (ImageView)itemView.findViewById(R.id.itemFavourite);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onEventClick(position);
                        }
                    }
                }
            });

        }
    }

    /***
     * Imposta l'immagine del Sito cercato
     */
    public void setImmagineSitoFromDB(String idSito, Context context, ImageView cardFotoSito){

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/fotoSiti/" + idSito);

        /**
         * Scarica il "DownloadURL" che ci serve per leggere l'immagine dal DB e metterla in una ImageView
         * */
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(context).load(downloadUrl).into(cardFotoSito);
            }
        });

    }

}
