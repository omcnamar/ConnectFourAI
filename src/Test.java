import java.util.InputMismatchException;
import java.util.Scanner;

public class Test {
    private static int depth = 7;
    private static int height;
    private static int width;
    private static int connect;
    private static char currentPlayer;
    private static int choice;
    private static GameState gameState;
    private static int firstPlayer;
    //create a scanner object
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args){


        //make a decision who's playing
        System.out.println("1 : Human vs Human \n2 : AI vs Human");
        //check for valid input for who's playing who
        do{
            try {
                choice = input.nextInt();
                if (choice == 1 || choice == 2) {
                    break;
                }else{
                    System.out.println("Enter 1 or 2 only");
                }
            }catch(InputMismatchException e){
                System.out.println(e);
            }
            input.nextLine();//clear the buffer
        }while(true);

        //loop to make sure of valid input for the settings of the game
        do{
            try{
                //prompt the user for Input
                System.out.println("Enter height of the Game");
                height = input.nextInt();
                System.out.println("Enter width of the Game");
                width = input.nextInt();
                System.out.println("Enter connect number");
                connect = input.nextInt();
                System.out.println("Enter who will play first: Enter x or y");
                //loop to make sure there is player x and y only
                do {
                    currentPlayer = input.next().charAt(0);
                    if(currentPlayer == 'x' || currentPlayer == 'y'){
                       break;
                    }else{
                        System.out.println("Enter x or y only:");
                    }
                    input.nextLine();//clear the buffer
                }while(true);
                //if it is AI vs human who will go first?
                if(choice == 2){
                    System.out.println("Who plays first Human or AI? \n1 : Human\n2 : AI");
                    do {
                        try {
                            firstPlayer = input.nextInt();
                            if(firstPlayer == 1 || firstPlayer == 2){
                                break;
                            }else{
                                System.out.println("Enter 1 or 2 only.");
                            }
                        }catch (InputMismatchException e){
                            System.out.println(e);
                        }
                        input.nextLine();
                    }while(true);
                }
                break;
            }catch(InputMismatchException e){
                System.out.println(e);
            }
            input.nextLine();//clear the buffer
        }while(true);

        //create a board and a GameState
        char[][] board = new char[height][width];
        gameState = new GameState(board, connect);

        //human vs human game is here
        if(choice == 1){
            do{
                askHumanMove();
            }while(!gameState.isOver());
        }
        //human vs AI
        else {
            do {
                //player turn when Human vs AI
                if (firstPlayer == 1) {
                    askHumanMove();
                    firstPlayer = 2;
                }
                //here is where it is AI turn o
                else {
                    //make a move
                    TreeNode root = new TreeNode(gameState);
                    System.out.println("Thinking...");
                    root.minMax(0, depth, true);
                    boolean moveSuccess = gameState.makeMove(root.bestMoveColumn, currentPlayer);
                    int colr = root.bestMoveColumn;
                    if (moveSuccess) {
                        //change player
                        switchPlayer();
                        gameState.print();
                        firstPlayer = 1;
                    } else {
                        while (gameState.isColumnFull(colr)) {
                            colr++;
                        }
                        gameState.makeMove(colr, currentPlayer);
                        //switch player
                        switchPlayer();
                        gameState.print();
                        firstPlayer = 1;
                    }
                    if (gameState.isWinning()) {
                        //since we have already switched the player we need to switch it back if AI won so the
                        //correct winner displayed
                        switchPlayer();
                        System.out.println(currentPlayer + " WON!!!");
                        break;
                    }
                }
            } while (!gameState.isOver());
        }

        //game over if we have reached this point
        System.out.println("GAME OVER!");

    }
    public static void askHumanMove(){
        //if current player is x then it is user not AI
        int column;
        do {
                //prompt the user to enter which column does he want to place his move
                System.out.println("Player " + currentPlayer + " Enter a column: ");
                //handle invalid input
                do {
                    try {
                        column = input.nextInt();
                        if (column >= 0 && column < width) {
                            break;
                        } else {
                            System.out.println("Invalid row, Enter a row in between [0 and " + width + ")");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid Input, Enter a number.");
                    }
                    input.nextLine();//clear the buffer
                } while (true);

                //make a move
                boolean moveSuccess = gameState.makeMove(column, currentPlayer);

                //change player/break/and let the user know if the win has happened
                if (moveSuccess) {
                    //first print the bord
                    gameState.print();
                    //check if the last move won
                    if (gameState.isWinning()) {
                        System.out.println(currentPlayer + " WON");
                        break;
                    }
                    //if not switch players
                    switchPlayer();
                    System.out.println("Move was success");
                    break;
                }
                //print move was false
                System.out.println("Move was: " + moveSuccess);
        }while(true);
    }

    public static void switchPlayer(){
        if(currentPlayer == 'x'){
            currentPlayer = 'y';
        }else{
            currentPlayer = 'x';
        }
    }
}
