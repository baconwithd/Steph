# Instalar el parche First Aid + Les Raisins

1. Abre `aplicar_firstaid_lr.py` en esta rama y pulsa **Download raw file** (icono de descarga, arriba del código). Si el repositorio es privado, inicia sesión en GitHub.
2. Guarda el archivo completo en la carpeta `mods` de tu instancia. No copies fragmentos del código.
3. Cierra Minecraft.
4. Abre PowerShell en `mods` y ejecuta:

```powershell
python .\aplicar_firstaid_lr.py
```

El instalador incluye el parche: no descarga archivos adicionales. Reemplaza los dos JAR originales conservando sus nombres y guarda copias en `copias_firstaid_lr`, junto a `mods`. Verifica las versiones y la integridad de los JAR antes de reemplazarlos. Si ya está aplicado, lo detecta.

Requiere exactamente los archivos originales `firstaid-1_3_3-ZOMBOID-HERIDAS-v3.1.jar` y `LesRaisins-Tactical-Equipements-1.21.1-0.4.3-zomboid-jsononly-safe.jar` usados para preparar el parche. No borres los originales antes de ejecutar el instalador.

Incluye la animación CAR desde 3,0 s para bandage/plaster, el modelo de venda, cloth de aplicación rápida, texturas nuevas e integración médica. La textura cloth fue recreada desde la imagen del chat. Las pruebas de código e instalación pasan; la prueba dentro de Minecraft sigue pendiente.
