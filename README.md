# 🏥 MedicArte  
### Sistema Integral de Gestión de Clínicas

MedicArte es un **proyecto académico** desarrollado como **Trabajo Final de Grado Superior (DAM)** cuyo objetivo es **digitalizar y optimizar la gestión de clínicas privadas pequeñas y medianas**, ofreciendo una solución moderna, segura y fácil de usar.

El sistema combina una **aplicación de escritorio** para personal sanitario y administrativo con una **arquitectura preparada para integrarse con una aplicación móvil** orientada a pacientes.

---

## 📌 Motivación del proyecto

Muchas clínicas privadas siguen gestionando citas, pacientes e historiales mediante:
- Documentos Word o Excel  
- Sistemas poco especializados  
- Software excesivamente complejo para su tamaño  

MedicArte nace para cubrir esa necesidad, apostando por:
- **Simplicidad**
- **Centralización de la información**
- **Usabilidad**
- **Cumplimiento legal (RGPD)**

---

## 🎯 Objetivos

### Objetivos generales
- Desarrollar un **sistema multiplataforma** para la gestión integral de clínicas.
- Mejorar la **eficiencia interna** del personal sanitario.
- Optimizar la **experiencia del paciente**.
- Preparar una arquitectura escalable para futuras ampliaciones (app móvil / web).

### Objetivos específicos
- Gestión completa de pacientes y expedientes clínicos.
- Sistema de citas con calendario médico.
- Registro de historias clínicas por consulta.
- Generación de informes médicos en PDF.
- Sistema de copias de seguridad.
- Seguridad y privacidad de datos conforme a RGPD y LOPD-GDD.

---

## 🧩 Funcionalidades principales

### 👨‍⚕️ Aplicación de escritorio
- Autenticación por roles (Administrador / Médico)
- Gestión de pacientes (alta, baja, modificación y búsqueda)
- Agenda de citas (crear, modificar, cancelar)
- Registro de historias clínicas por consulta
- Visualización de historial clínico completo
- Generación de informes médicos (PDF)
- Copias de seguridad y restauración de la base de datos
- Configuración básica de la clínica (nombre, logo, horarios)

### 📱 Aplicación móvil (planteada)
- Solicitud y gestión de citas
- Notificaciones y recordatorios
- Comunicación médico–paciente
- Sincronización mediante API REST

> ⚠️ La app móvil no se desarrolla en esta fase, pero queda **planteada a nivel de diseño y arquitectura**.

---

## 🛠️ Tecnologías utilizadas

### Backend / Escritorio
- **Java 21**
- **JavaFX**
- **Spring Boot** (para futura API REST)
- **Arquitectura MVC**
- **JasperReports** (informes PDF)

### Base de datos
- **PostgreSQL 16**
- Diseño E/R normalizado
- Preparado para despliegue local o en servidor

### Herramientas
- NetBeans / IntelliJ IDEA
- Git & GitHub
- DBeaver / pgAdmin
- Evolus Pencil (prototipado de interfaces)

---

## 🗂️ Arquitectura del sistema

- Aplicación de escritorio conectada a una base de datos centralizada.
- Diseño modular y escalable.
- Preparada para:
  - API REST
  - Aplicación móvil
  - Integración con servicios externos

---

## 🔐 Seguridad y legalidad

MedicArte tiene en cuenta la naturaleza sensible de los datos sanitarios:

- Control de acceso por roles
- Contraseñas cifradas
- Registro de acciones (logs)
- Cumplimiento del **RGPD**
- Preparado para:
  - Cifrado en tránsito (TLS)
  - Auditoría de accesos
  - Políticas de privacidad

---

## 📐 Metodología de desarrollo

El proyecto sigue una metodología **iterativa y MVP**, dividida en fases:

1. Análisis de requisitos  
2. Diseño del sistema y base de datos  
3. Desarrollo modular  
4. Pruebas funcionales y técnicas  
5. Documentación técnica y de usuario  

---

## 📊 Estado del proyecto

🔧 **En desarrollo / fase académica**

Este repositorio contiene:
- Análisis de requisitos
- Diseño funcional y técnico
- Prototipos de interfaz
- Diseño de base de datos
- Implementación progresiva de módulos

---

## 👤 Autor

**Juan José Zamorano García**  
🎓 DAM – Desarrollo de Aplicaciones Multiplataforma  
🏫 IES Aguadulce  

---

## 📄 Licencia

Proyecto académico desarrollado con fines educativos.  
Uso y redistribución sujetos a autorización del autor.
