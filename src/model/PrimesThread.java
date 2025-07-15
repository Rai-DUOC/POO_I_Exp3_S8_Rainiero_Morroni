
package model;

import java.util.concurrent.BlockingQueue;

/**
 * Hilo que toma numeros de una cola/queue para verificar si son primos los
 * agrega a PrimesList
 *
 * @author Rai
 */
public class PrimesThread implements Runnable {

    private final PrimesList primesList;
    private final BlockingQueue<Integer> numbersToProcess; //Cola de numeros a procesar.    
    private final int numbersToTake; // Numeros que usara el hilo.

    public PrimesThread(PrimesList primesList, BlockingQueue<Integer> numbersToProcess, int numbersToTake) {
        this.primesList = primesList;
        this.numbersToProcess = numbersToProcess;
        this.numbersToTake = numbersToTake;
    }

    @Override
    public void run() {
        System.out.println("Iniciando generacion de numeros aleatorios en: " + Thread.currentThread().getName());

        for (int i = 0; i < numbersToTake; i++) {

            try {
                Integer numero = numbersToProcess.take(); //Toma un numero de la cola.
                if (numero != null) {
                    if (primesList.isPrime(numero)) {
                        primesList.add(i); //Agrega el numero a la lista si es primo.
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Hilo " + Thread.currentThread().getName() + " interrumpido." + e.getMessage());
                Thread.currentThread().interrupt();
                break; //Sale si se interrumpe el hilo.
            } catch (IllegalArgumentException e) { //Excepcion en cas de que el numero no sea primo y no se agrege.
                System.err.println("Error al intentar agregar un numero no primo: " + e.getMessage());
            }
        }

        System.out.println("Finalizada generacion de numeros primos aleatorios en: " + Thread.currentThread().getName());
    }
}
