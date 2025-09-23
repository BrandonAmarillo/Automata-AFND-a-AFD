package com.unpsjb.automatas.application;

import com.unpsjb.automatas.io.AutomataReader;
import com.unpsjb.automatas.model.Automata;

public class Main {
    public static void main(String[] args) {
       System.out.println("Hello, Automata!");

       try {
            Automata automata = AutomataReader.readFromJson("afnd.json");
            System.out.println("Automata cargado:");
            System.out.println("Estados: " + automata.getState());
            System.out.println("Alfabeto: " + automata.getAlphabet());
            System.out.println("Estado inicial: " + automata.getInitialState());
            System.out.println("Estados finales: " + automata.getFinalStates());
            System.out.println("Transiciones: " + automata.getTransitions());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
