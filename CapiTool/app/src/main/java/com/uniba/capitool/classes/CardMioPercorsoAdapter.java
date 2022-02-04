package com.uniba.capitool.classes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniba.capitool.R;
import com.uniba.capitool.activities.BasicMethod;
import com.uniba.capitool.activities.EliminaOpere;
import com.uniba.capitool.activities.HomePage;
import com.uniba.capitool.activities.VisualizzaPercorso;

import java.util.ArrayList;

public class CardMioPercorsoAdapter extends RecyclerView.Adapter<CardMioPercorsoAdapter.ViewHolder>{

    // Store a member variable
    private ArrayList<CardPercorso> listaPercorsi;
    private String fragment;
    private View view;
    private Context context;
    private Activity activity;
    private CardMioPercorsoAdapter.OnEventClickListener mListener;

    public interface OnEventClickListener{
        void onEventClick (int position);
    }

    public void setOnEventClickListener(OnEventClickListener listener){
        mListener=listener;
    }

    // Pass in the array into the constructor
    public CardMioPercorsoAdapter(ArrayList<CardPercorso> listaPercorsi) {
        this.listaPercorsi = listaPercorsi;
        this.fragment = "";
    }

    public CardMioPercorsoAdapter(ArrayList<CardPercorso> listaPercorsi, String fragment) {
        this.listaPercorsi = listaPercorsi;
        this.fragment = fragment;
    }

    public CardMioPercorsoAdapter(ArrayList<CardPercorso> listaPercorsi, String fragment, View view, Context context) {
        this.listaPercorsi = listaPercorsi;
        this.fragment = fragment;
        this.view = view;
        this.context = context;
    }

