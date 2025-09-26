package com.unpsjb.automatas.exceptions;

import com.unpsjb.automatas.model.Automata;

public class AutomataValidator {

    public static void validate(Automata automata) {
        validateInitialState(automata);
        validateFinalStates(automata);
        validateTransitions(automata);
        validateAlphabet(automata);
    }

    private static void validateInitialState(Automata automata) {
        if (automata.getInitialState() == null) {
            throw new IllegalArgumentException("El autómata no tiene estado inicial definido.");
        }
        if (!automata.getState().contains(automata.getInitialState())) {
            throw new IllegalArgumentException("El estado inicial no pertenece a los estados definidos.");
        }
    }

    private static void validateFinalStates(Automata automata) {
        if (automata.getFinalStates() == null || automata.getFinalStates().isEmpty()) {
            throw new IllegalArgumentException("El autómata debe tener al menos un estado final.");
        }
    }

    private static void validateTransitions(Automata automata) {
        automata.getTransitions().forEach((state, transMap) -> {
            if (!automata.getState().contains(state)) {
                throw new IllegalArgumentException("El estado " + state + " en las transiciones no existe en los estados del autómata.");
            }
            transMap.forEach((symbol, nextStates) -> {
                if (!automata.getAlphabet().contains(symbol)) {
                    throw new IllegalArgumentException("El símbolo " + symbol + " no pertenece al alfabeto.");
                }
                for (String ns : nextStates) {
                    if (!automata.getState().contains(ns)) {
                        throw new IllegalArgumentException("El estado destino " + ns + " no existe en los estados del autómata.");
                    }
                }
            });
        });
    }

    private static void validateAlphabet(Automata automata) {
        if (automata.getAlphabet() == null || automata.getAlphabet().isEmpty()) {
            throw new IllegalArgumentException("El autómata debe tener un alfabeto definido.");
        }
    }
}

