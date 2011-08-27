/**
 * 
 */
package event;

/**
 * 
 * 
 * @author Denys Solyanyk <peacemaker@ukr.net>
 * @copyright 2010-2011 Denys Solyanyk <peacemaker@ukr.net>
 * @since 13 июля 2011
 */
public interface IObserver<E> {
    public void update(E eventData);
}
