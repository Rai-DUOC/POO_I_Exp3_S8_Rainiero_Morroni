package model;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import events.PrimeTopic;

/**
 *
 * @author Rai
 */
public class PrimesList extends ArrayList<Integer> {

    private final Lock countLock = new ReentrantLock();
    private PrimeTopic primeTopic; //para publicar eventos.

    /**
     * PrimeTopic para notificar sobre nuevos primos.
     *
     * @param primeTopic Intancia a usar.
     */
    public void setPrimeTopic(PrimeTopic primeTopic) {
        this.primeTopic = primeTopic;
    }

    /**
     * Verificar si el numero es primo: NO SON PRIMOS: numeros igual o menor a
     * 1. numeros con residuo == 0.
     *
     * @param numero: numero entero a verificar
     * @return : true es primo; false no es primo
     */
    public boolean isPrime(int numero) {
        if (numero <= 1) {
            return false;
        }
        for (int i = 2; i * i <= numero; i++) {
            if (numero % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sobrescribir metodo add para que solo agrege numeros primos:
     *
     * @param elemento El numero a añadir a la lista.
     * @return : true si el elemento fue añadido exitosamente.
     * @throws IllegalArgumentException si el numero no es primo.
     */
    @Override
    public synchronized boolean add(Integer elemento) { //Metodo sincronizado.

        if (!isPrime(elemento)) {
            throw new IllegalArgumentException("No se puede agregar el numero " + elemento + " ya que no es un numero primo.");
        }
        boolean added = super.add(elemento);

        //Notificar a topic si se añadio con exito
        if (added && primeTopic != null) {
            primeTopic.publishNewPrime(elemento);
        }
        return added;
    }

    /**
     * Sobrescribe el metodo remove de la clase 'ArrayList'. No se implementa
     * logica de eliminacion de primos, ya que se limito agregar solo numeros
     * primos en metodo isPrime.
     *
     * @param o El objeto a eliminar de la lissta.
     * @return true si el objeto fue encontrado y eliminado.
     */
    @Override
    public synchronized boolean remove(Object o) { //Metodo sincronizado.
        return super.remove(o);
    }

    /**
     * Despliega la cantidad de numeros primos agregados a lista. Protege acceso
     * con ReentrantLock.
     *
     * @return : Cantidad de numeros primos en la lista.
     */
    public synchronized int getPrimesCount() { //Metodo sincronizado.
        countLock.lock(); //Bloqueo
        try {
            return size();
        } finally {
            countLock.unlock(); // Desbloqueo
        }

    }
}
