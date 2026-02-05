package es.medicarte.util;

import es.medicarte.model.Cita;

public class ItemListaCitas {
    private final String texto;
    private final boolean esCabecera;
    private final Cita cita;

    // Constructor cabecera
    public ItemListaCitas(String texto) {
        this.texto = texto;
        this.esCabecera = true;
        this.cita = null;
    }

    // Constructor cita
    public ItemListaCitas(Cita cita) {
        this.cita = cita;
        this.texto = null;
        this.esCabecera = false;
    }

    public boolean isCabecera() { return esCabecera; }
    public String getTexto() { return texto; }
    public Cita getCita() { return cita; }
}

