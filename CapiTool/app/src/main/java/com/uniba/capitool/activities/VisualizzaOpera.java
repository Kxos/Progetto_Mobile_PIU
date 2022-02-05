package com.uniba.capitool.activities;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.WriterException;
import com.uniba.capitool.R;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.ItemOperaZona;

import java.io.ByteArrayOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class VisualizzaOpera extends AppCompatActivity {

    RelativeLayout layoutQR;
    Uri downloadUrl;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_opera);

        ImageView immagine = findViewById(R.id.imageViewVisualizzaOpera);
        TextView testo = findViewById(R.id.textView12);
        TextView titoloOpera = findViewById(R.id.titoloNomeOpera);
        CardView qrCode = findViewById(R.id.qrCodeButton);
        ImageView closeQRlayout=findViewById(R.id.closeQRlayout);

        layoutQR=findViewById(R.id.layout_qrcode);
        ImageView immagineQRCODE=findViewById(R.id.immagineQRCODE);
        Button buttonDownload=findViewById(R.id.buttonDownload);

        Bundle dati = getIntent().getExtras();
        ImageView esci = findViewById(R.id.esci);
        esci.setClickable(true);

        ItemOperaZona opera = new ItemOperaZona();

        if(dati!=null){

            if(dati.getString("ActivityChiamante") != null){
                if(BasicMethod.getUtente().getRuolo().equals("curatore") && dati.getString("ActivityChiamante").equals("VisualizzaZone")){
                    qrCode.setVisibility(View.VISIBLE);
                }
            }

            opera = (ItemOperaZona) dati.getSerializable("opera");
            Glide.with(this).load(opera.getFoto()).into(immagine);
            titoloOpera.setText(opera.getTitolo());
            testo.setText(opera.getDescrizione());

            setImmagineOperaFromDB(opera.getIdFoto(), this, immagine);
        }else{

        }

        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ItemOperaZona finalOpera = opera;
        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO

                layoutQR.setVisibility(View.VISIBLE);


                String idQRcode= finalOpera.getIdFoto();
                setImmagineQRFromDB(idQRcode, getApplicationContext(), immagineQRCODE);

            }
        });

        layoutQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutQR.setVisibility(View.GONE);
            }
        });

        closeQRlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutQR.setVisibility(View.GONE);
            }
        });

        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean save;
                String result;
                String qrCodeContent=finalOpera.getIdZona()+finalOpera.getId();
                String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";

                try {
                    Bitmap bitmap= generaQR(finalOpera.getIdZona(), finalOpera.getId());
                    save = QRGSaver.save(savePath, qrCodeContent.trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
                    result = save ? "Image Saved" : "Image Not Saved";
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void setImmagineQRFromDB(String idQR, Context context, ImageView imageViewQR){

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/QRCodeOpere/"+idQR);

        /**
         * Scarica il "DownloadURL" che ci serve per leggere l'immagine dal DB e metterla in una ImageView
         * */
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(context).load(downloadUrl).into(imageViewQR);
                VisualizzaOpera.this.downloadUrl=downloadUrl;
            }

        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Glide.with(context).load(R.drawable.image_not_found).into(imageViewQR);
                    }
                });

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();



      //  overridePendingTransition(R.anim.slide_from_top, R.anim.slide_in_top);


        if(layoutQR.getVisibility()==View.VISIBLE){
            layoutQR.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }

    }


    public Bitmap generaQR(String idZona, String idOpera){
        Bitmap bitmap= null;

        String inputValue = idZona + idOpera;
        if (inputValue.length() > 0) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    inputValue, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            } catch (WriterException e) {
                Log.v(TAG, e.toString());
            }
        }
            return bitmap;
    }
}