package se.umu.smnrk.schackplaneraren.database;

import java.util.List;

import se.umu.smnrk.schackplaneraren.model.DatabaseObject;

/**
 * Allows posting any DatabaseObject into an EventBus post method.
 * @param <T> the DatabaseObject to send as an EventBus event.
 * @author Simon Eriksson
 * @version 1.0
 */
public class EventBusEvent<T extends DatabaseObject> {
    private List<T> list;

    public EventBusEvent(List<T> list){
        this.list = list;
    }

    public List<T> getList(){
        return list;
    }

    public void setList(List<T> list){
        this.list = list;
    }
}
