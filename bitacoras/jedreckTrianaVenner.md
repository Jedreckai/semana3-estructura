# 📓 Bitácora Individual del Proyecto Integrador — Hito 1 (H1)

**Estudiante:** Jedreck Triana Venner  
**Equipo:** Plataforma de Monitoreo Ambiental Urbano — Red de Sensores IoT  
**Espacio Académico:** Estructuras de Datos · Ingeniería de Datos e Inteligencia Artificial (IDIA)  
**Repositorio GitHub:** [Jedreckai/semana3-estructura](https://github.com/Jedreckai/semana3-estructura.git)

---

# Parte I: Semana 02 — ¿Dónde viven los datos? (TAD y Almacenamiento)

## 1. Datos de la actividad
- **Semana:** 02
- **Tema principal:** TAD RepositorioLecturas, arreglos estáticos con redimensionamiento dinámico y matriz escalonada para análisis ambiental.
- **Pregunta de la semana:** ¿Cómo almacenar y consultar eficientemente flujos de datos ambientales sin utilizar colecciones predefinidas (`ArrayList`, `HashMap`)?

## 2. Conceptos clave y trabajo realizado
- **Modelado:** Creación e integración de `LecturaSensor.java` para encapsular timestamp, estación, temperatura, humedad y PM2.5, junto con validación física (`esValida()`).
- **TAD `RepositorioLecturas`:** Implementación sobre arreglo de objetos `LecturaSensor[]`. Se aplicó la estrategia de **redimensionamiento por duplicación** ($10 \to 20 \to 40 \to 80 \to 160 \to 320$), reduciendo el costo amortizado a $O(1)$ por inserción.
- **Eliminación por compactación:** Mantenimiento de la invariante de contigüidad desplazando elementos a la izquierda para evitar huecos en memoria.
- **Matriz Horaria:** Corrección del error del "cero fantasma" en `AnalizadorMatriz.java`, asegurando que celdas no reportadas ($0.0$) no distorsionen los promedios reales de la ciudad.

---

# Parte II: Semana 04 — ¿Cuánto cuesta el orden? (Ordenamiento y Eficiencia)

## 1. Datos de la actividad
- **Semana:** 04 (Cierre de Hito H1)
- **Tema principal:** Algoritmos de ordenamiento simples (Burbuja, Selección, Inserción), avanzados (MergeSort, QuickSort, HeapSort), estudio de complejidad empírica vs. teórica y efectos colaterales en búsquedas binarias.
- **Pregunta de la semana:** Si la búsqueda binaria exige datos ordenados para ser eficiente ($O(\log n)$), ¿cuánto cuesta ordenar los datos y qué consecuencias tiene cambiar el criterio de orden sobre el resto del sistema?

---

## 2. Predicción antes de ejecutar

1. **¿Qué creo que va a ocurrir?**  
   - Con datos desordenados, los algoritmos $O(n^2)$ tardarán un tiempo considerable a partir de 10.000 elementos, mientras que MergeSort y HeapSort ($O(n \log n)$) procesarán cientos de miles de registros en milisegundos.  
   - Si los datos ya vienen ordenados cronológicamente (como llegan de la red IoT), Inserción y Burbuja con bandera deberían comportarse de forma óptima ($O(n)$).
2. **¿Qué parte del programa o del algoritmo puede fallar?**  
   - La versión ingenua de QuickSort tomando siempre el primer elemento como pivote fallará catastróficamente con datos ordenados cronológicamente, porque cada partición dejará 0 elementos a un lado y $n-1$ al otro, profundizando la recursión hasta provocar un desbordamiento de pila (`StackOverflowError`).
3. **¿Cómo comprobaré mi predicción?**  
   - Mediante la instrumentación de `Ordenador.java` contando comparaciones, intercambios y tiempo en `BancoDeOrdenamiento.java`, contrastando el comportamiento con datos revueltos vs. datos cronológicos.

---

## 3. Evidencia del laboratorio (Mediciones Empíricas Reales)

### Experimento 1: Algoritmos simples sobre 10.000 lecturas desordenadas
| Algoritmo | Comparaciones | Intercambios | Tiempo (ms) |
|---|---:|---:|---:|
| **Burbuja** | 49.990.814 | 24.928.244 | 1.616 ms |
| **Selección** | 49.995.000 | 9.994 | 1.267 ms |
| **Inserción** | 24.938.233 | 24.928.244 | 427 ms |

> **Hallazgo:** Selección hace prácticamente las mismas comparaciones que Burbuja ($\approx 50$ millones), pero realiza únicamente **9.994 intercambios** (frente a casi 25 millones de Burbuja). Mover datos y comparar no cuestan lo mismo.

---

### Experimento 2: Algoritmos simples con 10.000 lecturas ordenadas (Llegada IoT)
| Algoritmo | Comparaciones | Intercambios | Tiempo (ms) |
|---|---:|---:|---:|
| **Burbuja (sin bandera)** | 49.995.000 | 0 | 775 ms |
| **Burbuja (con corte temprano)** | **9.999** | **0** | **0 ms** |
| **Selección** | 49.995.000 | 0 | 573 ms |
| **Inserción** | **9.999** | **0** | **0 ms** |

> **Hallazgo:** La bandera de corte temprano en Burbuja redujo el trabajo de **49.995.000 a 9.999 comparaciones** ($n - 1$). Inserción brilla naturalmente con datos preordenados ($O(n)$).

---

### Experimento 3: Escalabilidad — Simples vs. Avanzados a escala creciente
| $n$ | Algoritmo | Comparaciones | Intercambios | Tiempo (ms) |
|---:|---|---:|---:|---:|
| **1.000** | Inserción | 242.787 | 241.797 | 7 ms |
| 1.000 | MergeSort | 8.684 | 9.976 | 2 ms |
| 1.000 | HeapSort | 16.786 | 9.065 | 1 ms |
| **10.000** | Inserción | 24.938.233 | 24.928.244 | 407 ms |
| 10.000 | MergeSort | 120.396 | 133.616 | 9 ms |
| 10.000 | HeapSort | 235.434 | 124.208 | 12 ms |
| **100.000** | Inserción | **2.497.222.762** | **2.497.122.770** | **84.556 ms** (~84 s) |
| 100.000 | MergeSort | **1.536.325** | **1.668.928** | **98 ms** |
| 100.000 | HeapSort | **3.019.556** | **1.574.970** | **186 ms** |

#### Factor de Crecimiento al multiplicar $n \times 10$:
- **Inserción ($O(n^2)$):**  
  - De $1\text{k} \to 10\text{k}$: Factor **$102.7\times$**  
  - De $10\text{k} \to 100\text{k}$: Factor **$100.1\times$** *(muy cercano al valor teórico de $10^2 = 100$)*
- **MergeSort ($O(n \log n)$):**  
  - De $1\text{k} \to 10\text{k}$: Factor **$13.9\times$**  
  - De $10\text{k} \to 100\text{k}$: Factor **$12.8\times$** *(muy cercano al valor teórico $\approx 13$)*
- **HeapSort ($O(n \log n)$):**  
  - De $1\text{k} \to 10\text{k}$: Factor **$14.0\times$**  
  - De $10\text{k} \to 100\text{k}$: Factor **$12.8\times$**

---

### Experimento 4: QuickSort y la trampa del pivote fijo
- **50.000 lecturas desordenadas (pivote primer elemento):** 900.318 comparaciones, 450.373 intercambios, **72 ms**.
- **50.000 lecturas cronológicas (pivote primer elemento):**  
  💥 `StackOverflowError` (Recursión de profundidad 50.000 por particiones completamente desbalanceadas; alcanzó más de 731 millones de comparaciones antes de agotar la pila).
- **50.000 lecturas con QuickSort Mediana de Tres (TODO 2 resuelto):**
  - Desordenadas: 898.833 comparaciones en **50 ms**.
  - Cronológicas: **750.015 comparaciones, 382.517 intercambios en 28 ms**. ¡Sin desbordamiento de pila!

---

### Experimento 5: El efecto colateral del ordenamiento multidimensional
1. **Paso 1:** Datos en orden cronológico. Búsqueda binaria de timestamp encuentra la lectura en la posición `73412` con solo **16 comparaciones** ($O(\log n)$).
2. **Paso 2 (Solución implementada DEC-05):** Se solicita un ranking de estaciones más contaminadas. Se procesa mediante `rankingPorPm25(datos)` sobre una **copia** con MergeSort. El original permanece intacto y la búsqueda binaria sigue funcionando con **16 comparaciones**.
3. **Paso 3 y 4 (Simulación del fallo en sitio):** Al ordenar el arreglo original por PM2.5:
   - La búsqueda binaria por timestamp retorna `-1` (falla rotundamente tras 16 comparaciones porque el arreglo ya no está ordenado por timestamp).
   - La búsqueda lineal requiere **87.706 comparaciones** para encontrar el registro (demostrando que el dato sí existe, pero la precondición fue destruida).

---

## 4. Explicación en lenguaje llano

Imagina una biblioteca donde los libros están ordenados alfabéticamente por título. Encontrar un libro por su nombre toma segundos.  
Pero un día, alguien decide reorganizar todos los estantes por grosor del libro para ver cuáles son los más pesados. Los libros siguen estando en la biblioteca, pero ahora encontrar un libro por su título requiere mirar uno por uno desde la entrada hasta el fondo, porque el orden que permitía buscar rápido fue destruido.

---

## 5. El vacío que encontré y resolví
- **Mi duda concreta:** ¿Por qué QuickSort, siendo comúnmente llamado "el más rápido", fallaba con datos perfectamente ordenados?
- **Lo que comprendí:** Que el tiempo promedio $O(n \log n)$ asume particiones balanceadas. Si los datos ya están ordenados y el pivote es el primer elemento, el subarreglo izquierdo queda vacío y el derecho con $n-1$, degenerando la llamada recursiva a una lista encadenada de profundidad $n$, agotando la memoria de pila de la JVM (`StackOverflowError`).
- **Solución aplicada:** Implementar la **mediana de tres** (comparar inicio, centro y fin), lo cual garantiza que con datos ordenados el pivote sea el valor intermedio exacto, restaurando el balance $n/2$ en cada división.

---

## 6. Decisiones de diseño adoptadas

### DEC-04: Elección del pivote en QuickSort
- **Problema:** Evitar la degradación a $O(n^2)$ y desbordamiento de pila ante datos cronológicos.
- **Decisión:** Mediana de tres (frente a pivote aleatorio).
- **Justificación:** Es determinista (resultados reproducibles para auditoría científica) y equilibra idealmente las particiones en el caso real de la red IoT.

### DEC-05: Protección del ordenamiento base ante rankings
- **Problema:** Ordenar por PM2.5 invalida la precondición de la búsqueda binaria por timestamp.
- **Decisión:** Generar el ranking sobre una copia independiente mediante `rankingPorPm25()` utilizando MergeSort ($O(n \log n)$ estable).
- **Justificación:** Preserva la integridad del repositorio central y garantiza consultas cronológicas en tiempo $O(\log n)$.

---

## 7. Commits realizados

| Hash | Mensaje | Descripción |
|---|---|---|
| `b8402b5` | `Initial commit` | Base del proyecto integrador. |
| `ffd4795` | `feat: completa TODOs RepositorioLecturas, AnalizadorMatriz e IngestaSensores` | Solución Semana 2: TAD, redimensionamiento dinámico y matriz de promedios. |
| `c34fe7b` | `feat: implementar ordenamientos, analisis de eficiencia y experimentos de semana 4 (H1)` | Implementación de los 6 algoritmos, TODO 1, TODO 2, TODO 3, banco de pruebas y documentación H1. |

---

## 8. Reflexión final
1. **Lo que ahora puedo hacer y antes no podía:** Interpretar la complejidad computacional no como una fórmula teórica en un pizarrón, sino como una herramienta de ingeniería para predecir costos reales de cómputo en producción.
2. **El aprendizaje más valioso:** Ningún algoritmo es "el mejor" de forma aislada; su idoneidad depende enteramente de la naturaleza de los datos y de cómo su uso impacta a los demás módulos del sistema.
