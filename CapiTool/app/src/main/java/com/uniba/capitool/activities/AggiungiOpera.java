package com.uniba.capitool.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;
import com.uniba.capitool.R;
import com.uniba.capitool.classes.Opera;
import com.uniba.capitool.classes.SitoCulturale;
import com.uniba.capitool.classes.Utente;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.ItemOperaZona;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AggiungiOpera extends AppCompatActivity {

    private static final int SELECT_IMAGE_CODE = 1;
    private Uri image;
    ImageView fotoOpera;
    TextInputEditText descrizioneOpera, titoloOpera;
    Utente utente;
    SitoCulturale sito;
    String idZona;
    String nomeZona;
    AllZona allZone;
    ProgressBar progressBar;
    Button btaggiungiOpera;
    int nextIdOpera;

    String TAG = "GenerateQRCode";
    EditText edtValue;
    ImageView qrImage;
    Button start, save, convert;
    String inputValue;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    String qrCodeString;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 284;
    Uri percorsoImmagine = null;
    String chiave = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_opera);

        fotoOpera = findViewById(R.id.addOpera_foto);
        descrizioneOpera = findViewById(R.id.descrizioneOpera);
        titoloOpera = findViewById(R.id.titoloNomeModificaOpera);
        btaggiungiOpera = findViewById(R.id.bt_Aggiungi_Opera);
        qrImage = findViewById(R.id.qrCodeImage);

        Bundle dati = getIntent().getExtras();

        if(dati!=null){

            idZona = dati.getString("idZona");
            sito = (SitoCulturale) dati.getSerializable("sito");
            utente = (Utente) dati.getSerializable("utente");
            idZona = dati.getString("idZona");
            nomeZona=  dati.getString("nomeZona");
            allZone= (AllZona) dati.getSerializable("allZona");

        }else{
            Log.e("Visulizza Zone Sito", "Nessun Bundle trovato");
        }

        Toolbar toolbar = findViewById(R.id.toolbarAddOpera);
        toolbar.setTitle(getString(R.string.addOperaToolbar) + nomeZona);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AggiungiOpera.super.onBackPressed();
            }
        });

        fotoOpera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "title"), SELECT_IMAGE_CODE);
            }
        });

        btaggiungiOpera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean erroreDatiCompilati=false;

                if(titoloOpera.getText().toString().equals("")){
                    titoloOpera.setError(getString(R.string.errorOperaTitle));
                    erroreDatiCompilati=true;
                }

                if(descrizioneOpera.getText().toString().equals("")){
                    descrizioneOpera.setError(getString(R.string.errorOperaDescription));
                    erroreDatiCompilati=true;
                }

                if(erroreDatiCompilati==false){

                    saveChanges();
                   // onBackPressed();
                }

            }
        });

        leggiOpere();
    }

    private void saveChanges() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        } else {
            // Permission has already been granted

            try {

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference myRef= database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera);

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                String key = myRef.push().getKey();

                generateQRCode();

                chiave = key;

                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Title", null);
                percorsoImmagine = Uri.parse(path);

                StorageReference qrReference = FirebaseStorage.getInstance().getReference().child("QRCodeOpere").child(chiave);

                qrReference.putFile(percorsoImmagine).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    }
                });

                Opera opera=new Opera(""+nextIdOpera, titoloOpera.getText().toString(), descrizioneOpera.getText().toString(), null, key);
                myRef.setValue(opera);

                StorageReference fileReference= FirebaseStorage.getInstance().getReference().child("fotoOpere").child(key);

               /* final ProgressDialog pd = new ProgressDialog(AggiungiOpera.this);
                pd.setMessage("Caricamento");
                pd.show();*/

