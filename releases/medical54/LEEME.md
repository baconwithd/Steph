# Corrección 54 — fallo de arranque

Quita Flashlight .20 y coloca flashlight-5.1-neoforge-1.21.1-mixin-retention.21.jar. Conserva los otros cuatro mods de la corrección 53 y tu Iris. No dejes ambas versiones de Flashlight.

Corrige la retención de @Mixin en AfterWorld53: RuntimeInvisibleAnnotations, como requiere Sponge Mixin para esta anotación de retención CLASS. @Inject conserva su retención RUNTIME. El log anterior fallaba preparando este mixin, antes de abrir Minecraft.

Verificados bytecode y atributos de anotaciones. No se ejecutó el modpack completo; esta corrección resuelve la causa concreta del error de preparación mostrado. La iluminación y las poses todavía requieren comprobación visual.
