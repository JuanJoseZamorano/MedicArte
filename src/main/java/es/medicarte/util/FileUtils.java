package es.medicarte.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Clase utilitaria para la gestión de archivos en la aplicación.
 * En este caso se utiliza para copiar y organizar las fotos
 * asociadas a los pacientes.
 */
public class FileUtils {

    // Directorio donde se almacenan las fotos de los pacientes
    private static final String FOTO_DIR = "data/fotos";

    /**
     * Copia la foto seleccionada de un paciente al directorio de la aplicación.
     * El nombre del archivo se genera automáticamente utilizando el id del paciente,
     * lo que evita duplicados y facilita su identificación.
     *
     * @param origen     Archivo original seleccionado por el usuario
     * @param idPaciente Identificador del paciente
     * @return Ruta del archivo copiado
     * @throws IOException Si ocurre un error durante la copia
     */
    public static String copiarFotoPaciente(File origen, int idPaciente) throws IOException {

        // Se crea el directorio de fotos si no existe
        File dir = new File(FOTO_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Se obtiene la extensión original del archivo
        String extension = obtenerExtension(origen.getName());

        // Se genera el nombre del archivo destino usando el id del paciente
        File destino = new File(dir, "paciente_" + idPaciente + extension);

        // Copia del archivo, sustituyendo el anterior si ya existía
        Files.copy(
                origen.toPath(),
                destino.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

        return destino.getPath();
    }

    /**
     * Obtiene la extensión de un archivo a partir de su nombre.
     * Si no se encuentra extensión, se asigna por defecto ".jpg".
     *
     * @param nombre Nombre del archivo
     * @return Extensión del archivo
     */
    private static String obtenerExtension(String nombre) {
        int index = nombre.lastIndexOf('.');
        return (index != -1) ? nombre.substring(index) : ".jpg";
    }
}
