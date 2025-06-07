import javax.swing.*;
import java.awt.*; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 

// implements ActionListener to handle button click events
public class ConnectFour extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[6][7]; 
    private int[][] board = new int[6][7]; 
    private boolean playerOneTurn = true; // Boolean to track the current player's turn

    
    public ConnectFour() {
        setTitle("Connect Four"); 
        setSize(700, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLayout(new GridLayout(6, 7)); 

        // Initialize the buttons and add them to the frame
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                buttons[row][col] = new JButton(""); 
                buttons[row][col].setActionCommand(row + "," + col); // Set the action command to the button's position
                buttons[row][col].addActionListener(this); // Add an action listener to handle button clicks
                add(buttons[row][col]); // Add the button to the frame
            }
        }

        setVisible(true); 
    }

    // Method to handle button click events
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand(); //  button's position
        int col = Integer.parseInt(command.split(",")[1]); // Extract the column 

        // Player move
        if (playerMove(col)) { // If the player's move is valid
            if (checkWin(1)) { 
                updateBoard(); 
                JOptionPane.showMessageDialog(this, "Player One wins!"); 
                resetBoard(); 
                return;
            } else if (checkDraw()) { 
                updateBoard(); 
                JOptionPane.showMessageDialog(this, "It's a draw!"); 
                resetBoard(); 
                return;
            } else {
                // Computer move
                int bestMove = getBestMove(); // Get the best move for the computer
                computerMove(bestMove); // Make the computer's move
                if (checkWin(2)) { 
                    updateBoard(); 
                    JOptionPane.showMessageDialog(this, "Computer wins!"); 
                    resetBoard(); 
                } else if (checkDraw()) { 
                    updateBoard(); 
                    JOptionPane.showMessageDialog(this, "It's a draw!"); 
                    resetBoard(); // Reset the board for a new game
                }
            }
        }
    }

    // Method to handle the player's move
    private boolean playerMove(int col) {
        for (int row = 5; row >= 0; row--) { // Iterate from the bottom row to the top
            if (board[row][col] == 0) { // Find the first empty cell in the column
                board[row][col] = 1; // Set the cell to 1 //player one//
                playerOneTurn = !playerOneTurn; // التبديل الى دور الكمبيوتر  Toggle player's turn
                updateBoard(); 
                return true; 
            }
        }
        return false; // Move is invalid //column is full//
    }

    // Method to handle the computer's move
    private void computerMove(int col) {
        for (int row = 5; row >= 0; row--) { 
            if (board[row][col] == 0) { 
                board[row][col] = 2; // Set the cell to 2 (computer)
                playerOneTurn = !playerOneTurn; // التبديل الى دور اللاعب     Toggle the player's turn
                updateBoard(); 
                return; 
            }
        }
    }

    // Method to get the best move for the computer using //the minimax algorithm//
    private int getBestMove() {
        int bestScore = Integer.MIN_VALUE; // Initialize the best score to the lowest possible value
        int bestMove = 0; 

        for (int col = 0; col < 7; col++) { 
            for (int row = 5; row >= 0; row--) { 
                if (board[row][col] == 0) { 
                    board[row][col] = 2; 
                    
                    int score = minimax(board, 5, false); // Evaluate the move using the minimax algorithm
                    board[row][col] = 0; // Undo the move
                    if (score > bestScore) { // If the score is better than the best score
                        bestScore = score; // Update the best score
                        bestMove = col; // Update the best move
                    }
                    break; // Exit the loop after evaluating one cell in the column
                }
            }
        }

        return bestMove; // Return the best move
    }

    // Minimax algorithm to evaluate the game state
    private int minimax(int[][] board, int depth, boolean isMaximizing) {
        int result = evaluateBoard(); // Evaluate the current board state
        if (result != 0 || depth == 0) { // If the game is over or the maximum depth is reached
            return result; 
        }

        if (isMaximizing) { // If it's the computer's turn (maximizing player)
            int bestScore = Integer.MIN_VALUE; // Initialize the best score to the lowest possible value
            for (int col = 0; col < 7; col++) { 
                for (int row = 5; row >= 0; row--) { 
                    if (board[row][col] == 0) { 
                        board[row][col] = 2; 
                        int score = minimax(board, depth - 1, false); // Recursively call minimax for the player's turn
                        board[row][col] = 0; // Undo the move
                        bestScore = Math.max(score, bestScore); // Update the best score
                        break; 
                    }
                }
            }
            return bestScore; 
        } else { // If it's the player's turn (minimizing player)
            int worstScore = Integer.MAX_VALUE; // Initialize the worst score to the highest possible value
            for (int col = 0; col < 7; col++) { 
                for (int row = 5; row >= 0; row--) { 
                    if (board[row][col] == 0) { 
                        board[row][col] = 1; 
                        int score = minimax(board, depth - 1, true); // Recursively call minimax for the computer's turn
                        board[row][col] = 0; // Undo the move
                        worstScore = Math.min(score, worstScore); // Update the worst score
                        break; 
                    }
                }
            }
            return worstScore;
        }
    }

    // Method to evaluate the current board state
    private int evaluateBoard() {
        if (checkWin(1)) { 
            return -1000; 
        } else if (checkWin(2)) { 
            return 1000; 
        }
        return 0; //game over
    }

    // Method to check if a player has won
    private boolean checkWin(int player) {
        // Check for a win (horizontally, vertically, and diagonally)
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (board[row][col] == player) {
                    // Check horizontally
                    if (col + 3 < 7 &&
                        board[row][col + 1] == player &&
                        board[row][col + 2] == player &&
                        board[row][col + 3] == player) {
                        return true;
                    }
                    // Check vertically
                    if (row + 3 < 6 &&
                        board[row + 1][col] == player &&
                        board[row + 2][col] == player &&
                        board[row + 3][col] == player) {
                        return true;
                    }
                    // Check diagonally (down-right)
                    if (row + 3 < 6 && col + 3 < 7 &&
                        board[row + 1][col + 1] == player &&
                        board[row + 2][col + 2] == player &&
                        board[row + 3][col + 3] == player) {
                        return true;
                    }
                    // Check diagonally (down-left)
                    if (row + 3 < 6 && col - 3 >= 0 &&
                        board[row + 1][col - 1] == player &&
                        board[row + 2][col - 2] == player &&
                        board[row + 3][col - 3] == player) {
                        return true;
                    }
                }
            }
        }
        return false; // No win found
    }

    // Method to check if the game is a draw
    private boolean checkDraw() {
        for (int col = 0; col < 7; col++) { 
            if (board[0][col] == 0) { // If there's an empty cell in the top row
                return false; //not draw
            }
        }
        return true; // The game is a draw
    }

    // Method to update the board state visually gui
    private void updateBoard() {
        for (int row = 0; row < 6; row++) { 
            for (int col = 0; col < 7; col++) { 
                if (board[row][col] == 1) { // If the cell is occupied by the player
                    buttons[row][col].setText("X"); 
                    buttons[row][col].setEnabled(false); 
                } else if (board[row][col] == 2) { // If the cell is occupied by the computer
                    buttons[row][col].setText("AI"); 
                    buttons[row][col].setEnabled(false); 
                } else { // If the cell is empty
                    buttons[row][col].setText(""); 
                    buttons[row][col].setEnabled(true); 
                }
            }
        }
    }

    // Method to reset the board for a new game
    private void resetBoard() {
        for (int row = 0; row < 6; row++) { 
            for (int col = 0; col < 7; col++) { 
                board[row][col] = 0; // Set the cell to empty
                buttons[row][col].setText(""); // Clear the button text
                buttons[row][col].setEnabled(true); // Enable the button
            }
        }
        playerOneTurn = true; // Set the turn to player one
    }

    // Main method to create and display the game window
    public static void main(String[] args) {
        new ConnectFour(); 
    }
}
