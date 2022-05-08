/*
Author: Alex Cress

This class handles all CRUD operations for the Agent table
 */
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
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/** Helper class for CRUD operations with the web API table "Agent"
 *
 */
public final class AgentData {
    /**
     * URL that points to the Agent API
     */
    private static final String BASE_URL = "http://192.168.0.13:8080/Day7REST-1.0-SNAPSHOT/api/agent/";

    /**
     * Gets all Agent data and will notify the caller via the Consumer interface when ready.
     *
     * @param consumer callback when Agent data is ready
     * @param activity parent activity, will run UI threads on this
     */
    public static void getAgents(Consumer consumer, Activity activity){
        Runnable action = new Runnable() {
            @Override
            public void run() {
                try {
                    //Get raw JSON
                    URL url = new URL(BASE_URL + "getagents");
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    String line;
                    StringBuffer sb = new StringBuffer();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();

                    //Create an ArrayList of Agents from JSON
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
                            agtMiddleInitial = ""; //Some values can be null. Similar checks should be done on other attributes as well
                        }
                        String agtLastName = obj.getString("agtLastName");
                        String agtBusPhone = obj.getString("agtBusPhone");
                        String agtEmail = obj.getString("agtEmail");
                        String agtPosition = obj.getString("agtPosition");

                        Agent agent = new Agent(agentId, agtFirstName, agtMiddleInitial, agtLastName, agtBusPhone, agtEmail, agtPosition);
                        agents.add(agent);
                    }

                    //Sort by Last Name
                    agents.sort(new Comparator<Agent>() {
                        @Override
                        public int compare(Agent agent1, Agent agent2) {
                            return agent1.getAgtLastName().toLowerCase(Locale.ROOT).compareTo(agent2.getAgtLastName().toLowerCase(Locale.ROOT));
                        }
                    });

                    //Send data to Consumer
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

    /**
     * Deletes the given Agent. Currently only uses the AgentId for selection of an Agent to delete.
     *
     * @param agent an Agent with an existing ID
     */
    public static void deleteAgent(Agent agent){
        Runnable action = new Runnable() {
            @Override
            public void run() {
                try {
                    //Prepare URL
                    URL url = new URL(BASE_URL + "deleteagent/" + agent.getAgentId());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("DELETE");
                    //Open stream to commit changes
                    conn.getInputStream();

                } catch (IOException e) {
                    Log.d("cressDebug", e.toString());
                }

            }
        };

        Executors.newSingleThreadExecutor().execute(action);
    }

    /**
     * Creates a new Agent with the given object. AgentId is a primary key, and thus not used in this operation.
     * @param agent agent data to use in insertion
     */
    public static void newAgent(Agent agent){
        Runnable action = new Runnable() {
            @Override
            public void run() {
                try {
                    //Prepare URL + JSON
                    URL url = new URL(BASE_URL + "putagent/");
                    JSONObject agentJSON = new JSONObject();
                    agentJSON.put("agtFirstName", agent.getAgtFirstName());
                    agentJSON.put("agtMiddleInitial", agent.getAgtMiddleInitial());
                    agentJSON.put("agtLastName", agent.getAgtLastName());
                    agentJSON.put("agtBusPhone", agent.getAgtBusPhone());
                    agentJSON.put("agtEmail", agent.getAgtEmail());
                    agentJSON.put("agtPosition", agent.getAgtPosition());

                    //Open connection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    //Prepare metadata
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestMethod("PUT");

                    //Open output stream, commit JSON
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

    /**
     * Updates the given Agent.
     * @param agent agent data to use in insertion
     */
    public static void editAgent(Agent agent){
        Runnable action = new Runnable() {
            @Override
            public void run() {
                try {
                    //Prepare URL + JSON
                    URL url = new URL(BASE_URL + "postagent/");
                    JSONObject agentJSON = new JSONObject();
                    agentJSON.put("agentId", agent.getAgentId());
                    agentJSON.put("agtFirstName", agent.getAgtFirstName());
                    agentJSON.put("agtMiddleInitial", agent.getAgtMiddleInitial());
                    agentJSON.put("agtLastName", agent.getAgtLastName());
                    agentJSON.put("agtBusPhone", agent.getAgtBusPhone());
                    agentJSON.put("agtEmail", agent.getAgtEmail());
                    agentJSON.put("agtPosition", agent.getAgtPosition());

                    //Open connection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    //Prepare metadata
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestMethod("POST");

                    //Open output stream, commit JSON
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
