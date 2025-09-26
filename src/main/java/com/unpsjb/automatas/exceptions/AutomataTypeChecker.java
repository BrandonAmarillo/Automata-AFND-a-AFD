package com.unpsjb.automatas.exceptions;

import com.unpsjb.automatas.model.Automata;

public class AutomataTypeChecker {

    public static boolean isAFD(Automata automata) {
        for (var entry : automata.getTransitions().entrySet()) {
            for (var trans : entry.getValue().entrySet()) {
                if (trans.getValue().size() > 1) {
                    return false; // Más de un destino → AFND
                }
                if (trans.getKey().equals("λ")) {
                    return false; // λ-transición → AFND
                }
            }
        }
        return true;
    }

    public static boolean isAFND(Automata automata) {
        return !isAFD(automata);
    }
}

