package es.medicarte.util;

import es.medicarte.model.ConfiguracionDAO;
import javafx.scene.image.Image;

import java.io.File;

public class LogoUtils {

    private static final double DEFAULT_SIZE = 120;

    /**
     * Obtiene el logo de la clínica escalado
     */
    public static Image getLogo(double size) {

        ConfiguracionDAO dao = new ConfiguracionDAO();
        String logoPath = dao.getValor("LOGO_CLINICA");

        if (logoPath == null || logoPath.isBlank()) {
            return null;
        }

        File file = new File(logoPath);

        if (!file.exists()) {
            return null;
        }

        return new Image(
                file.toURI().toString(),
                size,
                size,
                true,   // preserve ratio
                true    // smooth
        );
    }

    /**
     * Logo por defecto (tamaño estándar)
     */
    public static Image getLogo() {
        return getLogo(DEFAULT_SIZE);
    }
}

