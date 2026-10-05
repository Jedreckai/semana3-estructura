/* ============================================================
   PLATAFORMA DE MONITOREO AMBIENTAL URBANO
   IngestaSensores - VERSION 1.0 COMPLETA

   Funcionalidades:
   - Carga lecturas desde CSV con validacion de formato y rango.
   - Descarta filas con formato incorrecto o valores fuera de rango.
   - Detecta y descarta lecturas duplicadas (misma estacion + misma hora)
     segun Decision 6 del documento de decisiones.
   - Muestra metricas de ingesta, redimensionamiento, promedio PM2.5,
     perfil horario, promedios por estacion y hora mas contaminada.
   ============================================================ */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IngestaSensores {

    private static final String ARCHIVO = "lecturas_ampliadas.csv";
    private static final int CAMPOS_ESPERADOS = 5;
    private static final int NUM_ESTACIONES = 9;

    private static int descartadasPorFormato = 0;
    private static int descartadasPorRango = 0;
    private static int descartadasPorDuplicado = 0;

    // Matriz de control de duplicados: [estacion][hora] = true si ya se registro
    private static boolean[][] yaRegistrado = new boolean[NUM_ESTACIONES][24];

    public static void main(String[] args) throws IOException {

        RepositorioLecturas repositorio = new RepositorioLecturas();
        AnalizadorMatriz analizador = new AnalizadorMatriz();

        cargarArchivo(repositorio, analizador);

        System.out.println();
        System.out.println("=== INGESTA ===");
        System.out.println("Lecturas almacenadas:      " + repositorio.tamano());
        System.out.println("Descartadas por formato:   " + descartadasPorFormato);
        System.out.println("Descartadas por rango:     " + descartadasPorRango);
        System.out.println("Descartadas por duplicado: " + descartadasPorDuplicado);
        System.out.println();
        System.out.println("=== METRICAS DE REDIMENSIONAMIENTO ===");
        System.out.println("Redimensionamientos:       " + repositorio.getRedimensionamientos());
        System.out.println("Copias de referencias:     " + repositorio.getCopiasRealizadas());
        System.out.println();
        System.out.println("=== ANALISIS ===");
        System.out.printf("PM2.5 promedio (repositorio): %.2f%n", repositorio.promedioPm25());
        System.out.println();
        System.out.println("=== PERFIL HORARIO DE LA CIUDAD ===");
        for (int h = 0; h < 24; h++) {
            System.out.printf("Hora %02d -> PM2.5 promedio: %.2f%n", h, analizador.promedioDeHora(h));
        }
        System.out.println();
        System.out.println("=== PROMEDIOS POR ESTACION ===");
        for (int e = 0; e < NUM_ESTACIONES; e++) {
            System.out.printf("EST-%03d -> PM2.5 promedio: %.2f%n", e + 1, analizador.promedioDeEstacion(e));
        }
        System.out.println();
        int horaPico = analizador.horaMasContaminada();
        System.out.printf("Hora mas contaminada: %02d (promedio PM2.5: %.2f)%n",
                horaPico, analizador.promedioDeHora(horaPico));
        System.out.println();
        System.out.println("=== MATRIZ COMPLETA ===");
        analizador.imprimirMatriz();

        // ---------- SEMANA 4: ordenamientos y comparacion de eficiencia ----------
        System.out.println();
        System.out.println("############ SEMANA 4: ORDENAMIENTOS ############");
        System.out.println("(los experimentos con 100.000 lecturas pueden tardar ~1 minuto)");
        System.out.println();
        BancoDeOrdenamiento banco = new BancoDeOrdenamiento();
        banco.experimentoUno();
        banco.experimentoDos();
        banco.experimentoTres();
        banco.experimentoCuatro();
        banco.experimentoCinco();
    }

    /**
     * Lee el archivo linea por linea y alimenta el repositorio y la matriz.
     * Implementa control de duplicados (Decision 6): ignora lectura si ya
     * existe registro para la misma estacion en la misma hora.
     */
    private static void cargarArchivo(RepositorioLecturas repositorio,
                                      AnalizadorMatriz analizador) throws IOException {
        BufferedReader lector = new BufferedReader(new FileReader(ARCHIVO));
        lector.readLine(); // encabezado

        String linea;
        while ((linea = lector.readLine()) != null) {
            LecturaSensor lectura = construirLectura(linea);
            if (lectura == null) {
                continue;
            }
            if (!lectura.esValida()) {
                descartadasPorRango++;
                continue;
            }
            // Decision 6: control de duplicados (misma estacion + misma hora)
            int idxEstacion = indiceDeEstacion(lectura.getIdSensor());
            int hora = lectura.getHora();
            if (idxEstacion >= 0 && idxEstacion < NUM_ESTACIONES
                    && hora >= 0 && hora < 24
                    && yaRegistrado[idxEstacion][hora]) {
                descartadasPorDuplicado++;
                continue;
            }
            if (idxEstacion >= 0 && idxEstacion < NUM_ESTACIONES
                    && hora >= 0 && hora < 24) {
                yaRegistrado[idxEstacion][hora] = true;
            }
            repositorio.agregar(lectura);
            analizador.registrar(lectura);
        }
        lector.close();
    }

    /**
     * Convierte "EST-004" en el indice de fila 3.
     * @return indice de 0 a 8, o -1 si el formato es invalido
     */
    private static int indiceDeEstacion(String idSensor) {
        try {
            String numero = idSensor.substring(4);
            return Integer.parseInt(numero) - 1;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Convierte una linea del CSV en un objeto LecturaSensor.
     * @return la lectura, o null si la linea esta mal formada
     */
    private static LecturaSensor construirLectura(String linea) {
        String[] campos = linea.split(",");
        if (campos.length != CAMPOS_ESPERADOS) {
            descartadasPorFormato++;
            return null;
        }
        try {
            double temperatura = Double.parseDouble(campos[2]);
            double humedad = Double.parseDouble(campos[3]);
            double pm25 = Double.parseDouble(campos[4]);
            return new LecturaSensor(campos[0], campos[1], temperatura, humedad, pm25);
        } catch (NumberFormatException e) {
            descartadasPorFormato++;
            return null;
        }
    }
}
