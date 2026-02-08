package es.medicarte.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupService {

    private static final String BACKUP_DIR = "backups";
    private static final String PG_DUMP =
            "\"C:\\Program Files\\PostgreSQL\\18\\bin\\pg_dump.exe\"";
    private static final String PSQL =
            "\"C:\\Program Files\\PostgreSQL\\18\\bin\\psql.exe\"";

    private static final String DB_NAME = "MedicArte";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Erkenenpostgres23";

    public static File crearBackup() throws Exception {

        File dir = new File(BACKUP_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
        File backupFile = new File(
                dir,
                "medicarte_backup_" + timestamp + ".sql"
        );

        String comando = String.format(
                "%s -U %s -F p -f \"%s\" %s",
                PG_DUMP,
                DB_USER,
                backupFile.getAbsolutePath(),
                DB_NAME
        );

        ProcessBuilder pb = new ProcessBuilder(
                "cmd", "/c", comando
        );

        pb.environment().put("PGPASSWORD", DB_PASSWORD);

        Process process = pb.start();
        // LEER ERROR DE pg_dump
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
                    "Error al crear la copia de seguridad:\n" + errorOutput
            );
        }


        if (exitCode != 0) {
            throw new RuntimeException("Error al crear la copia de seguridad");
        }

        return backupFile;
    }

    public static File[] listarBackups() {

        File dir = new File("backups");

        if (!dir.exists()) {
            return new File[0];
        }

        return dir.listFiles(
                file -> file.getName().endsWith(".sql")
        );
    }
    public static void restaurarBackup(File backupFile) throws Exception {

        if (backupFile == null || !backupFile.exists()) {
            throw new IllegalArgumentException("El archivo de backup no es válido.");
        }

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
            throw new RuntimeException("Error limpiando el esquema medicarte");
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
                    "Error restaurando el backup:\n" + errorOutput
            );
        }
    }





}
