package es.medicarte.util;

import es.medicarte.model.Cita;

/**
 * Clase auxiliar utilizada para representar los elementos del listado de citas.
 * Permite mezclar en un mismo ListView elementos de tipo cabecera (mes/año)
 * y elementos de tipo cita real.
 */
public class ItemListaCitas {

    // Texto de la cabecera (por ejemplo: "DICIEMBRE 2025")
    private final String texto;

    // Indica si el elemento es una cabecera o una cita
    private final boolean esCabecera;

    // Objeto cita asociado (solo si no es cabecera)
    private final Cita cita;

    /**
     * Constructor para crear un elemento de tipo cabecera.
     * Se utiliza para agrupar visualmente las citas por mes y año.
     *
     * @param texto Texto que se mostrará como cabecera
     */
    public ItemListaCitas(String texto) {
        this.texto = texto;
        this.esCabecera = true;
        this.cita = null;
    }

    /**
     * Constructor para crear un elemento de tipo cita.
     * Contiene la información completa de la cita seleccionada.
     *
     * @param cita Objeto Cita asociado al elemento
     */
    public ItemListaCitas(Cita cita) {
        this.cita = cita;
        this.texto = null;
        this.esCabecera = false;
    }

    /**
     * Indica si el elemento es una cabecera de agrupación.
     *
     * @return true si es cabecera, false si es una cita
     */
    public boolean isCabecera() {
        return esCabecera;
    }

    /**
     * Devuelve el texto de la cabecera.
     * Solo tiene valor cuando el elemento representa una cabecera.
     *
     * @return Texto de la cabecera
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Devuelve la cita asociada al elemento.
     * Solo tiene valor cuando el elemento representa una cita real.
     *
     * @return Objeto Cita o null si es cabecera
     */
    public Cita getCita() {
        return cita;
    }
}
