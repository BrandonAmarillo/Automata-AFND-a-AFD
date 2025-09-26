package com.unpsjb.automatas.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.unpsjb.automatas.model.AFD;
import com.unpsjb.automatas.model.Automata;

public class Validator {

    public static boolean validateAFD(AFD afd, String input) {
        String currentState = afd.getInitialState();

        for (char c : input.toCharArray()) {
            String symbol = String.valueOf(c);

            // Verificar que el símbolo pertenece al alfabeto
            if (!afd.getAlphabet().contains(symbol)) {
                return false;
            }

            // Transición
            Set<String> nextStates = afd.getTransitions()
                                        .getOrDefault(currentState, new HashMap<>())
                                        .getOrDefault(symbol, new HashSet<>());

            if (nextStates.isEmpty()) {
                return false; // no hay transición posible
            }

            currentState = nextStates.iterator().next(); // determinista = solo un destino
        }

        return afd.getFinalStates().contains(currentState);
    }

    public static boolean validateAFND(Automata afnd, String input) {
        Set<String> currentStates = new HashSet<>();
        currentStates.add(afnd.getInitialState());

        for (char c : input.toCharArray()) {
            String symbol = String.valueOf(c);
            Set<String> nextStates = new HashSet<>();

            for (String state : currentStates) {
                nextStates.addAll(
                    afnd.getTransitions()
                        .getOrDefault(state, new HashMap<>())
                        .getOrDefault(symbol, new HashSet<>())
                );
            }

            if (nextStates.isEmpty()) {
                return false; // sin transición posible
            }

            currentStates = nextStates;
        }

        // Acepta si alguno de los estados actuales es final
        for (String state : currentStates) {
            if (afnd.getFinalStates().contains(state)) {
                return true;
            }
        }

        return false;
    }

}

