package com.group7.workshop8;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AgentData {
    private static final String BASE_URL = "http://192.168.0.13:8080/Day7REST-1.0-SNAPSHOT/api/agent/";

    public static void getAgents(Consumer consumer, Activity activity){
        Runnable action = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(BASE_URL + "getagents");
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    String line;
                    StringBuffer sb = new StringBuffer();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();

                    JSONArray jsonArray = new JSONArray(sb.toString());
                    ArrayList<Agent> agents = new ArrayList<>();
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        int agentId = Integer.parseInt(obj.getString("agentId"));
                        String agtFirstName = obj.getString("agtFirstName");
                        String agtMiddleInitial;
                        if(obj.has("agtMiddleInitial")) {
                            agtMiddleInitial = obj.getString("agtMiddleInitial");
                        }else{
                            agtMiddleInitial = "";
                        }
                        String agtLastName = obj.getString("agtLastName");
                        String agtBusPhone = obj.getString("agtBusPhone");
                        String agtEmail = obj.getString("agtEmail");
                        String agtPosition = obj.getString("agtPosition");

                        Agent agent = new Agent(agentId, agtFirstName, agtMiddleInitial, agtLastName, agtBusPhone, agtEmail, agtPosition);
                        agents.add(agent);
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            consumer.accept(agents);
                        }
                    });
                } catch (IOException | JSONException e) {
                    Log.d("cressDebug", e.toString());
                }

            }
        };

        Executors.newSingleThreadExecutor().execute(action);
    }

    public static void deleteAgent(Agent agent){
        Runnable action = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(BASE_URL + "deleteagent/" + agent.getAgentId());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("DELETE");
                    conn.getInputStream();

                } catch (IOException e) {
                    Log.d("cressDebug", e.toString());
                }

            }
        };

        Executors.newSingleThreadExecutor().execute(action);
    }

    public static void newAgent(Agent agent){
        Runnable action = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(BASE_URL + "putagent/");
                    JSONObject agentJSON = new JSONObject();
                    agentJSON.put("agtFirstName", agent.getAgtFirstName());
                    agentJSON.put("agtMiddleInitial", agent.getAgtMiddleInitial());
                    agentJSON.put("agtLastName", agent.getAgtLastName());
                    agentJSON.put("agtBusPhone", agent.getAgtBusPhone());
                    agentJSON.put("agtEmail", agent.getAgtEmail());
                    agentJSON.put("agtPosition", agent.getAgtPosition());


                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestMethod("PUT");
                    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                    out.write(agentJSON.toString());
                    out.close();
                    conn.getInputStream();

                } catch (IOException | JSONException e) {
                    Log.d("cressDebug", e.toString());
                }

            }
        };

        Executors.newSingleThreadExecutor().execute(action);
    }

    public static void editAgent(Agent agent){
        Runnable action = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(BASE_URL + "postagent/");
                    JSONObject agentJSON = new JSONObject();
                    agentJSON.put("agentId", agent.getAgentId());
                    agentJSON.put("agtFirstName", agent.getAgtFirstName());
                    agentJSON.put("agtMiddleInitial", agent.getAgtMiddleInitial());
                    agentJSON.put("agtLastName", agent.getAgtLastName());
                    agentJSON.put("agtBusPhone", agent.getAgtBusPhone());
                    agentJSON.put("agtEmail", agent.getAgtEmail());
                    agentJSON.put("agtPosition", agent.getAgtPosition());


                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestMethod("POST");
                    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                    out.write(agentJSON.toString());
                    out.close();
                    conn.getInputStream();

                } catch (IOException | JSONException e) {
                    Log.d("cressDebug", e.toString());
                }

            }
        };

        Executors.newSingleThreadExecutor().execute(action);
    }
}
