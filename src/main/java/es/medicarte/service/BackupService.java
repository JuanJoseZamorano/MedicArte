package es.medicarte.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servicio encargado de realizar copias de seguridad y restauraciones
 * de la base de datos PostgreSQL.
 * Las copias se realizan mediante la ejecución de herramientas externas
 * (pg_dump y psql), ya sea desde Docker o desde la instalación local
 * de PostgreSQL, dependiendo del entorno detectado automáticamente.
 */
public class BackupService {

    // Directorio donde se almacenan los archivos de backup
    private static final String BACKUP_DIR = "backups";

    // Rutas a las herramientas de PostgreSQL (solo se usan si no hay Docker)
    private static final String PG_DUMP =
            "\"C:\\Program Files\\PostgreSQL\\18\\bin\\pg_dump.exe\"";
    private static final String PSQL =
            "\"C:\\Program Files\\PostgreSQL\\18\\bin\\psql.exe\"";

    // Nombre del contenedor Docker de la base de datos
    private static final String DOCKER_CONTAINER = "medicarte_db";

    // Datos de conexión a la base de datos
    private static final String DB_NAME = "MedicArte";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Erkenenpostgres23";

    // =====================================================================
    // DETECCIÓN DE ENTORNO
    // =====================================================================

    /**
     * Comprueba si la base de datos está corriendo en un contenedor Docker.
     * Se utiliza para decidir qué herramienta usar en backup y restauración.
     *
     * @return true si el contenedor Docker está activo, false en caso contrario
     */
    private static boolean isDockerRunning() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "cmd", "/c",
                    "docker inspect -f {{.State.Running}} " + DOCKER_CONTAINER
            );
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream())
            );
            String result = reader.readLine();
            p.waitFor();
            return "true".equals(result != null ? result.trim() : "");
        } catch (Exception e) {
            return false;
        }
    }

    // =====================================================================
    // BACKUP
    // =====================================================================

    /**
     * Crea una copia de seguridad completa de la base de datos.
     * El archivo generado incluye estructura y datos.
     * Detecta automáticamente si usar Docker o PostgreSQL local.
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

        if (isDockerRunning()) {
            System.out.print("Usando BBDD de Docker");
            return crearBackupDocker(backupFile);
        } else {
            System.out.print("Usando BBDD Local");
            return crearBackupLocal(backupFile);
        }
    }

    /**
     * Crea el backup usando pg_dump dentro del contenedor Docker.
     * No requiere PostgreSQL instalado en el equipo.
     *
     * @param backupFile Archivo destino del backup
     * @return Archivo SQL generado
     * @throws Exception Si ocurre algún error durante el proceso
     */
    private static File crearBackupDocker(File backupFile) throws Exception {

        // Comando que ejecuta pg_dump dentro del contenedor y redirige la salida al archivo
        String comando = String.format(
                "docker exec %s pg_dump -U %s %s > \"%s\"",
                DOCKER_CONTAINER,
                DB_USER,
                DB_NAME,
                backupFile.getAbsolutePath()
        );

        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", comando);
        pb.environment().put("PGPASSWORD", DB_PASSWORD);

        Process process = pb.start();

        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
        );

        String line;
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException(
                    "Error al crear la copia de seguridad (Docker):\n" + errorOutput
            );
        }

        return backupFile;
    }

    /**
     * Crea el backup usando pg_dump instalado localmente en el equipo.
     * Se utiliza cuando Docker no está corriendo.
     *
     * @param backupFile Archivo destino del backup
     * @return Archivo SQL generado
     * @throws Exception Si ocurre algún error durante el proceso
     */
    private static File crearBackupLocal(File backupFile) throws Exception {

        // Comando pg_dump para generar la copia usando instalación local
        String comando = String.format(
                "%s -U %s -F p -f \"%s\" %s",
                PG_DUMP,
                DB_USER,
                backupFile.getAbsolutePath(),
                DB_NAME
        );

        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", comando);
        pb.environment().put("PGPASSWORD", DB_PASSWORD);

        Process process = pb.start();

        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
        );

        String line;
        StringBuilder errorOutput = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorOutput.append(line).append("\n");
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException(
                    "Error al crear la copia de seguridad (local):\n" + errorOutput
            );
        }

        return backupFile;
    }

    // =====================================================================
    // LISTAR BACKUPS
    // =====================================================================

    /**
     * Devuelve la lista de archivos de backup existentes.
     *
     * @return Array de archivos .sql
     */
    public static File[] listarBackups() {

        File dir = new File(BACKUP_DIR);

        if (!dir.exists()) {
            return new File[0];
        }

        return dir.listFiles(
                file -> file.getName().endsWith(".sql")
        );
    }

    // =====================================================================
    // RESTAURACIÓN
    // =====================================================================

    /**
     * Restaura una copia de seguridad seleccionada.
     * Detecta automáticamente si usar Docker o PostgreSQL local.
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

        if (isDockerRunning()) {
            System.out.print("Usando BBDD de Docker");
            restaurarBackupDocker(backupFile);
        } else {
            System.out.print("Usando BBDD Local");
            restaurarBackupLocal(backupFile);
        }
    }

    /**
     * Restaura el backup usando psql dentro del contenedor Docker.
     * No requiere PostgreSQL instalado en el equipo.
     *
     * @param backupFile Archivo de backup a restaurar
     * @throws Exception Si ocurre algún error durante la restauración
     */
    private static void restaurarBackupDocker(File backupFile) throws Exception {

        // ===== 1. LIMPIAR ESQUEMA =====
        // Se elimina completamente el esquema para evitar conflictos
        ProcessBuilder dropSchema = new ProcessBuilder(
                "cmd", "/c",
                String.format(
                        "docker exec %s psql -U %s -d %s -c \"DROP SCHEMA medicarte CASCADE; CREATE SCHEMA medicarte;\"",
                        DOCKER_CONTAINER, DB_USER, DB_NAME
                )
        );

        dropSchema.environment().put("PGPASSWORD", DB_PASSWORD);
        Process p1 = dropSchema.start();
        int exit1 = p1.waitFor();

        if (exit1 != 0) {
            throw new RuntimeException("Error limpiando el esquema medicarte (Docker)");
        }

        // ===== 2. RESTAURAR BACKUP =====
        // Se pasa el archivo al contenedor mediante stdin
        ProcessBuilder restore = new ProcessBuilder(
                "cmd", "/c",
                String.format(
                        "docker exec -i %s psql -U %s -d %s < \"%s\"",
                        DOCKER_CONTAINER, DB_USER, DB_NAME,
                        backupFile.getAbsolutePath()
                )
        );

        restore.environment().put("PGPASSWORD", DB_PASSWORD);

        Process p2 = restore.start();

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
                    "Error restaurando el backup (Docker):\n" + errorOutput
            );
        }
    }

    /**
     * Restaura el backup usando psql instalado localmente en el equipo.
     * Se utiliza cuando Docker no está corriendo.
     *
     * @param backupFile Archivo de backup a restaurar
     * @throws Exception Si ocurre algún error durante la restauración
     */
    private static void restaurarBackupLocal(File backupFile) throws Exception {

        // ===== 1. LIMPIAR ESQUEMA =====
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
            throw new RuntimeException("Error limpiando el esquema medicarte (local)");
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
                    "Error restaurando el backup (local):\n" + errorOutput
            );
        }
    }
}
