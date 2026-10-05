# Guía de Estudio --- Algoritmos de Ordenamiento

**Espacio académico:** Estructuras de Datos\
**Programa:** Ingeniería de Datos e Inteligencia Artificial\
**Semana:** 4\
**Proyecto:** Plataforma de Monitoreo Ambiental Urbano --- Red de
Sensores IoT

------------------------------------------------------------------------

## 1. ¿Qué significa ordenar?

Supongamos que tenemos:

``` text
[7, 2, 9, 1, 5]
```

Ordenar ascendentemente significa obtener:

``` text
[1, 2, 5, 7, 9]
```

Parece sencillo, pero para conseguirlo el algoritmo debe tomar
decisiones:

-   ¿Qué elementos comparar?
-   ¿Cuándo moverlos?
-   ¿Cuántos elementos revisar?
-   ¿Cuántas veces repetir el proceso?
-   ¿Qué ocurre si los datos ya están ordenados?
-   ¿Qué ocurre si tenemos 100.000 o un millón de elementos?

Ahí aparece el verdadero problema de los algoritmos de ordenamiento.

> **Idea central:** los seis algoritmos estudiados producen el mismo
> resultado: un arreglo ordenado. Lo que cambia es el costo de llegar a
> ese resultado.

El costo depende no solamente del algoritmo, sino también de cómo llegan
los datos, qué operaciones son costosas y cuál es el tamaño del conjunto
de datos.

------------------------------------------------------------------------

# 2. La gran clasificación

En la Semana 4 se estudian seis algoritmos:

  -----------------------------------------------------------------------
  Grupo                   Algoritmos              Idea principal
  ----------------------- ----------------------- -----------------------
  **Simples**             Burbuja, Selección,     Comparaciones y
                          Inserción               movimientos directos

  **Avanzados**           MergeSort, QuickSort,   Dividir el problema o
                          HeapSort                utilizar estructuras
                                                  auxiliares
  -----------------------------------------------------------------------

Los tres algoritmos simples permiten comprender una idea fundamental:

> **No basta con saber cuántas comparaciones hace un algoritmo; también
> importa cuánto cuesta mover los datos.**

------------------------------------------------------------------------

# 3. Burbuja --- Bubble Sort

## 3.1 Idea principal

Burbuja compara elementos **vecinos**.

Ejemplo:

``` text
[5, 2, 8, 1]
```

Primera comparación:

``` text
5 > 2
```

Se intercambian:

``` text
[2, 5, 8, 1]
```

Después:

``` text
5 < 8
```

No se mueve:

``` text
[2, 5, 8, 1]
```

Después:

``` text
8 > 1
```

Se intercambian:

``` text
[2, 5, 1, 8]
```

El elemento más grande comienza a **"flotar" hacia el final**.

------------------------------------------------------------------------

## 3.2 Forma de recordarlo

``` text
REPETIR varias veces
    recorrer el arreglo
        comparar vecino con vecino
        si están al revés
            intercambiarlos
HASTA que esté ordenado
```

### Palabra clave

> **Burbuja = vecinos**

------------------------------------------------------------------------

## 3.3 Complejidad

En la versión tradicional, el comportamiento es:

``` text
O(n²)
```

El número aproximado de comparaciones es:

\[ `\frac{n(n-1)}{2}`{=tex} \]

Para:

``` text
n = 10.000
```

se obtiene:

\[ `\frac{10000(9999)}{2}`{=tex}=49.995.000 \]

Esto coincide con el resultado observado en los experimentos de la
Semana 4.

------------------------------------------------------------------------

## 3.4 Truco: bandera de corte temprano

Si el arreglo ya está ordenado:

``` text
[1, 2, 3, 4, 5]
```

no tiene sentido continuar recorriéndolo una y otra vez.

Podemos utilizar una bandera:

``` java
boolean huboIntercambio = false;
```

Durante la pasada:

``` java
if (a[j] > a[j + 1]) {
    intercambiar(...);
    huboIntercambio = true;
}
```

Al terminar:

``` java
if (!huboIntercambio) {
    break;
}
```

La lógica es:

