package es.medicarte.controller;

import es.medicarte.model.ConfiguracionDAO;
import es.medicarte.service.BackupService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Controlador encargado de la gestión de copias de seguridad.
 * Desde esta pantalla el administrador puede:
 *  - Crear una nueva copia de seguridad de la base de datos.
 *  - Restaurar una copia existente.
 *  - Registrar la fecha del último backup en la tabla de configuración.
 * La lógica real de creación y restauración se encuentra en BackupService.
 */
public class AdminBackupsController {

    // DAO para guardar información adicional (por ejemplo fecha último backup)
    private final ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();

    // ComboBox que contiene los archivos de backup encontrados en el directorio
    @FXML
    private ComboBox<File> cmbBackups;

    /**
     * Método initialize que se ejecuta automáticamente al cargar la vista.
     * Aquí configuramos cómo se muestran los archivos en el ComboBox
     * y cargamos la lista inicial de copias disponibles.
     */
    @FXML
    private void initialize() {

        // Convertimos el objeto File en texto visible (solo nombre del archivo)
        cmbBackups.setConverter(new StringConverter<>() {
            @Override
            public String toString(File file) {
                return file != null ? file.getName() : "";
            }

            @Override
            public File fromString(String string) {
                return null; // No se utiliza conversión inversa
            }
        });

        // Cargar backups existentes al iniciar
        cargarBackups();
    }

    /**
     * Acción asociada al botón "Crear Backup".
     * Llama al servicio que ejecuta pg_dump y genera el archivo .sql.
     */
    @FXML
    private void crearBackup() {
        try {
            File backup = BackupService.crearBackup();

            mostrarInfo("Backup creado:\n" + backup.getName());

            // Actualizamos el ComboBox con la nueva copia
            cargarBackups();

            // Guardamos fecha de último backup en tabla configuración
            fechaBackup();

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Muestra mensaje de error estándar.
     */
    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    /**
     * Muestra mensaje informativo estándar.
     */
    private void mostrarInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    /**
     * Carga en el ComboBox todos los archivos .sql disponibles
     * en el directorio de backups.
     */
    private void cargarBackups() {

        cmbBackups.getItems().clear();

        for (File f : BackupService.listarBackups()) {
            cmbBackups.getItems().add(f);
        }
    }

    /**
     * Acción asociada al botón "Restaurar Backup".

     * Restaura completamente la base de datos a partir del archivo seleccionado
     * IMPORTANTE:
     *  - Se elimina el esquema actual antes de restaurar.
     *  - Se solicita confirmación al usuario.
     */
    @FXML
    private void restaurarBackup() {

        File seleccionado = cmbBackups.getValue();

        // Validamos que se haya seleccionado un archivo
        if (seleccionado == null) {
            mostrarError("Debe seleccionar una copia de seguridad.");
            return;
        }

        // Confirmación antes de proceder (acción crítica)
        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Esta acción restaurará la base de datos completa.\n"
                        + "¿Desea continuar?",
                ButtonType.YES,
                ButtonType.NO
        );

        confirm.showAndWait();

        if (confirm.getResult() != ButtonType.YES) {
            return;
        }

        try {

            BackupService.restaurarBackup(seleccionado);

            mostrarInfo(
                    "La base de datos se ha restaurado correctamente.\n"
                            + "La aplicación debe reiniciarse."
            );

            // Cerramos la aplicación para evitar inconsistencias
            System.exit(0);

        } catch (Exception e) {
            mostrarInfo(e.getMessage());
        }
    }

    /**
     * Guarda en la tabla configuración la fecha del último backup realizado.
     * Se formatea en castellano para que sea más legible en el dashboard.
     */
    @FXML
    private void fechaBackup() {

        LocalDateTime fechaBKP = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "d 'de' MMMM 'de' yyyy. HH:mm",
                new Locale("es", "ES")
        );

        String fechaFormateada = fechaBKP.format(formatter);

        configuracionDAO.setValor("ULTIMA_FECHA_BKP", fechaFormateada);
    }
}
