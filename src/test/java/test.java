import com.Bufigol.generadores.GeneradorIdentificadoresAleatorios;
import com.Bufigol.comprobadores.ComprobadoresIdentificadores;

public class test {
    public static void main(String[] args) {
        String dni = GeneradorIdentificadoresAleatorios.generarDni();
        System.out.println(dni + " ¿es valido? " + ComprobadoresIdentificadores.isValidDni(dni));
        System.out.println("Mi dni: "+ ComprobadoresIdentificadores.isValidDni("53991929g"));
    }
}