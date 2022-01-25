package com.uniba.capitool.classes;

import android.content.Context;
import android.net.Uri;
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

public class CardZonaAdapter extends RecyclerView.Adapter<CardZonaAdapter.ViewHolder>{
    private Context myContext;
    private ArrayList<CardZona> listaZone;
    private ArrayList<CardZona> listaZoneChecked;
    private ArrayList<CardZona> listaZoneUnchecked;
    private CardZonaAdapter.OnEventClickListener mListener;
    private CheckBox cardCheckBoxZona;

    public interface OnEventClickListener{
        void onEventClick (int position);
    }

    // Pass in the array into the constructor
    public CardZonaAdapter(ArrayList<CardZona> listaZone, Context myContext) {
        this.listaZone = listaZone;
        this.listaZoneUnchecked = new ArrayList<>();
        this.myContext = myContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public CardZonaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.card_zona, parent, false);

        // Return a new holder instance
        CardZonaAdapter.ViewHolder viewHolder = new CardZonaAdapter.ViewHolder(contactView);
        return viewHolder;

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull CardZonaAdapter.ViewHolder holder, int position) {

        // Get the data model based on position
        CardZona cardZona = listaZone.get(position);

        TextView cardIdZona = holder.idZona;
        cardIdZona.setText(cardZona.getId());

        TextView cardNomeZona = holder.nome;
        cardNomeZona.setText(cardZona.getNome());

        TextView cardIdSito= holder.idSito;
        cardIdZona.setText(cardZona.getIdSito());

        cardCheckBoxZona = holder.checkBox;
        cardCheckBoxZona.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //Log.e("AAAAAAAAAAA: ", ""+isChecked);
                //Log.e("POSIZIONE: ", ""+position);

               /* if(isChecked){
                    cardZona.setCheckBoxCheckedStatus(true);
                    listaZoneChecked.add(cardZona);
                }else {
                    cardZona.setCheckBoxCheckedStatus(false);
                    listaZoneUnchecked.add(cardZona);
                    listaZoneChecked.remove(cardZona);
                }*/

                //Log.e("Esistono opere checked SINGOLO ITEM: ", ""+cardOpera);
                //Log.e("Esistono opere checked NELLA LISTA: ", ""+listaOpereChecked);
            }
        });


    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return listaZone.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public TextView idZona;
        public TextView nome;
        public CheckBox checkBox;
        public TextView idSito;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);


            idZona = (TextView) itemView.findViewById(R.id.itemIdZonaCard);
            nome = (TextView) itemView.findViewById(R.id.itemNomeZona);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkZonaSelezionata);
            idSito= (TextView) itemView.findViewById(R.id.itemIdSito);

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




    public ArrayList<CardZona> getListaOpereChecked() {
        return listaZoneChecked;
    }

    public ArrayList<CardZona> getListaOpereUnchecked() {
        return listaZoneUnchecked;
    }

}
