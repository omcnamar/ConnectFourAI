import java.util.ArrayList;

public class TreeNode {
    private GameState gameState;
    private ArrayList<TreeNode> children;
    public int bestMoveColumn;

    //constructor
    public TreeNode(GameState inGameState){
        gameState = new GameState(inGameState);
        children = new ArrayList<>();
    }

    //this method generates all possible states starting from the root till depth or ending state
    public int minMax(int col, int depth, boolean max){

        int heuristic = 0;
        bestMoveColumn = gameState.getWidth()/2;
        //if the calling state is not an ending state make a move on that state
        if(!gameState.isOver() && depth > 0) {
            //create a GameState that is same as the calling objects game state(deep copy)
            GameState gameStateChild = new GameState(gameState);
            //if the column is full move it to the next spot
            while(gameStateChild.isColumnFull(col)){
                col++;
            }
            //make a move on the game state with the next players move
            if (gameStateChild.getCurrentPlayer() == 'x') {
                gameStateChild.makeMove(col, 'y');
            } else {
                gameStateChild.makeMove(col, 'x');
            }

            //Create a new TreeNode and add it as a child to the calling Tree node
            TreeNode newChild = new TreeNode(gameStateChild);
            //newChild.printState();
            addChild(newChild);

            //switch between min and max
            if(max){
                max = false;
            }else{
                max = true;
            }

            //make a recursive call
            heuristic = newChild.minMax(col, depth-1, max);

            col++;
            //this while loop will make the rest of the children and work with heuristic
            while(col < gameState.getBoard()[0].length){
                GameState newCh = new GameState(gameState);
                boolean success;

                //make a move on the game state with the next players move
                if (newCh.getCurrentPlayer() == 'x') {
                    success = newCh.makeMove(col, 'y');
                } else {
                    success = newCh.makeMove(col, 'x');
                }
                //if move was success we will compare the previous child to the new one and keep the best heuristic
                if(success) {
                    TreeNode ch = new TreeNode(newCh);
                    addChild(ch);
                    int heu2 = ch.minMax(0, depth - 1, max);
                    //because of the recursive design !max means max is selecting else is where min is selecting
                    if (!max) {
                        if (heuristic < heu2) {
                            heuristic = heu2;
                            //update the column
                            bestMoveColumn = col;
                        }
                    } else {
                        if (heuristic > heu2) {
                            heuristic = heu2;
                            //update the column
                            bestMoveColumn = col;
                        }
                    }
                }
                col++;
            }

        }else{
            //the deepest TreeNode will return its heuristic to the parent for later comparison to the next child
            return gameState.evaluateHeuristic(max);
        }
        //return back up the tree
        return heuristic;
    }

    //method that will return the children of the Current node
    public ArrayList<TreeNode> getChildren(){
        return children;
    }

    //method to add a child to the Three
    public void addChild(TreeNode treeNode){
        children.add(treeNode);
    }

    //method that will print all of the next states based on the current state
    public void printAllChildren(){
        for(int i = 0 ; i < children.size(); i++){
            children.get(i).printState();
        }
    }

    //method that prints current state
    public void printState(){
        gameState.print();
    }
}
