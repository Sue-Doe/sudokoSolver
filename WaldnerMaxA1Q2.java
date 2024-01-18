/**
 * Sudoku Solver
 *
 * COMP 2140 SECTION D01
 * ASSIGNMENT Assignment 1, question 2
* @author Max Waldner, 7889322
* @version May 25, 2023
*
* PURPOSE: To take input of a sudoku row by row and output a solution if exists 
*/
import java.util.Scanner;
class WaldnerMaxA1Q2 {


    //takes user input and calls the the solver class
    public static void main(String[] args) {
        System.out.println("Please enter a game board, row by row, with - to represent empty cells and\nspaces between each cell:");
        Scanner input = new Scanner(System.in);
        String boardString = input.nextLine();
        

        //prints board before solve
        Sudoku game = new Sudoku(boardString);
        System.out.println("\n\nThe original board is:");
        System.out.println(game);

        //attemps to solve 
        game.solve(); 

        //prints game after solver has run
        System.out.println("\n\nThe soltuion is:");
        System.out.println(game);

        

        input.close();
    }
}



/*
 * The solver method that takes a string of a sudoku board row by row determines if it can be solved and returns the output 
 * with either the solution or tells you if its unsolveable
 */
class Sudoku {
    
    private int [][] board;
    private int n; // size of a block for a n^2 * n^2 board 
    private boolean gameSolved = true;
    //constructor makes board from string
    public Sudoku(String boardString) {
        String[] gameArr = boardString.split(" ");
        int[][] boardGame;

        //determines the size of each block n for an n^2 *n^2 soduku 
        int sizeInt =  (int) Math.sqrt(gameArr.length);
        Double sizeDouble = Math.sqrt(gameArr.length);
        int n = 0;

        //determines if the size is valid
        if ( sizeInt/ sizeDouble  == 1) {
            n = sizeInt;
            boardGame = new int[n][n];
            
            int count = 0;
            for (int i = 0; i < boardGame.length; i++) {
                for(int j = 0; j < boardGame[i].length; j++) {
                    if (gameArr[count].equals("-")) {
                        boardGame[i][j] = 0;
                    } else {
                        boardGame[i][j] = Integer.parseInt(gameArr[count]);
                    }
                    count++;
                }
            }

        } else {
            n = 9;
            boardGame = new int[n][n];
        }


        // initalizes the varibles 
        this.board = boardGame;
        this.n = (int) Math.sqrt(boardGame.length);

    }

    //starts the recusrion to solve the soduko
    public void solve() {
        gameSolved = solveHelper(0, 0, 1);
        
    }

    // solver helper method that does the recurison which will return true if solved or false if cannot be
    private boolean solveHelper(int i, int j, int start) {
        
        if (i == board.length) { //base case no more boxes to check 
            return true;
            }
            
        if (j == board[i].length) { // move down the column
            return solveHelper(i + 1, 0, 1);
        }
            
        if (board[i][j] != 0) { // box is already filled in so moves to next
            return solveHelper(i, j + 1, 1);
        }

        if ( start > board.length) { // if all numbers dont work cant be solved
            return false;
        }
        if (rowColCheck(i, j, start) && boxCheck((i), j, start)) { // checks if the number is valid in current postion 
            board[i][j] = start;
            if (solveHelper(i, j + 1, 1)) {  // checks future cases with current start number 
                return true;                      //returns true if solved if not resets the box and tries a new number
            }
            board[i][j] = 0; //resets box to try again
        }
        
        return solveHelper(i, j, start + 1); // tries new number
    }


    //checks if the number already exists in the same row or col
    private boolean rowColCheck(int i, int j, int start) {
        boolean result = true;
        for (int row = 0; row < board.length; row++) {
            if (board[row][j] == start) {
                result = false;
            }

        } 
        for (int col = 0; col < board.length; col++) {
            if (board[i][col] == start) {
                result = false;
            }
        }
        return result;
    }


    //checks whether the box n already has the number in it or not 
    private boolean boxCheck(int row, int col, int num) {
        int boxIpos = row - row % n; // row starting postion of the n box
        int boxJpos = col - col % n; // col starting postion of the n box

        for (int i = boxIpos; i < boxIpos + n; i++) {
            for (int j = boxJpos; j < boxJpos + n; j++) {
                if (board[i][j] == num) {
                    return false;  // Number is in the box
                }
            }
        }
        
        return true;  // Number is not in the box
    }


    public String toString() {
        String boardString = "";

        //creates string to print board
        if (gameSolved) {

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 0) {
                        boardString += "- ";
                    } else {
                        boardString += board[i][j] + " ";
                    }
                }
                boardString += "\n";
            }
        } else {
            boardString = "This board cannot be solved.";
        }
        return boardString;
    }




}

    
/* 1)
 * I chose ints becuase i figured it would be the easier way because all i had to do was change all the - to a null character which in this case is 0
 *  and parse the actual numbers also sudokus is a game that only involves integers so formatting was alot easier so it made sense to me to use ints
 *  rather than chars
 */

/* 2)
 * I wouldnt say i rejected any appoarches before my solution becuase everything else resulted in stack overflow errors, when i was getting
 * close to figuring out the soltion i implemented recursive backtracking wrong and everytime it found a dead end i had it start back at the very beginning
 * which resulted in all sorts of error as i would have half filled 4 by 4 boards and then stackoverflow errors. but i do believe mine is the most efficent 
 * becuase it backtracks to the most recent spot that it knows is valid so it doesnt have to waste as much time getting back to where it failed and 
 * trying something else  
 */


