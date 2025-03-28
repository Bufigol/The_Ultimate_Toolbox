import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.bufigol.generadores.GeneradoresWeb;
import com.google.zxing.WriterException;

public class TestGeneradorQR {
    public static void main(String[] args) {
        String contenido = "https://www.youtube.com/watch?v=1_Z5q152GSQ";
        int ancho = 3000;
        int alto = 3000;
        String rutaArchivo = "qr_code_test.png";
        try {
            GeneradoresWeb.generarQRCode(contenido, rutaArchivo, ancho, alto);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}