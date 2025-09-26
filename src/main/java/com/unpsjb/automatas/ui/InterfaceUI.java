package com.unpsjb.automatas.ui;

import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.unpsjb.automatas.exceptions.AutomataTypeChecker;
import com.unpsjb.automatas.exceptions.AutomataValidator;
import com.unpsjb.automatas.io.AutomataReader;
import com.unpsjb.automatas.io.AutomataWriter;
import com.unpsjb.automatas.model.AFD;
import com.unpsjb.automatas.model.AFND;
import com.unpsjb.automatas.model.Automata;
import com.unpsjb.automatas.model.StringTestResult;
import com.unpsjb.automatas.service.Converter;
import com.unpsjb.automatas.service.Minimizer;
import com.unpsjb.automatas.service.Validator;

public class InterfaceUI {

    private Scanner scanner;
    private Automata automata; // Cargado desde el JSON
    private List<StringTestResult> testResults; // Para las cadenas a validar
    private AFD afd = null;
    private AFD minimizedAFD = null;
    private AFND afnd = null;
    private Minimizer minimizer = new Minimizer();
    private Converter converter = new Converter();


    public InterfaceUI() {
        scanner = new Scanner(System.in); 
        testResults = new ArrayList<>();
    }

    public void start() {
        while (true) {
            showMenu();
            String option = readLine("Opción: ");
            switch (option) {
                case "1":
                    loadAutomata();
                    break;
                case "2":
                    enterString();
                    break;
                case "3":
                    seveReportPDF();
                    break;
                case "4":
                    System.out.println("Saliendo del programa...");
                    return;
            
                default:
                    System.out.println("Opción invalida");
            }
        }
    }

    private void loadAutomata() {
        if(automata != null) {
            boolean ask = askYesNo("¿Quieres agregar un nuevo autómata? (s/n)");
            if(ask) {
                automata = null;
            } else{
                return;
            }
        }

        while (automata == null) {
            String name = readLine("Ingrese el nombre del archivo (sin extensión):");

            String path = name.concat(".json");
            File file = new File(path);

            if (!file.exists()) {
                System.out.println("El archivo no existe. Intente nuevamente.");
                continue;
            }
            try {
                automata = AutomataReader.readFromJson(path);
                AutomataValidator.validate(automata);
                conversionAFNDtoAFD(automata);
            } catch (Exception e) {
                System.out.println("Error al cargar el autómata" + e.getMessage());
                automata = null;
            }
        }
    }

    private void conversionAFNDtoAFD(Automata automata){
        if(AutomataTypeChecker.isAFND(automata)){
        
            System.out.println("Se detecto un AFND. Convirtiendo a AFD...");
            afnd = (AFND) automata;
            afd = converter.convert(afnd);
            System.out.println("Conversión completado");
            System.out.println("Minimizando AFD...");
            minimizedAFD = minimizer.minimizer(afd);
            System.out.println("Minimización completado");

        } else if(AutomataTypeChecker.isAFD(automata)){
        
            afd = (AFD) automata;
            System.out.println("Se detecto un AFD. Minimizando...");
            minimizedAFD = minimizer.minimizer(afd);
            System.out.println("Minimización completado");
        
        }
    }

    private void enterString() {
        if(automata == null){
            System.out.println("Primero debe cargar un autómata");
            return;
        }
        testResults.clear();
        System.out.println("Ingrese cadenas a validar (escribir 'fin' para terminar):");
        while (true) {
            String str = scanner.nextLine();
            if("fin".equalsIgnoreCase(str)) break;
            if(str.isEmpty()){
                System.out.println("Cadena vacía, intente de nuevo");
                continue;
            }

            StringTestResult r = new StringTestResult(str);

            //Validar en AFND si existe
            if(afnd != null){
                boolean ok = Validator.validateAFND(afnd, str);
                r.setAcceptedByAFND(ok);
            }
            // Validar en AFD convertido si existe
            if (afd != null) {
                boolean ok = Validator.validateAFD(afd, str);
                r.setAcceptedByAFD(ok);
            }

            // Validar en AFD minimizado si existe
            if (minimizedAFD != null) {
                boolean ok = Validator.validateAFD(minimizedAFD, str);
                r.setAcceptedByMinimized(ok);
            }

            testResults.add(r);
            System.out.println("Resultados de los automatas: " + r);
        }
    }

    /**
     * Método que se encarga de generar un informe en formato PDF
     */
    private void seveReportPDF() {
        if(automata == null){
            System.out.println("Primero debe cargar un autómata");
            return;
        }
        
        String fileName = readLine("Ingrese nombre para general el PDF (sin extensión): ");
        String path = fileName.concat(".pdf");

        try {
            AutomataWriter.generatePDF(path, afnd, afd, minimizedAFD);
        } catch (Exception e) {
            System.out.println("Error al generar PDF: " + e.getMessage());
        }
    }


    private void showMenu(){
        System.out.println("\n===== MENÚ =====");
        System.out.println("1) Cargar autómata desde JSON");
        System.out.println("2) Ingresar cadenas para validar");
        System.out.println("3) Guardar informe en PDF");
        System.out.println("4) Salir");
    }

    private String readLine(String string) {
        System.out.print(string);
        return scanner.nextLine().trim();
    }

    private boolean askYesNo(String prompt) {
        String r;
        while (true) {
            r = readLine(prompt);
            if (r.equalsIgnoreCase("s") || r.equalsIgnoreCase("y")) return true;
            if (r.equalsIgnoreCase("n")) return false;
            System.out.println("Responda 's' o 'n'.");
        }
    }
}