package game;
public class SquareStandby extends Square {
    private int wt = 0; //waiting time
    public SquareStandby(int number) {
        super(number);
    }
    
    @Override
    public int action(int dice1,int dice2) {
        super.setStep(super.getNumber());
        super.getPlayer().getMarkes().redMoney();
        super.getPlayer().getMarkes().getMarkesCenter().incMoney();
        
        if ( super.getNumber() == 19 )
            wt = 2;
        return wt;
    }
    
    public void addPlayers() {
        
    }
    
    public String toString() {
        return "(The Tavern)";
    }
}