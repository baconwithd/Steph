# First Aid 59 · secuencias distintas en primera y tercera persona

Sustituye First Aid por firstaid-1.21.1-ZOMBOID-v3.6.22-distinct-medical-choreography.jar. Se incluye LR medical-bones.6, necesario para aplicar los huesos después de la animación nativa. Si instalaste el paquete 58, LR .6 es exactamente el mismo archivo: basta sustituir First Aid. No dejes versiones duplicadas. Conserva TaCZ .21 y el resto del paquete anterior.

## Qué cambia
Se elimina la combinación anterior de una oscilación común y posiciones de destino diferentes. Ahora hay rutas temporales escritas con fases de preparación, apoyo, trabajo y retirada, manos independientes y posturas diferentes en ambas vistas.

Zonas:
- Cabeza: alcance por coronilla, desplazamiento lateral y apoyo de la sien.
- Brazo izquierdo: presentar el antebrazo; la derecha recorre antebrazo/bíceps.
- Brazo derecho: soporte y recorrido cruzados distintos; trabaja la mano izquierda.
- Pierna izquierda: apoyo asimétrico de rodilla, recorrido exterior e interior.
- Pierna derecha: apoyo contrario y acceso inferior; no es una copia reflejada de la otra.
- Pie izquierdo: apoyo del tobillo y recorrido de talón a empeine.
- Pie derecho: acceso a la planta y cruce sobre cordones, con otra trayectoria.
- Torso: cruce lateral sobre el pecho, rodeo y presión final.

Tratamientos:
- Venda: sacar tira, pasar alrededor, cruzar, tensar y presionar.
- Tablilla: colocar longitudinalmente, sujetar extremos y apretar dos puntos.
- Extracción: inspeccionar, alinear, mantener la herramienta, tirar hacia fuera y apartarla.
- Inyección: apoyar, insertar perpendicularmente, mantener y retirar.
- Medicación oral: preparar la dosis, levantar la mano a la boca y bajarla.
- Desinfección: inclinar, limpiar en dos direcciones, separar y secar por presión.
- Retirada: buscar borde, pinzar, despegar diagonalmente y apartar hacia abajo.

Son movimientos sobre el rig existente; se conservan sus objetos y sonidos nativos. No se añaden dedos ni articulaciones de codo al modelo Minecraft. Se mantienen los ejercicios, reanimaciones, consumo, tiempos, selección de lesiones, sincronización médica y la interacción izquierda de TaCZ del paquete 58.

El reloj de reproducción se reinicia al empezar otra aplicación sobre la misma zona. Los relojes de primera y tercera persona se coordinan sin reiniciarse mutuamente.

## Previews
- FirstAid-59-Zonas: ocho sitios, cada uno en primera y tercera persona.
- FirstAid-59-Tratamientos: siete acciones, cada una en ambas vistas.

Son renders técnicos fuera de Minecraft. La primera persona usa los cubos de referencia y la jerarquía del rig Bedrock; la tercera muestra las rotaciones y desplazamientos del código aplicados a un maniquí de dimensiones Minecraft. No muestran la skin, shaders, modelo clínico final ni captura dentro del juego. Las trayectorias se exportan desde los métodos Java que utiliza el parche, sin animación aparte para el preview.

## Validación
297 clases verificadas con ASM. Pruebas JVM con -Xverify:all, reflexión sobre el rig con fixtures y JOML real. Se compara la trayectoria temporal de las 56 combinaciones de zona/acción en cada vista DESPUÉS de quitar la posición inicial y normalizar amplitud: ninguna coincide. Así se rechazan las variantes que solo cambian de destino. Se comprueba continuidad, independencia de manos, reposo, rigs ajenos, reinicios de tratamiento, fases compartidas entre vistas y orden update -> huesos -> render -> limpieza. test-results.json contiene los resultados.

No se ha ejecutado Minecraft con tu modpack. Queda pendiente la prueba visual de integración, especialmente si otro animador reemplaza las poses al final del render.

## Fuentes y recompilación
El ZIP de fuentes contiene medical59 (build.py, test.py, src, pruebas y preview), medical58 (fixtures, contratos y geometry.json), medical57 (contrato) y medical52 (utilidad ASM). Java 21; Python con numpy, Pillow y ffmpeg para el preview; joml-1.10.5.jar junto a medical58 para las pruebas. build.py utiliza como bases las salidas First Aid .21 y LR .6 del paquete 58, en medical58/output. test.py comprueba también TaCZ .21 del paquete 58 en esa carpeta. No se incluyen credenciales ni scripts de publicación.
