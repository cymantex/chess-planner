package se.umu.smnrk.schackplaneraren.helper;

/**
 * Defines an observable set of data.
 * @author Simon Eriksson
 * @version 1.0
 * @param <T> the type of data to share with the observers.
 */
public interface DataSet<T> {
    /**
     * @param observer to notify of changes to this object's DataSet.
     */
    void addObserver(DataSetObserver<T> observer);

    /**
     * Notifies the change of a single item in this object's DataSet.
     * @param position that was updated.
     */
    void notifyObservers(int position);
}
