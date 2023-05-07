package game;
public class SquareLeap extends Square {
    private int[] Goose = {5,9,14,18,23,27,32,36,41,45,50,54,59};
    public SquareLeap(int number) {
        super(number);
    }
    
    public int action(int dice1,int dice2) {
        if(super.getNumber() == 6 || super.getNumber() == 42 || super.getNumber() == 58){
            super.getPlayer().getMarkes().redMoney();
            super.getPlayer().getMarkes().getMarkesCenter().incMoney();
        }
        
        if( super.getNumber() == 6 )
            super.setStep(12);
        else if( super.getNumber() == 42 ) 
            super.setStep(30);
        else if( super.getNumber() == 58 )
            super.setStep(1);
        else 
            for( int g : Goose )
                if ( g == super.getNumber() ) {
                    super.setStep(g + dice1 + dice2);
                    return 0;
                }
        return 0;
    }
    
}