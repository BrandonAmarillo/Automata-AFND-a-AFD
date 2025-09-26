package com.unpsjb.automatas.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import com.unpsjb.automatas.model.AFD;
import com.unpsjb.automatas.model.AFND;
/*
 * clase para convertir un AFND a AFD
 */
public class Converter {
    
    // método principal para convertir un AFND a AFD
    public AFD convert(AFND afnd) {

            // Calcular el cierre lambda del estado inicial
            Set<String> initialLambdaClosure = lambdaClosure(afnd, afnd.getInitialState());

            // Inicializar estructuras para el AFD
            Set<String> afdStates = new HashSet<>();
            Set<String> afdFinalStates = new HashSet<>();
            Map<String, Map<String, String>> afdTransitions = new HashMap<>();

            // Usar una cola para procesar estados del AFD
            Queue<Set<String>> queue = new LinkedList<>();
            Map<Set<String>, String> stateMapping = new HashMap<>();
            
            //Estado incial del AFD
            String initialStateAFD = setToString(initialLambdaClosure);
            afdStates.add(initialStateAFD);
            stateMapping.put(initialLambdaClosure, initialStateAFD);
            queue.add(initialLambdaClosure);

            // Verificar si es estado final
            if(containsFinalState(afnd, initialLambdaClosure)){
                afdFinalStates.add(initialStateAFD);
            }

            // Porcesar todo los estados
            while(!queue.isEmpty()){
                Set<String> currentStateSet = queue.poll();
                String currentName = stateMapping.get(currentStateSet);

                // Para cada símbolo en el alfabeto
                for(String symbol: afnd.getAlphabet()){
                    if(symbol.equals("λ")) continue;

                    // Calcular movimiento y cierre lambda
                    Set<String> moveResult = move(afnd, currentStateSet, symbol);
                    Set<String> newStateSet = lambdaClosure(afnd, moveResult);

                    if (newStateSet.isEmpty()) continue;

                    String newStateName;
                    if(!stateMapping.containsKey(newStateSet)){
                        // Nuevo estado
                        newStateName = setToString(newStateSet);
                        afdStates.add(newStateName);
                        stateMapping.put(newStateSet, newStateName);
                        queue.add(newStateSet);

                        // Verificar si es estado final
                        if(containsFinalState(afnd, newStateSet)){
                            afdFinalStates.add(newStateName);
                        }
                    } else {
                        newStateName = stateMapping.get(newStateSet);
                    }

                    //Guardar la transición
                    afdTransitions.computeIfAbsent(currentName, k -> new HashMap<>()).put(symbol, newStateName);
                }
            }

            // Crear y retornar el AFD
            AFD afd = new AFD(afdStates, afnd.getAlphabet(), initialStateAFD, afdFinalStates);
            // Agregar todas las transiciones al AFD
            for(Map.Entry<String, Map<String, String>> entry : afdTransitions.entrySet()){
                String from = entry.getKey();
                for(Map.Entry<String, String> t: entry.getValue().entrySet()){
                    String symbol = t.getKey();
                    String to = t.getValue();
                    afd.addTransition(from, symbol, to);
                }
            }

            // Quitamos los sumideros
            removeSinkStates(afd);

            return afd;
        }

        private Set<String> move(AFND afnd, Set<String> currentStateSet, String symbol) {
            Set<String> result = new HashSet<>();
            for (String state : currentStateSet) {
                result.addAll(afnd.getTransition(state, symbol));
            }
            return result;
        }

        // método para verificar si un conjunto de estados contiene un estado de aceptación
        private boolean containsFinalState(AFND afnd, Set<String> states) {
            return states.stream().anyMatch(afnd::isFinal);
        }


        // método para convertir un conjunto de estados a una representación en cadena
        private String setToString(Set<String> states){
            if(states.isEmpty()) return "[]";
            
            List<String> sorted = new ArrayList<>(states);
            Collections.sort(sorted);
            return "{" + String.join(",", sorted) + "}";
        }

        private Set<String> lambdaClosure(AFND afnd, Set<String> state) { // Corregir
            Set<String> closure = new HashSet<>(state);
            Stack<String> stack = new Stack<>();
            stack.addAll(state);

            // Lógica para calcular el cierre lambda
            while (!stack.isEmpty()) {
                String current = stack.pop();
                Set<String> lambdaTransitions = afnd.getTransition(current, "λ");
                for (String nextState : lambdaTransitions){
                    if(!closure.contains(nextState)){
                        closure.add(nextState);
                        stack.push(nextState);
                    }
                }
            }
            return closure;
        }

        // Método sobrecargado para calcular λ-clausura de un solo estado
        private Set<String> lambdaClosure(AFND afnd, String state) {
            return lambdaClosure(afnd, Collections.singleton(state));
        }

        private void removeSinkStates(AFD afd) {
        Set<String> toRemove = new HashSet<>();

        for (String state : afd.getState()) {
            if (!afd.isFinal(state)) {
                boolean isSink = true;
                for (String symbol : afd.getAlphabet()) {
                    Set<String> targets = afd.getTransition(state, symbol);
                    if (targets.size() != 1 || !targets.contains(state)) {
                        isSink = false;
                        break;
                    }
                }
                if (isSink) {
                    toRemove.add(state);
                }
            }
        }

        // Quitar del autómata
        afd.getState().removeAll(toRemove);
        afd.getFinalStates().removeAll(toRemove);
        afd.getTransitions().keySet().removeAll(toRemove);

        // También limpiar referencias a esos estados en transiciones
        for (Map<String, Set<String>> trans : afd.getTransitions().values()) {
            for (Set<String> targets : trans.values()) {
                targets.removeAll(toRemove);
            }
        }
    }
}
