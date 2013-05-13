/**
 *
 */
package common.event;

/**
 * @author Denys Solianyk <peacemaker@ukr.net>
 * @since 13 июля 2011
 */
public interface ISubject<E> {
    public void attach(IObserver<E> observer);

    public void clear();

    public void detach(IObserver<E> observer);

    public void notifyObservers(E eventData);

    public int size();
}