    public CardMioPercorsoAdapter(ArrayList<CardPercorso> listaPercorsi, String fragment, Activity activity, View view, Context context) {
        this.listaPercorsi = listaPercorsi;
        this.fragment = fragment;
        this.activity = activity;
        this.context = context;
        this.view = view;
    }



    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.card_percorso, parent, false);

        // Return a new holder instance
        CardMioPercorsoAdapter.ViewHolder viewHolder = new CardMioPercorsoAdapter.ViewHolder(contactView);
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


        ImageView itemFavouriteBorder = holder.itemFavouriteBorder;
        ImageView itemFavourite = holder.itemFavourite;
        ImageView itemEliminaPercorso = holder.itemEliminaPercorso;

        setCuorePienoSePercorsoPresenteNeiPreferiti(cardPercorso.getId(), BasicMethod.getUtente().getUid(), itemFavouriteBorder, itemFavourite);

        itemFavouriteBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemFavouriteBorder.setVisibility(View.INVISIBLE);
                itemFavourite.setVisibility(View.VISIBLE);
                Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.toastAggiuntoPercorsoAiPreferiti), Toast.LENGTH_SHORT).show();

                if(fragment.equals("Consigliati")){
                    managePercorsoDaScegliereNeiPreferiti(cardPercorso.getId(), "Aggiungere");
                }

            }
        });

        itemFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemFavouriteBorder.setVisibility(View.VISIBLE);
                itemFavourite.setVisibility(View.INVISIBLE);
                Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.toastRimossoPercorsoDaiPreferiti), Toast.LENGTH_SHORT).show();

                if(fragment.equals("Consigliati")){
                    managePercorsoDaScegliereNeiPreferiti(cardPercorso.getId(), "Rimuovere");
                }

                if(fragment.equals("Preferiti")){
                    rimuoviPercorsoDaiPreferiti(cardPercorso.getId(), holder.getAdapterPosition());
                }

            }
        });

        itemEliminaPercorso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = BasicMethod.confermaEliminazione(activity);
                builder.setPositiveButton(R.string.Si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        eliminaPercorso(cardPercorso.getId(), holder.getAdapterPosition());
                        //Log.e("onClick: ", ""+cardIdPercorso.getText() );
                        dialogInterface.cancel();
                    }
                });

                builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog EliminaPercorsoDialog = builder.create();
                EliminaPercorsoDialog.show();

            }
        });


        // Verifico che mi trovo in FragmentConsigliati
        if(fragment.equals("Consigliati")){
            itemFavouriteBorder.setVisibility(View.VISIBLE);

            // Verifico che mi trovo in FragmentPreferiti
        }else if(fragment.equals("Preferiti")){
            itemFavourite.setVisibility(View.VISIBLE);
            itemFavouriteBorder.setVisibility(View.INVISIBLE);

            // Verifico che mi trovo in FragmentMieiPercorsi
        }else if(fragment.equals("MieiPercorsi")){
            itemFavouriteBorder.setVisibility(View.INVISIBLE);
            itemFavourite.setVisibility(View.INVISIBLE);

            itemEliminaPercorso.setVisibility(View.VISIBLE);

            ImageView itemVisibilityTruePercorso = holder.itemVisibilityTruePercorso;
            ImageView itemVisibilityFalsePercorso = holder.itemVisibilityFalsePercorso;

            if(!BasicMethod.getUtente().getRuolo().equals("guida")){
                //Rimuove l'occhio per la visibilità
                itemVisibilityTruePercorso.setVisibility(View.INVISIBLE);
                itemVisibilityFalsePercorso.setVisibility(View.INVISIBLE);
            }

        }else{
            itemFavouriteBorder.setVisibility(View.INVISIBLE);
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
    public static final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
        public ImageView itemEliminaPercorso;

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
            itemVisibilityTruePercorso = (ImageView) itemView.findViewById(R.id.itemVisibilityTruePercorso);
            itemVisibilityFalsePercorso = (ImageView) itemView.findViewById(R.id.itemVisibilityFalsePercorso);
            itemFavouriteBorder = (ImageView) itemView.findViewById(R.id.itemFavouriteBorder);
            itemFavourite = (ImageView) itemView.findViewById(R.id.itemFavourite);
            itemEliminaPercorso = (ImageView) itemView.findViewById(R.id.itemEliminaPercorso);

            // Clicco la Card per percorso per visualizzarne i Dettagli
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean boolean_public=false;
                    if(pubblico.getText().toString().equals("true")){
                        boolean_public=true;
                    }
                    //Log.e("Percorso cliccato", ""+getAdapterPosition()+""+id.getText().toString());
                    Bundle b = new Bundle();

                    CardPercorso percorsoSelezionato=new CardPercorso(id.getText().toString(), nome.getText().toString(),
                            idSitoAssociato.getText().toString(), nomeSitoAssociato.getText().toString(),
                            cittaSitoAssociato.getText().toString(), descrizione.getText().toString(), boolean_public);


                    Intent visualizzaPercorso = new Intent((HomePage)itemView.getContext(), VisualizzaPercorso.class);
                    b.putSerializable("percorso", percorsoSelezionato);
                    visualizzaPercorso.putExtras(b);
                    ((HomePage)itemView.getContext()).startActivity(visualizzaPercorso);

                }
            });

        }

        @Override
        public void onClick(View v) {

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


    /***
     * Ottiene il Percorso cercando per l'id, poi a seconda dell'azione lo aggiunge o rimuove dai preferiti
     *
     * @param idPercorso: parametro su cui effettuare la ricerca (Sarà un id)
     * @param azione: azione da eseguire (Aggiungere o Rimuovere)
     */
    public void managePercorsoDaScegliereNeiPreferiti(String idPercorso, String azione) {

        final Percorso[] percorsoDeiPreferiti = {new Percorso()};
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        recentPostsQuery = myRef.child("Percorsi").orderByChild("id").equalTo(idPercorso);     //SELECT * FROM Percorsi WHERE id LIKE "xyz"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    percorsoDeiPreferiti[0] = snapshot.getValue(Percorso.class);

                }


                // Ottengo le opere per quel percorso
                Query secondRecentPostsQuery = myRef.child("Percorsi/"+idPercorso+"/OpereScelte");
                secondRecentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        percorsoDeiPreferiti[0].setOpereScelte((ArrayList<Opera>)snapshot.getValue());

                        // Aggiunge il Percorso ai Preferiti dell'utente corrente
                        if(azione.equals("Aggiungere")){
                            percorsoDeiPreferiti[0].setIdUtenteSelezionatoPercorsoTraPreferiti(BasicMethod.getUtente().getUid());
                            percorsoDeiPreferiti[0].setId(percorsoDeiPreferiti[0].getId()+"_"+BasicMethod.getUtente().getUid());

                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                            DatabaseReference myRef;

                            myRef = database.getReference("/PercorsiPreferiti/"+percorsoDeiPreferiti[0].getId());

                            myRef.setValue(percorsoDeiPreferiti[0]);
                        }

                        // Rimuove il Percorso ai Preferiti dell'utente corrente
                        if(azione.equals("Rimuovere")){

                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                            DatabaseReference myRef;

                            myRef = database.getReference("/PercorsiPreferiti/"+percorsoDeiPreferiti[0].getId()+"_"+BasicMethod.getUtente().getUid());

                            myRef.removeValue();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }
    /** FINE
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    /***
     * Ottiene il Percorso cercando per l'id (in PercorsiPreferiti), poi lo rimuove dai preferiti
     *
     * @param idPercorso: parametro su cui effettuare la ricerca (Sarà un id)
     * @param position: indice che indica la CardPercorso selezionata dalla listaPercorsi
     */
    public void rimuoviPercorsoDaiPreferiti(String idPercorso, int position) {

        final Percorso[] percorsoDeiPreferiti = {new Percorso()};
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        recentPostsQuery = myRef.child("PercorsiPreferiti").orderByChild("id").equalTo(idPercorso);     //SELECT * FROM Percorsi WHERE id LIKE "xyz"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    percorsoDeiPreferiti[0] = snapshot.getValue(Percorso.class);

                }


                // Ottengo le opere per quel percorso
                Query secondRecentPostsQuery = myRef.child("Percorsi/"+idPercorso+"/OpereScelte");
                secondRecentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        percorsoDeiPreferiti[0].setOpereScelte((ArrayList<Opera>)snapshot.getValue());

                        // Rimuove il Percorso dalla lista dei Preferiti
                        listaPercorsi.remove(position);

                        //Aggiorna la RecyclerView
                        RecyclerView rvCardsSiti = (RecyclerView) view.findViewById(R.id.recyclerViewPercorsi);
                        CardMioPercorsoAdapter adapter = new CardMioPercorsoAdapter(listaPercorsi, "Preferiti", view, context);
                        rvCardsSiti.setAdapter(adapter);
                        rvCardsSiti.setLayoutManager(new LinearLayoutManager(context));

                        // Rimuove il Percorso dal DB
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                        DatabaseReference myRef;

                        myRef = database.getReference("/PercorsiPreferiti/"+percorsoDeiPreferiti[0].getId());

                        myRef.removeValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }
    /** FINE
     * ------------------------------------------------------------------------------------------------------------------------------------------------------------------ */

    /***
     * Elimina un Percorso scelto dal DB
     *
     * @param idPercorso:
     * @param position: indice che indica la CardPercorso selezionata dalla listaPercorsi
     */
    private void eliminaPercorso(String idPercorso, int position) {

        final Percorso[] percorsoDaEliminare = {new Percorso()};
        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        recentPostsQuery = myRef.child("Percorsi").orderByChild("id").equalTo(idPercorso);     //SELECT * FROM Percorsi WHERE id LIKE "xyz"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Salva l'oggetto restituito in una lista di oggetti dello stesso tipo
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    percorsoDaEliminare[0] = snapshot.getValue(Percorso.class);

                }

                // Rimuove il Percorso dalla lista
                listaPercorsi.remove(position);

                //Aggiorna la RecyclerView
                RecyclerView rvCardsSiti = (RecyclerView) view.findViewById(R.id.recyclerViewPercorsi);
                CardMioPercorsoAdapter adapter = new CardMioPercorsoAdapter(listaPercorsi, "MieiPercorsi", view, context);
                rvCardsSiti.setAdapter(adapter);
                rvCardsSiti.setLayoutManager(new LinearLayoutManager(context));

                // Rimuove il Percorso dal DB
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference myRef;

                myRef = database.getReference("/Percorsi/"+percorsoDaEliminare[0].getId());

                myRef.removeValue();

                Toast.makeText((activity).getApplicationContext(), R.string.percorsoEliminato, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }

    /***
     * Imposta l'ImageView del cuore a Pieno se il Percorso è presente nei preferiti dell'utente corrente
     *
     * @param idPercorso
     * @param idUtente
     * @param itemFavouriteBorder: ImageView del cuore vuoto
     * @param itemFavourite: ImageView del cuore pieno
     */
    public void setCuorePienoSePercorsoPresenteNeiPreferiti(String idPercorso, String idUtente, ImageView itemFavouriteBorder, ImageView itemFavourite){

        Query recentPostsQuery;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        recentPostsQuery = myRef.child("PercorsiPreferiti").orderByChild("id").equalTo(idPercorso+"_"+BasicMethod.getUtente().getUid());
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshotCuore : snapshot.getChildren()){

                    if(snapshotCuore.getValue(Percorso.class).getIdUtenteSelezionatoPercorsoTraPreferiti().equals(idUtente)){
                        itemFavouriteBorder.setVisibility(View.INVISIBLE);
                        itemFavourite.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
