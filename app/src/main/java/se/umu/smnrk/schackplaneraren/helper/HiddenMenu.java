package se.umu.smnrk.schackplaneraren.helper;

/**
 * Defines an API for revealing a hidden menu based on different states.
 * @author Simon Eriksson
 * @version 1.0
 */
public interface HiddenMenu {
    enum State {
        DEFAULT,
        EDIT,
        DELETE
    }

    /**
     * Manipulates the state of a menu containing a HiddenMenu.
     */
    interface StateManipulator {
        State getState();
        void manipulateState();
    }

    /**
     * Defines what happens when any of the HiddenMenu options are selected.
     */
    interface Controller {
        /**
         * @return an object manipulating the state of a menu containing a
         *         HiddenMenu.
         */
        StateManipulator getStateManipulator();
        void onMenuDelete();
        void onMenuEdit();
    }
}
