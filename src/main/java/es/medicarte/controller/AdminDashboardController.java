package es.medicarte.controller;


import es.medicarte.util.LogoUtils;
import es.medicarte.util.SceneManager;
import es.medicarte.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Controlador del dashboard de administración.
 * Esta clase gestiona la navegación interna dentro del panel de administrador,
 * cargando dinámicamente las distintas vistas (Configuración y Backups)
 * dentro del contenedor central (contentPane).
 */
public class AdminDashboardController {

    /**
     * Contenedor principal donde se cargan dinámicamente las vistas
     * de configuración y copias de seguridad.
     */
    @FXML
    private StackPane contentPane;

    /**
     * Imagen del logo de la clínica que aparece en la cabecera.
     */
    @FXML
    private ImageView imgLogo;

    /**
     * Método que se ejecuta automáticamente al cargar el FXML.
     * Aquí se realiza:
     * - Carga del logo desde base de datos (si existe).
     * - Carga por defecto de la vista de configuración.
     */
    @FXML
    private void initialize() {

        // Cargamos el logo configurado en base de datos
        Image logo = LogoUtils.getLogo(120);

        if (logo != null) {
            imgLogo.setImage(logo);
        }

        // Por defecto, al entrar en el panel de administración,
        // se muestra la vista de configuración
        mostrarConfiguracion();
    }

    /**
     * Muestra la vista de configuración de usuarios y datos de clínica.
     */
    @FXML
    private void mostrarConfiguracion() {
        cargarVista("/es/medicarte/view/admin_configuracion.fxml");
    }

    /**
     * Muestra la vista de gestión de copias de seguridad.
     */
    @FXML
    private void mostrarBackups() {
        cargarVista("/es/medicarte/view/admin_backups.fxml");
    }

    /**
     * Método privado reutilizable para cargar vistas dentro del StackPane.
     * Se utiliza para no duplicar código cada vez que cambiamos de sección.
     * Limpia el contenido actual y añade el nuevo FXML cargado.
     */
    private void cargarVista(String fxml) {
        try {

            // Limpiamos el contenido actual
            contentPane.getChildren().clear();

            // Cargamos el nuevo FXML dentro del contenedor
            contentPane.getChildren().add(
                    FXMLLoader.load(getClass().getResource(fxml))
            );

        } catch (Exception e) {
            // En caso de error, lo mostramos por consola
            // (En producción sería recomendable mejorar la gestión)
            e.printStackTrace();
        }
    }

    /**
     * Cierra la sesión del usuario administrador y vuelve al login.
     * Se limpia la sesión actual para evitar accesos indebidos.
     */
    @FXML
    private void cerrarSesion() {

        // Eliminamos el usuario actual de la sesión
        UserSession.clear();

        // Volvemos a la pantalla de login
        SceneManager.loadScene(
                "/es/medicarte/view/login.fxml",
                "MedicArte - Login"
        );
    }
}