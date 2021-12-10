package com.uniba.capitool.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.uniba.capitool.R;

public class AggiungiNomeSito extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_nome_sito);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Aggiungi Nome Sito");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}