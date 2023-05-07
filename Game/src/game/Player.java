package game;
public class Player {
    private String name;
    private Board board;
    private Square square = new Square(0);
    private Square prevsquare = new Square(0);
    private boolean turn = true, prisoner = false;
    private int wt = 0;
  
    private Markes money;
    private boolean money_flag = true; // == o paiktis exei lefta
    
    public Player(String name, Board board, Markes money) {
        this.name = name;
        this.board = board;
        this.money = money;
        this.money.setPlayer(this);
    }
    
    public boolean moveTo(int dice1, int dice2) {
        square.removePlayer();
        //1h riyh
        if( square.getNumber() == 0 && turn ) {
            if ( (dice1 == 6 && dice2 == 3) || (dice1 == 3 && dice2 == 6) )
                square = board.getSquare(26);
            else if( (dice1 == 5 && dice2 == 4) || (dice1 == 4 && dice2 == 5) )
                square = board.getSquare(53);
            else  
                square = board.getSquare(square.getNumber() + dice1 + dice2);
            
            square.setPlayer(this);
            wt = square.action(dice1,dice2);
            square = board.getSquare(square.getStep());
        }
        else if(turn && !prisoner) {
            prevsquare = square;
            
            //out of bounds
            if( dice1 + dice2 + square.getNumber() > 63 ) {
                square = board.getSquare(63 - (square.getNumber() + dice1 + dice2 - 63) );
                square.setPlayer(this);
                wt = square.action(dice1, dice2);
                if ( square.getClass() == board.getSquare(6).getClass() && square.getNumber() != 58 ) {//58 = DEATH
                    square = board.getSquare(square.getNumber() - dice1 - dice2);
                    if ( square.getClass() == board.getSquare(6).getClass() ) {
                        square.setPlayer(this);
                        wt = square.action(dice1, dice2);
                        square = board.getSquare(square.getStep());
                    }
                }
                else
                    square = board.getSquare(square.getStep());
            }
             
            //inside the board
            else {
                square = board.getSquare(square.getNumber() + dice1 + dice2);
                square.setPlayer(this);
                wt = square.action(dice1,dice2);
                if ( square.getStep() > 63 ) {
                    square = board.getSquare(63 - (square.getNumber() + dice1 + dice2 - 63) );
                    square.setPlayer(this);
                    wt = square.action(dice1, dice2);
                    if ( square.getClass() == board.getSquare(6).getClass() && square.getNumber() != 58 ) {//58 = DEATH
                        square = board.getSquare(square.getNumber() - dice1 - dice2);
                        square.setPlayer(this);
                        if ( square.getClass() == board.getSquare(6).getClass() ) {
                            wt = square.action(dice1, dice2);
                            square = board.getSquare(square.getStep());
                        }
                    }
                }
                
                else {//STEP inside the board
                    Square temp = square;
                    square = board.getSquare(square.getStep());
                    square.setPlayer(this);
                    //squareleap meta thn 1h riyh
                    if( square.getClass() == board.getSquare(6).getClass() ) {
                        wt = square.action(dice1,dice2);
                        if ( square.getStep() > 63 ) {
                            square = board.getSquare(63 - (square.getNumber() + dice1 + dice2 - 63) );
                            square.setPlayer(this);
                            wt = square.action(dice1, dice2);
                            if ( square.getClass() == board.getSquare(6).getClass() && square.getNumber() != 58 ) {//58 = DEATH
                                square = board.getSquare(square.getNumber() - dice1 - dice2);
                                square.setPlayer(this);
                                if ( square.getClass() == board.getSquare(6).getClass() ) {
                                    wt = square.action(dice1, dice2);
                                    square = board.getSquare(square.getStep());
                                }
                            }
                        }
                        
                        else {//inside the board
                            square = board.getSquare(square.getStep());
                            square.setPlayer(this);
                            if( square.getClass() == board.getSquare(31).getClass() )
                                    square.action(dice1,dice2);
                            else if( square.getClass() == board.getSquare(58).getClass() ) {
                                square.action(dice1, dice2);
                                square = board.getSquare(square.getStep());
                                square.setPlayer(this);
                                if( square.getClass() == board.getSquare(31).getClass() )
                                    square.action(dice1,dice2);
                            }
                            else if( square.getClass() == board.getSquare(19).getClass() )
                                wt = square.action(dice1,dice2);
                            
                        }
                    }
                    else if( square.getClass() == board.getSquare(31).getClass()){
                        if(temp != square)
                            wt = square.action(dice1, dice2);
                    }
                }
            }
        }
        if ( square.getNumber() == 63 )
            return true;
        square.setPlayer(this);
        square.addPlayer();
        
        //waiting time
        if ( wt != 0 ) 
            turn = false;
        else
            turn = true;
        return false;
        
    }
    
    public void setSquare(Square square) {
        this.square = square;
    }
    
    public Square getSquare() {
        return square;
    }
    
    public Square getPreviousSquare() {
        return prevsquare;
    }
    
    public boolean getTurn() {
        return (!prisoner && turn);
    }
    
    public void setTurn(boolean turn) {
        this.turn = turn;
    }
    
    public void setBoard(Board board) {
        this.board = board;
    }
    
    public void setPrisoner(boolean prisoner) {
        this.prisoner = prisoner;
    }
    
    public boolean getPrisoner() {
        return prisoner;
    }
    
    public String getName() {
        return name;
    }
    
    public void setWT(int wt) {
        this.wt = wt;
        if( this.wt > 0 )
            turn = false;
    }
    
    public int getWT() {
        return wt;
    }
    
    public void reduceWT() {
        if( wt > 0 )
            wt--;
        if( wt == 0 )
            turn = true;
    }
    
    public Markes getMarkes(){
        return money;
    }
    
    public void setMoneyFlag(boolean money_flag){
        this.money_flag = money_flag;
    }
    
    public boolean getMoneyFlag(){
        return money_flag;
    }
    
    public String toString() {
        return  name + " is on square: " + square.getNumber() + " Money = " + money.getMoney();
    }
}