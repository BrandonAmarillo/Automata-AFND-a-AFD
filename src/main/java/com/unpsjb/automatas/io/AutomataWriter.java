package com.unpsjb.automatas.io;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.unpsjb.automatas.model.AFD;
import com.unpsjb.automatas.model.AFND;
import com.unpsjb.automatas.model.StringTestResult;

public class AutomataWriter {
    
    public static void generatePDF(String fileName,
                                   AFND afnd,
                                   AFD afd,
                                   AFD minimizedAFD,
                                   List<StringTestResult> testResults) throws Exception {

        // Crear archivo PDF
        PdfWriter writer = new PdfWriter(fileName);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // ======= Título principal =======
        document.add(new Paragraph("Informe de Conversión y Minimización")
                .setBold().setFontSize(18));

        // ======= Identificador =======
        document.add(new Paragraph("Los que tienen " + " -> " + " Son Estado Inicial y los que tienen " + " * " + "Son Estados Finales"));
        
        if(afnd != null){
            // ======= AFND =======
            document.add(new Paragraph("\nAutómata Finito No Determinista (AFND)")
                    .setBold().setFontSize(16));
            
            // Datos básicos AFND
            document.add(new Paragraph("Estados: " + afnd.getState()));
            document.add(new Paragraph("Alfabeto: " + afnd.getAlphabet()));
            document.add(new Paragraph("Estado Inicial: " + afnd.getInitialState()));
            document.add(new Paragraph("Estados Finales: " + afnd.getFinalStates()));
            
            // Tabla AFND
            addTransitionTable(document, afnd.getState(), afnd.getAlphabet(), 
                               afnd.getInitialState(), afnd.getFinalStates(), 
                               afnd.getTransitions(), "Tabla de Transiciones AFND");
        }
        // ======= AFD =======
        document.add(new Paragraph("\nAutómata Finito Determinista (AFD)")
                .setBold().setFontSize(16));

        document.add(new Paragraph("Estados: " + afd.getState()));
        document.add(new Paragraph("Alfabeto: " + afd.getAlphabet()));
        document.add(new Paragraph("Estado Inicial: " + afd.getInitialState()));
        document.add(new Paragraph("Estados Finales: " + afd.getFinalStates()));

        addTransitionTable(document, afd.getState(), afd.getAlphabet(),
                           afd.getInitialState(), afd.getFinalStates(),
                           afd.getTransitions(), "Tabla de Transiciones AFD");

        // ======= AFD Minimizado =======
        document.add(new Paragraph("\nAutómata Finito Determinista Minimizado (AFD Mínimo)")
                .setBold().setFontSize(16));

        document.add(new Paragraph("Estados: " + minimizedAFD.getState()));
        document.add(new Paragraph("Alfabeto: " + minimizedAFD.getAlphabet()));
        document.add(new Paragraph("Estado Inicial: " + minimizedAFD.getInitialState()));
        document.add(new Paragraph("Estados Finales: " + minimizedAFD.getFinalStates()));

        addTransitionTable(document, minimizedAFD.getState(), minimizedAFD.getAlphabet(),
                           minimizedAFD.getInitialState(), minimizedAFD.getFinalStates(),
                           minimizedAFD.getTransitions(), "Tabla de Transiciones AFD Minimizado");

        // ======= Tabla de resultados de cadenas =======
        if (testResults != null && !testResults.isEmpty()) {
            document.add(new Paragraph("\nResultados de validación de cadenas").setBold().setFontSize(14));
            float[] columnWidths = {200, 100, 100, 100};
            Table table = new Table(columnWidths);
            table.addHeaderCell("Cadena");
            table.addHeaderCell("AFND");
            table.addHeaderCell("AFD");
            table.addHeaderCell("AFDmin");

            for (StringTestResult result : testResults) {
                table.addCell(result.getString());
                table.addCell(result.isAcceptedByAFND() ? "Aceptado" : "Rechazado");
                table.addCell(result.isAcceptedByAFD() ? "Aceptado" : "Rechazado");
                table.addCell(result.isAcceptedByMinimized() ? "Aceptado" : "Rechazado");
            }
            document.add(table);
        }

        document.close();
    }

    // ======= Método para agregar tabla de transiciones =======
    private static void addTransitionTable(Document document, Set<String> states, Set<String> alphabet,
                                           String initialState, Set<String> finalStates,
                                           Map<String, Map<String, Set<String>>> transitions,
                                           String tableTitle) {

        document.add(new Paragraph("\n" + tableTitle).setBold());

        float[] columnWidths = new float[alphabet.size() + 1];
        Arrays.fill(columnWidths, 100);
        Table table = new Table(columnWidths);

        // Cabecera
        table.addHeaderCell("Estado");
        for (String symbol : alphabet) {
            table.addHeaderCell(symbol);
        }

        // Filas
        for (String state : states) {
            String mark = "";
            if (state.equals(initialState)) mark += "->";
            //System.out.println(state);
            //System.out.println(initialState);
            if (finalStates.contains(state)) mark += "*";
            table.addCell(mark + state);

            for (String symbol : alphabet) {
                Set<String> destiny = transitions.getOrDefault(state, new HashMap<>())
                                                .getOrDefault(symbol, new HashSet<>());
                table.addCell(destiny.isEmpty() ? "-" : destiny.toString());
            }
        }

        document.add(table);
    }
}
