package com.uniba.capitool.classes;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class CardOperaAdapter extends RecyclerView.Adapter<CardOperaAdapter.ViewHolder>{

    private ArrayList<CardOpera> listaOpere;
    private ArrayList<CardOpera> listaOpereChecked;
    private CardOperaAdapter.OnEventClickListener mListener;
    private CheckBox cardCheckBoxOpera;

    public interface OnEventClickListener{
        void onEventClick (int position);
    }

    public void setOnEventClickListener(CardOperaAdapter.OnEventClickListener listener){
        mListener=listener;
    }

    // Pass in the array into the constructor
    public CardOperaAdapter(ArrayList<CardOpera> listaOpere) {
        this.listaOpere = listaOpere;
        listaOpereChecked = new ArrayList<>();
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public CardOperaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.card_opera, parent, false);

        // Return a new holder instance
        CardOperaAdapter.ViewHolder viewHolder = new CardOperaAdapter.ViewHolder(contactView);
        return viewHolder;

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull CardOperaAdapter.ViewHolder holder, int position) {

        // Get the data model based on position
        CardOpera cardOpera = listaOpere.get(position);

        // Set item views based on your views and data model
        ImageView cardFotoOpera = holder.foto;
        setImmagineOperaFromDB(cardOpera.getId(), holder.itemView.getContext(), cardFotoOpera);

        TextView cardIdOpera = holder.id;
        cardIdOpera.setText(cardOpera.getId());

        TextView cardTitoloOpera = holder.titolo;
        cardTitoloOpera.setText(cardOpera.getTitolo());

        cardCheckBoxOpera = holder.checkBox;
        cardCheckBoxOpera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //Log.e("AAAAAAAAAAA: ", ""+isChecked);
                //Log.e("POSIZIONE: ", ""+position);

                if(isChecked){
                    cardOpera.setCheckBoxCheckedStatus(true);
                    listaOpereChecked.add(cardOpera);
                }else {
                    cardOpera.setCheckBoxCheckedStatus(false);
                    listaOpereChecked.remove(cardOpera);
                }

                //Log.e("Esistono opere checked SINGOLO ITEM: ", ""+cardOpera);
                //Log.e("Esistono opere checked NELLA LISTA: ", ""+listaOpereChecked);
            }
        });
        cardCheckBoxOpera.setChecked(cardOpera.getCheckBox().isChecked());

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return listaOpere.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView foto;
        public TextView id;
        public TextView titolo;
        public CheckBox checkBox;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            foto = (ImageView) itemView.findViewById(R.id.itemImmagineOpera);
            id = (TextView) itemView.findViewById(R.id.itemIdOpera);
            titolo = (TextView) itemView.findViewById(R.id.itemNomeOpera);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkOperaSelezionata);

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
     * Imposta l'immagine dell'opera nella card
     *
     * @param idOpera
     * @param context
     * @param cardFotoSito
     */
    public void setImmagineOperaFromDB(String idOpera, Context context, ImageView cardFotoSito){

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/fotoOpere/" + idOpera);

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

    public ArrayList<CardOpera> getListaOpereChecked() {
        return listaOpereChecked;
    }

}
