# Guía Paso a Paso de Implementación en Java — Semana 4
## Ordenamientos y comparación de eficiencia

**Espacio académico:** Estructuras de Datos · **Programa:** IDIA, II semestre  
**Hito:** H1

> **Nota importante:** los documentos suministrados definen los objetivos, experimentos, archivos y TODO de la semana, pero no incluyen el contenido completo de `Ordenador.java` ni las firmas exactas del código base. Los ejemplos de esta guía muestran una implementación compatible conceptualmente con el reto. Si el esqueleto entregado por el docente utiliza otros nombres o firmas, conserva esas firmas y adapta la lógica.

---

# 1. Resultado que vamos a construir

La Semana 4 debe incorporar:

```text
Ordenador.java
    ├── Burbuja
    ├── Selección
    ├── Inserción
    ├── MergeSort
    ├── HeapSort
    └── QuickSort

BancoDeOrdenamiento.java
    ├── experimentoUno
    ├── experimentoDos
    ├── experimentoTres
    ├── experimentoCuatro
    └── experimentoCinco
```

Y mantener:

```text
IngestaSensores.java
        ↓
      main()
```

como **único punto de entrada** del proyecto.

---

# 2. Crear la rama

Primero:

```bash
git switch main
git pull
git status
```

Después:

```bash
git switch -c feature/semana-4-ordenamientos
```

No desarrolles directamente sobre `main`.

---

# 3. Verificar las semanas anteriores

Antes de tocar código nuevo:

```bash
javac *.java
java IngestaSensores
```

Comprueba que funcionan las clases existentes:

```text
LecturaSensor
RepositorioLecturas
AnalizadorMatriz
BuscadorLecturas
GeneradorDatos
BancoDePruebas
IngestaSensores
```

La Semana 4 debe **integrarse** con las semanas 2 y 3; no reemplazarlas.

---

# 4. Crear `Ordenador.java`

Una estructura mínima puede comenzar así:

```java
public class Ordenador {

    private long comparaciones;
    private long intercambios;

    public Ordenador() {
        reiniciarContadores();
    }

    public void reiniciarContadores() {
        comparaciones = 0;
        intercambios = 0;
    }

    public long getComparaciones() {
        return comparaciones;
    }

    public long getIntercambios() {
        return intercambios;
    }

    private void registrarComparacion() {
        comparaciones++;
    }

    private void registrarIntercambio() {
        intercambios++;
    }
}
```

Los contadores permitirán estudiar el comportamiento de los algoritmos.

---

# 5. Crear el intercambio

Si se ordenan objetos `LecturaSensor`:

```java
private void intercambiar(
        LecturaSensor[] datos,
        int i,
        int j) {

    LecturaSensor temporal = datos[i];
    datos[i] = datos[j];
    datos[j] = temporal;

    registrarIntercambio();
}
```

Centralizar el intercambio permite contar cuántos movimientos de este tipo realiza cada algoritmo.

---

# 6. Definir una comparación

Para PM2.5:

```java
private boolean mayorPM25(
        LecturaSensor a,
        LecturaSensor b) {

    registrarComparacion();

    return a.getPm25() > b.getPm25();
}
```

Para el elemento menor:

```java
private boolean menorPM25(
        LecturaSensor a,
        LecturaSensor b) {

    registrarComparacion();

    return a.getPm25() < b.getPm25();
}
```

> Adapta `getPm25()` al nombre exacto utilizado por tu `LecturaSensor.java`.

---

# 7. Implementar Bubble Sort

Versión inicial:

```java
public void burbuja(LecturaSensor[] datos) {

    int n = datos.length;

    for (int pasada = 0; pasada < n - 1; pasada++) {

        for (int j = 0; j < n - 1 - pasada; j++) {

            if (mayorPM25(datos[j], datos[j + 1])) {
                intercambiar(datos, j, j + 1);
            }
        }
    }
}
```

Esta versión tiene intencionalmente el problema de la semana:

> No detecta que el arreglo ya está ordenado.

---

