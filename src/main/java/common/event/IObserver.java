/**
 *
 */
package common.event;

/**
 * @author Denys Solianyk <peacemaker@ukr.net>
 * @since 2011-06-13
 */
public interface IObserver<E> {
    public void update(E eventData);
}
