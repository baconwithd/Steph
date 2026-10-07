# Fuentes correspondientes a los cambios v3.3 y v3.4

Se proporcionan las fuentes Java de los dos nuevos componentes, los transformadores ASM para las clases originales y el generador de recursos. Los cambios de First Aid están bajo la licencia GPLv3 del mod; la adaptación del manejador de The Hordes conserva su LGPLv2.1. Los mods originales conservan sus autores y licencias. No se cambia el código de Self Expression ni de Horde Hoard.

El parche depende de los JAR exactos aportados en esta conversación. Las huellas de los originales, las entradas modificadas y las huellas de todas las entradas finales están en `SPEC` del instalador. `PAYLOAD` contiene sólo las entradas reemplazadas o nuevas.

## Reconstrucción

Trabaja en una carpeta temporal y usa Java 21, ECJ 3.39.0 y ASM 9.7.1 (asm, asm-tree, asm-analysis). Para descompilar clases originales de referencia se usó CFR 0.152; los transformadores preservan el bytecode no cambiado y sus frames, por lo que no requieren recompilar Minecraft.

1. Extrae el First Aid exacto enviado (v3.2 interno, aunque su nombre termine en v3.1) a `work/v33/fa` y The Hordes 1.6.3f a `work/v33/hordes`.
2. Guarda `PatchV33.java`, `PatchV34.java`, `ClinicalState.java`, `TreatmentV33.java` y `ValidateAll.java` en `work/v34`. Coloca ECJ en `work/ecj.jar` y los componentes ASM en `work/asm.jar`, `work/asm-tree.jar`, `work/asm-analysis.jar`.
3. Compila:

```sh
java -jar work/ecj.jar -21 -warn:none -cp work/asm.jar:work/asm-tree.jar:work/asm-analysis.jar:work/v33/fa -d work/v34/classes work/v34/PatchV33.java work/v34/PatchV34.java work/v34/ClinicalState.java work/v34/TreatmentV33.java work/v34/ValidateAll.java
java -cp work/v34/classes:work/asm.jar:work/asm-tree.jar PatchV33
```

4. Copia los directorios completos `work/v33/fa` y `work/v33/hordes` a `work/v34/fa` y `work/v34/hordes`, respectivamente. Ejecuta el transformador siguiente **una sola vez**:

```sh
java -cp work/v34/classes:work/asm.jar:work/asm-tree.jar PatchV34
cp work/v34/classes/ichttt/mods/firstaid/survival/ClinicalState*.class work/v34/fa/ichttt/mods/firstaid/survival/
cp work/v34/classes/ichttt/mods/firstaid/compat/lrtactical/TreatmentV33.class work/v34/fa/ichttt/mods/firstaid/compat/lrtactical/
```

5. Coloca `resources.py` y `recycled_items.json` en `work/v34`; ejecuta `python work/v34/resources.py`. Las cantidades se derivan de las ranuras registradas de las 876 prendas de Self Expression 2.22a. El generador añade recetas y un modelo para la tela sucia, textos y dependencias. Conserva los recursos existentes, incluidas las texturas del parche anterior.
6. Verifica:

```sh
java -cp work/v34/classes:work/asm.jar:work/asm-tree.jar:work/asm-analysis.jar ValidateAll work/v34/fa work/v34/hordes
```

7. Empaqueta cada árbol como JAR, conservando todos sus archivos. La huella completa del contenedor puede cambiar por las marcas de tiempo y compresión; cada entrada debe coincidir con las huellas de `SPEC`. El instalador incluye también las entradas de directorio de los JAR originales.

`TreatmentV33` mantiene su nombre binario por compatibilidad con los hooks v3.3, pero este archivo contiene las reglas finales v3.4. Sus métodos de servidor no tienen tipos de cliente en sus firmas ni inicializadores. Las llamadas por reflexión usan las firmas públicas de las versiones exactas del juego y de los mods.
