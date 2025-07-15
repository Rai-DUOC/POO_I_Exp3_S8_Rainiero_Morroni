package events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Para notificar cuando se encuentra un nuevo primo.
 *
 * @author Rai
 */
public class PrimeTopic {

    private final List<PrimeListener> listeners = Collections.synchronizedList(new ArrayList<>());

    //Subscribe "escuchadores" al topic.
    public void subscribe(PrimeListener listener) {
        listeners.add(listener);
        System.out.println(listener.getClass().getSimpleName() + "suscrito.");
    }

    public void unsubscribe(PrimeListener listener) {
        listeners.remove(listener);
        System.out.println(listener.getClass().getSimpleName() + "desuscrito");
    }

    public void publishNewPrime(int prime) {
        for (PrimeListener listener : new ArrayList<>(listeners)) {
            try {
                listener.onNewPrimeFound(prime);
            } catch (Exception e) {
                System.out.println("Error al notificar a " + listener.getClass().getSimpleName() + ": " + e.getMessage());

            }
        }
    }

}
