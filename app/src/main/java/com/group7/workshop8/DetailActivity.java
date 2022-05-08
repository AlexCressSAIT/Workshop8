/*
Author: Alex Cress

Activity containing a form used for CRUD operation on Agent data
 */
package com.group7.workshop8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This Activity displays a form that will operate differently based on the "mode" given (add or view).
 * An Intent with field "agent" and "mode" is required, and accepts an Agent object and String, respectively.
 * "view" mode: accepts an Agent and allows you to edit and delete an existing Agent.
 * "add" mode: displays an empty form and uses that data to create a new Agent.
 */
//TODO Create an Enum so that passing these string modes isn't so ambiguous
//TODO rename "add" and "view" as they are very poorly named
public class DetailActivity extends AppCompatActivity {
    //Entities in the xml layout
    EditText etAgentId, etFirstName, etMiddleInitial, etLastName, etPhone, etEmail, etPosition;
    Button btnEdit, btnSave, btnDelete, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Initialize entity references
        etAgentId = findViewById(R.id.etAgentId);
        etFirstName = findViewById(R.id.etFirstName);
        etMiddleInitial = findViewById(R.id.etMiddleInitial);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPosition = findViewById(R.id.etPosition);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnCancel = findViewById(R.id.btnCancel);

        //Get the intent and Agent data passed to this Activity
        Intent intent = getIntent();
        Agent agent = (Agent) intent.getSerializableExtra("agent");
        String mode = intent.getStringExtra("mode");

        //Default cancel functionality
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToMainActivity();
            }
        });

        //Initialize form for "add" mode
        if(mode.equals("add")){
            //Hide/disable entities as necessary
            etAgentId.setEnabled(false);
            etFirstName.setEnabled(true);
            etMiddleInitial.setEnabled(true);
            etLastName.setEnabled(true);
            etPhone.setEnabled(true);
            etEmail.setEnabled(true);
            etPosition.setEnabled(true);
            btnEdit.setVisibility(View.INVISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.VISIBLE);

            //Set handler to retrieve data from the form and save the Agent
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Create an Agent from data in fields
                    //TODO: Needs validation
                    Agent agent = new Agent(etFirstName.getText().toString(), etMiddleInitial.getText().toString(), etLastName.getText().toString(), etPhone.getText().toString(), etEmail.getText().toString(), etPosition.getText().toString());
                    AgentData.newAgent(agent);

                    //TODO: Make this toast actually check for success/failure
                    showToast("Insert Successful!");

                    //Automatically return to parent
                    redirectToMainActivity();
                }
            });

        //Initialize form for "view" mode
        }else if(mode.equals("view")){
            populateFields(agent);

            //Hide entities
            btnSave.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);

            //Show entities
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);

            //Disable all fields
            etAgentId.setEnabled(false);
            etFirstName.setEnabled(false);
            etMiddleInitial.setEnabled(false);
            etLastName.setEnabled(false);
            etPhone.setEnabled(false);
            etEmail.setEnabled(false);
            etPosition.setEnabled(false);



            //Enable/show fields and change buttons for editing
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Hide entities
                    btnEdit.setVisibility(View.INVISIBLE);
                    btnDelete.setVisibility(View.INVISIBLE);

                    //Show entities
                    btnSave.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);

                    //Allow user to edit fields
                    etAgentId.setEnabled(false); //Primary key cannot be changed
                    etFirstName.setEnabled(true);
                    etMiddleInitial.setEnabled(true);
                    etLastName.setEnabled(true);
                    etPhone.setEnabled(true);
                    etEmail.setEnabled(true);
                    etPosition.setEnabled(true);
                }
            });

            //Deletes the selected Agent
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Agent agent = getAgentDetails();
                    //TODO: needs validation
                    AgentData.deleteAgent(agent);
                    //TODO: Make this toast actually check for success/failure
                    showToast("Delete Agent ID: " + etAgentId.getText().toString() + " Successful!");
                    redirectToMainActivity();
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Agent agent = getAgentDetails();
                    //TODO: needs validation
                    AgentData.editAgent(agent);
                    //TODO: Make this toast actually check for success/failure
                    showToast("Update Agent ID: " + etAgentId.getText().toString() + " Successful!");
                    redirectToMainActivity();
                }
            });
        }
    }

    /**
     * Closes this Activity and starts MainActivity
     */
    private void redirectToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Gets the data from input fields and creates an Agent object.
     * @return an Agent built from input field data
     */
    private Agent getAgentDetails() {
        Agent agent = new Agent(Integer.parseInt(etAgentId.getText().toString()),
                etFirstName.getText().toString(),
                etMiddleInitial.getText().toString(),
                etLastName.getText().toString(),
                etPhone.getText().toString(),
                etEmail.getText().toString(),
                etPosition.getText().toString());
        return agent;
    }

    /**
     * Inserts given Agent data into fields
     * @param agent the Agent to show
     */
    private void populateFields(Agent agent){
        etAgentId.setText(agent.getAgentId() + "");
        etFirstName.setText(agent.getAgtFirstName());
        etMiddleInitial.setText(agent.getAgtMiddleInitial());
        etLastName.setText(agent.getAgtLastName());
        etPhone.setText(agent.getAgtBusPhone());
        etEmail.setText(agent.getAgtEmail());
        etPosition.setText(agent.getAgtPosition());
    }

    /**
     * Shows a Toast (LENGTH_SHORT) with given message
     * @param message the message to show
     */
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}