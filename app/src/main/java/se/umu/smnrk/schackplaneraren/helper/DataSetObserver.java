package se.umu.smnrk.schackplaneraren.helper;

/**
 * Defines an observer of some DataSet.
 * @author Simon Eriksson
 * @version 1.0
 * @param <T> the kind of DataSet to observe.
 * @see DataSet
 */
public interface DataSetObserver<T> {
    /**
     * Defines what happens when a single entry in the DataSet is changed.
     * @param position that was updated.
     * @param dataSet to examine.
     */
    void onDataSetEvent(int position, T dataSet);
}
