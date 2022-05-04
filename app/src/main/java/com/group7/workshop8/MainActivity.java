package com.group7.workshop8;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity implements Consumer {

    ListView lvAgents;
    Button btnAdd;
    ArrayAdapter<Agent> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        lvAgents = findViewById(R.id.lvAgents);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);

        lvAgents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("agent", (Agent) lvAgents.getAdapter().getItem(i));
                intent.putExtra("mode", "view");
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("mode", "add");
                startActivity(intent);
            }
        });

        //Executors.newSingleThreadExecutor().execute(new AgentRetrieval());

        AgentData db = new AgentData();
        db.getAgents(this, this);
    }

    @Override
    public void accept(Object o) {
        ArrayList<Agent> list = (ArrayList<Agent>) o;
        ArrayAdapter<Agent> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, list);
        lvAgents.setAdapter(adapter);
    }

    @Override
    public Consumer andThen(Consumer after) {
        return Consumer.super.andThen(after);
    }
}