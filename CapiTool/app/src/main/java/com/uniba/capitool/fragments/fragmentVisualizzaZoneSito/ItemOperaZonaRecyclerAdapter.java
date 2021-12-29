package com.uniba.capitool.fragments.fragmentVisualizzaZoneSito;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniba.capitool.R;

import java.util.List;

public class ItemOperaZonaRecyclerAdapter extends RecyclerView.Adapter<ItemOperaZonaRecyclerAdapter.ItemOperaZonaViewHolder> {

    private Context context;
    private List<ItemOperaZona> listaOpereZona;
    private OnOperaListener mOnOperaListener;

    public ItemOperaZonaRecyclerAdapter(Context context, List<ItemOperaZona> listaOpereZona, OnOperaListener onOperaListener) {
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

        //holder.fotoOpera.setImageURI(listaOpereZona.get(position).getFoto());
//        Glide.with(holder.itemView.getContext()).load(listaOpereZona.get(position).getFoto()).into(holder.fotoOpera);

        setImmagineOperaFromDB(listaOpereZona.get(position).getId(), holder.itemView.getContext(), holder.fotoOpera);
        holder.titoloOpera.setText(listaOpereZona.get(position).getTitolo());

     /*   holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return listaOpereZona.size();
    }

    public static final class ItemOperaZonaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

       ImageView fotoOpera;
       TextView titoloOpera;
       OnOperaListener onOperaListener;
        public ItemOperaZonaViewHolder(@NonNull View itemView, OnOperaListener onOperaListener) {
            super(itemView);
            fotoOpera=itemView.findViewById(R.id.item_immagineOpera);
            titoloOpera=itemView.findViewById(R.id.item_nomeOpera);

            this.onOperaListener=onOperaListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onOperaListener.onOperaClick(getAdapterPosition());
        }
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
                Glide.with(context).load(R.drawable.images).into(imageViewOpera);
            }
        });

    }

    public interface OnOperaListener{
        void onOperaClick(int position);
    }
}
