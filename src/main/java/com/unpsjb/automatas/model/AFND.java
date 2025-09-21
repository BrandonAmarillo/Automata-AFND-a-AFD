package com.unpsjb.automatas.model;

import java.util.Set;

public class AFND extends Automata {

    public AFND(Set<String> states, Set<String> alphabet, String initialState, Set<String> finalStates) {
        super(states, alphabet, initialState, finalStates);
    }

    @Override
    public boolean validateString(String input) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateString'");
    }

}
