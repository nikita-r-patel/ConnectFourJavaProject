import java.util.Scanner;


public class GameState{
	private String[][] board;
	private int rowCount;
	private int columnCount;
	private String firstPlayer;
	private String secondPlayer;
	private String currentPlayer;
	private int moveRow;
	private int moveColumn;
	private boolean isWinnerPresent;
	private String winner;
	private boolean isGameStateFull;
	private Scanner scanner;
	
	public GameState() {
		rowCount = 7;
		columnCount = 6;
		board = new String[rowCount][columnCount];
		scanner = new Scanner(System.in);
		isWinnerPresent = false;
	}
	
	public void initializeBoard() {
		//Initializes empty game state as a 2D array. Represents spaces with . 
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < columnCount; col++) {
				board[row][col] = ".";
			}
		}
	}
	
	public void printBoard() {
		//Prints game state by printing out all elements in board which is a 2D array
		for (int r = 0; r < columnCount; r++) {
			System.out.print((r+1) + "  ");
		}
		System.out.println();
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < columnCount; col++) {
				System.out.print(board[row][col]);
				System.out.print("  ");
			}
			System.out.println();
		}
	}
	
	public void getFirstPlayerInput() {
		//Gets user input for first player.  Throws exceptions for incorrect input.
		while(true) {
			try {
				System.out.println();
				System.out.print("Enter Player 1 Color (R = red, B = black): ");
				firstPlayer = scanner.next();
				if (firstPlayer.equals("B") || firstPlayer.equals("b")) {
					firstPlayer = firstPlayer.toUpperCase();
					secondPlayer = "R";
					currentPlayer = firstPlayer;
					break;
				}
				else if (firstPlayer.equals("R") || firstPlayer.equals("r")){
					firstPlayer = firstPlayer.toUpperCase();
					secondPlayer = "B";
					currentPlayer = firstPlayer;
					break;
				}
				else {throw new firstPlayerInputException();}
			}
			catch (firstPlayerInputException e) {
				System.out.println("ERROR: Enter R for Red or B for Black");
			}
		}
	}
	
	
	public void setUpGameState() {
		//Sets up game by initializing board and getting first player input
		System.out.println("Connect Four: 2 Player Game");
		initializeBoard();
		getFirstPlayerInput();
	}
	
	public void play() {
		//Runs Connect Four game
		setUpGameState();
		printBoard();
		while (isWinnerPresent == false) {
			System.out.println();
			printTurn();
			makeMove();
			printBoard();
			checkWinner();
			checkFullState();
			if (isGameStateFull == true) {break;}
			if (isWinnerPresent == false) {switchCurrentPlayer();}
		}
		displayWinner();
	}
	
	public int getMove() {
		//Gets user input for move
		int moveColumn;
		System.out.print("Enter Column: ");
		moveColumn = scanner.nextInt();
		return moveColumn;
	}
	
	public void switchCurrentPlayer() {
		//Changes curentPlayer attribute after every turn
		if (currentPlayer.equals("B")) {currentPlayer = "R";}
		else if (currentPlayer.equals("R")){currentPlayer = "B";}
	}
	
	public void printTurn() {
		//Prints current player's turn
		if (currentPlayer.equals("B")) {
			System.out.println("Black Player's Turn!");
		}
		else {System.out.println("Red Player's Turn!");}
	}
	
	public void makeMove(){
		//Attempts to make move with given move input.  Throws exceptions for incorrect input
		boolean madeMove = false;
		int moveCol;
		while (madeMove == false) {
			try {
				moveCol = getMove() - 1;
				for (int row = rowCount-1; row > -1; row--) {
					if (board[row][moveCol].equals(".")) {
						board[row][moveCol] = currentPlayer;
						madeMove = true;
						moveColumn = moveCol;
						moveRow = row;
						break;
					}
				}
				if (madeMove == false) {
					throw new invalidMoveException();
				}
			}
			catch (invalidMoveException e) {
				System.out.println("ERROR: Invalid Move!");
				scanner.nextLine();
				continue;
			}
			catch (Exception e) {
				System.out.println("ERROR: Invalid Move!");
				scanner.nextLine();
				continue;
			}
		}
	}
	
	public void checkRow() {
		//Checks an entire row for winner based on currentPlayer at every turn
		int counter = 0;
		for (int col = 0; col < columnCount; col++) {
			if (board[moveRow][col].equals(currentPlayer)) {
				counter++;
				if (counter >= 4) {
					isWinnerPresent = true;
					break;
				}
			}
			else {
				counter = 0;
			}
		}
	}
	
	public void checkColumn() {
		//Checks an entire column for winner based on currentPlayer at every turn.
		int counter = 0;
		for (int row = 0; row < rowCount; row++) {
			if (board[row][moveColumn].equals(currentPlayer)) {
				counter++;
				if (counter >= 4) {
					isWinnerPresent = true;
					break;
				}
			}
			else {
				counter = 0;
			}
		}
	}
	
	public void checkLeftDiagonal() {
		//Checks entire left diagonal for winner based on currentPlayer at every turn
		int copyMoveCol = moveColumn;
		int copyMoveRow = moveRow;
		int counter = 0;
		while (copyMoveCol != 0 && copyMoveRow != 0) {
			copyMoveCol--;
			copyMoveRow--;
		}
		while(copyMoveRow < rowCount && copyMoveCol < columnCount) {
			if (board[copyMoveRow][copyMoveCol].equals(currentPlayer)) {
				counter++;
				if (counter >= 4) {
				isWinnerPresent = true;
					break;
				}
			}
			else {
				counter = 0;
			}
			copyMoveRow++;
			copyMoveCol++;
		}
	}
	
	public void checkRightDiagonal(){
		//Checks right diagonal for winner based on currentPlayer at every turn
		int copyMoveCol = moveColumn;
		int copyMoveRow = moveRow;
		int counter = 0;
		while (copyMoveRow != rowCount-1 && copyMoveCol != 0) {
			copyMoveCol--;
			copyMoveRow++;
		}
		while (copyMoveCol != columnCount - 1 && copyMoveRow != 0) {
			if (board[copyMoveRow][copyMoveCol].equals(currentPlayer)) {
				counter++;
				if (counter >= 4) {
					isWinnerPresent = true;
					break;
				}
			}
			else {
				counter = 0;
			}
			copyMoveRow--;
			copyMoveCol++;
		}
	}
	
	
	public void checkWinner() {
		//Checks for winner and updates winner + winnerPresent attributes by using all 4 checks
		checkRow();
		checkColumn();
		checkLeftDiagonal();
		checkRightDiagonal();
	}
	
	public void displayWinner() {
		//Displays winner or whether no winner was found
		System.out.println();
		if (isWinnerPresent == true) {
			if (currentPlayer.equals("R")) {System.out.println("---- Red Player Wins!!! ---");}
			else {System.out.println("--- Black Player Wins!!! ---");}
		}
		else if (isGameStateFull == true) {
			System.out.println("--- GAME OVER: No Winner Present! ---");
		}
	}
	
	public void checkFullState() {
		//Checks for full game state and updates isGameStateFull attribute
		isGameStateFull = true;
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < columnCount; col++) {
				if (board[row][col].equals(".")) {
					isGameStateFull = false;
				}
			}
		}
	}

	//GET METHODS FOR EACH ATTRIBUTE
	
	public String[][] getBoard(){
		return board;
	}
	
	public int getRowCount() {
		return rowCount;
	}
	
	public int getColumnCount() {
		return columnCount;
	}
	
	public String getFirstPlayer() {
		return firstPlayer;
	}
	
	public String getSecondPlayer() {
		return secondPlayer;
	}
	
	public String getCurrentPlayer() {
		return currentPlayer;
	}
	
	public int getMoveRow() {
		return moveRow;
	}
	
	public int getMoveCol() {
		return moveColumn;
	}
	
	public boolean getIsWinnerPresent() {
		return isWinnerPresent;
	}
	
	public String getWinner() {
		return winner;
	}
	
	public boolean getIsGameStateFull() {
		return isGameStateFull;
	}
};