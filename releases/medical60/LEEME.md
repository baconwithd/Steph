# First Aid 60 · contacto real con la pierna seleccionada

Reemplaza los JAR anteriores de First Aid y LesRaisins por los dos incluidos: First Aid 3.6.23 y LR medical-leg-view.7. No dejes copias antiguas en mods. Conserva TaCZ .21 y los demás mods del paquete anterior.

## Causa
La entrega 59 tenía trayectorias diferentes, pero ninguna restricción que obligase a la palma a llegar a la pierna. Inclinar el cuerpo no bastaba. Además, al seleccionar la pierna izquierda se flexionaba la derecha: los índices rightLeg/leftLeg de la pose estaban cruzados. Las pruebas anteriores medían diferencias entre gestos; no comprobaban contacto corporal.

## Corrección
- Tercera persona: se levanta la pierna seleccionada y se inclina el torso; el pie de apoyo conserva su altura de suelo. La orientación de cada brazo se calcula para que su palma alcance una superficie exterior de esa pierna o tobillo. La mano de apoyo y la que trabaja tienen puntos distintos. Se conservan giros de agarre diferentes para cada tratamiento, sin separar la palma del objetivo.
- Primera persona: durante estos tratamientos se dibuja la pierna seleccionada con el modelo y la textura de piel del propio jugador. Las manos se ajustan en el mismo espacio del rig a las superficies de esa pierna. El desplazamiento global antiguo que solo bajaba las manos se desactiva para piernas/pies. La pierna aparece y desaparece suavemente con el tratamiento.
- La pasada adicional de primera persona restaura la posición, rotación, escala y visibilidad de los ModelPart compartidos incluso si falla el render. No cambia la cámara del mundo ni añade estados OpenGL manuales.

Esto es contacto con la región corporal seleccionada (espinilla/tobillo), no seguimiento de la coordenada exacta de cada agujero de bala. No añade articulaciones de codo al modelo Minecraft. El primer plano de pierna muestra la piel y la capa de pantalón del modelo; no reproduce capas externas de armadura ni el render completo de heridas de tercera persona.

Se mantienen los gestos de otras zonas, las curaciones y su duración, los objetos, los sonidos y la lógica de selección/sincronización anteriores. No se modifica TaCZ.

## Preview
FirstAid-60-Contacto.mp4 / gif: render técnico fuera de Minecraft. Primera persona: jerarquía del rig Bedrock con las transformaciones corregidas y la pierna de referencia en el mismo espacio. Tercera: maniquí con los ángulos y desplazamientos exportados por el código de producción. La marca roja es una guía de zona seleccionada, no una herida del juego. No muestra shaders, texturas del pack, capas clínicas ni gameplay.

## Validación
298 clases First Aid y 220 LR verificadas con ASM. En 2.492 combinaciones de pierna/pie, tratamiento y momento se comprueba la posición calculada de ambas palmas contra la superficie objetivo, el lado correcto y la altura del pie de apoyo. La prueba rechaza expresamente la pose libre de la entrega anterior. Se usa la jerarquía/pivotes del rig original para la prueba de primera persona y JOML real; no solo se comparan parámetros de animación.

También se ejecuta el puente opcional LR -> First Aid con fixtures de render: pierna correcta, textura del jugador, matrices compartidas entre manos/pierna, reposo, restauración de los ModelPart y limpieza tras fallo. Se verifica el orden de render y sus argumentos nativos. Consulte test-results.json.

No se ha ejecutado Minecraft con tu modpack completo. Está pendiente validar visualmente la integración con sus modelos, capas y otros animadores.

## Fuentes
El ZIP de fuentes contiene medical60 (código, build.py/test.py, fixture de rig, contratos y preview) y contexto de medical52/57/58 requerido por esas herramientas. Para recompilar: Java 21; bases First Aid .22 y LR .6 de la entrega 59 en medical59/output; JOML 1.10.5 en medical58/joml.jar. test.py utiliza TaCZ .21 de la entrega 58 solo para comprobar que su render de manos sigue intacto. El preview requiere numpy, Pillow y ffmpeg. No contiene credenciales ni herramientas de publicación.
