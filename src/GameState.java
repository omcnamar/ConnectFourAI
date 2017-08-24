
public class GameState {
    //private data
    private char[][] board;
    private int heuristic;
    private int connect;
    private int currentRow;
    private int currentColumn;
    private char currentPlayer;
    private int numberOfMoves;
    private int height;
    private int width;

    //constructor
    public GameState(char[][] inboard, int connect){
        board = new char[inboard.length][inboard[0].length];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                board[i][j] = inboard[i][j];
            }
        }
        this.connect = connect;
        numberOfMoves =0;
        height = inboard.length;
        width = inboard[0].length;
    }

    //copy constructor
    public GameState(GameState givenGameState){
        heuristic     = givenGameState.getHeuristic();
        connect       = givenGameState.getConnect();
        currentRow    = givenGameState.getCurrentRow();
        currentColumn = givenGameState.getCurrentColumn();
        currentPlayer = givenGameState.getCurrentPlayer();
        numberOfMoves = givenGameState.getNumberOfMoves();
        height        = givenGameState.getHeight();
        width         = givenGameState.getWidth();
        //getBoard returns deep copy
        board = givenGameState.getBoard();
    }

    //getters and setters
    public int evaluateHeuristic(boolean max){
        //evaluate the board and determine heuristic
        //calculate if the board is a draw
        int countOfRowsWhereCantInsert = 0;
        for(int i = 0; i < board[0].length; i++){
            if(isColumnFull(i)){
                countOfRowsWhereCantInsert++;
            }
        }
        //draw gets 50
        if(countOfRowsWhereCantInsert == board[0].length){
            return 0;
        }
        //win for max gets positive and win for min gets negative
        if(isWinning()){
            if(max){
                return -((board[0].length*board.length+1 - numberOfMoves)/2);
            }else {
                return ((board[0].length*board.length+1 - numberOfMoves)/2);
            }
        }
        //lose gets worst possible
        if(isLosing()){
            return (-(getWidth()*getHeight()));
        }

        //this i don't know yet (needs work)
        return board[0].length*board.length;
    }

    //this method checks if the current state is loosing in the sense that next player can place a winning move
    public boolean isLosing(){
        for(int i = 0; i < board[0].length; i++){
            GameState next = new GameState(this);
            if(currentPlayer == 'y'){
                next.makeMove(i, 'x');
                if(next.isWinning()){
                    return true;
                }
            }else {
                next.makeMove(i,'y');
                if(next.isWinning()){
                    return false;
                }
            }
        }
        return false;
    }

    //some getters and setters that might be used
    public int getHeuristic(){
        return heuristic;
    }

    public void setConnect(int connect){
        this.connect = connect;
    }

    public int getConnect(){
        return connect;
    }

    public void setCurrentRow(int currentRow){
        this.currentRow = currentRow;
    }

    public int getCurrentRow(){
        return currentRow;
    }

    public void setCurrentColumn(int currentColumn){
        this.currentColumn = currentColumn;
    }

    public int getCurrentColumn(){
        return currentColumn;
    }

    public void setCurrentPlayer(char currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    public char getCurrentPlayer(){
        return currentPlayer;
    }

    public void setBoard(char[][] nboard){
        board = new char[nboard.length][nboard[0].length];
        for(int i = 0; i < nboard.length; i++){
            for(int j = 0; j < nboard[i].length; j++){
                board[i][j] = nboard[i][j];
            }
        }
    }

    public char[][] getBoard(){
        char[][] retrBoard = new char[board.length][board[0].length];
        for(int i = 0; i < retrBoard.length; i++){
            for(int j = 0; j < retrBoard[i].length; j++){
                retrBoard[i][j] = board[i][j];
            }
        }
        return retrBoard;
    }

    public int getBoardLength(){
        return board.length;
    }

    public boolean isColumnFull(int col){

        //validate that the column is valid. if not return true that the column is full
        if(col >= board[0].length || col < 0){
            return true;
        }else {
            //if the top of the row is empty spot means the column is not full
            if (board[board.length - 1][col] == '\u0000') {
                return false;
            } else {
                return true;
            }
        }
    }

    public int getNumberOfMoves(){
        return numberOfMoves;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    //method that takes the column and returns boolean based on if the move was legal
    public boolean makeMove(int column, char player){
        //first check if move is valid
        if(column >= board[0].length || column < 0){
            return false;
        }

        //make the move in the next available spot in the provided column
        for(int i = 0; i < board.length; i++){
            if(board[i][column] == 0){
                board[i][column] = player;
                setCurrentPlayer(player);
                setCurrentColumn(column);
                setCurrentRow(i);
                numberOfMoves++;
                return true;
            }
        }

        //if we reached this point the move was into the column that is full
        return false;
    }

    //this method will return true if the game has been won provided that a move has been made
    public boolean isOver(){

        //initialize variables that will be used as helper variables
        int move = 0;
        int countConnected = 0;
        boolean flag = false;

        //up right check from current position
        for(int i = getCurrentRow(); i < board.length; i++){
            for(int j = getCurrentColumn() + move; j < board[0].length;){
                move++;
                if(board[i][j] == getCurrentPlayer()){
                    countConnected++;
                    break;
                }else {
                    flag = true;
                    break;
                }
            }
            if(flag){
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //reset some variables that will be used again
        flag = false;
        move = 0;

        //down left check
        for(int i = getCurrentRow() - 1; i >= 0; i--){
            for(int j = getCurrentColumn()-1+move; j >= 0;){
                move--;
                if(board[i][j] == getCurrentPlayer()){
                   countConnected++;
                   break;
                }else {
                    flag = true;
                    break;
                }
            }
            if(flag){
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //now we will check up left and down right so we reset countConnected to zero as well
        flag = false;
        move = 0;
        countConnected = 0;

        //up left check
        for(int i = getCurrentRow(); i < board.length; i++){
            for(int j = getCurrentColumn() + move; j >= 0;){
                move--;
                if(board[i][j] == getCurrentPlayer()){
                    countConnected++;
                    break;
                }else {
                    flag = true;
                    break;
                }
            }
            if(flag){
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //reset some variables that will be used again
        flag = false;
        move = 0;

        //down right
        for(int i = getCurrentRow() - 1; i >= 0; i--){
            for(int j = getCurrentColumn()+1+move; j < board[0].length; j++){
                move++;
                if(board[i][j] == getCurrentPlayer()){
                    countConnected++;
                    break;
                }else {
                    flag = true;
                    break;
                }
            }
            if(flag){
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //now we will check right and left and so we reset countConnected to zero
        countConnected = 0;

        //right only check (horizontal)
        for(int j = getCurrentColumn(); j < board[0].length; j++){
            if(board[getCurrentRow()][j] == getCurrentPlayer()){
                countConnected++;
            }else{
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //check left
        for(int j = getCurrentColumn()-1; j >= 0; j--){
            if(board[getCurrentRow()][j] == getCurrentPlayer()){
                countConnected++;
            }else{
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //now we will check down (no need to check up) so we reset countConnected to zero
        countConnected = 0;

        //check down
        for(int i = getCurrentRow(); i >= 0; i--){
            if(board[i][getCurrentColumn()] == getCurrentPlayer()){
                countConnected++;
            }else{
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //final check is a draw
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                //if at anytime the board has an empty spot the game is not over
                if(board[i][j]=='\u0000'){
                    return false;
                }
            }
        }
        //if we have gotten to this point that means the board is full and no one won
        return true;
    }

    //method that returns if the current state is the winning state
    public boolean isWinning(){
        //initialize variables that will be used as helper variables
        int move = 0;
        int countConnected = 0;
        boolean flag = false;

        //up right check from current position
        for(int i = getCurrentRow(); i < board.length; i++){
            for(int j = getCurrentColumn() + move; j < board[0].length;){
                move++;
                if(board[i][j] == getCurrentPlayer()){
                    countConnected++;
                    break;
                }else {
                    flag = true;
                    break;
                }
            }
            if(flag){
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //reset some variables that will be used again
        flag = false;
        move = 0;

        //down left check
        for(int i = getCurrentRow() - 1; i >= 0; i--){
            for(int j = getCurrentColumn()-1+move; j >= 0;){
                move--;
                if(board[i][j] == getCurrentPlayer()){
                    countConnected++;
                    break;
                }else {
                    flag = true;
                    break;
                }
            }
            if(flag){
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //now we will check up left and down right so we reset countConnected to zero as well
        flag = false;
        move = 0;
        countConnected = 0;

        //up left check
        for(int i = getCurrentRow(); i < board.length; i++){
            for(int j = getCurrentColumn() + move; j >= 0;){
                move--;
                if(board[i][j] == getCurrentPlayer()){
                    countConnected++;
                    break;
                }else {
                    flag = true;
                    break;
                }
            }
            if(flag){
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //reset some variables that will be used again
        flag = false;
        move = 0;

        //down right
        for(int i = getCurrentRow() - 1; i >= 0; i--){
            for(int j = getCurrentColumn()+1+move; j < board[0].length; j++){
                move++;
                if(board[i][j] == getCurrentPlayer()){
                    countConnected++;
                    break;
                }else {
                    flag = true;
                    break;
                }
            }
            if(flag){
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //now we will check right and left and so we reset countConnected to zero
        countConnected = 0;

        //right only check (horizontal)
        for(int j = getCurrentColumn(); j < board[0].length; j++){
            if(board[getCurrentRow()][j] == getCurrentPlayer()){
                countConnected++;
            }else{
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //check left
        for(int j = getCurrentColumn()-1; j >= 0; j--){
            if(board[getCurrentRow()][j] == getCurrentPlayer()){
                countConnected++;
            }else{
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        //now we will check down (no need to check up) so we reset countConnected to zero
        countConnected = 0;

        //check down
        for(int i = getCurrentRow(); i >= 0; i--){
            if(board[i][getCurrentColumn()] == getCurrentPlayer()){
                countConnected++;
            }else{
                break;
            }
        }

        //check to see if the winner is known
        if(countConnected == getConnect()){
            return true;
        }

        return false;
    }

    //print state method
    public void print(){
        System.out.println("Current State");
        System.out.println("Columns:");
        //print column numbers
        for(int i = 0; i < board[0].length; i++){
            System.out.print(i + " ");
        }
        System.out.println();
        //print dasher to separate board from column numbers
        for(int i = 0; i < board[0].length; i++){
            System.out.print("= ");
        }
        System.out.println();

        //print actual content
        for(int i = board.length-1; i >= 0;  i--){
            for(int j = 0; j < board[i].length; j++){
                if(board[i][j] == '\u0000'){
                    System.out.print("- ");
                }else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}