# 8. TODO 1 — Corte temprano

Modifica Bubble Sort:

```java
public void burbuja(LecturaSensor[] datos) {

    int n = datos.length;

    for (int pasada = 0; pasada < n - 1; pasada++) {

        boolean huboIntercambio = false;

        for (int j = 0; j < n - 1 - pasada; j++) {

            if (mayorPM25(datos[j], datos[j + 1])) {

                intercambiar(datos, j, j + 1);
                huboIntercambio = true;
            }
        }

        if (!huboIntercambio) {
            break;
        }
    }
}
```

La lógica es:

```text
Pasada
  ↓
¿Hubo intercambio?
  ├── Sí → continuar
  └── No → terminar
```

Con datos ordenados, el resultado esperado es aproximadamente:

```text
n - 1 comparaciones
```

---

# 9. Implementar Selection Sort

```java
public void seleccion(LecturaSensor[] datos) {

    int n = datos.length;

    for (int i = 0; i < n - 1; i++) {

        int posicionMenor = i;

        for (int j = i + 1; j < n; j++) {

            if (menorPM25(
                    datos[j],
                    datos[posicionMenor])) {

                posicionMenor = j;
            }
        }

        if (posicionMenor != i) {
            intercambiar(
                    datos,
                    i,
                    posicionMenor);
        }
    }
}
```

Comprueba que el algoritmo ordene correctamente.

---

# 10. Implementar Insertion Sort

```java
public void insercion(LecturaSensor[] datos) {

    for (int i = 1; i < datos.length; i++) {

        LecturaSensor actual = datos[i];
        int j = i - 1;

        while (j >= 0) {

            registrarComparacion();

            if (datos[j].getPm25()
                    <= actual.getPm25()) {
                break;
            }

            datos[j + 1] = datos[j];
            j--;
        }

        datos[j + 1] = actual;
    }
}
```

Prueba especialmente:

```text
datos desordenados
datos ordenados
datos casi ordenados
```

La tercera prueba es especialmente importante para comprender por qué inserción cambia tanto su comportamiento.

---

# 11. Crear una prueba pequeña

Antes de utilizar miles de lecturas, prueba con pocos elementos.

Por ejemplo:

```text
[5, 2, 8, 1, 4]
```

Resultado:

```text
[1, 2, 4, 5, 8]
```

No pases al siguiente algoritmo hasta comprobar que el actual produce un orden correcto.

---

# 12. Medir tiempo

Utiliza:

```java
long inicio = System.nanoTime();

ordenador.insercion(datos);

long fin = System.nanoTime();

double milisegundos =
        (fin - inicio) / 1_000_000.0;
```

Pero recuerda:

> El tiempo no es la única métrica.

Debes conservar:

```text
comparaciones
intercambios
tiempo
```

---

# 13. Trabajar con copias

Los algoritmos modifican el arreglo.

Por eso, para comparar algoritmos justamente:

```java
LecturaSensor[] original =
        GeneradorDatos.generarLecturas(10000);

LecturaSensor[] datosBurbuja =
        original.clone();

LecturaSensor[] datosSeleccion =
        original.clone();

LecturaSensor[] datosInsercion =
        original.clone();
```

Así todos comienzan con el mismo estado.

---

# 14. Implementar MergeSort

Método principal:

```java
public void mergeSort(LecturaSensor[] datos) {

    if (datos.length <= 1) {
        return;
    }

    int medio = datos.length / 2;

    LecturaSensor[] izquierda =
            new LecturaSensor[medio];

    LecturaSensor[] derecha =
            new LecturaSensor[datos.length - medio];

    System.arraycopy(
            datos, 0,
            izquierda, 0,
            izquierda.length);

    System.arraycopy(
            datos, medio,
            derecha, 0,
            derecha.length);

    mergeSort(izquierda);
    mergeSort(derecha);

    fusionar(datos, izquierda, derecha);
}
```

---

# 15. Implementar la fusión

