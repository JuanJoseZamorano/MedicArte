package es.medicarte.util;

import es.medicarte.model.ConfiguracionDAO;
import javafx.scene.image.Image;

import java.io.File;

/**
 * Clase utilitaria encargada de gestionar el logo de la clínica.
 * Permite obtener la imagen del logo almacenada en la configuración
 * de la aplicación y devolverla ya escalada para su uso en las vistas.
 */
public class LogoUtils {

    // Tamaño por defecto del logo cuando no se especifica otro
    private static final double DEFAULT_SIZE = 120;

    /**
     * Obtiene el logo de la clínica escalado al tamaño indicado.
     * El logo se carga desde la ruta almacenada en la tabla de configuración.
     *
     * @param size Tamaño (ancho y alto) del logo
     * @return Imagen del logo escalada o null si no existe
     */
    public static Image getLogo(double size) {

        // Acceso a la configuración para obtener la ruta del logo
        ConfiguracionDAO dao = new ConfiguracionDAO();
        String logoPath = dao.getValor("LOGO_CLINICA");

        // Si no hay logo configurado, no se devuelve imagen
        if (logoPath == null || logoPath.isBlank()) {
            return null;
        }

        File file = new File(logoPath);

        // Si el archivo no existe físicamente, se evita el error
        if (!file.exists()) {
            return null;
        }

        // Se devuelve la imagen escalada manteniendo la proporción
        return new Image(
                file.toURI().toString(),
                size,
                size,
                true,   // Mantiene la proporción de la imagen
                true    // Suaviza el escalado
        );
    }

    /**
     * Obtiene el logo de la clínica utilizando el tamaño por defecto.
     * Se usa en la mayoría de vistas de la aplicación.
     *
     * @return Imagen del logo escalada al tamaño estándar
     */
    public static Image getLogo() {
        return getLogo(DEFAULT_SIZE);
    }
}

