import com.the_ultimate_toolbox.util.GeneradoresWeb;

public class TestGeneradorQR {
    public static void main(String[] args) {
        String contenido = "https://www.youtube.com/watch?v=1_Z5q152GSQ";
        int ancho = 3000;
        int alto = 3000;
        String rutaArchivo = "qr_code_test.png";
        GeneradoresWeb.generarQRCode(contenido, rutaArchivo, ancho, alto);
    }
}