> **Si recorrí todo el arreglo y no tuve que mover nada, ya está
> ordenado.**

En los experimentos de la Semana 4, Burbuja sin bandera realiza
49.995.000 comparaciones sobre 10.000 datos ordenados. Con la bandera
puede reducir el trabajo a 9.999 comparaciones.

### Truco mental

**Burbuja = vecinos + intercambio + bandera.**

------------------------------------------------------------------------

# 4. Selección --- Selection Sort

## 4.1 Idea principal

Selección trabaja de una manera diferente.

En lugar de preguntar:

> "¿Estos dos vecinos están bien?"

pregunta:

> **"¿Cuál es el elemento más pequeño que queda?"**

Ejemplo:

``` text
[5, 3, 8, 1, 2]
```

Busca el menor:

``` text
1
```

Lo lleva al comienzo:

``` text
[1, 3, 8, 5, 2]
```

Ahora ignora el `1` y busca el menor del resto:

``` text
[1, 2, 8, 5, 3]
```

Y continúa hasta ordenar todo el arreglo.

------------------------------------------------------------------------

## 4.2 Forma de recordarlo

``` text
PARA cada posición i
    buscar el menor desde i hasta el final
    ponerlo en i
```

### Palabra clave

> **Selección = mínimo**

------------------------------------------------------------------------

## 4.3 Una característica importante

Selección hace muchas comparaciones, pero realiza pocos intercambios.

En el experimento con 10.000 datos desordenados:

  Algoritmo     Comparaciones   Intercambios
  ----------- --------------- --------------
  Burbuja          49.995.000     24.928.244
  Selección        49.995.000          9.994

Esto permite una conclusión importante:

> **Comparar y mover no necesariamente tienen el mismo costo.**

Si estamos ordenando objetos grandes, moverlos puede ser mucho más
costoso que comparar sus valores.

### Ejemplo

Si cada registro ocupa 1 MB, mover grandes cantidades de registros puede
convertirse en una operación costosa.

### Truco mental

**Selección = buscar mínimo + colocarlo.**

------------------------------------------------------------------------

# 5. Inserción --- Insertion Sort

## 5.1 Idea principal

Inserción funciona como cuando una persona organiza cartas.

Supongamos:

``` text
[3, 5, 8]
```

y aparece:

``` text
4
```

No volvemos a ordenar todo.

Tomamos el `4` y lo insertamos en la posición correcta:

``` text
[3, 4, 5, 8]
```

La idea es mantener una parte del arreglo ya ordenada e insertar cada
nuevo elemento en su posición.

------------------------------------------------------------------------

## 5.2 Forma de recordarlo

``` text
considerar que el primer elemento ya está ordenado

tomar el siguiente elemento

mientras haya elementos mayores a la izquierda
    desplazarlos hacia la derecha

insertar el elemento en el hueco
```

### Palabra clave

> **Inserción = insertar en una parte ya ordenada**

------------------------------------------------------------------------

# 6. El gran secreto de Inserción

Inserción tiene un comportamiento especialmente interesante cuando los
datos ya están ordenados o casi ordenados.

Ejemplo:

``` text
[1, 2, 3, 4, 5, 6]
```

Cada elemento prácticamente está donde debe estar.

Por eso, en el mejor caso, su comportamiento puede acercarse a:

``` text
O(n)
```

En el experimento de la Semana 4:

``` text
10.000 datos ordenados
```

Inserción realizó:

``` text
9.999 comparaciones
0 intercambios
```

mientras Burbuja y Selección realizaron:

``` text
49.995.000 comparaciones
0 intercambios
```

### Idea clave

> **Un algoritmo O(n²) en el peor caso no necesariamente es una mala
> elección para todos los problemas.**

Si los datos llegan casi ordenados, Inserción puede ser muy eficiente.

Esto es especialmente relevante para la plataforma de sensores, donde
las lecturas llegan cronológicamente y, por naturaleza, pueden
encontrarse casi ordenadas.

### Truco mental

**Inserción = datos casi ordenados → candidato interesante.**

------------------------------------------------------------------------

