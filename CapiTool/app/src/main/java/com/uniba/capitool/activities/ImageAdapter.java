package com.uniba.capitool.activities;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uniba.capitool.R;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.ItemOperaZona;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    List<ItemOperaZona> listaOpereZona;
    Context mContext;

    public ImageAdapter(List<ItemOperaZona> listaOpereZona, Context mContext) {
        this.listaOpereZona=listaOpereZona;
        this.mContext=mContext;
    }

    LayoutInflater inflater;

    @Override
    public int getCount() {

        if (listaOpereZona==null){
            return 0;
        }else{
            return listaOpereZona.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(listaOpereZona.get(position).getIdOpera());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView = (ImageView) convertView;
//        TextView textView = (TextView) convertView;
//
//        if(imageView == null){
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(350, 450));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
////           textView = new TextView(mContext);
////            textView.setText("prova");
//        }
//
//        imageView.setImageResource(R.drawable.add_icon);


        if(inflater==null){
            inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_opera_zona, null);
        }

        ImageView fotoOpera=convertView.findViewById(R.id.item_immagineOpera);
        TextView titolo=convertView.findViewById(R.id.item_nomeOpera);


        titolo.setText(listaOpereZona.get(position).getTitolo());
        setImmagineOperaFromDB(listaOpereZona.get(position).getIdOpera(), mContext, fotoOpera);

        return convertView;
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
}
