package com.unpsjb.automatas.model;

public class StringTestResult {
    
    private String string;
    private boolean acceptedByAFND;
    private boolean acceptedByAFD;
    private boolean acceptedByMinimized;

    public StringTestResult(String string) {
        this.string = string;
    }

    public String getString() { return string; }

    public boolean isAcceptedByAFND() { return acceptedByAFND; }
    public boolean isAcceptedByAFD() { return acceptedByAFD; }
    public boolean isAcceptedByMinimized() { return acceptedByMinimized; }

    public void setAcceptedByAFND(boolean values) { this.acceptedByAFND = values; }
    public void setAcceptedByAFD(boolean values) { this.acceptedByAFD = values; }
    public void setAcceptedByMinimized(boolean values) { this.acceptedByMinimized = values; }

    @Override
    public String toString() {
        return string + " | AFND: " + (acceptedByAFND ? "Aceptado" : "Rechazado") +
               " | AFD: " + (acceptedByAFD ? "Aceptado" : "Rechazado") +
               " | AFDmin: " + (acceptedByMinimized ? "Aceptado" : "Rechazado");
    }
}