# 7. Comparación de los tres algoritmos simples

  ------------------------------------------------------------------------
  Característica    Burbuja           Selección         Inserción
  ----------------- ----------------- ----------------- ------------------
  Idea              Vecinos           Buscar mínimo     Insertar

  Peor caso         O(n²)             O(n²)             O(n²)

  Datos ordenados   Mejora con        Sigue haciendo    **Excelente
                    bandera           comparaciones     comportamiento**

  Movimientos       Muchos            **Pocos**         Dependen de los
                                                        datos

  Principal         Corte temprano    Costo de mover    Adaptación a los
  enseñanza                                             datos
  ------------------------------------------------------------------------

------------------------------------------------------------------------

# 8. ¿Qué significa realmente "eficiente"?

Supongamos dos algoritmos:

``` text
Algoritmo A
50 millones de comparaciones
1.000 movimientos
```

y:

``` text
Algoritmo B
10 millones de comparaciones
20 millones de movimientos
```

¿A cuál debemos elegir?

No podemos responder sin conocer el costo de cada operación.

Comparar puede ser barato:

``` text
leer dos valores
```

Mientras que intercambiar puede ser costoso:

``` text
mover objetos grandes en memoria
```

Por eso:

> **"Eficiente" no significa simplemente "menos comparaciones".**

Un algoritmo que compara mucho y mueve poco puede ser mejor para un
problema determinado si mover datos es mucho más costoso que
compararlos.

### Pregunta de ingeniería

Cuando alguien diga:

> "Este algoritmo es mejor porque hace menos comparaciones."

pregunta:

> **¿Cuánto cuesta cada comparación frente a cada movimiento?**

------------------------------------------------------------------------

# 9. MergeSort

## 9.1 Idea principal

MergeSort utiliza la estrategia:

> **Divide y vencerás**

Tenemos:

``` text
[8, 3, 6, 1, 4, 7, 2, 5]
```

Dividimos:

``` text
[8, 3, 6, 1]   [4, 7, 2, 5]
```

Seguimos:

``` text
[8,3] [6,1] [4,7] [2,5]
```

Hasta llegar a:

``` text
[8] [3] [6] [1] [4] [7] [2] [5]
```

Después comienza la combinación.

Por ejemplo:

``` text
[8] + [3]
```

produce:

``` text
[3,8]
```

Y:

``` text
[6] + [1]
```

produce:

``` text
[1,6]
```

Las partes ordenadas continúan fusionándose hasta formar el arreglo
completo.

------------------------------------------------------------------------

## 9.2 La idea para memorizar

``` text
DIVIDIR
   ↓
ORDENAR
   ↓
FUSIONAR
```

### Palabra clave

> **MergeSort = dividir y fusionar**

------------------------------------------------------------------------

## 9.3 Complejidad

MergeSort presenta crecimiento:

``` text
O(n log n)
```

Esto hace que su comportamiento sea mucho más favorable que el de los
algoritmos cuadráticos cuando el tamaño de los datos aumenta
considerablemente.

En los experimentos de la Semana 4, al multiplicar `n` por 10, las
comparaciones de MergeSort aumentaron aproximadamente por un factor de
13--14, mientras Inserción llegó a aumentar por factores cercanos a 100.

### Idea clave

> **No importa solamente cuánto tarda hoy; importa cómo crece el costo
> cuando crecen los datos.**

------------------------------------------------------------------------

# 10. HeapSort

## 10.1 Idea principal

HeapSort utiliza una estructura llamada:

> **Heap o montículo**

El montículo permite organizar los elementos de manera que podamos
identificar rápidamente un elemento extremo, como el máximo.

Conceptualmente:

``` text
        máximo
       /      \
     ...      ...
```

Después se extrae ese elemento y se reorganiza la estructura.

------------------------------------------------------------------------

## 10.2 Complejidad

HeapSort presenta crecimiento del orden:

``` text
O(n log n)
```

En el experimento:

          n   Comparaciones
  --------- ---------------
      1.000          16.786
     10.000         235.434
    100.000       3.019.556

### Truco para recordar

**HeapSort = montículo + extracción + reorganización.**

------------------------------------------------------------------------

# 11. QuickSort

## 11.1 Idea principal

QuickSort utiliza un elemento denominado:

