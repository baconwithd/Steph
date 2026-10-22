# First Aid .26 — fuente, cámara y chat

Reemplaza SOLO firstaid .25 por el JAR .26 de este paquete. Conserva Survivor Creator v38. En LAN/servidor, mantén la misma versión de First Aid en ambos lados.

Cambios:
- Se corrige la ruta de la fuente. El log mostraba `FileNotFoundException: firstaid:font/font/pz64.ttf`: Minecraft ya añade `font/` al nombre del proveedor TTF. Ahora resuelve `assets/firstaid/font/pz64.ttf` y se añade la fuente normal del juego como respaldo.
- La cámara de muerte conserva al jugador muerto como referencia nativa, sin crear el ArmorStand auxiliar de .25. Así se mantiene la integración normal de cámara y visibilidad. El bloqueo de respawn hasta confirmar el personaje se conserva.
- No se dibuja el menú de muerte si ya no hay jugador o mundo cargado. Esto evita que quede superpuesto sobre una salida del mundo o una pantalla de guardado de Replay Mod.
- Se puede abrir el chat desde el menú de muerte y desde la pantalla de derribado. Se respeta la tecla de chat configurada; la tecla de comandos abre el chat con `/`.
- El chat permanece abierto mientras escribes; al cerrarlo reaparece el menú correspondiente. Mientras escribes se permite el HUD nativo para que se vea el historial; al cerrar se vuelve a ocultar.

Validación: se verificaron las clases de los JAR y se probaron los flujos de muerte, creación, reanimación, chat normal, comandos, tecla reasignada y desconexión con API simulada. Se reprodujo la ruta rota de .25 y se comprobó la resolución de la nueva ruta, la apertura del TTF y el dibujado de caracteres españoles. No se ejecutó el modpack real; falta comprobar visualmente el fondo con tus shaders y mods de visibilidad.
