package game;

/*
Rantses Vinse
AM:2022201900196
dit19196@go.uop.gr

Alexandropoulos Ilias
AM:2022201900007
dit19007@go.uop.gr
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Game {
    public static void main(String[] args) {
        Board board = new Board();
        int answer;
        Scanner input = new Scanner(System.in);
        ArrayList<Player> players = new ArrayList<Player>();
        do{
            
            do {
                Menu();
                try {
                    answer = input.nextInt();
                }
                catch(InputMismatchException | NumberFormatException e) {
                    System.err.println("Error found.");
                    System.out.println("Enter only integers from 1 to 4.\n");
                    answer = -1;
                    input.nextLine();
                }
            }while( answer < 0  || answer > 4 );
            ////////////////////////////////////////////////////////////////////
            if ( answer == 3 ) 
                System.out.printf("Coded by: L0uSg3 & TheHueMaster.\n\n");
            else if( answer == 1 ) {
                board.boardClear();
                boardCreator(board);
                MarkesCenter center = new MarkesCenter(0);
                
                int numplayers = totalPlayers();
                
                players.removeAll(players);
                String name;
                for ( int i = 0; i < numplayers ; i++ ) {
                    System.out.print("-{Player" + (i + 1) + "}- Please enter your nickname: ");
                    name = input.next();
                    System.out.print("Enter the amount of money you want to play: ");
                    
                    try {
                        int x = input.nextInt();
                        if( x > 0) {
                            Markes money = new Markes(x);
                            money.setMarkesCenter(center);
                            players.add(new Player(name,board,money));
                            money.setPlayer(players.get(i));
                        }
                        else {
                            System.err.println("Error found.");
                            System.out.println("Enter a positive integer. Please try again.\n");
                            i--;
                        }
                            
                    }
                    catch( InputMismatchException | NumberFormatException e ) {
                        System.err.println("Error found.");
                        System.out.println("Enter a positive integer. Please try again.\n");
                        i--;
                        input.nextLine();
                    }
                }
                
                input.nextLine();
                play(players,center);
            }
            else if ( answer == 2 ) {
                players.removeAll(players);
                System.out.print("Please enter filename: ");
                String filename;
                ArrayList<Player> savedplayers = new ArrayList<Player>();
                MarkesCenter center;
                ////////////////////////////////////////////////////////////////
                while(true) {
                    try {
                        filename  = input.next();
                        File f = new File(filename);
                        Scanner scan = new Scanner(f);
                        board.boardClear();
                        boardCreator(board);
                        String curplayer = "";
                        int center_money = 0;
                        ////////////////////////////////////////////////////////
                        while( scan.hasNextLine() ) {
                            String[] lines = scan.nextLine().split(" ");
                            String name = lines[0];
                            int cell;
                            if( scan.hasNextLine() && lines.length > 3 ) {
                                Player player = new Player(name,board, new Markes(0) );
                                players.add(player);
                                cell = Integer.parseInt(lines[4]);
                                player.getMarkes().setMoney(Integer.parseInt(lines[7]));
                                if(Integer.parseInt(lines[7]) == 0)
                                    player.setMoneyFlag(false);
                                if( lines.length > 8 )
                                    if( lines[8].equals("Prisoner") )
                                        player.setPrisoner(Boolean.parseBoolean(lines[10]));
                                    else
                                        player.setWT(Integer.parseInt(lines[10]));
                                if( cell != 0 )
                                    player.setSquare(board.getSquare(cell));
                            }
                            if(!name.equals("center"))
                                curplayer = name;  
                            else
                                center_money = Integer.parseInt(lines[2]);
                        }
                        center = new MarkesCenter(center_money);
                        ////////////////////////////////////////////////////////
                        int counter = 0;
                        for( Player p : players ){
                            if( p.getName().equals(curplayer) ) 
                                break;//for
                            counter++;
                        }
                        for (int i = 0 ; i < players.size() ; i++ )
                            if( i >= counter )
                                savedplayers.add( players.get(i) );

                        for (int i = 0 ; i < players.size() ; i++ ) {
                            if( i < counter )
                                savedplayers.add( players.get(i) );
                            else
                                break;//for
                        }
                        break;//while
                    }
                    catch (FileNotFoundException e) {
                        System.err.println("Error found");
                        System.out.println("File not found.");
                        input.nextLine();
                    }
                }
                ////////////////////////////////////////////////////////////////
                for(Player p: savedplayers)
                    p.getMarkes().setMarkesCenter(center);
                
                play(savedplayers,center);
                savedplayers.removeAll(savedplayers);                
            }
            else
                System.out.println("~{Goodbye! Thanks for playing}~");
            
        }while( answer != 4 );
    }
    
    
    public static void Menu() {
        System.out.println("1. New Game.");
        System.out.println("2. Load Game.");
        System.out.println("3. Creators.");
        System.out.println("4. Exit.");
        System.out.print("Choose from 1 to 4.\n~");
    }
    
    public static void boardCreator(Board board) {
        for( int i = 1 ; i < 64 ; i++ ) {
            board.addSquare(i);
            board.getSquare(i).setBoard(board);
        }
    }
    
    public static int totalPlayers() {
        Scanner input = new Scanner(System.in);
        int numplayers;
        boolean validNumPlayers;
        do {
            System.out.print("Please enter number of players: ");
            try {
                validNumPlayers = true;
                numplayers = input.nextInt();
                if( numplayers < 2 ) {
                    System.err.println("Error found.");
                    System.out.println("Enter a positive integer greater than 1.");
                }
            }
            catch( InputMismatchException | NumberFormatException e ) {
                validNumPlayers = false;
                numplayers = 0;
                input.nextLine();
                System.err.println("Error found.");
                System.out.println("Enter a positive integer greater than 1.");
            }
        }while(!validNumPlayers || numplayers < 2 || numplayers > 6);
        return numplayers;
    }
    
    public static void play(ArrayList<Player> players, MarkesCenter center) {
        Random rng = new Random();
        Scanner input = new Scanner(System.in);
        String choice;
        int dice1 , dice2;
        boolean tryflag, EOG = false;
        do{
            for ( Player p : players ) {
                if( PlayersWithMoney(players) ){
                    p.getMarkes().setMoney(p.getMarkes().getMarkesCenter().getMoney() + p.getMarkes().getMoney());
                    System.out.println(p.getName()+", you are the only one with money. You won!");
                    System.out.println("You have " + p.getMarkes().getMoney());
                    EOG = true;
                    break;//for
                }
                if( p.getTurn() && p.getMoneyFlag() ) {
                    System.out.printf("\n= Type 'roll' to roll the dice. Or type 'save' to save the game. Or type 'exit' to return on the main menu. =\n~" + p.getName() +  " is on square " + p.getSquare().getNumber() + ": " );
                    choice = input.next();
                    
                    while( !choice.equals("save") && !choice.equals("roll") && !choice.equals("exit")) {
                        System.err.println("Error found.");
                        System.out.println("Valid answers are: 'roll' or 'save' or 'exit' .");
                        System.out.printf("\n= Type 'roll' to roll the dice. Or type 'save' to save the game. Or type 'exit' to return on the main menu. =\n~" + p.getName() +  " is on square " + p.getSquare().getNumber() + ": " );
                        choice = input.next();
                    }
                    ////////////////////////////////////////////////////////////
                    if ( choice.equals("save") ) {
                        save(p,players);
                        System.out.printf("\n= Type 'roll' to roll the dice. Or type 'exit' to return on the main menu. =\n~" + p.getName() +  " is on square " + p.getSquare().getNumber() + ": " );
                        choice = input.next();
                        while( !choice.equals("roll") && !choice.equals("exit")) {
                            System.err.println("Error found.");
                            System.out.println("Valid answers are: 'roll' or 'save' or 'exit' .");
                            System.out.printf("\n= Type 'roll' to roll the dice. Or type 'exit' to return on the main menu. =\n~" + p.getName() +  " is on square " + p.getSquare().getNumber() + ": " );
                            choice = input.next();
                        }
                        if( choice.equals("roll") ) {
                            dice1 = rng.nextInt(6) + 1;
                            dice2 = rng.nextInt(6) + 1;
                            EOG = p.moveTo(dice1, dice2);
                            System.out.println("\n==================================================================");
                            boardPrint(players);
                            System.out.print("\n==================================================================");
                            System.out.println("\n{Dice1 = " + dice1 +  " " + "|| Dice2 = " + dice2 + "} " + p.toString() );
                            if( EOG ) {
                                System.out.println("Congratiolations! " + p.getName() + " is the winner. You finished the game with " + p.getMarkes() + " and you got " + center.getMarkesCenter() + " money." );
                                break;//for
                            }
                        }
                        else {
                            EOG = true;
                            break;
                        }
                    }
                    else if( choice.equals("roll") ) {
                        dice1 = rng.nextInt(6) + 1;
                        dice2 = rng.nextInt(6) + 1;
                        EOG = p.moveTo(dice1, dice2);
                        System.out.println("\n==================================================================");
                        boardPrint(players);
                        System.out.print("\n==================================================================");
                        System.out.println("\n{Dice1 = " + dice1 +  " " + "|| Dice2 = " + dice2 + "} " + p.toString() );
                        if( EOG ) {
                            System.out.println("Congratiolations! " + p.getName() + " is the winner. You finished the game with " + p.getMarkes() + " and you got " + center.getMarkesCenter() + " money.");
                            break;//for
                        }
                    }
                    else {
                        EOG = true;
                        break;
                    }
                    ////////////////////////////////////////////////////////////
                }
                else if(!p.getMoneyFlag())
                    System.out.println("You lost all your money " + p.getName());
                else {
                    System.out.println(p.getName() + " you're in square: " +p.getSquare().getNumber() + p.getSquare().toString() + " and you lost your turn.");
                    System.out.print("Type 'save' to save the game. Or 'exit' to end the game. Or press a key to continue: ");
                    System.out.println();
                    String answ = input.next();
                    if( answ.equals("save") )
                        save(p,players);
                    else if( answ.equals("exit")) {
                        EOG = true;
                        break;
                    }
                    else {
                        p.reduceWT();
                    }
                    
                }
            }
        }while(!EOG);
    }
    
    public static boolean PlayersWithMoney(ArrayList<Player> players) {//returns true if there is only one player with money
        int counter = 0;//counter of players without money
        for(Player p: players)
            if( p.getMarkes().getMoney() == 0 )
                counter++;
        
        if( players.size() - counter == 1 )
            return true;
        else 
            return false;
    }
    
    public static void save(Player p, ArrayList<Player> players) {
        Scanner input = new Scanner(System.in);
        boolean tryflag;
        do{
            try {
                System.out.print("Enter the filename: ");
                String filename = input.next();
                tryflag = false;
                PrintWriter pw = new PrintWriter(new FileOutputStream(filename) );
                System.out.println("Data saved.");
                for( Player pl : players )
                    if ( pl.getSquare().getNumber() == 52 || p.getSquare().getNumber() == 31 )
                        pw.println(pl.toString() + " Prisoner = " + pl.getPrisoner() );
                    else if( pl.getSquare().getNumber() == 19 ) 
                        pw.println(pl.toString() + " WT = " + pl.getWT() );
                    else
                        pw.println(pl.toString());
                pw.println(p.getName());
                pw.println("center = " + p.getMarkes().getMarkesCenter().getMoney());
                pw.close();
            }
            catch( FileNotFoundException e ) {
                System.err.println("Error found");
                System.out.println("File not found.");
                tryflag = true;
            }
        }while(tryflag);
    }
    
    public static void boardPrint(ArrayList<Player> players) {
        ArrayList<String> cells = new ArrayList<String>();
        int[] Goose = {5, 9, 14, 18, 23, 27, 32, 36, 41, 45, 50, 54, 59};
        boolean gooseflag;
        int player_counter = 1;
        for (Player p : players) {

            if (player_counter >= 2) {
                for (int i = 0; i <= 63; i++) {
                    gooseflag = false;
                    for (int g : Goose) {
                        if (g == p.getSquare().getNumber()) {
                            cells.set(g, "P" + player_counter + "   ");
                            gooseflag = true;
                            break;
                        }
                    }
                    if (!gooseflag && i != 0) {
                        if (i == 19) {
                            if (i == p.getSquare().getNumber()) {
                                cells.set(i, "       P" + player_counter);
                                break;
                            }
                        } else if (i == 31) {
                            if (i == p.getSquare().getNumber()) {
                                cells.set(i, "P" + player_counter + "      ");
                                break;
                            }
                        } else if (i == 42) {
                            if (i == p.getSquare().getNumber()) {
                                cells.set(i, "  P" + player_counter + "    ");
                                break;
                            }
                        } else if (i == 52) {
                            if (i == p.getSquare().getNumber()) {
                                cells.set(i, "     P" + player_counter + "   ");
                                break;
                            }
                        } else if (i == 58) {
                            if (i == p.getSquare().getNumber()) {
                                cells.set(i, "P" + player_counter + "   ");
                                break;
                            }
                        } else {
                            if (i == p.getSquare().getNumber()) {
                                cells.set(i, "P" + player_counter);
                                break;
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i <= 63; i++) {
                    gooseflag = false;
                    for (int g : Goose) {
                        if (g == p.getSquare().getNumber()) {
                            cells.add("P" + player_counter + "   ");
                            gooseflag = true;
                            break;
                        } else if (g == i) {
                            cells.add("GOOSE");
                            gooseflag = true;
                            break;
                        }
                    }
                    if (!gooseflag) {
                        if (i == 0) {
                            cells.add("Start");
                        } else if (i == 19) {
                            if (i == p.getSquare().getNumber()) {
                                cells.add("       P" + player_counter);
                            } else {
                                cells.add("THE TAVERN");
                            }
                        } else if (i == 31) {
                            if (i == p.getSquare().getNumber()) {
                                cells.add("P" + player_counter + "      ");
                            } else {
                                cells.add("THE WELL");
                            }
                        } else if (i == 42) {
                            if (i == p.getSquare().getNumber()) {
                                cells.add("  P" + player_counter + "    ");
                            } else {
                                cells.add("THE MAZE");
                            }
                        } else if (i == 52) {
                            if (i == p.getSquare().getNumber()) {
                                cells.add("     P" + player_counter + "   ");
                            } else {
                                cells.add("THE PRISON");
                            }
                        } else if (i == 58) {
                            if (i == p.getSquare().getNumber()) {
                                cells.add("P" + player_counter + "   ");
                            } else {
                                cells.add("DEATH");
                            }
                        } else if (i == 63) {
                            cells.add("Finish");
                        } else {

                            if (i == p.getSquare().getNumber()) {
                                cells.add("P" + player_counter);

                            } else {
                                cells.add(String.valueOf(i));
                            }
                        }
                    }
                }
            }
            player_counter++;
        }
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            if (cells.get(counter).length() > 2) {
                System.out.print(cells.get(counter) + " ");
                counter++;
            } else {
                System.out.print("   " + cells.get(counter) + "    ");
                counter++;
            }
        }
        System.out.println();
        System.out.print("\t\t\t\t\t\t\t     " + cells.get(counter) + "   ");
        counter++;
        System.out.println();
        for (int i = 36; i < 43; i++) {
            if (cells.get(i).length() > 2) {
                System.out.print(cells.get(i) + " ");
            } else {
                System.out.print("   " + cells.get(i) + "   ");
            }
        }
        System.out.print("\t       " + cells.get(counter) + "   ");
        counter++;
        System.out.println();

        System.out.println(cells.get(35) + "\t\t\t\t\t      " + cells.get(43) + "\t       " + cells.get(counter));
        counter++;
        System.out.println(cells.get(34) + "\t\t" + cells.get(58) + "   " + cells.get(59) + "    " + cells.get(60) + "\t      " + cells.get(44) + "\t       " + cells.get(counter));
        counter++;

        System.out.println(cells.get(33) + "\t\t" + cells.get(57) + "\t\t " + cells.get(61) + "\t    " + cells.get(45) + "\t       " + cells.get(counter));
        counter++;

        System.out.println(cells.get(32) + "\t\t" + cells.get(56) + "\t\t " + cells.get(62) + "\t      " + cells.get(46) + "\t     " + cells.get(counter));
        counter++;

        System.out.println(cells.get(31) + "\t" + cells.get(55) + "\t       " + cells.get(63) + "\t      " + cells.get(47) + "\t       " + cells.get(counter));
        counter++;

        System.out.println(cells.get(30) + "\t\t" + cells.get(54) + "\t\t\t      " + cells.get(48) + "\t       " + cells.get(counter));
        counter++;

        System.out.println(cells.get(29) + "\t\t" + cells.get(53) + " " + cells.get(52) + "\t " + cells.get(51) + "   " + cells.get(50) + "   " + cells.get(49) + "\t       " + cells.get(counter));
        counter++;

        System.out.println(cells.get(28) + "\t\t\t\t\t\t\t     " + cells.get(counter));
        counter++;

        for (int i = 0; i < 9; i++) {
            System.out.print(cells.get(8 + counter));
            if (i == 0) {
                System.out.print("  ");
            } else if (i >= 1 && i <= 3) {
                System.out.print("   ");
            } else if (i >= 4 && i <= 6) {
                System.out.print("  ");
                if (i == 5) {
                    System.out.print(" ");
                }
            }
            System.out.print("  ");
            counter--;
        }
    }
    
}