package game;

public class Markes 
{
    private int money;
    private Player player;
    private MarkesCenter center;
    
    public Markes(int money) {
        this.money = money;
    }
    
    public void setMarkesCenter(MarkesCenter center) {
        this.center = center;
    }
    public MarkesCenter getMarkesCenter() {
        return center;
    }
    public int getMoney() {
        return money;
    }
    
    public void setMoney(int money) {
        this.money = money;
    }
    
    public void redMoney(){
        if( money > 0 )
            money--;
        if( money == 0 )
            player.setMoneyFlag(false);
    }
    public void incMoney() {
        money++;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
}
