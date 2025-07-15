package util;

import model.PrimesList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author Rai
 */
public class PrimesFromCSV {

    /**
     * Carga numeros primos desde un archivo CSV y los añade a PrimesList.
     *
     * @param filePath La ruta del archivo CSV.
     * @param primesList La instancia de PrimesList donde se añadiran los
     * primos.
     */
    public static void PrimesFromCsv(String filePath, PrimesList primesList) {
        System.out.println("Intentando cargar numeros primos desde: " + filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) { //Lee el archivo hasta que encuentre una linea vacia.
                String[] primeStrings = line.split(","); //Separa los numeros 1 por 1 usando la coma y los pasa a string..
                for (String primes : primeStrings) {
                    try {
                        int prime = Integer.parseInt(primes.trim()); //Para dar fomato, eliminar espacios en blanco y pasar a int.                        
                        primesList.add(prime); // Se agrega directamente sin isPrime aquí porque PrimesList.add() ya valida que es primo.
                        System.out.println("Numero primo cargado: " + prime);
                    } catch (NumberFormatException e) { //Manejo de excepcion cuando no es el formato correcto.
                        System.err.println("No se pudo leer el numero " + primes + " del archivo CSV. " + e.getMessage());
                    } catch (IllegalArgumentException e) { //Manejo de excepcion cuando no es un numero primo.
                        System.err.println("El numero '" + primes + "' del CSV no es primo y no fue agregado a la lista. " + e.getMessage());
                    }
                }
            }
            System.out.println("Carga de numeros primos desde CSV finalizada con exito.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage()); //Manejo de excepcion ante un error de lectura en el archivo CSV.
        }
    }

    /**
     * Escribe mensajes encriptados y sus codigos primos en un archivo de texto.
     * Se implementa con FileWriter + PrintWriter para facilitar escritura.
     *
     * @param filePath La ruta del archivo de texto donde se escribira.
     * @param encryptedMessage El mensaje encriptado.
     * @param primeCode El código primo asociado al mensaje.
     */
    public static void writeEncryptedMessage(String filePath, String encryptedMessage, int primeCode) {
        System.out.println("Escribriendo mensaje encriptado en: " + filePath);
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) { // 'true' para modo edicion del archivo. Agregando contenido al final del mismo en vez de sobreescribirlo.
            pw.println("Mensaje Encriptado: " + encryptedMessage);
            pw.println("Codigo Primo: " + primeCode);
            
            System.out.println("Mensaje encriptado y codigo primo escritos exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de mensajes encriptados: " + e.getMessage());
        }
    }

// Metodo de ejemplo para simular un mensaje encriptado y un código primo
    public static void simulateEncryptionAndWrite(String filePath) {
        String simulatedMessage1 = "Mi voto secreto es A1B2C3D4";
        int simulatedPrimeCode1 = 19; // Un primo de ejemplo

        String simulatedMessage2 = "Confirmacion de identidad X5Y6Z7W8";
        int simulatedPrimeCode2 = 23; // Otro primo de ejemplo

        writeEncryptedMessage(filePath, simulatedMessage1, simulatedPrimeCode1);
        writeEncryptedMessage(filePath, simulatedMessage2, simulatedPrimeCode2);
    }
}
