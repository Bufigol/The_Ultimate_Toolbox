import com.the_ultimate_toolbox.util.GeneradorIdentificadoresAleatorios;
import com.the_ultimate_toolbox.api.ComprobadoresIdentificadores;

public class test {
    public static void main(String[] args) {
        String dni = GeneradorIdentificadoresAleatorios.generarDni();
        System.out.println(dni + " ¿es valido? " + ComprobadoresIdentificadores.isValidDni(dni));
        System.out.println("Mi dni: "+ ComprobadoresIdentificadores.isValidDni("53991929g"));
    }
}