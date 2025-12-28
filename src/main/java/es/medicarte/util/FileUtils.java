package es.medicarte.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    private static final String FOTO_DIR = "data/fotos";

    public static String copiarFotoPaciente(File origen, int idPaciente) throws IOException {

        File dir = new File(FOTO_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String extension = obtenerExtension(origen.getName());
        File destino = new File(dir, "paciente_" + idPaciente + extension);

        Files.copy(
                origen.toPath(),
                destino.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

        return destino.getPath();
    }

    private static String obtenerExtension(String nombre) {
        int index = nombre.lastIndexOf('.');
        return (index != -1) ? nombre.substring(index) : ".jpg";
    }
}