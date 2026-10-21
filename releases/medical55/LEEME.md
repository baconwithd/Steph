# Corrección 55 — iluminación, impactos y vendas

Reemplaza las versiones anteriores de los SEIS mods incluidos. No dejes duplicados:

- Zomboid Gore .38 (sustituye .37)
- Ragdollified .31 (sustituye .30)
- Flashlight .22 (sustituye .21 o .20)
- First Aid 3.6.18 (sustituye 3.6.17)
- Hardcore True Darkness .6 (sustituye .5)
- Dungeons ISO .34 (sustituye .33)

Conserva FracturePoint .15 de la corrección 53, Iris 1.8.14-beta.1 y TaCZ First Aid Compat 1.0.5. Esta entrega no modifica protocolos de red ni formatos de inventario. Actualiza los mods de contenido en los equipos donde ya los utilizabas; no agregues los mods exclusivos de renderizado a un servidor dedicado. Guarda una copia del mundo antes de probar.

## Qué cambia

Iluminación: se elimina la oclusión mediante rayos sobre profundidad de pantalla que repetía siluetas del arma dentro del haz. La linterna utiliza el mapa de distancias de bloques existente, con filtrado y borde suave. Reduce la amplificación que deslavaba el color y mezcla la iluminación con la escena. En isométrica agrega una contribución ambiental tenue sobre superficies válidas; la oscuridad final conserva más gradación y deja de excluir todo el cono mediante una marca binaria de alfa. Conserva la composición diferida tras Iris y evita volver a renderizar el terreno. Es una aproximación inspirada en la referencia de Project Zomboid, no una reproducción de su motor.

Crash: corrige el tipo de referencia de la llamada Armor53.provider. Antes se emitía una referencia de interfaz hacia una clase, causando IncompatibleClassChangeError al renderizar armaduras del ragdoll. Ahora se prueba esa instrucción en una JVM, además de verificar bytecode.

Impactos: los cuerpos físicos de NPCs derribados que siguen vivos dejan de omitirse en la búsqueda de impactos de balas TaCZ. Se entrega el impacto al método original onHitEntity de TaCZ, conservando armadura, eventos, daño y protecciones del NPC. El colisionador lógico del mismo NPC no bloquea su cuerpo físico; otras entidades cercanas mantienen su prioridad. Las protecciones temporales originales del NPC siguen vigentes. No se promete que todo fallo de cualquier arma o ataque cuerpo a cuerpo quede resuelto.

Vendas: el snapshot visible incluye una marca de venda aunque la lesión original no tuviera coordenadas procedurales. Se evita duplicarla si ya existe una marca. Las mordidas vendadas dejan de recortar la piel; el dibujo de marcas médicas se realiza antes de las rutas que pueden cancelar el modelo. No se cambian los tiempos de curación, infección ni inventarios.

Cámara: antes de la interpolación de apuntado isométrico se descartan destinos no finitos o coincidentes con el punto de los ojos. El log tenía numerosos avisos de rotación NaN. Esto protege esa ruta concreta; no identifica por sí solo el origen de todos los NaN del modpack.

## Validación y límites

Compilados seis JARs con Java 21 y verificados sus métodos mediante ASM. Pruebas de regresión: 4030 comprobaciones médicas/colisión y 217 de impactos en movimiento, más ejecución de Bullet13.tick sobre cadáveres. Pruebas aisladas de vendas sin coordenadas, suciedad, deduplicación y retirada; 64 mordidas vendadas sin recorte. Ejecución JVM de la instrucción de proveedor corregida. Pruebas del despacho de daño a NPC vivo, tipo de impacto y prioridad de colisionadores. Filtro de destinos inválidos probado.

Ambos fragment shaders compilan y enlazan con OpenGL real mediante Mesa llvmpipe. El fragment de linterna dibuja sobre un plano controlado con valores finitos y caída suave de brillo. Es una prueba sintética, no una captura de Minecraft.

NO se ejecutó el modpack completo con Iris, MakeUp, EMF y todos tus mods. Falta validar visualmente iluminación y vendas en ese entorno. El mapa de sombras de bloques tiene resolución limitada y se actualiza con presupuesto de rayos; no genera sombras físicas de cada entidad. El color de linterna con shaders sigue recuperándose de una escena ya iluminada, por lo que no equivale a iluminación nativa del shaderpack. No se incluye una modificación de MakeUp ni de Iris.
