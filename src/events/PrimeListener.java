package events;

/**
 * Interfaz para notificar cuando se encuentre un nuevo primo.
 *
 * @author Rai
 */
public interface PrimeListener {

    void onNewPrimeFound(int prime);
}