```java
private void fusionar(
        LecturaSensor[] datos,
        LecturaSensor[] izquierda,
        LecturaSensor[] derecha) {

    int i = 0;
    int j = 0;
    int k = 0;

    while (i < izquierda.length
            && j < derecha.length) {

        registrarComparacion();

        if (izquierda[i].getPm25()
                <= derecha[j].getPm25()) {

            datos[k++] = izquierda[i++];

        } else {

            datos[k++] = derecha[j++];
        }
    }

    while (i < izquierda.length) {
        datos[k++] = izquierda[i++];
    }

    while (j < derecha.length) {
        datos[k++] = derecha[j++];
    }
}
```

Comprueba:

```text
división
ordenamiento recursivo
fusión
```

---

# 16. Implementar HeapSort

Método principal:

```java
public void heapSort(LecturaSensor[] datos) {

    int n = datos.length;

    for (int i = n / 2 - 1; i >= 0; i--) {
        heapify(datos, n, i);
    }

    for (int fin = n - 1; fin > 0; fin--) {

        intercambiar(datos, 0, fin);

        heapify(datos, fin, 0);
    }
}
```

Método `heapify`:

```java
private void heapify(
        LecturaSensor[] datos,
        int n,
        int raiz) {

    int mayor = raiz;
    int izquierda = 2 * raiz + 1;
    int derecha = 2 * raiz + 2;

    if (izquierda < n) {

        registrarComparacion();

        if (datos[izquierda].getPm25()
                > datos[mayor].getPm25()) {

            mayor = izquierda;
        }
    }

    if (derecha < n) {

        registrarComparacion();

        if (datos[derecha].getPm25()
                > datos[mayor].getPm25()) {

            mayor = derecha;
        }
    }

    if (mayor != raiz) {

        intercambiar(datos, raiz, mayor);

        heapify(datos, n, mayor);
    }
}
```

---

# 17. Implementar QuickSort

Método público:

```java
public void quickSort(LecturaSensor[] datos) {
    quickSort(
            datos,
            0,
            datos.length - 1);
}
```

Método recursivo:

```java
private void quickSort(
        LecturaSensor[] datos,
        int inicio,
        int fin) {

    if (inicio >= fin) {
        return;
    }

    int posicionPivote =
            particionar(datos, inicio, fin);

    quickSort(
            datos,
            inicio,
            posicionPivote - 1);

    quickSort(
            datos,
            posicionPivote + 1,
            fin);
}
```

---

# 18. QuickSort con pivote inicial

La idea inicial del experimento es utilizar el primer elemento:

```java
LecturaSensor pivote = datos[inicio];
```

Esto permite observar el problema planteado en la guía.

Si los datos están:

```text
ordenados cronológicamente
```

y el primer elemento es siempre el menor, las particiones pueden quedar extremadamente desbalanceadas.

---

# 19. TODO 2 — Cambiar el pivote

Puedes utilizar pivote aleatorio:

```java
int posicion =
        inicio
        + (int) (
            Math.random()
            * (fin - inicio + 1)
          );

intercambiar(
        datos,
        inicio,
        posicion);
```

Después de esto:

```java
LecturaSensor pivote =
        datos[inicio];
```

continúa la partición.

Otra opción es implementar mediana de tres:

```text
primero
medio
último
```

y colocar el valor intermedio en la posición del pivote.

La guía permite ambas alternativas.

---

# 20. Crear `BancoDeOrdenamiento.java`

Debe contener los cinco experimentos:

```java
public class BancoDeOrdenamiento {

    public void experimentoUno() {
        // simples con datos desordenados
    }

    public void experimentoDos() {
        // simples con datos ordenados
    }

    public void experimentoTres() {
        // inserción vs MergeSort vs HeapSort
    }

    public void experimentoCuatro() {
        // QuickSort
    }

    public void experimentoCinco() {
        // efecto colateral
    }
}
```

**No agregues `main()`.**

El proyecto mantiene un solo `main()` en `IngestaSensores.java`.

---

# 21. Experimento 1

Utiliza:

```text
10.000 lecturas desordenadas
```

