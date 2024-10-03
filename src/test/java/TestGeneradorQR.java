import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.bufigol.generadores.GeneradoresWeb;
import com.google.zxing.WriterException;

public class TestGeneradorQR {
    public static void main(String[] args) {
        String contenido = "https://www.ejemplo.com";
        int ancho = 300;
        int alto = 300;
        String rutaArchivo = "qr_code_test.png";

        try {
            // Generar el código QR
            byte[] qrCodeBytes = GeneradoresWeb.generarQRCode(contenido, ancho, alto);

            // Guardar el código QR como un archivo de imagen
            try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
                fos.write(qrCodeBytes);
            }

            System.out.println("Código QR generado y guardado como: " + rutaArchivo);

            // Intentar abrir la imagen (esto funciona en sistemas de escritorio)
            try {
                java.awt.Desktop.getDesktop().open(Paths.get(rutaArchivo).toFile());
            } catch (IOException e) {
                System.out.println("No se pudo abrir la imagen automáticamente. Por favor, busca el archivo " + rutaArchivo + " en tu sistema de archivos.");
            }

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}