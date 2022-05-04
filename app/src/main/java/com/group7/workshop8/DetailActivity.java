package com.group7.workshop8;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    EditText etAgentId, etFirstName, etMiddleInitial, etLastName, etPhone, etEmail, etPosition;
    Button btnEdit, btnSave, btnDelete, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToMainActivity();
            }
        });



        Intent intent = getIntent();
        Agent agent = (Agent) intent.getSerializableExtra("agent");
        String mode = intent.getStringExtra("mode");

        if(mode.equals("add")){
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

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Agent agent = new Agent(etFirstName.getText().toString(), etMiddleInitial.getText().toString(), etLastName.getText().toString(), etPhone.getText().toString(), etEmail.getText().toString(), etPosition.getText().toString());
                    AgentData.newAgent(agent);
                    showToast("Insert Successful!");
                    redirectToMainActivity();
                }
            });

        }else if(mode.equals("view")){
            populateFields(agent);

            etAgentId.setEnabled(false);
            etFirstName.setEnabled(false);
            etMiddleInitial.setEnabled(false);
            etLastName.setEnabled(false);
            etPhone.setEnabled(false);
            etEmail.setEnabled(false);
            etPosition.setEnabled(false);

            btnEdit.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnEdit.setVisibility(View.INVISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.INVISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);

                    etAgentId.setEnabled(false);
                    etFirstName.setEnabled(true);
                    etMiddleInitial.setEnabled(true);
                    etLastName.setEnabled(true);
                    etPhone.setEnabled(true);
                    etEmail.setEnabled(true);
                    etPosition.setEnabled(true);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Agent agent = getAgentDetails();

                    AgentData.deleteAgent(agent);
                    showToast("Delete Agent ID: " + etAgentId.getText().toString() + " Successful!");
                    redirectToMainActivity();
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Agent agent = getAgentDetails();
                    AgentData.editAgent(agent);
                    showToast("Update Agent ID: " + etAgentId.getText().toString() + " Successful!");
                    redirectToMainActivity();
                }
            });
        }
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

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

    private void populateFields(Agent agent){
        etAgentId.setText(agent.getAgentId() + "");
        etFirstName.setText(agent.getAgtFirstName());
        etMiddleInitial.setText(agent.getAgtMiddleInitial());
        etLastName.setText(agent.getAgtLastName());
        etPhone.setText(agent.getAgtBusPhone());
        etEmail.setText(agent.getAgtEmail());
        etPosition.setText(agent.getAgtPosition());
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();


    }
}