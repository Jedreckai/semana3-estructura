# Plataforma de Monitoreo Ambiental Urbano 🌍

> Proyecto integrador del curso de Estructuras de Datos.

Sistema de ingesta, almacenamiento y análisis de datos ambientales provenientes de una red IoT de 9 estaciones de monitoreo distribuidas en la ciudad.

## Descripción

El proyecto modela una **Plataforma de Monitoreo Ambiental Urbano** que:
- Lee lecturas de sensores (temperatura, humedad, PM2.5) desde archivos CSV
- Valida datos descartando filas con formato incorrecto o fuera de rangos físicos
- Detecta y descarta lecturas duplicadas (misma estación + misma hora)
- Almacena las lecturas en un TAD basado en arreglos con redimensionamiento dinámico
- Analiza la contaminación por PM2.5 usando una matriz estaciones × horas
- Calcula promedios por hora, por estación y encuentra la hora más contaminada

## Estructura del proyecto

```
ed_red_sensores_iot/
├── src/
│   ├── LecturaSensor.java        # Modelo de una lectura de sensor
│   ├── RepositorioLecturas.java   # TAD con arreglo dinámico
│   ├── AnalizadorMatriz.java      # Matriz 9×24 para análisis por hora
│   └── IngestaSensores.java       # Ingesta CSV y punto de entrada (main)
├── data/
│   ├── lecturas.csv               # Dataset de prueba (29 filas)
│   └── lecturas_ampliadas.csv     # Dataset completo (212 filas)
├── docs/
│   ├── contrato_tad.md            # Especificación formal del TAD
│   ├── decisiones.md              # Registro de decisiones de diseño
│   └── GUIA_GIT.md                # Guía de uso de Git para el equipo
├── bitacoras/                     # Bitácoras individuales de los estudiantes
├── out/                           # Archivos compilados (excluido de Git)
└── .gitignore
```

## Cómo compilar y ejecutar

Desde la carpeta raíz del proyecto, con un JDK instalado:

```bash
# Compilar
javac -d out src/LecturaSensor.java src/RepositorioLecturas.java src/AnalizadorMatriz.java src/IngestaSensores.java

# Ejecutar (desde la carpeta data/ para que encuentre el CSV)
cd data
java -cp ../out IngestaSensores
```

### Conceptos principales

- `BufferedReader` lee el archivo una línea a la vez.
- `split(",")` separa las columnas del CSV.
- `Double.parseDouble` convierte texto numérico a `double`.
- Arreglos con redimensionamiento por duplicación de capacidad.
- Validación de rangos físicos y detección de duplicados.
- Matriz de 9 estaciones × 24 horas para análisis de PM2.5.

## Restricciones de diseño

- **No se usan colecciones de Java** (`ArrayList`, `HashMap`, etc.)
- Solo arreglos primitivos y de objetos
- Redimensionamiento por **duplicación** de capacidad
- Eliminación por **compactación** (desplazamiento a la izquierda)
- Validación de rangos físicos: temperatura (-40 a 60°C), humedad (0-100%) y PM2.5 (≥ 0)

## Equipo

| Integrante | Rol |
|---|---|
| Alejandro Tafur Rodriguez | TAD, Contrato y Documentación |
| Juan Felipe Castellanos Bran | RepositorioLecturas (búsqueda, actualización, eliminación) |
| Juan Pablo Lozada Lopez | AnalizadorMatriz (promedios, ceros fantasma) |
| Jedreck Triana Venner | LecturaSensor y estructura base |
| Julian Hernandez | Configuración del proyecto y estructura |
