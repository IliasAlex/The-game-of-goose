package game;

import java.util.ArrayList;

public class Board {
    private int[] Goose = {5,9,14,18,23,27,32,36,41,45,50,54,59};
    private ArrayList<Square> squares = new ArrayList<Square>();
    
    
    public void addSquare(int number) {
        for( int g : Goose )
            if ( g == number ) {
                SquareLeap goose = new SquareLeap(number);
                squares.add(goose);
                return;
            }
        
        if( number == 6 || number == 42 || number == 58 ) {
            SquareLeap leap = new SquareLeap(number);
            squares.add(leap);
        }
        else if( number == 19 ) {
            SquareStandby sb = new SquareStandby(number);
            squares.add(sb);
        }
        else if( number == 31 | number == 52 ) {
            SquarePause pause = new SquarePause(number);
            squares.add(pause);
        }
        else if ( number == 63 ) {
            SquareEnd end = new SquareEnd(number);
            squares.add(end);
        }
        else {
            Square square = new Square(number);
            squares.add(square);
        }
    }
    
    public Square getSquare(int num) {
        return squares.get(num - 1);
    }
    
    public void boardClear() {
        squares.removeAll(squares);
    }
    
}