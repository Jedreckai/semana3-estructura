/* ============================================================
   PLATAFORMA DE MONITOREO AMBIENTAL URBANO
   Ordenador - VERSION 1.0 (TODO 1, 2 y 3 resueltos)

   La semana pasada descubriste que la busqueda binaria exige
   datos ordenados. Esta semana averiguas cuanto cuesta eso.

   Todos los metodos estan instrumentados: cuentan comparaciones
   e intercambios. No quites los contadores.

   ADVERTENCIA: dos de estos algoritmos tienen defectos y uno
   de ellos hace explotar el programa con los datos REALES de
   la red, no con datos raros.
   ============================================================ */

public class Ordenador {

    private static long comparaciones = 0;
    private static long intercambios = 0;

    public static long getComparaciones() { return comparaciones; }
    public static long getIntercambios()  { return intercambios; }
    public static void reiniciarContadores() { comparaciones = 0; intercambios = 0; }

    // =========================================================
    //  UTILIDADES
    // =========================================================

    private static void intercambiar(LecturaSensor[] datos, int i, int j) {
        LecturaSensor temporal = datos[i];
        datos[i] = datos[j];
        datos[j] = temporal;
        intercambios++;
    }

    /** Compara dos lecturas por su timestamp. */
    private static int comparar(LecturaSensor a, LecturaSensor b) {
        comparaciones++;
        return a.getTimestamp().compareTo(b.getTimestamp());
    }

    /** Compara dos lecturas por su valor de PM2.5. */
    private static int compararPorPm25(LecturaSensor a, LecturaSensor b) {
        comparaciones++;
        return Double.compare(a.getPm25(), b.getPm25());
    }

    // =========================================================
    //  2.7  ALGORITMOS SIMPLES
    // =========================================================

