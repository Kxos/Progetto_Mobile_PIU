package com.uniba.capitool.fragments.fragmentsAggiungiPercorso;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.uniba.capitool.activities.AggiungiPercorso;
import com.uniba.capitool.classes.Opera;
import com.uniba.capitool.classes.Percorso;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.AllZona;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.ItemOperaZona;
import com.uniba.capitool.fragments.fragmentVisualizzaZoneSito.MainRecyclerAdapterVisualizzaPercorso;

import java.util.ArrayList;
import java.util.List;

public class FragmentPreviewPercorso extends Fragment {

    static Percorso percorso = new Percorso();
    static ArrayList<Opera> listaOpere = new ArrayList<>();
    private Toolbar toolbar;
    ImageView fotoSito;
    TextView nomeSito;
    TextView descrizionePercorso;
    List<AllZona> zoneSito;
    RecyclerView mainZoneRecycler;
    MainRecyclerAdapterVisualizzaPercorso mainRecyclerAdapter;
    List<AllZona> allZoneList;

    public FragmentPreviewPercorso() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preview_percorso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView fotoSito=view.findViewById(R.id.fotoSito);

        // Leggo dal file SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if(sharedPreferences!=null) {

            Bundle args = getArguments();

            percorso = (Percorso) args.getSerializable("nuovoPercorso");
            listaOpere = (ArrayList<Opera>) args.getSerializable("listaOpereNuovoPercorso");

            toolbar = ((AggiungiPercorso)getActivity()).getToolbar();
            toolbar.setTitle("Preview");

            TextView nomeSitoAssociato = view.findViewById(R.id.nomeMuseo);
            nomeSitoAssociato.setText(percorso.getNomeSitoAssociato());

            TextView descrizionePercorso = view.findViewById(R.id.descrizionePercorso);
            descrizionePercorso.setText(percorso.getDescrizione());

            setImmagineSitoFromDB(percorso.getIdSitoAssociato(), getActivity(), fotoSito);

            setRecyclerView(view);

            Button bottoneIndietro = view.findViewById(R.id.buttonIndietroPreview);
            bottoneIndietro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentDatiPercorso fragmentDatiPercorso = new FragmentDatiPercorso();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("nuovoPercorso",  percorso);
                    bundle.putSerializable("listaOpereRicevuteDaPreview",  listaOpere);
                    fragmentDatiPercorso.setArguments(bundle);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerRicercaSiti, fragmentDatiPercorso );
                    fragmentTransaction.commit();
                }
            });

            Button bottoneSalva = view.findViewById(R.id.buttonSalva);
            bottoneSalva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("/");

                    myRef = database.getReference("/Percorsi/"+percorso.getId());
                    myRef.setValue(percorso);

                    myRef = database.getReference("/Percorsi/"+percorso.getId()+"/OpereScelte");
                    myRef.setValue(listaOpere);

                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.toastPercorsoSalvato), Toast.LENGTH_SHORT).show();

                    // Ritorna ad "I Miei Percorsi"
                    getActivity().onBackPressed();
                }
            });

        }

    }

    private void setRecyclerView(View view) {

        /***
         * Le opere non sono salvate per zona, quindi in qualche modo le devo ordinare.
         * In questo for mi recupero ogni id di ogni zona, così saprò quante zone ci sono
         * e quali sono
         * Salvo tutto nell'array indiciZone
         */
        String idZona=listaOpere.get(0).getIdZona();
        List<String> indiciZone=new ArrayList<>();
        indiciZone.add(listaOpere.get(0).getIdZona());

        for(int i=0; i<listaOpere.size(); i++){
            if(!idZona.equals(listaOpere.get(i).getIdZona())){
                idZona=listaOpere.get(i).getIdZona();
                indiciZone.add(listaOpere.get(i).getIdZona());
            }
        }

        /***
         * Per creare la doppia recycle view, devo salvare le opere e le zone in una lista
         * List<AllZona> allZoneList
         * Nel for creo una lista List<ItemOperaZona> listaOpereZona per ogni zona che ho trovato con il for precedente
         * Scorro la lista listaOpere che contiene tutte le opere del percorso in modo sparso e le salvo in listaOpereZona
         * che conterrà le opere solo di quella zona
         * Alla fine del ciclo salvo la lista creata (listaOpereZona) in allZoneList
         * Quindi ora in allZoneList in posizione i-esima ci saranno tutte le opere relative alla zona i                 *
         */
        List<AllZona> allZoneList=new ArrayList<>();

        for(int i=0; i<indiciZone.size(); i++){
            List<ItemOperaZona> listaOpereZona=new ArrayList<>();
            for(int j=0; j<listaOpere.size(); j++){
                if(listaOpere.get(j).getIdZona().equals(indiciZone.get(i))){
                    listaOpereZona.add(new ItemOperaZona(listaOpere.get(j).getId(), listaOpere.get(j).getTitolo(), listaOpere.get(j).getIdZona(), listaOpere.get(j).getIdFoto()));
                }
            }
            allZoneList.add(new AllZona(indiciZone.get(i), indiciZone.get(i), listaOpereZona));
        }

        /***
         * Pero ogni oggetto della lista allZoneList, vado a leggere da DB il suo "nomeZona"
         * @param allZoneList
         */
        for(int i=0; i<allZoneList.size(); i++){

            int finalI = i;
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://capitool-6a9ea-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("/");

            Query recentPostsQuery = myRef.child("Siti").child(percorso.getIdSitoAssociato()).child("Zone").child(allZoneList.get(i).getId()).child("nome");
            recentPostsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                    allZoneList.get(finalI).setNomeZona((String) datasnapshot.getValue());

                    setZoneRecycler(allZoneList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    private void setZoneRecycler(List<AllZona> allZoneList) {

        mainZoneRecycler = getActivity().findViewById(R.id.recyclerPercorso);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity());
        mainZoneRecycler.setLayoutManager(layoutManager);

        mainRecyclerAdapter=new MainRecyclerAdapterVisualizzaPercorso(getActivity(), allZoneList);
        mainZoneRecycler.setAdapter(mainRecyclerAdapter);

        zoneSito=allZoneList;

    }


    public static Percorso getPercorso() {
        return percorso;
    }

    public static ArrayList<Opera> getListaOpere() {
        return listaOpere;
    }

    public void setImmagineSitoFromDB(String idSito, Context context, ImageView immagineSito){

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/fotoSiti/"+idSito);

        /**
         * Scarica il "DownloadURL" che ci serve per leggere l'immagine dal DB e metterla in una ImageView
         * */
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(context).load(downloadUrl).into(immagineSito);
            }

        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Glide.with(context).load(R.drawable.image_not_found).into(immagineSito);
                    }
                });

    }
}