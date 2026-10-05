# Guía de Explicaciones — Semana 4
## Cuánto cuesta el orden

**Espacio académico:** Estructuras de Datos · **Programa:** IDIA, II semestre  
**Contenidos:** 2.7 Ordenamientos simples · 2.8 Ordenamientos avanzados · 2.9 Comparación de eficiencia  
**Hito:** H1

---

## 1. Propósito

Esta guía explica los conceptos de la Semana 4. No pretende ser un manual de programación: busca que puedas comprender **qué hace cada algoritmo, por qué se comporta de determinada manera y cómo utilizar los resultados para tomar decisiones**.

La semana parte de una situación conocida: la búsqueda binaria permite encontrar datos con muy pocas comparaciones, pero solamente cuando los datos están ordenados. Por eso aparece una nueva pregunta:

> **Si ordenar es necesario para buscar rápidamente, ¿cuánto cuesta ordenar y qué consecuencias tiene hacerlo?**

La guía docente plantea cuatro ideas centrales:

1. La eficiencia depende de la relación entre algoritmo, datos y operación costosa.
2. La forma en que llegan los datos debe influir en la elección del algoritmo.
3. El crecimiento del costo importa más que un tiempo aislado.
4. Optimizar una parte del sistema puede afectar otra.

---

# 2. Los ordenamientos simples

Los tres primeros algoritmos producen el mismo resultado, pero utilizan estrategias diferentes.

## 2.1 Burbuja

Compara elementos vecinos y los intercambia cuando están en el orden incorrecto.

Ejemplo:

```text
[5, 2, 4, 1]

5 > 2  → [2, 5, 4, 1]
5 > 4  → [2, 4, 5, 1]
5 > 1  → [2, 4, 1, 5]
```

Los elementos grandes van desplazándose hacia el final.

### Problema de la versión básica

Si los datos ya están ordenados, puede seguir haciendo recorridos completos aunque no tenga que mover nada.

Por eso se incorpora una **bandera de corte temprano**:

```text
¿Hubo algún intercambio?

NO → terminar
SÍ → continuar
```

---

## 2.2 Selección

Busca el elemento que debe ocupar la siguiente posición y lo coloca allí.

Ejemplo:

```text
[5, 2, 4, 1]

menor = 1

[1, 2, 4, 5]
```

Una característica importante es que puede realizar muchas comparaciones pero pocos intercambios.

Esto demuestra que:

> **Comparar y mover datos son operaciones diferentes y pueden tener costos diferentes.**

En los datos de referencia de la guía, con 10.000 elementos desordenados, burbuja y selección realizan 49.995.000 comparaciones, pero selección realiza solamente 9.994 intercambios frente a más de 24 millones de burbuja.

---

## 2.3 Inserción

Funciona como ordenar cartas.

Supongamos:

```text
[2, 4, 7, 9, 5]
```

Los primeros cuatro ya están ordenados. Se toma `5` y se inserta en su posición:

```text
[2, 4, 5, 7, 9]
```

Su comportamiento es especialmente interesante cuando los datos ya están ordenados o casi ordenados.

En el experimento de la Semana 4, con 10.000 datos ordenados, inserción realiza solamente 9.999 comparaciones, mientras burbuja y selección mantienen aproximadamente 50 millones.

---

# 3. ¿Qué nos enseñan los datos?

La comparación no debe hacerse solamente con el reloj.

Hay que observar:

- `n`: cantidad de elementos.
- Comparaciones.
- Intercambios.
- Tiempo.
- Estado inicial de los datos.

Para datos desordenados, la guía utiliza como referencia:

| Algoritmo | Comparaciones | Intercambios |
|---|---:|---:|
| Burbuja | 49.995.000 | 24.928.244 |
| Selección | 49.995.000 | 9.994 |
| Inserción | 24.938.233 | 24.928.244 |

Los valores obtenidos por cada equipo deben ser sus propias mediciones.