    /**
     * Ordenamiento burbuja CON corte temprano.
     *
     * TODO 1 (RESUELTO): si en una pasada completa no hubo ningun
     * intercambio, el arreglo ya esta ordenado y se corta ahi.
     * Con datos ordenados hace n-1 comparaciones en vez de n(n-1)/2.
     */
    public static void burbuja(LecturaSensor[] datos) {
        reiniciarContadores();
        int n = datos.length;
        for (int i = 0; i < n - 1; i++) {
            boolean huboIntercambio = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (comparar(datos[j], datos[j + 1]) > 0) {
                    intercambiar(datos, j, j + 1);
                    huboIntercambio = true;
                }
            }
            if (!huboIntercambio) {
                break;
            }
        }
    }

    /**
     * Version ORIGINAL de burbuja (sin bandera). Se conserva solo para
     * medir el "antes" del TODO 1 en el experimento 2.
     */
    public static void burbujaSinBandera(LecturaSensor[] datos) {
        reiniciarContadores();
        int n = datos.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (comparar(datos[j], datos[j + 1]) > 0) {
                    intercambiar(datos, j, j + 1);
                }
            }
        }
    }

    /**
     * Ordenamiento por seleccion. Busca el menor y lo pone al inicio.
     * Este metodo esta correcto. Observa cuantos intercambios hace.
     */
    public static void seleccion(LecturaSensor[] datos) {
        reiniciarContadores();
        int n = datos.length;
        for (int i = 0; i < n - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < n; j++) {
                if (comparar(datos[j], datos[menor]) < 0) {
                    menor = j;
                }
            }
            if (menor != i) {
                intercambiar(datos, i, menor);
            }
        }
    }

    /**
     * Ordenamiento por insercion. Inserta cada elemento en su lugar
     * dentro de la parte ya ordenada, como quien organiza cartas.
     * Este metodo esta correcto.
     */
    public static void insercion(LecturaSensor[] datos) {
        reiniciarContadores();
        for (int i = 1; i < datos.length; i++) {
            LecturaSensor actual = datos[i];
            int j = i - 1;
            while (j >= 0 && comparar(datos[j], actual) > 0) {
                datos[j + 1] = datos[j];
                intercambios++;
                j--;
            }
            datos[j + 1] = actual;
        }
    }

    // =========================================================
    //  2.8  ALGORITMOS AVANZADOS
    // =========================================================

    /**
     * MergeSort. Divide el arreglo por la mitad, ordena cada parte
     * y luego fusiona las dos partes ordenadas. Este metodo esta correcto.
     *
     * Fijate en la idea: es la misma de la busqueda binaria, dividir
     * el problema en dos, pero aplicada a ordenar en vez de a buscar.
     */
    public static void mergeSort(LecturaSensor[] datos) {
        reiniciarContadores();
        LecturaSensor[] auxiliar = new LecturaSensor[datos.length];
        mergeSortRecursivo(datos, auxiliar, 0, datos.length - 1, false);
    }

    private static void mergeSortRecursivo(LecturaSensor[] datos, LecturaSensor[] aux,
                                           int inicio, int fin, boolean porPm25) {
        if (inicio >= fin) return;
        int medio = inicio + (fin - inicio) / 2;
        mergeSortRecursivo(datos, aux, inicio, medio, porPm25);
        mergeSortRecursivo(datos, aux, medio + 1, fin, porPm25);
        fusionar(datos, aux, inicio, medio, fin, porPm25);
    }

    private static void fusionar(LecturaSensor[] datos, LecturaSensor[] aux,
                                 int inicio, int medio, int fin, boolean porPm25) {
        for (int i = inicio; i <= fin; i++) aux[i] = datos[i];
        int izq = inicio, der = medio + 1;
        for (int k = inicio; k <= fin; k++) {
            if (izq > medio) {
                datos[k] = aux[der++];
            } else if (der > fin) {
                datos[k] = aux[izq++];
            } else if ((porPm25 ? compararPorPm25(aux[der], aux[izq])
                                : comparar(aux[der], aux[izq])) < 0) {
                datos[k] = aux[der++];
            } else {
                datos[k] = aux[izq++];
            }
            intercambios++;
        }
    }

    /**
     * QuickSort con el PRIMER elemento como pivote (version original).
     *
     * Se conserva sin cambios para reproducir el "antes" del TODO 2:
     * con datos ya ordenados cada particion deja 0 elementos a la
     * izquierda y n-1 a la derecha -> recursion de profundidad n
     * -> StackOverflowError con 50.000 lecturas.
     */
    public static void quickSortPivotePrimero(LecturaSensor[] datos) {
        reiniciarContadores();
        quickRecursivo(datos, 0, datos.length - 1, false);
    }

    /**
     * TODO 2 (RESUELTO): QuickSort con pivote = MEDIANA DE TRES
     * (primero, medio, ultimo). Con datos ordenados la mediana es el
     * elemento del medio, asi que las particiones quedan balanceadas.
     *
     * Supuesto: se eligio mediana de tres en vez de pivote aleatorio
     * porque es determinista (los experimentos dan siempre los mismos
     * numeros) y resuelve justo el caso real: datos cronologicos.
     */
    public static void quickSort(LecturaSensor[] datos) {
        reiniciarContadores();
        quickRecursivo(datos, 0, datos.length - 1, true);
    }

    private static void quickRecursivo(LecturaSensor[] datos, int inicio, int fin,
                                       boolean medianaDeTres) {
        if (inicio >= fin) return;
        if (medianaDeTres) {
            colocarMedianaDeTres(datos, inicio, fin);
        }
        int posicionPivote = particionar(datos, inicio, fin);
        quickRecursivo(datos, inicio, posicionPivote - 1, medianaDeTres);
        quickRecursivo(datos, posicionPivote + 1, fin, medianaDeTres);
    }

    /** Lleva la mediana de (inicio, medio, fin) a la posicion inicio. */
    private static void colocarMedianaDeTres(LecturaSensor[] datos, int inicio, int fin) {
        int medio = inicio + (fin - inicio) / 2;
        int a = inicio, b = medio, c = fin;
        int mediana;
        if (comparar(datos[a], datos[b]) <= 0) {
            if (comparar(datos[b], datos[c]) <= 0) mediana = b;          // a <= b <= c
            else if (comparar(datos[a], datos[c]) <= 0) mediana = c;     // a <= c < b
            else mediana = a;                                            // c < a <= b
        } else {
            if (comparar(datos[a], datos[c]) <= 0) mediana = a;          // b < a <= c
            else if (comparar(datos[b], datos[c]) <= 0) mediana = c;     // b <= c < a
            else mediana = b;                                            // c < b < a
        }
        if (mediana != inicio) {
            intercambiar(datos, inicio, mediana);
        }
    }

    private static int particionar(LecturaSensor[] datos, int inicio, int fin) {
        LecturaSensor pivote = datos[inicio];
        int limite = inicio;
        for (int i = inicio + 1; i <= fin; i++) {
            if (comparar(datos[i], pivote) < 0) {
                limite++;
                intercambiar(datos, limite, i);
            }
        }
        intercambiar(datos, inicio, limite);
        return limite;
    }

    /**
     * HeapSort. Ordena usando una estructura llamada monticulo.
     * Este metodo esta correcto y funciona como caja negra por ahora:
     * la estructura que usa por dentro la vas a construir en la semana 6.
     */
    public static void heapSort(LecturaSensor[] datos) {
        reiniciarContadores();
        int n = datos.length;
        for (int i = n / 2 - 1; i >= 0; i--) hundir(datos, n, i);
        for (int i = n - 1; i > 0; i--) {
            intercambiar(datos, 0, i);
            hundir(datos, i, 0);
        }
    }

    private static void hundir(LecturaSensor[] datos, int tamano, int raiz) {
        int mayor = raiz;
        int izq = 2 * raiz + 1;
        int der = 2 * raiz + 2;
        if (izq < tamano && comparar(datos[izq], datos[mayor]) > 0) mayor = izq;
        if (der < tamano && comparar(datos[der], datos[mayor]) > 0) mayor = der;
        if (mayor != raiz) {
            intercambiar(datos, raiz, mayor);
            hundir(datos, tamano, mayor);
        }
    }

    // =========================================================
    //  ORDENAR POR OTRO CRITERIO
    // =========================================================

    /**
     * Ordena las lecturas por concentracion de PM2.5, de menor a mayor.
     * Sirve para construir el ranking de estaciones mas contaminadas.
     *
     * TODO 3 (ANALIZADO): el metodo es correcto, pero ordena EN SITIO.
     * Al reordenar el arreglo por PM2.5 se destruye el orden por
     * timestamp y la busqueda binaria por timestamp pierde su
     * precondicion (efecto colateral entre modulos). Se conserva tal
     * cual para reproducir el problema en el experimento 5; la
     * solucion adoptada es rankingPorPm25() (ver DEC-05).
     */
    public static void ordenarPorPm25(LecturaSensor[] datos) {
        reiniciarContadores();
        for (int i = 1; i < datos.length; i++) {
            LecturaSensor actual = datos[i];
            int j = i - 1;
            while (j >= 0 && compararPorPm25(datos[j], actual) > 0) {
                datos[j + 1] = datos[j];
                intercambios++;
                j--;
            }
            datos[j + 1] = actual;
        }
    }

    /**
     * SOLUCION al TODO 3 (Estrategia A - trabajar sobre una copia).
     * Devuelve una COPIA ordenada por PM2.5 (MergeSort, estable y
     * O(n log n)); el arreglo original NO se modifica y sigue
     * ordenado por timestamp para la busqueda binaria.
     */
    public static LecturaSensor[] rankingPorPm25(LecturaSensor[] datos) {
        reiniciarContadores();
        LecturaSensor[] copia = new LecturaSensor[datos.length];
        System.arraycopy(datos, 0, copia, 0, datos.length);
        LecturaSensor[] auxiliar = new LecturaSensor[copia.length];
        mergeSortRecursivo(copia, auxiliar, 0, copia.length - 1, true);
        return copia;
    }

    /** Verifica si un arreglo esta ordenado ascendentemente por PM2.5. */
    public static boolean estaOrdenadoPorPm25(LecturaSensor[] datos) {
        for (int i = 1; i < datos.length; i++) {
            if (datos[i - 1].getPm25() > datos[i].getPm25()) {
                return false;
            }
        }
        return true;
    }

    /** Verifica si un arreglo esta ordenado ascendentemente por timestamp. */
    public static boolean estaOrdenadoPorTimestamp(LecturaSensor[] datos) {
        for (int i = 1; i < datos.length; i++) {
            if (datos[i - 1].getTimestamp().compareTo(datos[i].getTimestamp()) > 0) {
                return false;
            }
        }
        return true;
    }
}