//                           //pd.dismiss();
//                           Toast.makeText(AggiungiOpera.this, getString(R.string.correctEnterOpera), Toast.LENGTH_LONG).show();


                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        try {
                            fileReference.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    //pd.dismiss();
                                    Toast.makeText(AggiungiOpera.this, "Opera aggiunta correttamente", Toast.LENGTH_LONG).show();

                                    //Intent eliminaOpere = new Intent(AggiungiOpera.this, VisualizzaZoneSito.class);

//                           Bundle datiZona = new Bundle();
//                           datiZona.putSerializable("sito",sito);
//                           datiZona.putSerializable("utente", utente);
//                           eliminaOpere.putExtras(datiZona);
//                           startActivity(eliminaOpere);
                                    Intent backVisualizzaZona= new Intent(AggiungiOpera.this, VisualizzaZona.class);
                                    Bundle dati = new Bundle();
                                    dati.putSerializable("sito", sito);
                                    dati.putSerializable("utente", utente);
                                    dati.putString("nomeZona", nomeZona);
                                    dati.putString("idZona", allZone.getId());

                                    List<ItemOperaZona> newOpereList= new ArrayList<>();
                                    if(nextIdOpera==1){
                                        newOpereList.add(new ItemOperaZona(""+nextIdOpera, titoloOpera.getText().toString(), descrizioneOpera.getText().toString(), null, key));
                                    }else{

                                        newOpereList=allZone.getListaOpereZona();
                                        newOpereList.add(new ItemOperaZona(""+nextIdOpera, titoloOpera.getText().toString(), descrizioneOpera.getText().toString(), null, key));
                                    }

                                    allZone.setListaOpereZona(newOpereList);
                                    dati.putSerializable("allZone", allZone);
                                    backVisualizzaZona.putExtras(dati);
                                    finish();
                                    startActivity(backVisualizzaZona);
                                    Log.e("********************************************nextId",""+nextIdOpera);
                                }

                            });
                            Log.e("********************************************nextId",""+nextIdOpera);
                        }catch (Exception e){

                            Toast.makeText(AggiungiOpera.this, "Opera aggiunta correttamente", Toast.LENGTH_LONG).show();

                            Intent backVisualizzaZona= new Intent(AggiungiOpera.this, VisualizzaZona.class);
                            Bundle dati = new Bundle();
                            dati.putSerializable("sito", sito);
                            dati.putSerializable("utente", utente);
                            dati.putString("nomeZona", nomeZona);
                            dati.putString("idZona", allZone.getId());

                            List<ItemOperaZona> newOpereList= new ArrayList<>();
                            if(nextIdOpera==0){
                                newOpereList.add(new ItemOperaZona(""+nextIdOpera, titoloOpera.getText().toString(), descrizioneOpera.getText().toString(), null, key));
                            }else{

                                newOpereList=allZone.getListaOpereZona();
                                newOpereList.add(new ItemOperaZona(""+nextIdOpera, titoloOpera.getText().toString(), descrizioneOpera.getText().toString(), null, key));
                            }

                            allZone.setListaOpereZona(newOpereList);
                            dati.putSerializable("allZone", allZone);
                            backVisualizzaZona.putExtras(dati);
                            finish();
                            startActivity(backVisualizzaZona);
                            Log.e("********************************************nextId",""+nextIdOpera);
                        }




//                myRef =database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera + "/titolo");
//                myRef.setValue(titoloOpera.getText().toString());
//
//                myRef =database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera + "/id");
//                myRef.setValue(""+nextIdOpera);
//
//                myRef = database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera + "/descrizione");
//                myRef.setValue(descrizioneOpera.getText().toString());
//
//                myRef = database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera + "/idFoto");
//                myRef.setValue(key);





//                myRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        AggiungiOpera.super.onBackPressed();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && data!=null){
            Log.e("***********************************************************","");
            image=data.getData();
            if(image!=null){
                fotoOpera.setImageURI(image);

            }
        }
    }


   private void leggiOpere(){

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
        //TODO mettere l'id del sito nel path e togliere 1
        DatabaseReference myRef = database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/");

        Query recentPostsQuery = myRef.limitToLast(1);//.orderByChild("id");    //SELECT * FROM Utenti WHERE ruolo="visitatore"
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Opera> opere=new ArrayList<>();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){        //un for che legge tutti i risultati della query e li formatta esattamente come se fossero oggetti di classe Zona
                    try{
                        Opera opera = snapshot.getValue(Opera.class);
                        opere.add(opera);

                        Log.e("Opera id",""+opera.getId());
                    }catch (Exception e){

                    }
                }

                Log.e("***********************************************size opere",""+opere.size());

               if(opere.size()==0){
                   salvaIndice(-1);
               }else{
                   salvaIndice(Integer.parseInt(opere.get(opere.size()-1).getId()));

               }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("QuertActivity", "loadPost:onCancelled", error.toException());
            }
        });

    }

    private void salvaIndice(int idOperaPrecedente) {
        Log.e("nextIdSalvaIndice",""+idOperaPrecedente);
        nextIdOpera=idOperaPrecedente+1;
    }

