package es.medicarte.service;

import es.medicarte.model.*;
import net.sf.jasperreports.engine.*;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class InformeConsultaService {

    /**
     * Genera un informe clínico en PDF a partir de una consulta
     */
    public static void generarPdfConsulta(Consulta consulta) {
        ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();
        try {
            // =========================
            // CARGAR PLANTILLA JRXML
            // =========================
            InputStream reportStream =
                    InformeConsultaService.class
                            .getResourceAsStream("/report/informe_consulta.jrxml");

            if (reportStream == null) {
                throw new RuntimeException("No se encuentra el informe informe_consulta.jrxml");
            }

            JasperReport jasperReport =
                    JasperCompileManager.compileReport(reportStream);

            // =========================
            // DAOs
            // =========================
            EpisodioDAO episodioDAO = new EpisodioDAO();
            HistoriaDAO historiaDAO = new HistoriaDAO();
            PacienteDAO pacienteDAO = new PacienteDAO();
            EspecialidadDAO especialidadDAO = new EspecialidadDAO();
            MedicoDAO medicoDAO = new MedicoDAO();

            // =========================
            // RECONSTRUIR CONTEXTO
            // =========================
            Episodio episodio = episodioDAO.findById(consulta.getIdEpisodio());
            HistoriaClinica historia = historiaDAO.findById(episodio.getIdHistoria());
            Paciente paciente = pacienteDAO.findById(historia.getIdPaciente());
            Especialidad especialidad = especialidadDAO.findById(episodio.getIdEspecialidad());
            Medico medico = medicoDAO.findById(consulta.getIdMedico());

            // =========================
            // MAPA DE PARÁMETROS
            // =========================
            Map<String, Object> params = new HashMap<>();

            // ---------- DATOS CLÍNICA ----------
            String nombreClinica = configuracionDAO.getValor("NOMBRE_CLINICA");

            params.put(
                    "NOMBRE_CLINICA",
                    nombreClinica != null ? nombreClinica : "MedicArte"
            );
            params.put("DIRECCION_CLINICA", "C/ Ejemplo 123");
            params.put("TELEFONO_CLINICA", "955 123 456");

            String logoPath = configuracionDAO.getValor("LOGO_CLINICA");
            InputStream logoStream = null;

            if (logoPath != null && !logoPath.isBlank()) {
                File logoFile = new File(logoPath);
                if (logoFile.exists()) {
                    logoStream = new FileInputStream(logoFile);
                }
            }

// Si no hay logo configurado o falla, usar logo por defecto
            if (logoStream == null) {
                logoStream = InformeConsultaService.class
                        .getResourceAsStream("/report/logo.png");
            }

            params.put("LOGO_CLINICA", logoStream);

            // ---------- DATOS PACIENTE ----------
            params.put("PACIENTE_NOMBRE",
                    paciente.getApellidos() + ", " + paciente.getNombre());
            params.put("PACIENTE_DNI", paciente.getDni());
            params.put("PACIENTE_FECHA_NAC",
                    paciente.getFechaNacimiento() != null
                            ? paciente.getFechaNacimiento().toString()
                            : "");
            params.put("PACIENTE_EDAD",
                    calcularEdad(paciente.getFechaNacimiento()));
            params.put("PACIENTE_SEXO", nvl(paciente.getSexo()));
            params.put("PACIENTE_DIRECCION", nvl(paciente.getDireccion()));
            params.put("PACIENTE_CP", nvl(paciente.getCp()));
            params.put("PACIENTE_PROVINCIA", nvl(paciente.getProvincia()));
            params.put("PACIENTE_TELEFONO", nvl(paciente.getTelefono()));
            params.put("PACIENTE_EMAIL", nvl(paciente.getEmail()));
            params.put("PACIENTE_NHC", nvl(paciente.getNhc()));
            params.put("PACIENTE_NUHSA", nvl(paciente.getNuhsa()));
            params.put("PACIENTE_NUSS", nvl(paciente.getNuss()));
            params.put("ASEGURADORA", nvl(paciente.getAseguradora()));
            params.put("POLIZA", nvl(paciente.getNumPoliza()));

            // ---------- ANTECEDENTES ----------
            params.put("ANTECEDENTES_PERSONALES",
                    nvl(paciente.getAntecedentesPersonales()));
            params.put("ANTECEDENTES_FAMILIARES",
                    nvl(paciente.getAntecedentesFamiliares()));

            // ---------- DATOS CONSULTA ----------
            params.put("FECHA_CONSULTA",
                    consulta.getFechaHora() != null
                            ? consulta.getFechaHora().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                            : "");
            params.put("MOTIVO_CONSULTA", nvl(consulta.getMotivoConsulta()));
            params.put("EXPLORACION", nvl(consulta.getExploracion()));
            params.put("DIAGNOSTICO", nvl(consulta.getDiagnostico()));
            params.put("TRATAMIENTO", nvl(consulta.getTratamiento()));
            params.put("OBSERVACIONES", nvl(consulta.getObservaciones()));

            // ---------- EPISODIO / MÉDICO ----------
            params.put("EPISODIO", nvl(episodio.getMotivo()));
            params.put("ESPECIALIDAD",
                    especialidad != null ? especialidad.getNombre() : "");
            params.put("MEDICO",
                    medico != null ? medico.getNombreApellidos() : "");

            // =========================
            // GENERAR INFORME
            // =========================
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(
                            jasperReport,
                            params,
                            new JREmptyDataSource()
                    );

            // =========================
            // EXPORTAR A PDF
            // =========================
            String outputPath =
                    System.getProperty("user.home")
                            + File.separator
                            + "informe_consulta.pdf";

            JasperExportManager.exportReportToPdfFile(
                    jasperPrint,
                    outputPath
            );

            // =========================
            // ABRIR PDF
            // =========================
            Desktop.getDesktop().open(new File(outputPath));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Error generando el informe clínico", e);
        }
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================
    private static String nvl(String valor) {
        return valor != null ? valor : "";
    }

    private static String calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return "";
        return String.valueOf(
                Period.between(fechaNacimiento, LocalDate.now()).getYears()
        );
    }
}
