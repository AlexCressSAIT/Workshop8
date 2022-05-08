/*
Author: Alex Cress

Main Activity of the application. Shows a list of Agents and handles delegation of
add/edit operations by passing to the appropriate Activity.
 */
package com.group7.workshop8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * This Activity displays a selectable list of Agents loaded from a web API.
 * Uses DetailActivity to handle all CRUD operations.
 */
public class MainActivity extends AppCompatActivity implements Consumer{
    //Entities in the xml layout
    ListView lvAgents;
    Button btnAdd;
    SearchView svAgent;

    //Adapter for lvAgents
    ArrayAdapter<Agent> adapter;

    //Saved cache of Agent data, used for filter operations
    ArrayList<Agent> cachedAgents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize entity references
        btnAdd = findViewById(R.id.btnAdd);
        lvAgents = findViewById(R.id.lvAgents);
        svAgent = findViewById(R.id.svAgent);

        //Adapter for lvAgents
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);

        //Create listener for search box
        svAgent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.clear();
                //Show all Agents if there is no search query
                if(s.isEmpty()){
                    adapter.addAll(cachedAgents);
                }
                //Filter Agents by given query string
                else{
                    ArrayList<Agent> filteredList = new ArrayList<>();
                    for(Agent a : cachedAgents){
                        if(a.getAgtLastName().toLowerCase(Locale.ROOT).startsWith(s.toLowerCase(Locale.ROOT))){
                            filteredList.add(a);
                        }
                    }
                    adapter.addAll(filteredList);
                }
                lvAgents.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Will show all Agents if focus on the input field is lost and there is no query string
                if(s.isEmpty()){
                    adapter.clear();
                    adapter.addAll(cachedAgents);
                    lvAgents.setAdapter(adapter);
                }
                return true;
            }
        });

        //Starts DetailActivity with selected Agent
        lvAgents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("agent", (Agent) lvAgents.getAdapter().getItem(i));
                intent.putExtra("mode", "view");
                startActivity(intent);
            }
        });

        //Starts DetailActivity with blank form for a new Agent
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("mode", "add");
                startActivity(intent);
            }
        });

        //Start thread to get Agent data
        AgentData.getAgents(this, this);
    }

    //Will be called by the system when data is available
    //TODO: What if there is lag or the server is down? There should be some sort of loading screen and error handling
    @Override
    public void accept(Object o) {
        //Cast to appropriate type
        ArrayList<Agent> list = (ArrayList<Agent>) o;
        //Update the cache
        cachedAgents = list;
        //Set adapter for ListView
        ArrayAdapter<Agent> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, list);
        lvAgents.setAdapter(adapter);
    }

    //Stub for interface
    @Override
    public Consumer andThen(Consumer after) {
        return Consumer.super.andThen(after);
    }

}