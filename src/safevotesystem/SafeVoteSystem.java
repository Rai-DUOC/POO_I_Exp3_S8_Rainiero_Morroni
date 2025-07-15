package safevotesystem;

import events.PrimeListener;
import events.PrimeTopic;
import model.PrimesList;
import model.PrimesThread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import model.NumberProducer;
import util.PrimesFromCSV;

/**
 *
 * @author Rai
 */
public class SafeVoteSystem {

    private static final String PRIMES_CSV_PATH = "primes.csv";
    private static final String ENCRYPTED_MESSAGES_PATH = "encrypted_messages.txt";

    public static void main(String[] args) {
        System.out.println("--- Iniciando Aplicacion SafeVoteSystem ---");

    

       
        //Lista sincronizada para almacenar numeros primos.
        PrimesList myPrimes = new PrimesList();

        // --- Configuración de la Cola (Queue) para distribuir tareas --- 
        BlockingQueue<Integer> numbersToProcessQueue = new LinkedBlockingQueue<>(100);

       // --- Configuracion de Topic (Publicar-subscribir)
        PrimeTopic primeTopic = new PrimeTopic();
        myPrimes.setPrimeTopic(primeTopic); 

        // --- Subscribir 'escuchadores' al Topic
        primeTopic.subscribe(new PrimeListener() {
            @Override
            public void onNewPrimeFound(int prime) {
                System.out.println("[Listener 1] Nuevo primo detectado: " + prime);
            }
        });

        primeTopic.subscribe(prime -> System.out.println("[Listener 2] Primo encontrado: " + prime + ". Tamano actual de lista: " + myPrimes.getPrimesCount()));

        // --- Productores y Consumidores ---
        int totalNumbersToGenerate = 10000; // Total de numeros aleatorios a generar para la cola
        int maxNumberValue = 100000; // Valor maximo para los numeros aleatorios
        int numberOfConsumerThreads = 5; // Numero de hilos consumidores        
        int numbersPerConsumer = totalNumbersToGenerate / numberOfConsumerThreads; // Cada consumidor procesa una parte de los numeros generados.

        // --- Hilo Productor: Genera numeros y los encola ---
        Thread producerThread = new Thread(new NumberProducer(numbersToProcessQueue, totalNumbersToGenerate, maxNumberValue), "Productor de Numeros");
        producerThread.start();

        // --- Hilos Consumidores: Toman numeros de la cola y los procesan ---
        Thread[] consumerThreads = new Thread[numberOfConsumerThreads];
        for (int i = 0; i < numberOfConsumerThreads; i++) {
            consumerThreads[i] = new Thread(new PrimesThread(myPrimes, numbersToProcessQueue, numbersPerConsumer), "Hilo-" + (i + 1));
            consumerThreads[i].start();
        }

        // --- Esperar a que los hilos terminen de procesaar ---
        try {
            producerThread.join(); // Espera que cada hilo termine
            for (Thread t : consumerThreads) {
                t.join(); // Espera que cada hilo consumidor termine
            }
        } catch (InterruptedException e) {
            System.err.println("El hilo principal fue interrumpido mientras esperaba: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== Finalizando Generacion de Numeros Primos ===");       
        System.out.println("Cantidad final de numeros primos en la lista: " + myPrimes.getPrimesCount());       
        System.out.println("Lista final de primos: " + myPrimes.subList(0, Math.min(myPrimes.size(), 20)));

        // --- Pruebas de Lectura/Escritura de Archivos ---
        System.out.println("\n--- Prueba de carga de primos desde CSV ---");     
        PrimesList loadedPrimes = new PrimesList();
        loadedPrimes.setPrimeTopic(primeTopic); // Topic 
        PrimesFromCSV.PrimesFromCsv(PRIMES_CSV_PATH, loadedPrimes);
        System.out.println("Primos cargados de CSV. Cantidad total: " + loadedPrimes.getPrimesCount());
        

        System.out.println("\n--- Prueba de escritura de mensajes encriptados ---");
        PrimesFromCSV.simulateEncryptionAndWrite(ENCRYPTED_MESSAGES_PATH);
        System.out.println("Verifica el archivo '" + ENCRYPTED_MESSAGES_PATH + "' para ver los mensajes encriptados.");

        System.out.println("\n--- Aplicacion SafeVoteSystem Finalizada ---");

    }
}
