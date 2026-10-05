/* ============================================================
   PLATAFORMA DE MONITOREO AMBIENTAL URBANO
   BancoDeOrdenamiento - VERSION 1.0

   Cinco experimentos de la semana 4.

   Cambios frente a la version entregada:
   - Se quito main(): el proyecto tiene un solo punto de entrada,
     IngestaSensores.main(), que llama a estos experimentos
     (Guia de implementacion, secciones 20 y 28).
   - Exp 2 muestra burbuja ANTES y DESPUES de la bandera (TODO 1).
   - Exp 3 calcula las razones de crecimiento n x10.
   - Exp 4 repite el caso B con mediana de tres (TODO 2).
   - Exp 5 agrega el paso 4: ranking sobre una copia (TODO 3 / DEC-05).
   ============================================================ */

import java.util.Random;

public class BancoDeOrdenamiento {

    /** Ejecuta los cinco experimentos en orden. */
    public void ejecutarTodos() {
        experimentoUno();
        experimentoDos();
        experimentoTres();
        experimentoCuatro();
        experimentoCinco();
    }

    // ---------- utilidades ----------

    /** Copia el arreglo para que cada algoritmo empiece en igualdad de condiciones. */
    private static LecturaSensor[] copiar(LecturaSensor[] original) {
        LecturaSensor[] copia = new LecturaSensor[original.length];
        System.arraycopy(original, 0, copia, 0, original.length);
        return copia;
    }

    /** Desordena un arreglo con una semilla fija, para que el experimento sea repetible. */
    private static LecturaSensor[] desordenar(LecturaSensor[] original) {
        LecturaSensor[] copia = copiar(original);
        Random azar = new Random(777L);
        for (int i = copia.length - 1; i > 0; i--) {
            int j = azar.nextInt(i + 1);
            LecturaSensor t = copia[i]; copia[i] = copia[j]; copia[j] = t;
        }
        return copia;
    }

    private static void reportar(String nombre, long milis) {
        System.out.printf("%-22s comparaciones: %,14d   intercambios: %,14d   %6d ms%n",
                nombre, Ordenador.getComparaciones(), Ordenador.getIntercambios(), milis);
    }

    /** Verificacion de correccion: el resultado debe quedar ordenado. */
    private static void verificar(LecturaSensor[] datos) {
        if (!Ordenador.estaOrdenadoPorTimestamp(datos)) {
            System.out.println("   ** ERROR: el arreglo NO quedo ordenado **");
        }
    }

    // ---------- EXPERIMENTO 1 ----------

    /** Los tres algoritmos simples sobre 10.000 lecturas DESORDENADAS. */
    public void experimentoUno() {
        System.out.println("=== EXP 1: ALGORITMOS SIMPLES, 10.000 LECTURAS DESORDENADAS ===");
        LecturaSensor[] base = desordenar(GeneradorDatos.generar(10_000));

        LecturaSensor[] a = copiar(base);
        long t = System.currentTimeMillis();
        Ordenador.burbuja(a);
        reportar("Burbuja", System.currentTimeMillis() - t);
        verificar(a);

        LecturaSensor[] b = copiar(base);
        t = System.currentTimeMillis();
        Ordenador.seleccion(b);
        reportar("Seleccion", System.currentTimeMillis() - t);
        verificar(b);

        LecturaSensor[] c = copiar(base);
        t = System.currentTimeMillis();
        Ordenador.insercion(c);
        reportar("Insercion", System.currentTimeMillis() - t);
        verificar(c);
        System.out.println();
    }

    // ---------- EXPERIMENTO 2 ----------

