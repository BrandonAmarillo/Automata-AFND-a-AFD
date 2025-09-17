package src.service;

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

import src.model.AFD;
import src.model.AFND;
/*
 * clase para convertir un AFND a AFD
 */
public class Converter {

    private int stateCounter = 0;
    
    // método principal para convertir un AFND a AFD
    public AFD convert(AFND afnd) {

        // Calcular el cierre lambda del estado inicial
        Set<String> initialLambdaClosure = lambdaClosure(afnd, afnd.getInitialState());

        // Inicializar estructuras para el AFD
        Set<String> afdStates = new HashSet<>();
        Set<String> afdFinalStates = new HashSet<>();
        Map<String, Map<String, Set<String>>> afdTransitions = new HashMap<>();
        Set<String> alphabet = afnd.getAlphabet();
        // Lógica para convertir AFND a AFD

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
                if(symbol.equals("λ")) continue; // Saltar lambda

                // Calcular movimiento y cierre lambda
                Set<String> moveResult = move(afnd, currentStateSet, symbol);
                Set<String> newStateSet = lambdaClosure(afnd, moveResult);

                if (newStateSet.isEmpty()) continue; // No hay transición

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
            }
        }

        // Crear y retornar el AFD
        AFD afd = new AFD(afdStates, afnd.getAlphabet(), initialStateAFD, afdFinalStates);

        // Agregar todas las transiciones al AFD
        return afd;
    }

    private Set<String> move(AFND afnd, Set<String> currentStateSet, String symbol) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

    // método para verificar si un conjunto de estados contiene un estado de aceptación
    public boolean containsFinalState(AFND afnd, Set<String> states) {
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

    // Método sobrecargado para calcular ε-clausura de un solo estado
    private Set<String> lambdaClosure(AFND afnd, String state) {
        return lambdaClosure(afnd, Collections.singleton(state));
    }

}
