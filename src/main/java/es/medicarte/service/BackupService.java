package es.medicarte.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servicio encargado de realizar copias de seguridad y restauraciones
 * de la base de datos PostgreSQL.
 *
 * Las copias se realizan mediante la ejecución de herramientas externas
 * (pg_dump y psql).
 */
public class BackupService {

    // Directorio donde se almacenan los archivos de backup
    private static final String BACKUP_DIR = "backups";

    // Rutas a las herramientas de PostgreSQL (pg_dump y psql)
    private static final String PG_DUMP =
            "\"C:\\Program Files\\PostgreSQL\\18\\bin\\pg_dump.exe\"";
    private static final String PSQL =
            "\"C:\\Program Files\\PostgreSQL\\18\\bin\\psql.exe\"";

    // Datos de conexión a la base de datos
    private static final String DB_NAME = "MedicArte";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Erkenenpostgres23";

    /**
     * Crea una copia de seguridad completa de la base de datos.
     * El archivo generado incluye estructura y datos.
     *
     * @return Archivo SQL generado con la copia de seguridad
     * @throws Exception Si ocurre algún error durante el proceso
     */
    public static File crearBackup() throws Exception {

        // Se crea el directorio de backups si no existe
        File dir = new File(BACKUP_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Nombre del archivo con fecha y hora
        String timestamp =
                new SimpleDateFormat("yyyy-MM-dd_HH-mm")
                        .format(new Date());

        File backupFile = new File(
                dir,
                "medicarte_backup_" + timestamp + ".sql"
        );

        // Comando pg_dump para generar la copia
        String comando = String.format(
                "%s -U %s -F p -f \"%s\" %s",
                PG_DUMP,
                DB_USER,
                backupFile.getAbsolutePath(),
                DB_NAME
        );

        // Se ejecuta el comando en consola de Windows
        ProcessBuilder pb = new ProcessBuilder(
                "cmd", "/c", comando
        );

        // Se pasa la contraseña por variable de entorno
        pb.environment().put("PGPASSWORD", DB_PASSWORD);

        Process process = pb.start();

        // Lectura del flujo de errores de pg_dump
        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
        );

        String line;
        StringBuilder errorOutput = new StringBuilder();

        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        int exitCode = process.waitFor();

        // Si el proceso falla, se lanza una excepción con el error
        if (exitCode != 0) {
            throw new RuntimeException(
                    "Error al crear la copia de seguridad:\n" + errorOutput
            );
        }

        return backupFile;
    }

    /**
     * Devuelve la lista de archivos de backup existentes.
     *
     * @return Array de archivos .sql
     */
    public static File[] listarBackups() {

        File dir = new File("backups");

        if (!dir.exists()) {
            return new File[0];
        }

        return dir.listFiles(
                file -> file.getName().endsWith(".sql")
        );
    }

    /**
     * Restaura una copia de seguridad seleccionada.
     * El proceso elimina previamente el esquema y lo vuelve a crear
     * para garantizar una restauración completa.
     *
     * @param backupFile Archivo de backup a restaurar
     * @throws Exception Si ocurre algún error durante la restauración
     */
    public static void restaurarBackup(File backupFile) throws Exception {

        if (backupFile == null || !backupFile.exists()) {
            throw new IllegalArgumentException(
                    "El archivo de backup no es válido."
            );
        }

        // ===== 1. LIMPIAR ESQUEMA =====
        // Se elimina completamente el esquema para evitar conflictos
        ProcessBuilder dropSchema = new ProcessBuilder(
                "C:\\Program Files\\PostgreSQL\\18\\bin\\psql.exe",
                "-h", "localhost",
                "-p", "5432",
                "-U", DB_USER,
                "-d", DB_NAME,
                "-c", "DROP SCHEMA medicarte CASCADE; CREATE SCHEMA medicarte;"
        );

        dropSchema.environment().put("PGPASSWORD", DB_PASSWORD);

        Process p1 = dropSchema.start();
        int exit1 = p1.waitFor();

        if (exit1 != 0) {
            throw new RuntimeException(
                    "Error limpiando el esquema medicarte"
            );
        }

        // ===== 2. RESTAURAR BACKUP =====
        ProcessBuilder restore = new ProcessBuilder(
                "C:\\Program Files\\PostgreSQL\\18\\bin\\psql.exe",
                "-h", "localhost",
                "-p", "5432",
                "-U", DB_USER,
                "-d", DB_NAME,
                "-f", backupFile.getAbsolutePath()
        );

        restore.environment().put("PGPASSWORD", DB_PASSWORD);

        Process p2 = restore.start();

        // Lectura de errores durante la restauración
        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(p2.getErrorStream())
        );

        StringBuilder errorOutput = new StringBuilder();
        String line;
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        int exit2 = p2.waitFor();

        if (exit2 != 0) {
            throw new RuntimeException(
                    "Error restaurando el backup:\n" + errorOutput
            );
        }
    }
}

