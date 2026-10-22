# Iris · recargar shaders con }

Sustituye el Iris anterior por iris-neoforge-1.8.14-beta.1+mc1.21.1-reload-brace.1.jar. No instales dos Iris a la vez. Conserva tu shaderpack y el resto de mods.

Dentro del mundo, pulsa la combinación que escribe `}` en tu teclado. Se detecta el carácter real: funciona con Shift en un teclado estadounidense y con la combinación correspondiente de tu distribución española/latinoamericana. Pulsar solamente `]` no recarga. Escribir en chat, inventarios u otros menús no activa el atajo. Mantener la tecla pulsada no provoca recargas continuas.

El antiguo enlace R guardado en options.txt se migra a la nueva asignación al primer tick de Iris; no necesitas borrar options.txt. Se conservan otras teclas personalizadas. En Controles, Minecraft representa la asignación predeterminada mediante la tecla física de corchete derecho; el activador predeterminado efectivo es el carácter `}`. Puedes asignar otra tecla personalizada en Controles; el atajo literal `}` también seguirá disponible.

La recarga usa la ruta original de Iris, incluidos sus mensajes y manejo de errores. No cambia shaders, iluminación, render, programas GLSL ni configuraciones del shaderpack.

Pruebas: verificación ASM de 950 clases; ejecución JVM del controlador con fixtures de API para teclado estadounidense/español, migración de R, teclas personalizadas, repetición, ventanas ajenas, chat/menús y ausencia de mundo; comprobación de las anotaciones y argumentos exactos de los dos hooks de KeyboardHandler. Solo cambian Iris.class y el registro de mixins, y se añaden el controlador y su mixin. No se incluyen clases simuladas de Minecraft dentro del mod. No se ha ejecutado Minecraft con el modpack completo.

El ZIP de fuentes contiene el parche reproducible, código, fixtures y pruebas. Java 21. build.py espera el JAR original subido por el usuario en la ruta base indicada; ajusta esa ruta si recompilas fuera de este workspace. Iris original: https://github.com/IrisShaders/Iris (LGPL-3.0-only); se conserva la licencia y metadatos del JAR original.
