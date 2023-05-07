package game;
public class SquarePause extends Square {
    private Player firstplayer;
    private int numprisoners = 0;
    public SquarePause(int number) {
        super(number);
        super.setStep(number);
    }
    
    public int action(int dice1, int dice2) {
        super.setStep(super.getNumber());
        super.getPlayer().getMarkes().redMoney();
        super.getPlayer().getMarkes().getMarkesCenter().incMoney();
        
        if( super.getNumber() == 31 ) // well
            if( numprisoners == 0 ) {
                firstplayer = super.getPlayer();
                super.getPlayer().setPrisoner(true);
            }
            else {
                firstplayer.setPrisoner(false);
                super.getPlayer().setPrisoner(true);
                numprisoners--;
            }
        else //prison
            if( numprisoners == 0 ) {
                firstplayer = super.getPlayer();
                super.getPlayer().setPrisoner(true);
            }
            else {
                firstplayer.setPrisoner(false);
                numprisoners = 0;
            }
        return 0;
    }
    
    public void addPlayer() {
        numprisoners++;
    }
    
    public String toString() {
        return super.getNumber() == 31 ? "(The Well)" : "(The Prison)";
    }
}