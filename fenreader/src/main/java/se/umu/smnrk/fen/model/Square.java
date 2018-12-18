package se.umu.smnrk.fen.model;

/**
 * Defines the Coordinate for all Squares in a chess board.
 * @author Simon Eriksson
 * @version 1.0
 * @see Coordinate
 */
public enum Square {
    A1(0,7), B1(1,7), C1(2,7), D1(3,7), E1(4,7), F1(5,7), G1(6,7), H1(7,7),
    A2(0,6), B2(1,6), C2(2,6), D2(3,6), E2(4,6), F2(5,6), G2(6,6), H2(7,6),
    A3(0,5), B3(1,5), C3(2,5), D3(3,5), E3(4,5), F3(5,5), G3(6,5), H3(7,5),
    A4(0,4), B4(1,4), C4(2,4), D4(3,4), E4(4,4), F4(5,4), G4(6,4), H4(7,4),
    A5(0,3), B5(1,3), C5(2,3), D5(3,3), E5(4,3), F5(5,3), G5(6,3), H5(7,3),
    A6(0,2), B6(1,2), C6(2,2), D6(3,2), E6(4,2), F6(5,2), G6(6,2), H6(7,2),
    A7(0,1), B7(1,1), C7(2,1), D7(3,1), E7(4,1), F7(5,1), G7(6,1), H7(7,1),
    A8(0,0), B8(1,0), C8(2,0), D8(3,0), E8(4,0), F8(5,0), G8(6,0), H8(7,0);

    private Coordinate coordinate;

    Square(int x, int y){
        coordinate = new Coordinate(x, y);
    }

    public Coordinate getCoordinate(){
        return coordinate;
    }
}