> **Pivote**

Ejemplo:

``` text
[7, 2, 9, 1, 5]
```

Elegimos:

``` text
pivote = 5
```

Intentamos organizar los elementos alrededor del pivote:

``` text
menores     pivote     mayores

[2,1]         5          [7,9]
```

Después aplicamos el mismo proceso a las partes restantes.

------------------------------------------------------------------------

## 11.2 La palabra clave

> **QuickSort = pivote + partición**

------------------------------------------------------------------------

# 12. El gran problema de QuickSort

QuickSort puede tener un excelente comportamiento promedio, pero una
mala elección del pivote puede producir un comportamiento muy
desfavorable.

En el experimento de la Semana 4 se utiliza un pivote fijo en el primer
elemento.

Con 50.000 lecturas desordenadas:

``` text
≈ 900.318 comparaciones
≈ 450.373 intercambios
≈ 145 ms
```

Pero con las 50.000 lecturas en orden cronológico:

``` text
StackOverflowError
```

Esto sucede porque la partición puede quedar extremadamente
desequilibrada.

------------------------------------------------------------------------

## 12.1 ¿Qué ocurre?

Supongamos:

``` text
[1, 2, 3, 4, 5, 6, 7]
```

Si siempre elegimos el primero:

``` text
pivote = 1
```

podemos terminar con:

``` text
[]
1
[2,3,4,5,6,7]
```

Después:

``` text
[]
2
[3,4,5,6,7]
```

Después:

``` text
[]
3
[4,5,6,7]
```

En lugar de dividir el problema aproximadamente en dos partes, estamos
dejando una parte vacía y otra casi completa.

### Idea clave

> **El comportamiento promedio de un algoritmo no garantiza que sea
> adecuado para cualquier distribución de datos.**

### Pregunta de ingeniería

No preguntes solamente:

> "¿Cuál es la complejidad promedio?"

Pregunta también:

> **"¿Qué supuestos necesita este algoritmo para comportarse bien?"**

------------------------------------------------------------------------

# 13. Comparación de los algoritmos avanzados

  -----------------------------------------------------------------------
  Algoritmo         Idea              Crecimiento       Punto clave
  ----------------- ----------------- ----------------- -----------------
  **MergeSort**     Divide y fusiona  O(n log n)        Crecimiento
                                                        predecible

  **HeapSort**      Heap + extracción O(n log n)        Utiliza un
                                                        montículo

  **QuickSort**     Pivote +          Depende de la     La selección del
                    partición         partición         pivote es
                                                        importante
  -----------------------------------------------------------------------

------------------------------------------------------------------------

# 14. El verdadero significado del crecimiento

Supongamos:

``` text
n = 1.000
```

Un algoritmo O(n²) puede ser perfectamente manejable.

Pero si:

``` text
n = 1.000.000
```

la situación cambia completamente.

Una forma sencilla de visualizarlo:

``` text
O(n)

1 → 10 → 100 → 1.000


O(n²)

1 → 100 → 10.000 → 1.000.000
```

Por eso, cuando evaluamos un algoritmo, debemos preguntarnos:

> **¿Qué sucede si mañana tenemos diez veces más datos?**

------------------------------------------------------------------------

# 15. No confundir tiempo de ejecución con complejidad

Este es un error frecuente.

Si dos algoritmos tardan:

``` text
15 ms
15 ms
```

no significa necesariamente que sean equivalentes.

El tiempo depende de:

-   computador;
-   JVM;
-   memoria;
-   implementación;
-   datos;
-   optimizaciones;
-   carga del sistema.

La complejidad intenta responder otra pregunta:

> **¿Cómo crece el costo cuando crece `n`?**

Por eso es más importante observar el comportamiento al pasar de:

``` text
1.000
↓
10.000
↓
100.000
```

que comparar únicamente un tiempo aislado.

------------------------------------------------------------------------

# 16. El efecto colateral del ordenamiento

Este es uno de los aprendizajes más importantes para el proyecto.

Inicialmente las lecturas están ordenadas por:

``` text
timestamp
```

Por lo tanto:

``` text
Búsqueda binaria por timestamp
        ↓
funciona
```