    /** Los mismos tres algoritmos sobre datos que YA VIENEN ORDENADOS. */
    public void experimentoDos() {
        System.out.println("=== EXP 2: LOS MISMOS TRES, PERO CON DATOS YA ORDENADOS ===");
        System.out.println("(asi es como llegan de la red de sensores: en orden cronologico)");
        LecturaSensor[] base = GeneradorDatos.generar(10_000);

        LecturaSensor[] a0 = copiar(base);
        long t = System.currentTimeMillis();
        Ordenador.burbujaSinBandera(a0);
        reportar("Burbuja (sin bandera)", System.currentTimeMillis() - t);

        LecturaSensor[] a = copiar(base);
        t = System.currentTimeMillis();
        Ordenador.burbuja(a);
        reportar("Burbuja (con bandera)", System.currentTimeMillis() - t);

        LecturaSensor[] b = copiar(base);
        t = System.currentTimeMillis();
        Ordenador.seleccion(b);
        reportar("Seleccion", System.currentTimeMillis() - t);

        LecturaSensor[] c = copiar(base);
        t = System.currentTimeMillis();
        Ordenador.insercion(c);
        reportar("Insercion", System.currentTimeMillis() - t);
        System.out.println();
    }

    // ---------- EXPERIMENTO 3 ----------

    /** Simples contra avanzados, a escala creciente, con razones de crecimiento. */
    public void experimentoTres() {
        System.out.println("=== EXP 3: SIMPLES CONTRA AVANZADOS ===");
        int[] tamanos = {1_000, 10_000, 100_000};
        String[] nombres = {"Insercion", "MergeSort", "HeapSort"};
        long[][] comps = new long[nombres.length][tamanos.length];

        for (int k = 0; k < tamanos.length; k++) {
            int n = tamanos[k];
            System.out.println("-- " + String.format("%,d", n) + " lecturas desordenadas --");
            LecturaSensor[] base = desordenar(GeneradorDatos.generar(n));

            LecturaSensor[] a = copiar(base);
            long t = System.currentTimeMillis();
            Ordenador.insercion(a);
            reportar("Insercion", System.currentTimeMillis() - t);
            comps[0][k] = Ordenador.getComparaciones();
            verificar(a);

            LecturaSensor[] b = copiar(base);
            t = System.currentTimeMillis();
            Ordenador.mergeSort(b);
            reportar("MergeSort", System.currentTimeMillis() - t);
            comps[1][k] = Ordenador.getComparaciones();
            verificar(b);

            LecturaSensor[] c = copiar(base);
            t = System.currentTimeMillis();
            Ordenador.heapSort(c);
            reportar("HeapSort", System.currentTimeMillis() - t);
            comps[2][k] = Ordenador.getComparaciones();
            verificar(c);
            System.out.println();
        }

        System.out.println("-- Razones de crecimiento de comparaciones (n x10) --");
        for (int i = 0; i < nombres.length; i++) {
            System.out.printf("%-10s  10k/1k = %6.1f   100k/10k = %6.1f%n", nombres[i],
                    (double) comps[i][1] / comps[i][0],
                    (double) comps[i][2] / comps[i][1]);
        }
        System.out.println("(O(n^2) deberia acercarse a 100; O(n log n) a ~13)");
        System.out.println();
    }

    // ---------- EXPERIMENTO 4 ----------

    /** QuickSort con pivote fijo vs mediana de tres, con datos desordenados y reales. */
    public void experimentoCuatro() {
        System.out.println("=== EXP 4: QUICKSORT CON PIVOTE = PRIMER ELEMENTO ===");

        System.out.println("-- Caso A: 50.000 lecturas DESORDENADAS --");
        LecturaSensor[] revueltas = desordenar(GeneradorDatos.generar(50_000));
        long t = System.currentTimeMillis();
        Ordenador.quickSortPivotePrimero(revueltas);
        reportar("QuickSort", System.currentTimeMillis() - t);

        System.out.println();
        System.out.println("-- Caso B: 50.000 lecturas EN ORDEN CRONOLOGICO (como llegan de la red) --");
        LecturaSensor[] enOrden = GeneradorDatos.generar(50_000);
        try {
            t = System.currentTimeMillis();
            Ordenador.quickSortPivotePrimero(enOrden);
            reportar("QuickSort", System.currentTimeMillis() - t);
        } catch (StackOverflowError e) {
            System.out.println("QuickSort      -> StackOverflowError: el programa se quedo sin pila.");
            System.out.println("                  Comparaciones alcanzadas antes de morir: "
                    + String.format("%,d", Ordenador.getComparaciones()));
        }
        System.out.println();

        System.out.println("=== EXP 4 (TODO 2): QUICKSORT CON PIVOTE = MEDIANA DE TRES ===");
        System.out.println("-- Caso A: 50.000 lecturas DESORDENADAS --");
        revueltas = desordenar(GeneradorDatos.generar(50_000));
        t = System.currentTimeMillis();
        Ordenador.quickSort(revueltas);
        reportar("QuickSort mediana3", System.currentTimeMillis() - t);
        verificar(revueltas);

        System.out.println("-- Caso B: 50.000 lecturas EN ORDEN CRONOLOGICO --");
        enOrden = GeneradorDatos.generar(50_000);
        t = System.currentTimeMillis();
        Ordenador.quickSort(enOrden);
        reportar("QuickSort mediana3", System.currentTimeMillis() - t);
        verificar(enOrden);
        System.out.println();
    }

