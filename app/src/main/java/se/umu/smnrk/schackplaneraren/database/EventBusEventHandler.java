package se.umu.smnrk.schackplaneraren.database;

import se.umu.smnrk.schackplaneraren.model.DatabaseObject;

/**
 * Defines a method where results from database queries are handled.
 * @param <T> the type of DatabaseObject to receive in an EventBusEvent.
 * @author Simon Eriksson
 * @version 1.0
 */
@FunctionalInterface
public interface EventBusEventHandler<T extends DatabaseObject> {
    /**
     * Handles the result of an EventBus post.
     * @param listEvent containing a list of DatabaseObjects.
     */
    void onEventBusPost(EventBusEvent<T> listEvent);
}
