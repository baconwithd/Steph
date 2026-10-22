# First Aid 58: manos independientes e interacción izquierda

Reemplazar los tres JAR anteriores de First Aid, LesRaisins y TaCZ por los incluidos. No conservar versiones duplicadas. Tactical marks200-85s.5 y taczaddon 1.1.8.3 se mantienen; no instalar copias adicionales. Requiere las dependencias habituales del modpack.

- First Aid 3.6.21 + LR medical-bones.6: sustitución tardía de los huesos de ambas manos en el rig de vendas de First Aid, después del estado nativo de LR. Destinos distintos para cabeza, torso, cada brazo, pierna y pie; gestos diferentes al vendar, retirar venda, inmovilizar, extraer, inyectar, medicar y desinfectar. Conserva la duración, consumo y sonidos originales. Conserva las poses médicas de tercera persona de la entrega 57.
- TaCZ .21: alcance/contacto/retirada del brazo izquierdo al abrir puertas o interactuar con objetos con el arma equipada, usando la selección de interacción existente. Duración 620 ms, sin mover la mano derecha. Primera y tercera persona del jugador local, también la vista isométrica del propio jugador. No añade sincronización remota para que otros clientes vean este gesto. Las interacciones conservan su lógica original: una puerta bloqueada puede mostrar el intento de alcance. Inspecciones, recargas y accesorios mantienen sus animaciones nativas.
- Conserva la postura baja ante paredes de TaCZ .20. No modifica iluminación, daño, curaciones, inventarios ni networking.

## Preview
FirstAid-58-preview.mp4 / gif: visualización técnica de seis tratamientos, con la jerarquía y los cubos de referencia del rig Bedrock de First Aid y los destinos exportados por el código Java nuevo. Render fuera de Minecraft: no representa los shaders, la skin del jugador, el campo de visión ni el resultado final en el juego. No es una grabación de gameplay. Los cubos de referencia de los brazos son sustituidos por los brazos del jugador en el render nativo.

## Validación
Verificación ASM de 296 clases First Aid, 219 LR y 1073 TaCZ; ejecución JVM con -Xverify:all de los ayudantes reales y fixtures de API; JOML real para interpolación de cuaterniones; combinaciones de zona/acción y continuidad; exclusión de rigs ajenos y reposo; orden update -> huesos -> render -> limpieza; interacción restringida al brazo izquierdo, mangas, reenvío único del render nativo y aislamiento de jugador/mundo. Consulte test-results.json y audit.json. El TaCZ .20 del archivo multipart subido coincide por SHA256 con la base utilizada.

No se ejecutó Minecraft con el modpack completo. Es necesaria la prueba visual de integración con tus packs y animadores, especialmente al tratar piernas/pies y al abrir contenedores.

## Fuentes
El ZIP de fuentes incluye el parche 58, pruebas, preview y contexto de las entregas 57 y 52. build.py usa los JAR de salida 57 y TaCZ .20 como bases; ajuste sus rutas si recompila fuera del workspace. test.py necesita joml-1.10.5.jar (Maven Central) junto al script. No se incluyen credenciales ni herramientas de publicación.
