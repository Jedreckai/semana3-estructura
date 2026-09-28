/* ============================================================
   PLATAFORMA DE MONITOREO AMBIENTAL URBANO
   TAD RepositorioLecturas - VERSION 1.0 COMPLETA

   Tipo Abstracto de Dato que almacena lecturas de sensores
   en un arreglo dinamico con redimensionamiento por duplicacion.

   Operaciones publicas: agregar, obtener, buscarPorEstacion,
   actualizar, eliminar, tamano, promedioPm25.
   ============================================================ */

public class RepositorioLecturas {

    private static final int CAPACIDAD_INICIAL = 10;

    private LecturaSensor[] lecturas;
    private int cantidad;
    // Metricas internas (contrato seccion 3)
    private int copiasRealizadas;
    private int redimensionamientos;

    public RepositorioLecturas() {
        this.lecturas = new LecturaSensor[CAPACIDAD_INICIAL];
        this.cantidad = 0;
        this.copiasRealizadas = 0;
        this.redimensionamientos = 0;
    }

    // ---------- OPERACIONES DEL CONTRATO ----------

    /**
     * Agrega una lectura al final del repositorio.
     * Si el arreglo alcanza su capacidad, invoca redimensionar().
     * @return true si se agrego, false si lectura es null
     */
    public boolean agregar(LecturaSensor lectura) {
        if (lectura == null) {
            return false;
        }
        if (cantidad == lecturas.length) {
            redimensionar();
        }
        lecturas[cantidad] = lectura;
        cantidad++;
        return true;
    }

    /**
     * Devuelve la lectura que esta en la posicion indicada.
     * @return la lectura, o null si la posicion esta fuera de rango
     */
    public LecturaSensor obtener(int posicion) {
        if (posicion < 0 || posicion >= cantidad) {
            return null;
        }
        return lecturas[posicion];
    }

    /**
     * Cantidad de lecturas almacenadas actualmente.
     */
    public int tamano() {
        return cantidad;
    }

    /**
     * Elimina la lectura de la posicion indicada usando compactacion:
     * desplaza elementos posteriores una posicion a la izquierda.
     * @return true si se elimino, false si la posicion esta fuera de rango
     */
    public boolean eliminar(int posicion) {
        if (posicion < 0 || posicion >= cantidad) {
            return false;
        }

        for (int i = posicion; i < cantidad - 1; i++) {
            lecturas[i] = lecturas[i + 1];
        }

        lecturas[cantidad - 1] = null;
        cantidad--;
        return true;
    }

    /**
     * Busca la primera lectura de una estacion.
     * @return la lectura encontrada, o null si no existe
     */
    public LecturaSensor buscarPorEstacion(String idSensor) {
        if (idSensor == null) {
            return null;
        }
        for (int i = 0; i < cantidad; i++) {
            if (lecturas[i].getIdSensor().equals(idSensor)) {
                return lecturas[i];
            }
        }
        return null;
    }

    /**
     * Reemplaza la lectura de una posicion por otra.
     * @return true si se actualizo, false si posicion invalida o nueva es null
     */
    public boolean actualizar(int posicion, LecturaSensor nueva) {
        if (posicion < 0 || posicion >= cantidad || nueva == null) {
            return false;
        }
        lecturas[posicion] = nueva;
        return true;
    }

    /**
     * Duplica la capacidad interna del arreglo conservando el contenido.
     * Crea un arreglo nuevo del doble de tamano y copia las referencias.
     */
    private void redimensionar() {
        LecturaSensor[] nuevo = new LecturaSensor[lecturas.length * 2];
        for (int i = 0; i < cantidad; i++) {
            nuevo[i] = lecturas[i];
            copiasRealizadas++;
        }
        lecturas = nuevo;
        redimensionamientos++;
    }

    /**
     * Promedio de PM2.5 de todas las lecturas almacenadas.
     * Usa 'cantidad' (no lecturas.length) para no incluir posiciones vacias.
     */
    public double promedioPm25() {
        if (cantidad == 0) {
            return 0;
        }

        double suma = 0;

        for (int i = 0; i < cantidad; i++) {
            suma = suma + lecturas[i].getPm25();
        }

        return suma / cantidad;
    }

    /**
     * Retorna el numero de redimensionamientos realizados (metrica interna).
     */
    public int getRedimensionamientos() {
        return redimensionamientos;
    }

    /**
     * Retorna el numero total de copias de referencias realizadas (metrica interna).
     */
    public int getCopiasRealizadas() {
        return copiasRealizadas;
    }
}