Pero alguien necesita obtener un ranking por:

``` text
PM2.5
```

Se ordenan las lecturas por PM2.5.

Ahora el arreglo está ordenado por:

``` text
PM2.5
```

y ya no necesariamente por:

``` text
timestamp
```

La búsqueda binaria por timestamp pierde su precondición.

La guía reproduce este escenario:

``` text
Paso 1:
ordenado por timestamp
→ búsqueda binaria encuentra el dato

Paso 2:
ordenar por PM2.5

Paso 3:
timestamp deja de estar ordenado
→ búsqueda binaria falla
→ búsqueda lineal todavía encuentra el dato
```

------------------------------------------------------------------------

# 17. Lección de arquitectura

Aquí aparece una idea que va más allá del algoritmo:

> **Un método puede funcionar correctamente y, aun así, el sistema
> completo puede dejar de funcionar correctamente por un cambio
> realizado en otro módulo.**

Podemos visualizarlo así:

``` text
Ordenamiento
     ↓
cambia el orden de los datos
     ↓
Búsqueda binaria
     ↓
pierde su precondición
```

No necesariamente existe un error dentro de:

``` text
Ordenador
```

ni dentro de:

``` text
BuscadorLecturas
```

El problema aparece en la interacción entre ambos.

Esto es un ejemplo de **efecto colateral entre módulos**.

------------------------------------------------------------------------

# 18. ¿Cómo evitar el problema?

## Estrategia A --- Trabajar con una copia

Mantener:

``` text
datos originales
```

y crear:

``` text
copia ordenada por PM2.5
```

Así no destruimos el orden original.

------------------------------------------------------------------------

## Estrategia B --- Restaurar el orden

Ordenar:

``` text
por PM2.5
```

hacer el ranking y después restaurar:

``` text
por timestamp
```

Tiene el costo adicional de volver a ordenar.

------------------------------------------------------------------------

## Estrategia C --- Mantener índices separados

Podemos mantener estructuras que permitan acceder a los datos según
diferentes criterios:

``` text
Índice por timestamp
Índice por PM2.5
```

Esta estrategia conecta con conceptos posteriores de estructuras de
datos e indexación.

------------------------------------------------------------------------

# 19. ¿Qué algoritmo elegir?

No existe una regla universal como:

> "Siempre use QuickSort."

La elección depende del problema.

  -----------------------------------------------------------------------
  Situación                           Algoritmo que vale la pena
                                      considerar
  ----------------------------------- -----------------------------------
  Aprender el concepto básico         Burbuja

  Minimizar intercambios              Selección

  Datos pequeños o casi ordenados     Inserción

  Grandes volúmenes y crecimiento     MergeSort / HeapSort
  predecible                          

  Particionamiento eficiente          QuickSort

  Datos cronológicos o casi ordenados Inserción puede ser interesante

  Necesidad de preservar el orden     Considerar estabilidad
  relativo                            
  -----------------------------------------------------------------------

La recomendación debe justificarse con las características reales de los
datos y con mediciones.

------------------------------------------------------------------------

# 20. Trucos para recordar los seis algoritmos

## Truco 1 --- Una palabra por algoritmo

``` text
Burbuja    → VECINOS
Selección  → MÍNIMO
Inserción  → INSERTAR
MergeSort  → DIVIDIR
QuickSort  → PIVOTE
HeapSort   → MONTÍCULO
```

------------------------------------------------------------------------

## Truco 2 --- Pregunta por las operaciones

No preguntes solamente:

> "¿Cuál hace menos comparaciones?"

Pregunta:

``` text
¿Qué compara?
¿Qué mueve?
¿Cuántas veces?
```

------------------------------------------------------------------------

## Truco 3 --- Pregunta por los datos

Antes de elegir:

``` text
¿Están desordenados?
¿Están ordenados?
¿Están casi ordenados?
¿Llegan cronológicamente?
```

------------------------------------------------------------------------

## Truco 4 --- Pregunta por `n`

No analices únicamente:

``` text
n = 100
```

También:

``` text
n = 1.000
n = 10.000
n = 100.000
n = 1.000.000
```