//    @Override
//    public void onBackPressed() {
//        try{
//            super.onBackPressed();
//        } catch (NullPointerException nullPointerException){
//            System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
//        }
//    }

    private Uri generateQRCode(){

        inputValue = idZona + nextIdOpera;
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
                qrImage.setImageBitmap(bitmap);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            } catch (WriterException e) {
                Log.v(TAG, e.toString());
            }
        } else {
            edtValue.setError("Required");
        }

        return percorsoImmagine;
    }
    public String bitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted!

                    try {

                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                        DatabaseReference myRef= database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera);

                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();
                        String key = myRef.push().getKey();

                        generateQRCode();

                        chiave = key;

                        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Title", null);
                        percorsoImmagine = Uri.parse(path);

                        StorageReference qrReference = FirebaseStorage.getInstance().getReference().child("QRCodeOpere").child(chiave);

                        qrReference.putFile(percorsoImmagine).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            }
                        });

                        Opera opera=new Opera(""+nextIdOpera, titoloOpera.getText().toString(), descrizioneOpera.getText().toString(), null, key);
                        myRef.setValue(opera);

                        StorageReference fileReference= FirebaseStorage.getInstance().getReference().child("fotoOpere").child(key);

               /* final ProgressDialog pd = new ProgressDialog(AggiungiOpera.this);
                pd.setMessage("Caricamento");
                pd.show();*/

//                           //pd.dismiss();
//                           Toast.makeText(AggiungiOpera.this, getString(R.string.correctEnterOpera), Toast.LENGTH_LONG).show();


                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                try {
                                    fileReference.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                            //pd.dismiss();
                                            Toast.makeText(AggiungiOpera.this, "Opera aggiunta correttamente", Toast.LENGTH_LONG).show();

                                            //Intent eliminaOpere = new Intent(AggiungiOpera.this, VisualizzaZoneSito.class);

//                           Bundle datiZona = new Bundle();
//                           datiZona.putSerializable("sito",sito);
//                           datiZona.putSerializable("utente", utente);
//                           eliminaOpere.putExtras(datiZona);
//                           startActivity(eliminaOpere);
                                            Intent backVisualizzaZona= new Intent(AggiungiOpera.this, VisualizzaZona.class);
                                            Bundle dati = new Bundle();
                                            dati.putSerializable("sito", sito);
                                            dati.putSerializable("utente", utente);
                                            dati.putString("nomeZona", nomeZona);
                                            dati.putString("idZona", allZone.getId());

                                            List<ItemOperaZona> newOpereList= new ArrayList<>();
                                            if(nextIdOpera==1){
                                                newOpereList.add(new ItemOperaZona(""+nextIdOpera, titoloOpera.getText().toString(), descrizioneOpera.getText().toString(), null, key));
                                            }else{

                                                newOpereList=allZone.getListaOpereZona();
                                                newOpereList.add(new ItemOperaZona(""+nextIdOpera, titoloOpera.getText().toString(), descrizioneOpera.getText().toString(), null, key));
                                            }

                                            allZone.setListaOpereZona(newOpereList);
                                            dati.putSerializable("allZone", allZone);
                                            backVisualizzaZona.putExtras(dati);
                                            finish();
                                            startActivity(backVisualizzaZona);
                                            Log.e("********************************************nextId",""+nextIdOpera);
                                        }

                                    });
                                    Log.e("********************************************nextId",""+nextIdOpera);
                                }catch (Exception e){

                                    Toast.makeText(AggiungiOpera.this, "Opera aggiunta correttamente", Toast.LENGTH_LONG).show();

                                    Intent backVisualizzaZona= new Intent(AggiungiOpera.this, VisualizzaZona.class);
                                    Bundle dati = new Bundle();
                                    dati.putSerializable("sito", sito);
                                    dati.putSerializable("utente", utente);
                                    dati.putString("nomeZona", nomeZona);
                                    dati.putString("idZona", allZone.getId());

                                    List<ItemOperaZona> newOpereList= new ArrayList<>();
                                    if(nextIdOpera==0){
                                        newOpereList.add(new ItemOperaZona(""+nextIdOpera, titoloOpera.getText().toString(), descrizioneOpera.getText().toString(), null, key));
                                    }else{

                                        newOpereList=allZone.getListaOpereZona();
                                        newOpereList.add(new ItemOperaZona(""+nextIdOpera, titoloOpera.getText().toString(), descrizioneOpera.getText().toString(), null, key));
                                    }

                                    allZone.setListaOpereZona(newOpereList);
                                    dati.putSerializable("allZone", allZone);
                                    backVisualizzaZona.putExtras(dati);
                                    finish();
                                    startActivity(backVisualizzaZona);
                                    Log.e("********************************************nextId",""+nextIdOpera);
                                }




//                myRef =database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera + "/titolo");
//                myRef.setValue(titoloOpera.getText().toString());
//
//                myRef =database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera + "/id");
//                myRef.setValue(""+nextIdOpera);
//
//                myRef = database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera + "/descrizione");
//                myRef.setValue(descrizioneOpera.getText().toString());
//
//                myRef = database.getReference("Siti/"+ sito.getId() + "/Zone/" + idZona + "/Opere/" + nextIdOpera + "/idFoto");
//                myRef.setValue(key);





//                myRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        AggiungiOpera.super.onBackPressed();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    // permission denied!

                }
                return;
            }

        }
    }

}