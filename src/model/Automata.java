package src.model;

import java.util.Set;

public abstract class Automata {
    //Atributos
    private Set<String> states;
    private Set<String> alphabet;
    private String initialState;
    private Set<String> finalStates;

    //Constructor
    public Automata(Set<String> states, Set<String> alphabet, String initialState, Set<String> finalStates){
        this.states = states;
        this.alphabet = alphabet;
        this.initialState = initialState;
        this. finalStates = finalStates;
    }

    //Getters
    public Set<String> getState(){ return states; }
    public Set<String> getAlphabet(){ return alphabet; }
    public String getInitialState(){ return initialState; }
    public Set<String> getFinalStates(){ return finalStates; }

    /**
     * Método para verificar si el estado es de aceptación
     * @param state
     * @return
     */
    public boolean isFinal(String state){
        return finalStates.contains(state);
    }

    /**
     * Método abstracto para agregar las transiciones
     * @param from
     * @param symbol
     * @param until
     */
    public abstract void addTransitions(String from, String symbol, String until);

}
