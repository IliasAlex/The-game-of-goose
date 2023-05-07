package game;
public class SquareEnd extends Square {

    public SquareEnd(int number) {
        super(number);
    }
    
    public int action(int dice1, int dice2) {
        super.setStep(63);
        return 0;
    }
    
}