
package model;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Rai
 */
public class NumberProducer implements Runnable {

    /**
     * Hilo que genera numeros aleatorios y los coloca en una cola
     * (BlockingQueue) para su procesamiento.
     */
    private final BlockingQueue<Integer> numbersToProcess;
    private final int numberOfNumbersToGenerate;
    private final int maxNumberValue;

    /**
     * Constructor.
     *
     * @param numbersToProcess La cola donde se colocaran los numeros generados.
     * @param numberOfNumbersToGenerate La cantidad total de numeros a generar.
     * @param maxNumberValue Para controlar el numero maximo para los numeros
     * aleatorios generados.
     */
    public NumberProducer(BlockingQueue<Integer> numbersToProcess, int numberOfNumbersToGenerate, int maxNumberValue) {
        this.numbersToProcess = numbersToProcess;
        this.numberOfNumbersToGenerate = numberOfNumbersToGenerate;
        this.maxNumberValue = maxNumberValue;
    }

    @Override
    public void run() {
        System.out.println("Productor de numeros iniciando generacion.");
        Random random = new Random();
        for (int i = 0; i < numberOfNumbersToGenerate; i++) {
            // Genera numeros aleatorios desde 2 hasta maxNumberValue.
            int number = random.nextInt(maxNumberValue) + 2;
            try {
                // Coloca el numero en la cola y espera si esta llena.
                numbersToProcess.put(number);
            } catch (InterruptedException e) {
                System.err.println("Productor interrumpido... " + e.getMessage());
                Thread.currentThread().interrupt(); // Restablece el estado de interrupcion
                break;
            }
        }
        System.out.println("El productor de numeros ha finalizado de generar numeros.");
    }
}
