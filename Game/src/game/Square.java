package game;
public class Square {
    private int number;
    private Board board;
    private Player player,prevplayer;
    private int step, numplayers = 0;
    
    public Square(int number) {
        this.number = number;        
    }
    
    public int getNumber() {
        return number;
    }
    
    public void setNumber(int number)  {
        this.number = number;
    }
    
    public int action(int dice1, int dice2) {
        step = number;
        return 0;
    }
    
    public void setBoard(Board board) {
        this.board = board;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public void setStep(int number) {
        step = number;
    }
    
    public int getStep() {
        return step;
    }
    
    public int getNumPlayers() {
        return numplayers;
    }
    
    public void removePlayer() {
        if (numplayers > 0)
            numplayers--;
    }
    
    public void addPlayer() {
        numplayers++;
        if( numplayers == 1 )
            prevplayer = player;
        else {
            prevplayer.setSquare(player.getPreviousSquare());
            if( prevplayer.getSquare().getClass() ==  board.getSquare(52).getClass() ) {
                prevplayer.getSquare().setPlayer(prevplayer);
                prevplayer.getSquare().action(0,0);
            }
            numplayers--;
        }
    }
    
    public String toString() {
        return "";
    }
}