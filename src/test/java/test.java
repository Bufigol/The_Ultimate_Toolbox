import com.bufigol.generadores.GeneradorIdentificadoresAleatorios;
import com.bufigol.comprobadores.ComprobadoresIdentificadores;

public class test {
    public static void main(String[] args) {
        String dni = GeneradorIdentificadoresAleatorios.generarDni();
        System.out.println(dni + " ¿es valido? " + ComprobadoresIdentificadores.isValidDni(dni));
        System.out.println("Mi dni: "+ ComprobadoresIdentificadores.isValidDni("53991929g"));
    }
}