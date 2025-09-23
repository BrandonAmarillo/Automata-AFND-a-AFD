package com.unpsjb.automatas.io;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.unpsjb.automatas.model.AFD;
import com.unpsjb.automatas.model.AFND;
import com.unpsjb.automatas.model.Automata;

public class AutomataReader {
    
    public static Automata readFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(new FileReader(filePath), JsonObject.class);

        String type = json.get("type").getAsString();

        Type setType = new TypeToken<Set<String>>(){}.getType();
        Set<String> states = gson.fromJson(json.get("states"), setType);
        Set<String> alphabet = gson.fromJson(json.get("alphabet"), setType);
        Set<String> finalStates = gson.fromJson(json.get("finalStates"), setType);
        String initialState = json.get("initialState").getAsString();

        Automata automata;

        if ("AFD".equalsIgnoreCase(type)) {
            automata = new AFD(states, alphabet, initialState, finalStates);
        } else {
            automata = new AFND(states, alphabet, initialState, finalStates);
        }

        // Leer transiciones
        JsonObject transitionsJson = json.getAsJsonObject("transitions");
        for(String from: transitionsJson.keySet()){
            JsonObject transBySymbol = transitionsJson.getAsJsonObject(from);
            for(String symbol: transBySymbol.keySet()){
                for(var toState: transBySymbol.getAsJsonArray(symbol)){
                    automata.addTransition(from, symbol, toState.getAsString());
                }
            }
        }
        return automata;
    }
    
}
