package src.model;

import java.util.Set;

public class AFD extends Automata {

    //Constructor
    public AFD(Set<String> states, Set<String> alphabet, String initialState, Set<String> finalStates) {
        super(states, alphabet, initialState, finalStates);
    }

     @Override
     public boolean validateString(String input) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateString'");
     }
  
}
