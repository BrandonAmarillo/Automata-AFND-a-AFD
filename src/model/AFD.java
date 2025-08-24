package src.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AFD extends Automata {
    //Atributos
    private Map<String, Map<String, Set<String>>> transitions;

    //Constructor
    public AFD(Set<String> states, Set<String> alphabet, String initialState, Set<String> finalStates) {
        super(states, alphabet, initialState, finalStates);
        this.transitions = new HashMap<>();
    }

    @Override
    public void addTransitions(String from, String symbol, String until) {

    }    
}
