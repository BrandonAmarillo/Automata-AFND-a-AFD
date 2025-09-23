package com.unpsjb.automatas.model;

import java.util.Set;

public class AFD extends Automata {

    //Constructor
    public AFD(Set<String> states, Set<String> alphabet, String initialState, Set<String> finalStates) {
        super(states, alphabet, initialState, finalStates);
    }

     @Override
     public boolean validateString(String input) {
        String currentState = getInitialState();

        for(char c: input.toCharArray()){
            String symbol = String.valueOf(c);

            Set<String> nextStates = getTransition(currentState, symbol);

            if(nextStates.isEmpty()){
                return false;
            }

            currentState = nextStates.iterator().next();
        }

        return isFinal(currentState);
     }
  
}