La primera conclusión no debe ser “X es el mejor”. Debe ser:

> **La operación que cuesta más en un problema puede cambiar la elección del algoritmo.**

---

# 4. El caso que cambia la elección: datos ordenados

Las lecturas de sensores llegan con una característica importante: el tiempo avanza.

Por ejemplo:

```text
08:00
08:01
08:02
08:03
...
```

Por tanto, los datos pueden llegar ordenados por timestamp o casi ordenados.

Con datos ordenados, la guía muestra:

| Algoritmo | Comparaciones |
|---|---:|
| Burbuja | 49.995.000 |
| Selección | 49.995.000 |
| Inserción | 9.999 |

Esto conduce a una idea fundamental:

> **El mismo algoritmo puede comportarse de manera muy diferente dependiendo de los datos que recibe.**

Por eso no basta con memorizar nombres o rankings de algoritmos.

---

# 5. El crecimiento importa

Ahora se aumenta el tamaño:

```text
1.000
10.000
100.000
```

y se comparan:

```text
Inserción
MergeSort
HeapSort
```

Los resultados de referencia muestran un crecimiento muy diferente.

Para 100.000 elementos, inserción alcanza más de 2.497 millones de comparaciones, mientras MergeSort ronda 1,5 millones.

La pregunta importante es:

> ¿Qué ocurre cuando `n` aumenta?

---

# 6. Complejidad

Los ordenamientos simples se caracterizan por un crecimiento cuadrático en sus implementaciones básicas:

```text
O(n²)
```

Los algoritmos avanzados estudiados utilizan estrategias que permiten un crecimiento más favorable:

```text
MergeSort → O(n log n)
HeapSort  → O(n log n)
QuickSort → O(n log n) promedio
```

Pero:

> **O(n log n) no significa que siempre sea más rápido.**

Inserción puede ser una excelente opción para conjuntos pequeños o casi ordenados.

La notación O grande describe principalmente **cómo crece el trabajo**, no una velocidad absoluta.

---

# 7. MergeSort

MergeSort aplica la estrategia:

> **Divide y vencerás.**

El problema se divide en partes:

```text
[8,3,7,1,5,2]
       ↓
[8,3,7] [1,5,2]
       ↓
subproblemas pequeños
       ↓
fusión ordenada
```

La fusión es eficiente porque las dos partes que se combinan ya están ordenadas.

La idea que debes comprender es:

> Resolver problemas pequeños y luego combinar sus resultados puede ser mucho más eficiente que resolver todo directamente.

---

# 8. QuickSort

QuickSort también divide el problema, pero utiliza un **pivote**.

Una buena partición intenta producir:

```text
menores | pivote | mayores
```

con tamaños razonablemente equilibrados.

El problema aparece cuando se toma siempre el primer elemento como pivote y los datos ya están ordenados:

```text
[1,2,3,4,5,6]
 ^
 pivote
```

El grupo de menores queda vacío y el de mayores contiene casi todo.

En vez de dividir:

```text
n → n/2 + n/2
```

se obtiene algo parecido a:

```text
n → n-1 → n-2 → n-3...
```

Con 50.000 lecturas ordenadas, el experimento puede terminar en:

```text
StackOverflowError
```

No significa necesariamente que QuickSort esté “mal programado”. Significa que **la estrategia de elección del pivote es inadecuada para ese patrón de datos**.

---

# 9. TODO 2: mejorar el pivote

La guía propone dos alternativas:

### Pivote aleatorio

Seleccionar una posición aleatoria y colocar ese elemento como pivote.

### Mediana de tres

Comparar:

```text
primero
medio
último
```

y utilizar como pivote el valor intermedio.

Después:

1. Implementa una alternativa.
2. Ejecuta nuevamente el caso ordenado.
3. Compara antes y después.
4. Registra la decisión en `docs/decisiones.md`.

---

# 10. El efecto colateral del ordenamiento

Esta es una de las partes más importantes de la semana.

El escenario es:

```text
Datos ordenados por timestamp
        ↓
Búsqueda binaria funciona
        ↓
Se solicita ranking por PM2.5
        ↓
Se ordenan los mismos datos por PM2.5
        ↓
Se vuelve a buscar por timestamp
        ↓
Búsqueda binaria falla
```

La lectura sigue existiendo.

El buscador puede seguir funcionando correctamente.

El problema es que la **precondición de la búsqueda binaria dejó de cumplirse**.

La búsqueda binaria necesita:

```text
datos ordenados por el criterio de búsqueda
```

pero ahora los datos están ordenados por:

```text
PM2.5
```

---

# 11. Tres alternativas de diseño

La guía propone tres caminos.

### 1. Trabajar sobre una copia

Se conserva el arreglo original y se ordena una copia para producir el ranking.

Costo:

- Memoria adicional.
- Tiempo de copia.

### 2. Restaurar el orden

Después del ranking, se vuelve a ordenar por timestamp.

Costo:

- Cada restauración tiene un costo de ordenamiento.

### 3. Mantener índices separados

Se mantiene una estructura auxiliar para cada criterio:

```text
Datos
 ├── índice por timestamp
 └── índice por PM2.5
```

Costo:

- Mayor complejidad.
- Hay que mantener las estructuras sincronizadas.

No existe una respuesta universal. Lo importante es justificar técnicamente la decisión.

---

# 12. La pregunta de ingeniería

La pregunta de la semana no es:

> ¿Cuál es el algoritmo más rápido?

La pregunta es:

> **¿Qué algoritmo tiene sentido para mis datos, mi problema y mis restricciones?**

Antes de elegir, analiza:

```text
¿Cómo llegan los datos?
        ↓
¿Cuántos datos tengo?
        ↓
¿Qué operación es costosa?
        ↓
¿Necesito mantener algún orden?
        ↓
¿El algoritmo tiene precondiciones?
        ↓
¿Qué costo tiene mantenerlas?
```

---

# 13. Predicción

Antes de ejecutar:

1. ¿Cuál tendrá menos comparaciones con datos desordenados?
2. ¿Cuál tendrá menos comparaciones con datos ordenados?
3. ¿Alguno cambia significativamente según el estado inicial?

Guarda la predicción y compárala con tus resultados al finalizar.

---

# 14. Preguntas de reflexión

1. Explica la diferencia entre burbuja y selección sin usar las palabras “comparar” ni “intercambiar”.
2. ¿En qué situación podría ser conveniente selección aunque realice muchas comparaciones?
3. Si los datos llegan casi ordenados, ¿qué algoritmo resulta especialmente interesante y por qué?
4. ¿Qué enseña el `StackOverflowError` de QuickSort sobre confiar únicamente en el rendimiento promedio?
5. ¿En qué acertaste o te equivocaste respecto de tu predicción?

---

# 15. Hito H1

La Semana 4 cierra el Hito H1, que integra las semanas 2, 3 y 4.

Entregables:

1. `Ordenador.java` con TODO 1 y TODO 2 resueltos.
2. Tabla comparativa con mediciones propias.
3. Gráfica de crecimiento.
4. `docs/decisiones.md` actualizado.
5. Capa de almacenamiento y consulta integrada.
6. Bitácora individual.

Etiqueta:

```bash
git tag -a H1 -m "Entrega Hito 1: almacenamiento y consulta"
git push origin H1
```

---

# 16. Idea final

Esta semana representa un cambio de enfoque:

```text
Antes:
"Aprendo a programar un algoritmo."

Ahora:
"Aprendo a elegir un algoritmo
basándome en evidencia."
```

El verdadero aprendizaje está en poder explicar:

- por qué un algoritmo se comportó así;
- qué característica de los datos produjo ese comportamiento;
- qué costo tiene;
- qué alternativa existe;
- qué decisión tomarías para la plataforma;
- y qué consecuencia tiene esa decisión sobre otros componentes.
