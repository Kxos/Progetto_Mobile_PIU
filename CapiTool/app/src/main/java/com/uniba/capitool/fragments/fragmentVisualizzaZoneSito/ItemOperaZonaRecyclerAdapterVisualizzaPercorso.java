package com.uniba.capitool.fragments.fragmentVisualizzaZoneSito;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.uniba.capitool.classes.Visitatore;

import java.util.ArrayList;
import java.util.List;

public class ItemOperaZonaRecyclerAdapterVisualizzaPercorso extends RecyclerView.Adapter<ItemOperaZonaRecyclerAdapterVisualizzaPercorso.ItemOperaZonaViewHolder> {

    private Context context;
    private List<ItemOperaZona> listaOpereZona;
    private OnOperaListener mOnOperaListener;

    public ItemOperaZonaRecyclerAdapterVisualizzaPercorso(Context context, List<ItemOperaZona> listaOpereZona, OnOperaListener onOperaListener) {
        this.context = context;
        this.listaOpereZona = listaOpereZona;
        this.mOnOperaListener = onOperaListener;
    }

    @NonNull
    @Override
    public ItemOperaZonaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemOperaZonaViewHolder(LayoutInflater.from(context).inflate(R.layout.item_opera_zona, parent, false), mOnOperaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemOperaZonaViewHolder holder, int position) {

        setImmagineOperaFromDB(listaOpereZona.get(position).getIdFoto(), holder.itemView.getContext(), holder.fotoOpera);
        holder.titoloOpera.setText(listaOpereZona.get(position).getTitolo());

        //setto in una TextVew invisibile l'id della Zona in modo da avere un riferimento quando clicco sull'opera (brutto ma efficace)
        holder.idZona.setText(listaOpereZona.get(position).getIdZona());

    }

    /***
     *Se listaOpereZona è null significa che quella zona è stata creata ma che non ha nessun opera ancora al suo interno
     * Per gestire la situazione creo un if che restituisce 0 in caso di lista vuota
     * @return
     */
    @Override
    public int getItemCount() {

        if (listaOpereZona==null){
            return 0;
        }else{
            return listaOpereZona.size();
        }

    }

    public static final class ItemOperaZonaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

       ImageView fotoOpera;
       TextView titoloOpera;
       TextView idZona;
       OnOperaListener onOperaListener;

        public ItemOperaZonaViewHolder(@NonNull View itemView, OnOperaListener onOperaListener) {
            super(itemView);
            fotoOpera=itemView.findViewById(R.id.item_immagineOpera);
            titoloOpera=itemView.findViewById(R.id.item_nomeOpera);
            idZona =itemView.findViewById(R.id.item_idZona);

            this.onOperaListener=onOperaListener;
            itemView.setOnClickListener(this);
        }

        /***
         * Quando avviene il clic di una item Opera passo la posizione dell'opera nella recycler view e l'id della Zona (salvata in una TextView non visibile)
         * @param v
         */
        @Override
        public void onClick(View v) {
            onOperaListener.onOperaClick(getAdapterPosition(), idZona.getText().toString(), v);
        }
    }

    public void setDataNascitaUtente(String email){

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("/");

        Query recentPostsQuery = myRef.child("Utenti").orderByChild("email").equalTo(email);     //SELECT * FROM Utenti WHERE ruolo="visitatore"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Visitatore> visitatori=new ArrayList<>();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){        //un for che legge tutti i valori trovati dalla query, anche se è 1 solo
                    Visitatore visitatore=snapshot.getValue(Visitatore.class);  //così facendo si associa un l'oggetto della query nell'oggetto della classe
                    visitatori.add(visitatore);                                 //se ci sarà più di un risultato nella query creiamo una lista di oggetti per gestirli da codice
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });
    }

    public void setImmagineOperaFromDB(String idOpera, Context context, ImageView imageViewOpera){

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/fotoOpere/"+idOpera);

        /**
         * Scarica il "DownloadURL" che ci serve per leggere l'immagine dal DB e metterla in una ImageView
         * */
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(context).load(downloadUrl).into(imageViewOpera);
            }

        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Glide.with(context).load(R.drawable.image_not_found).into(imageViewOpera);
            }
        });

    }

    public interface OnOperaListener{
        void onOperaClick(int posizioneOpera, String idZona, View v);
    }

}
