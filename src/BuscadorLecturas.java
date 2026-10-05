/* ============================================================
   PLATAFORMA DE MONITOREO AMBIENTAL URBANO
   BuscadorLecturas

   Supuesto: BancoDeOrdenamiento (semana 4) usa esta clase de la
   semana 3, pero no estaba en el repositorio. Se implementa lo
   minimo que el experimento 5 necesita: busqueda lineal y binaria
   por timestamp, con contador de comparaciones.
   ============================================================ */

public class BuscadorLecturas {

    private static long comparaciones = 0;

    public static long getComparaciones() { return comparaciones; }

    /**
     * Busqueda lineal por timestamp. No exige ningun orden.
     * @return posicion de la lectura, o -1 si no existe
     */
    public static int busquedaLinealPorTimestamp(LecturaSensor[] datos, String timestamp) {
        comparaciones = 0;
        for (int i = 0; i < datos.length; i++) {
            comparaciones++;
            if (datos[i].getTimestamp().equals(timestamp)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Busqueda binaria por timestamp.
     * PRECONDICION: el arreglo debe estar ordenado ascendentemente por timestamp.
     * @return posicion de la lectura, o -1 si no la encuentra
     */
    public static int busquedaBinariaPorTimestamp(LecturaSensor[] datos, String timestamp) {
        comparaciones = 0;
        int inicio = 0;
        int fin = datos.length - 1;
        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            comparaciones++;
            int resultado = datos[medio].getTimestamp().compareTo(timestamp);
            if (resultado == 0) {
                return medio;
            } else if (resultado < 0) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return -1;
    }
}
