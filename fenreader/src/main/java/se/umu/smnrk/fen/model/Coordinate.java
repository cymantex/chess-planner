package se.umu.smnrk.fen.model;

/**
 * Defines a basic 2D integer based coordinate.
 * @author Simon Eriksson
 * @version 1.0
 */
public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    /**
     * @return true if the object is a Position and contains the same value
     *         for the x and y coordinate, false otherwise.
     */
    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }

        if(o == this){
            return true;
        }

        if(o instanceof Coordinate){
            Coordinate test = (Coordinate)o;
            if((getX() == test.getX()) && (getY() == test.getY())){
                return true;
            }
        }

        return false;
    }

    /**
     * @return The hash value of x and y.
     */
    @Override
    public int hashCode(){
        return 31 * x + y;
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}
