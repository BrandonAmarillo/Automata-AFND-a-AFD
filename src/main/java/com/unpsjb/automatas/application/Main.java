package com.unpsjb.automatas.application;

import com.unpsjb.automatas.io.AutomataReader;
import com.unpsjb.automatas.io.AutomataWriter;
import com.unpsjb.automatas.model.AFD;
import com.unpsjb.automatas.model.AFND;
import com.unpsjb.automatas.model.Automata;
import com.unpsjb.automatas.service.Converter;
import com.unpsjb.automatas.service.Minimizer;

public class Main {
    public static void main(String[] args) {
       System.out.println("Hello, Automata!");

       try {
            // Leer AFND desde JSON
            Automata automata = AutomataReader.readFromJson("afnd3.json");
            System.out.println("Automata cargado:");
            System.out.println("Estados: " + automata.getState());
            System.out.println("Alfabeto: " + automata.getAlphabet());
            System.out.println("Estado inicial: " + automata.getInitialState());
            System.out.println("Estados finales: " + automata.getFinalStates());
            System.out.println("Transiciones: " + automata.getTransitions());

            // Convertir AFND → AFD
            AFND afnd = (AFND) automata;
            Converter converter = new Converter();
            AFD afd = converter.convert(afnd);

            // Mostrar AFD resultante
            System.out.println("\n=== AFD resultante ===");
            System.out.println("Estados: " + afd.getState());
            System.out.println("Alfabeto: " + afd.getAlphabet());
            System.out.println("Estado inicial: " + afd.getInitialState());
            System.out.println("Estados finales: " + afd.getFinalStates());
            System.out.println("Transiciones: " + afd.getTransitions());

            Minimizer minimizer = new Minimizer();
            AFD minimized = minimizer.minimizer(afd);

            System.out.println("=== AFD Minimizado ===");
            System.out.println("Estados: " + minimized.getState());
            System.out.println("Inicial: " + minimized.getInitialState());
            System.out.println("Finales: " + minimized.getFinalStates());
            System.out.println("Transiciones: " + minimized.getTransitions());

            AutomataWriter.generatePDF("informe.pdf", afnd, afd, minimized);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
