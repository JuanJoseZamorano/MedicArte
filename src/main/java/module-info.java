module es.medicarte {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Base de datos
    requires java.sql;

    // BCrypt
    requires jbcrypt;

    // JasperReports (se abre en tiempo de ejecución para generar PDFs)
    requires net.sf.jasperreports.core;

    // Abre los paquetes a JavaFX para que pueda acceder a los controladores FXML
    opens es.medicarte.app to javafx.fxml, javafx.graphics;
    opens es.medicarte.controller to javafx.fxml;
    opens es.medicarte.model to javafx.base;

    // Exporta el paquete principal
    exports es.medicarte.app;
    exports es.medicarte.controller;
    exports es.medicarte.model;
    exports es.medicarte.service;
    exports es.medicarte.util;

    // Necesita java.awt explicito
    requires java.desktop;
}