    // ---------- EXPERIMENTO 5 ----------

    /** Ordenar por PM2.5 para el ranking... y consultar por timestamp despues. */
    public void experimentoCinco() {
        System.out.println("=== EXP 5: EL RANKING Y LA CONSULTA ===");

        LecturaSensor[] datos = GeneradorDatos.generar(100_000);
        String objetivo = GeneradorDatos.timestampEnPosicion(73_412);

        System.out.println("Paso 1. Los datos llegan de la red en orden cronologico.");
        System.out.println("        Ordenado por timestamp: " + Ordenador.estaOrdenadoPorTimestamp(datos));
        int pos = BuscadorLecturas.busquedaBinariaPorTimestamp(datos, objetivo);
        System.out.println("        Consulta binaria por timestamp -> posicion: " + pos
                + "  (comparaciones: " + BuscadorLecturas.getComparaciones() + ")");

        // Paso 4 (solucion): se hace ANTES de destruir el arreglo, sobre una copia.
        System.out.println();
        System.out.println("Paso 2 (SOLUCION DEC-05). Ranking sobre una COPIA con rankingPorPm25().");
        LecturaSensor[] ranking = Ordenador.rankingPorPm25(datos);
        System.out.println("        Ranking ordenado por PM2.5: " + Ordenador.estaOrdenadoPorPm25(ranking)
                + "  (comparaciones: " + String.format("%,d", Ordenador.getComparaciones()) + ")");
        System.out.println("        PM2.5 mas bajo: " + ranking[0].getPm25()
                + " | mas alto: " + ranking[ranking.length - 1].getPm25());
        System.out.println("        Original sigue ordenado por timestamp: "
                + Ordenador.estaOrdenadoPorTimestamp(datos));
        pos = BuscadorLecturas.busquedaBinariaPorTimestamp(datos, objetivo);
        System.out.println("        Consulta binaria por timestamp -> posicion: " + pos
                + "  (comparaciones: " + BuscadorLecturas.getComparaciones() + ")");

        System.out.println();
        System.out.println("Paso 3 (PROBLEMA). El area de comunicaciones pide el ranking de estaciones");
        System.out.println("        mas contaminadas. Ordenamos EL MISMO arreglo por PM2.5.");
        Ordenador.ordenarPorPm25(datos);
        System.out.println("        Ranking listo. PM2.5 mas bajo: " + datos[0].getPm25()
                + " | mas alto: " + datos[datos.length - 1].getPm25());

        System.out.println();
        System.out.println("Paso 4. Otro usuario vuelve a consultar la misma lectura de siempre.");
        System.out.println("        Ordenado por timestamp: " + Ordenador.estaOrdenadoPorTimestamp(datos));
        pos = BuscadorLecturas.busquedaBinariaPorTimestamp(datos, objetivo);
        System.out.println("        Consulta binaria por timestamp -> posicion: " + pos
                + "  (comparaciones: " + BuscadorLecturas.getComparaciones() + ")");

        System.out.println();
        System.out.println("        Verificacion con busqueda lineal -> posicion: "
                + BuscadorLecturas.busquedaLinealPorTimestamp(datos, objetivo)
                + "  (comparaciones: " + BuscadorLecturas.getComparaciones() + ")");
        System.out.println("        => La lectura existe, pero la binaria perdio su precondicion.");
        System.out.println();
    }
}
