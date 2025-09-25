package com.unpsjb.automatas.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.unpsjb.automatas.model.AFD;

public class Minimizer {
    
    public AFD minimizer(AFD afd){

        Set<String> states = afd.getState();
        Set<String> alphabet = afd.getAlphabet();
        String initialState = afd.getInitialState();
        Set<String> finalStates = afd.getFinalStates();

        // Partición inicial
        Set<Set<String>> partitions = new HashSet<>();
        Set<String> nonFinalStates = new HashSet<>(states);
        nonFinalStates.removeAll(finalStates);

        if(!nonFinalStates.isEmpty()) partitions.add(nonFinalStates);
        if(!finalStates.isEmpty()) partitions.add(finalStates);

        boolean change;

        do {
            change = false;
            Set<Set<String>> newPartitions = new HashSet<>();

            for(Set<String> group: partitions){
                // Se divide en Subgrupos
                Map<String, Set<String>> signatureToAGroup = new HashMap<>();
                
                for(String state: group){
                    // crear una firma para el estado segun a donde va
                    StringBuilder signature = new StringBuilder();
                    for(String symbol: alphabet){
                        Set<String> destiny = afd.getTransition(state, symbol);
                        String represent = findRepresentative(destiny, partitions);
                        signature.append(symbol).append(":").append(represent).append(";");
                    }
                    String sig = signature.toString();
                    signatureToAGroup.computeIfAbsent(sig, k -> new HashSet<>()).add(state);
                }

                newPartitions.addAll(signatureToAGroup.values());
            }

            if(!newPartitions.equals(partitions)){
                partitions = newPartitions;
                change = true;
            }

        } while(change);

        return minimizedAFD(initialState, alphabet, finalStates, partitions, afd);
    }

    // Construcción del nuevo AFD
    private AFD minimizedAFD(String initialState, Set<String> alphabet, Set<String> finalStates, Set<Set<String>> partitions, AFD afd) {
        Set<String> newStates = new HashSet<>();
        Map<String, String> representative = new HashMap<>();
        Set<String> newFinalStates = new HashSet<>();
        String newInitialState = null;

        for(Set<String> group: partitions){
            String rep = group.iterator().next();
            String groupName = group.toString();
            newStates.add(groupName);

            for(String state: group){
                representative.put(state, groupName);
            }

            if(group.contains(initialState)){
                newInitialState = groupName;
            }

            if(!Collections.disjoint(group, finalStates)){
                newFinalStates.add(groupName);
            }
        }

        AFD miniAFD = new AFD(newStates, alphabet, newInitialState, newFinalStates);

        for(Set<String> group: partitions){
            String groupName = group.toString();
            String rep = group.iterator().next();

            for(String symbol: alphabet){
                Set<String> destiny = afd.getTransition(rep, symbol);
                if(!destiny.isEmpty()){
                    String target = representative.get(destiny.iterator().next());
                    miniAFD.addTransition(groupName, symbol, target);
                }
            }
        }
        return miniAFD;
    }

    // Busca el representante del conjunto de destino en las particiones
    private String findRepresentative(Set<String> destiny, Set<Set<String>> partitions) {
        if(destiny == null || destiny.isEmpty()) return null;
        String state = destiny.iterator().next();
        for(Set<String> group: partitions){
            if(group.contains(state)){
                return group.toString();
            }
        }
        return null;
    }
}