Un algoritmo que funciona bien con 100 elementos puede convertirse en
una mala decisión con un millón.

------------------------------------------------------------------------

## Truco 5 --- Pregunta por las precondiciones

Por ejemplo:

``` text
Búsqueda binaria
       ↓
requiere datos ordenados
```

Entonces:

``` text
Ordenar por otra variable
       ↓
¿sigo cumpliendo la precondición?
```

Esta pregunta conecta directamente:

``` text
Ordenamiento
     +
Búsqueda
     +
Arquitectura
```

------------------------------------------------------------------------

# 21. Resumen comparativo

  -----------------------------------------------------------------------
  Algoritmo         Idea clave        Complejidad       Pregunta que
                                      característica    debemos hacernos
  ----------------- ----------------- ----------------- -----------------
  **Burbuja**       Vecinos           O(n²)             ¿Puedo detenerme
                                                        temprano?

  **Selección**     Buscar mínimo     O(n²)             ¿Mover es más
                                                        costoso que
                                                        comparar?

  **Inserción**     Insertar          O(n²), pero puede ¿Los datos llegan
                                      acercarse a O(n)  casi ordenados?

  **MergeSort**     Dividir y         O(n log n)        ¿Necesito
                    fusionar                            crecimiento
                                                        predecible?

  **QuickSort**     Pivote y          Depende de la     ¿Mi estrategia de
                    partición         partición         pivote es
                                                        adecuada?

  **HeapSort**      Montículo y       O(n log n)        ¿Necesito un
                    extracción                          comportamiento
                                                        consistente?
  -----------------------------------------------------------------------

------------------------------------------------------------------------

# 22. Las cinco preguntas que deberían quedar al terminar

### 1. ¿Por qué Selección puede ser interesante aunque haga muchas comparaciones?

Porque realiza pocos intercambios y mover datos puede ser mucho más
costoso que compararlos.

### 2. ¿Por qué Inserción puede ser excelente aunque sea O(n²)?

Porque su comportamiento mejora considerablemente cuando los datos están
ordenados o casi ordenados.

### 3. ¿Por qué un algoritmo O(n log n) resulta importante cuando crece `n`?

Porque su costo crece mucho más lentamente que el de un algoritmo O(n²).

### 4. ¿Por qué QuickSort puede fallar con datos ordenados?

Porque una mala elección del pivote puede producir particiones muy
desequilibradas y una profundidad de recursión excesiva.

### 5. ¿Por qué ordenar puede romper la búsqueda binaria?

Porque la búsqueda binaria necesita que los datos permanezcan ordenados
según el mismo criterio de búsqueda.

------------------------------------------------------------------------

# 23. La regla de oro de la Semana 4

> **No existe un algoritmo de ordenamiento "mejor" en abstracto; existe
> un algoritmo cuyo costo y comportamiento son adecuados para unos
> datos, un tamaño y un problema determinados.**

Y una segunda regla:

> **La verdadera eficiencia no se mide solamente por cuánto tarda una
> ejecución, sino por cómo crece el costo cuando cambian los datos y el
> tamaño del problema.**

En el proyecto de la Red de Sensores IoT, el objetivo no es implementar
seis métodos por separado. El objetivo es aprender a justificar por qué
una estrategia de ordenamiento tiene sentido para las lecturas reales de
la plataforma.

------------------------------------------------------------------------

# 24. Conexión con el proyecto integrador

Los algoritmos de ordenamiento no deben verse como ejercicios
independientes.

En la plataforma:

``` text
Lecturas de sensores
        ↓
RepositorioLecturas
        ↓
Ordenamiento
        ↓
Ranking / Top-N
        ↓
Consultas
```

Pero existe una condición importante:

``` text
Ordenar por PM2.5
        ↓
puede cambiar el orden por timestamp
        ↓
puede afectar la búsqueda binaria
```

Por eso, durante la Semana 4, la pregunta importante no es solamente:

> **"¿Cómo ordeno?"**

También debemos preguntar:

> **"¿Qué consecuencias tiene ordenar de esta manera sobre el resto del
> sistema?"**

Ese cambio de perspectiva es uno de los principales aprendizajes de la
semana.
