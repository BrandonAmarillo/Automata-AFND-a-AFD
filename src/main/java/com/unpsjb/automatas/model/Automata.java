package com.unpsjb.automatas.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Automata {
    //Atributos
    private Set<String> states;
    private Set<String> alphabet;
    private String initialState;
    private Set<String> finalStates;
    private Map<String, Map<String, Set<String>>> transitions;

    //Constructor
    public Automata(Set<String> states, Set<String> alphabet, String initialState, Set<String> finalStates){
        this.states = states;
        this.alphabet = alphabet;
        this.initialState = initialState;
        this. finalStates = finalStates;
        this.transitions = new HashMap<>();
    }

    //Getters
    public Set<String> getState(){ return states; }
    public Set<String> getAlphabet(){ return alphabet; }
    public String getInitialState(){ return initialState; }
    public Set<String> getFinalStates(){ return finalStates; }
    public Map<String, Map<String, Set<String>>> getTransitions() { return transitions; }


    public void addTransition(String from, String symbol, String to) {
        transitions.computeIfAbsent(from, k -> new HashMap<>())
                  .computeIfAbsent(symbol, k -> new HashSet<>())
                  .add(to);
    }

    public Set<String> getTransition(String state, String symbol){
        return transitions.getOrDefault(state, new HashMap<>())
        .getOrDefault(symbol, new HashSet<>());
    }

    public void setTransitions(Map<String, Map<String, Set<String>>> transitions) {
        this.transitions = transitions;
    }
    /**
     * Método para verificar si el estado es de aceptación
     * @param state
     * @return
     */
    public boolean isFinal(String state){
        return finalStates.contains(state);
    }

    public boolean containsFinalState(Set<String> states) {
    if (states == null || states.isEmpty()) return false;
    for (String s : states) {
        if (finalStates.contains(s)) return true;
    }
    return false;
}

    public abstract boolean validateString(String input);

}
