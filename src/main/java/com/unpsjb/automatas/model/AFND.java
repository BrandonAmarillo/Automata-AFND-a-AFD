package com.unpsjb.automatas.model;

import java.util.HashSet;
import java.util.Set;

public class AFND extends Automata {

    public AFND(Set<String> states, Set<String> alphabet, String initialState, Set<String> finalStates) {
        super(states, alphabet, initialState, finalStates);
    }

    @Override
    public boolean validateString(String input) {
        Set<String> currentStates = new HashSet<>();

        currentStates.add(getInitialState());

        for (char c : input.toCharArray()) {
            String symbol = String.valueOf(c);
            Set<String> nextStates = new HashSet<>();

            for (String state : currentStates) {
                nextStates.addAll(getTransition(state, symbol));
            }

            currentStates = nextStates;
            if (currentStates.isEmpty()) {
                return false; // ningún estado alcanzable
            }
        }

        return containsFinalState(currentStates);
    }

}