Ejecuta:

```text
Burbuja
Selección
Inserción
```

Registra:

| Algoritmo | Comparaciones | Intercambios | Tiempo |
|---|---:|---:|---:|
| Burbuja | | | |
| Selección | | | |
| Inserción | | | |

No copies las cifras de referencia.

---

# 22. Experimento 2

Utiliza los mismos algoritmos, pero con:

```text
datos ordenados
```

Compara especialmente:

```text
antes de la bandera
después de la bandera
```

El objetivo es verificar que Bubble Sort corregido detecta que el arreglo ya está ordenado.

---

# 23. Experimento 3

Compara:

```text
Inserción
MergeSort
HeapSort
```

con:

```text
1.000
10.000
100.000
```

Tabla:

| n | Algoritmo | Comparaciones | Intercambios | Tiempo |
|---:|---|---:|---:|---:|
| 1.000 | Inserción | | | |
| 1.000 | MergeSort | | | |
| 1.000 | HeapSort | | | |
| 10.000 | Inserción | | | |
| 10.000 | MergeSort | | | |
| 10.000 | HeapSort | | | |
| 100.000 | Inserción | | | |
| 100.000 | MergeSort | | | |
| 100.000 | HeapSort | | | |

---

# 24. Calcular crecimiento

Para cada algoritmo calcula:

```text
medición(10.000) / medición(1.000)
```

y:

```text
medición(100.000) / medición(10.000)
```

No te quedes solamente con el tiempo.

El objetivo es observar cómo crece el trabajo.

---

# 25. Experimento 4

Prueba QuickSort con:

```text
Caso A:
50.000 desordenadas
```

y:

```text
Caso B:
50.000 ordenadas cronológicamente
```

Antes de modificar el pivote registra el resultado.

Después implementa:

```text
pivote aleatorio
```

o:

```text
mediana de tres
```

y repite el caso B.

---

# 26. Experimento 5

Conecta la Semana 4 con la búsqueda binaria.

Escenario:

```text
1. Datos ordenados por timestamp.
2. Búsqueda binaria.
3. Ordenamiento por PM2.5.
4. Nueva búsqueda binaria por timestamp.
5. Verificación con búsqueda lineal.
```

Comprueba que:

```text
la lectura sigue existiendo
```

pero:

```text
el arreglo ya no está ordenado
por el criterio que necesita
la búsqueda binaria.
```

---

# 27. Documentar decisiones

En:

```text
docs/decisiones.md
```

registra al menos:

### Decisión 1 — Pivote

```markdown
## DEC-04 — Pivote de QuickSort

**Semana:** 4

**Problema:**
QuickSort con pivote fijo puede producir particiones
desbalanceadas con datos ordenados.

**Alternativas:**
- Pivote aleatorio.
- Mediana de tres.

**Decisión:**
[Escribir la decisión del equipo]

**Justificación:**
[Explicar utilizando resultados]

**Consecuencia:**
[Explicar qué cambia]
```

### Decisión 2 — Ordenamiento por múltiples criterios

```markdown
## DEC-05 — Ordenamiento y búsqueda

**Semana:** 4

**Problema:**
Ordenar por PM2.5 destruye el orden por timestamp.

**Alternativas:**
- Copia.
- Restaurar orden.
- Índices separados.

**Decisión:**
[Escribir decisión]

**Justificación:**
[Escribir justificación]

**Consecuencia:**
[Escribir consecuencia]
```

---

# 28. Integrar sin crear otro `main()`

En `IngestaSensores.java`:

```java
BancoDeOrdenamiento banco =
        new BancoDeOrdenamiento();

banco.experimentoUno();
banco.experimentoDos();
banco.experimentoTres();
banco.experimentoCuatro();
banco.experimentoCinco();
```

La ubicación exacta debe respetar el flujo existente del proyecto.

No reemplaces el `main()` anterior.

---

# 29. Validación

Compila:

```bash
javac *.java
```

Ejecuta:

```bash
java IngestaSensores
```

Verifica:

```text
[ ] Ingesta funciona
[ ] Almacenamiento funciona
[ ] Búsqueda funciona
[ ] Ordenamientos funcionan
[ ] Experimentos funcionan
[ ] No existen errores de compilación
```

---

# 30. Gráfica

El H1 solicita una gráfica de crecimiento para al menos:

```text
un algoritmo cuadrático
+
un algoritmo avanzado
```

Una comparación recomendable:

```text
Inserción
vs.
MergeSort
```

Eje X:

```text
n
```

Eje Y:

```text
comparaciones
```

---

# 31. Actualizar la bitácora

Completa:

```text
docs/bitacora.html
```

en la pestaña:

```text
Semana 4
```

Registra:

- Objetivo.
- Trabajo realizado.
- Archivos modificados.
- Dificultades.
- Soluciones.
- Pruebas.
- Resultados.
- Aprendizajes.
- Evidencias.

La bitácora debe describir **el trabajo real del equipo**, no copiar esta guía.

---

# 32. Revisar Git

```bash
git status
git diff
```

Después:

```bash
git add .
git commit -m "feat: implementar ordenamientos de semana 4"
```

Si quieres separar los cambios:

```bash
git commit -m "feat: implementar ordenamientos simples"
git commit -m "fix: agregar corte temprano a burbuja"
git commit -m "feat: implementar ordenamientos avanzados"
git commit -m "docs: registrar decisiones de ordenamiento"
git commit -m "docs: actualizar bitacora de semana 4"
```

---

# 33. Integrar a `main`

Cuando todo funcione:

```bash
git switch main
git pull
git merge feature/semana-4-ordenamientos
```

Después:

```bash
javac *.java
java IngestaSensores
```

Comprueba nuevamente que las semanas anteriores continúan funcionando.

---

# 34. Crear H1

El H1 integra las semanas 2, 3 y 4.

Ejecuta:

```bash
git tag -a H1 -m "Entrega Hito 1: almacenamiento y consulta"
git push origin H1
```

Verifica:

```bash
git tag
```

Debe aparecer:

```text
H1
```

---

# 35. Checklist final

```text
[ ] Ordenador.java creado.
[ ] Burbuja implementado.
[ ] TODO 1: corte temprano implementado.
[ ] Selección implementado.
[ ] Inserción implementado.
[ ] MergeSort implementado.
[ ] HeapSort implementado.
[ ] QuickSort implementado.
[ ] TODO 2: pivote corregido.
[ ] Experimento 1 ejecutado.
[ ] Experimento 2 ejecutado.
[ ] Experimento 3 ejecutado.
[ ] Experimento 4 ejecutado.
[ ] Experimento 5 ejecutado.
[ ] Mediciones propias registradas.
[ ] Razones de crecimiento calculadas.
[ ] Gráfica creada.
[ ] decisiones.md actualizado.
[ ] bitacora.html actualizado.
[ ] Proyecto integrado.
[ ] Un solo main().
[ ] H1 creado.
```

---

# 36. Qué debe quedar funcionando

Al finalizar:

```text
IngestaSensores
      │
      ├── almacenamiento
      ├── búsqueda
      ├── análisis
      ├── ordenamiento
      │     ├── Burbuja
      │     ├── Selección
      │     ├── Inserción
      │     ├── MergeSort
      │     ├── HeapSort
      │     └── QuickSort
      │
      └── experimentos
```

La Semana 4 no debe convertirse en un programa separado.

Debe quedar integrada a la plataforma construida durante las semanas anteriores.

---

## Resultado esperado

Al terminar puedes demostrar:

```text
Código
  ↓
Experimentos
  ↓
Mediciones
  ↓
Análisis
  ↓
Decisiones
  ↓
Integración
  ↓
H1
```

La meta no es únicamente que los seis algoritmos funcionen.

La meta es poder explicar **por qué el comportamiento cambia, qué costo tiene cada alternativa y qué decisión de ingeniería tiene sentido para la plataforma de sensores**.
