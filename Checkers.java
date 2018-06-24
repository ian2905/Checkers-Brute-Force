//TODO: 1. Make it so every move is done i.e. leftOrRight is a varible for each piece, 3. make the ai that the computer
//...plays against look forward 2 moves, 4. fix issue with not being able to play if a no set of fours moves is posible by taking the current total score as that score
//...when any move fails, 6. Add in a check that will subtract points if a piece is taken off of the back row, this works both
//...ways as if com2 takes one of their pieces off then it is to the benefit of comMain, 7. Win conditions


//note to self: if a piece is kinged that ends that pieces turn and it cannot double jump backwards in the same turn
// points: jump piece = 5, go into side = 1, kinged = 9, lose piece = -5, go out of side = -.5

import java.util.*;

public class Checkers {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		char [][] board = {{'_', 'C', '_', 'C', '_', 'C', '_', 'C'},
						   {'C', '_', 'C', '_', 'C', '_', 'C', '_'},
						   {'_', 'C', '_', 'C', '_', 'C', '_', 'C'},
						   {'_', '_', '_', '_', '_', '_', '_', '_'},
						   {'_', '_', '_', '_', '_', '_', '_', '_'},
						   {'P', '_', 'P', '_', 'P', '_', 'P', '_'},
						   {'_', 'P', '_', 'P', '_', 'P', '_', 'P'},
						   {'P', '_', 'P', '_', 'P', '_', 'P', '_'},
						   };
		int [][] comPieces = {{0, 0, 0, 1, 0}, //taken?(0=no), kinged?(0=no), row, column, cpu or player(0 for cpu)
				 			  {0, 0, 0, 3, 0},
				 			  {0, 0, 0, 5, 0},
				 			  {0, 0, 0, 7, 0},
				 			  {0, 0, 1, 0, 0},
				 			  {0, 0, 1, 2, 0},
				 			  {0, 0, 1, 4, 0},
				 			  {0, 0, 1, 6, 0},
				 			  {0, 0, 2, 1, 0},
				 			  {0, 0, 2, 3, 0},
				 			  {0, 0, 2, 5, 0},
				 			  {0, 0, 2, 7, 0},
				 			  };
		int [][] playerPieces = {{0, 0, 5, 0, 1}, //taken?(0=no), kinged?(0=no), row, column, cpu or player(0 for cpu)
				  				 {0, 0, 5, 2, 1},
				  				 {0, 0, 5, 4, 1},
				  				 {0, 0, 5, 6, 1},
				  				 {0, 0, 6, 1, 1},
				  				 {0, 0, 6, 3, 1},
				  				 {0, 0, 6, 5, 1},
				  				 {0, 0, 6, 7, 1},
				  				 {0, 0, 7, 0, 1},
				  				 {0, 0, 7, 2, 1},
				  				 {0, 0, 7, 4, 1},
				  				 {0, 0, 7, 6, 1},
				  				 };
		String [] playerMove = new String[2];
		String [] comMove = new String[2];
		
		System.out.println("Welcome to Checkers!");
		System.out.print("Would you like to go first or second(f or s)?: ");
		String firstOrSecond = s.nextLine().toLowerCase();
		if(firstOrSecond.charAt(0) == 'f') {
			displayBoard(board);
			while(true) {
				playerMove = getPlayerMove(playerPieces, board);
				movePlayer(playerMove, playerPieces, comPieces, board);
				displayBoard(board);
				System.out.println("Calculating computer's move...");
				comMove = calcComputerMove(playerPieces, comPieces, board);
				moveComputer(comMove[1], comPieces[Integer.parseInt(comMove[0])], playerPieces, board);
				displayBoard(board);
			}
		}
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static String [] calcComputerMove(int [][] playerPieces, int [][] comPieces, char [][] board) { //TODO may need a function for when the first piece is kinged for which direction to go
		Random rand = new Random();
		
		int firstPiece = 0;
		int leftOrRight = 0;
		int isFirstPiece;
		int secondPiece = 0;
		int thirdPiece = 0;
		int forthPiece = 0;
		double points = 0;
		double secondPoints = 0;
		double thirdPoints = 0;
		double forthPoints = 0;
		double highscore = 0;
		int pieceToMove = 0;
		int directionToMove = 0;
		
		char [][] displayBoard = new char[8][8];
		int [][] fakePlayerPieces = new int[12][5];
		int [][] fakeComPieces = new int[12][5];
		String [] move = new String[2];
		
		int [][] firstComPieces = new int [8][8];
		int [][] secondComPieces = new int [8][8];
		int [][] thirdComPieces = new int [8][8];
		int [][] forthComPieces = new int [8][8];
		
		int [][] firstPlayerPieces = new int [8][8];
		int [][] secondPlayerPieces = new int [8][8];
		int [][] thirdPlayerPieces = new int [8][8];
		int [][] forthPlayerPieces = new int [8][8];
		
		char [][] firstBoard = new char[8][8];
		char [][] secondBoard = new char[8][8];
		char [][] thirdBoard = new char[8][8];
		char [][] forthBoard = new char[8][8];
		
		for(int i = 0; i < board.length; i++) { // copy board to display board
			for(int j = 0; j < board[i].length; j++) {
				displayBoard[i][j] = board[i][j];
			}
		}
		for(int i = 0; i < playerPieces.length; i++) { // copy playerPieces to fakePlayerPieces
			for(int j = 0; j < playerPieces[i].length; j++) {
				fakePlayerPieces[i][j] = playerPieces[i][j];
			}
		}
		for(int i = 0; i < comPieces.length; i++) { // copy comPieces to fakeComPieces
			for(int j = 0; j < comPieces[i].length; j++) {
				fakeComPieces[i][j] = comPieces[i][j];
			}
		}
		
		while(firstPiece < 12) { // this way of doing it is way faster as it only moves the pieces that need to change and holds the values of the piece that arent moving.
			firstBoard = copyBoard(displayBoard);
			firstPlayerPieces = copyPieces(fakePlayerPieces);
			firstComPieces = copyPieces(fakeComPieces);
			isFirstPiece = 1;
			points = moveComputerMainPiece(firstPiece, leftOrRight, isFirstPiece, firstComPieces[firstPiece], firstPlayerPieces, firstBoard);
			if(points != -999) {
				points += calcComputer2Move(firstPlayerPieces, firstComPieces, firstBoard); // += because it returns -highscore
			}
			while(secondPiece < 12 && points > -100) {
				secondBoard = copyBoard(firstBoard);
				secondPlayerPieces = copyPieces(firstPlayerPieces);
				secondComPieces = copyPieces(firstComPieces);
				isFirstPiece = 0;
				secondPoints = moveComputerMainPiece(secondPiece, leftOrRight, isFirstPiece, secondComPieces[secondPiece], secondPlayerPieces, secondBoard);
				if(secondPoints != -999) {
					secondPoints += calcComputer2Move(secondPlayerPieces, secondComPieces, secondBoard);
					points += secondPoints;
				}
				while(thirdPiece < 12 && secondPoints > -100) {
					thirdBoard = copyBoard(secondBoard);
					thirdPlayerPieces = copyPieces(secondPlayerPieces);
					thirdComPieces = copyPieces(secondComPieces);
					thirdPoints = moveComputerMainPiece(thirdPiece, leftOrRight, isFirstPiece, thirdComPieces[thirdPiece], thirdPlayerPieces, thirdBoard);
					if(thirdPoints != -999) {
						thirdPoints += calcComputer2Move(thirdPlayerPieces, thirdComPieces, thirdBoard);
						points += thirdPoints;
					}
					while(forthPiece < 12 && thirdPoints > -100) {
						forthBoard = copyBoard(thirdBoard);
						forthPlayerPieces = copyPieces(thirdPlayerPieces);
						forthComPieces = copyPieces(thirdComPieces);
						forthPoints = moveComputerMainPiece(forthPiece, leftOrRight, isFirstPiece, forthComPieces[forthPiece], forthPlayerPieces, forthBoard);
						if(forthPoints != -999) {
							forthPoints += calcComputer2Move(forthPlayerPieces, forthComPieces, forthBoard);
							points += forthPoints;
						}
						if(points > highscore) {
							highscore = points;
							pieceToMove = firstPiece;
							directionToMove = leftOrRight;
						}
						else if(points == highscore) {
							if(rand.nextInt(10) < 5) {
								highscore = points;
								pieceToMove = firstPiece;
								directionToMove = leftOrRight;
							}
						}
						if(forthPoints != -999) {
							forthBoard = copyBoard(thirdBoard);
							forthPlayerPieces = copyPieces(thirdPlayerPieces);
							forthComPieces[forthPiece] = copyPiece(thirdComPieces, forthPiece);
						}
						forthPiece++;
						if(forthPoints != -999) {
							points -= forthPoints;
						}
						forthPoints = 0;
					}
					if(thirdPoints != -999) {
						thirdBoard = copyBoard(secondBoard);
						thirdPlayerPieces = copyPieces(secondPlayerPieces);
						thirdComPieces[thirdPiece] = copyPiece(secondComPieces, thirdPiece);
					}
					forthPiece = 0;
					thirdPiece++;
					if(thirdPoints != -999) {
						points -= thirdPoints;
					}
					thirdPoints = 0;
				}
				if(secondPoints != -999) {
					secondBoard = copyBoard(firstBoard);
					secondPlayerPieces = copyPieces(firstPlayerPieces);
					secondComPieces[secondPiece] = copyPiece(firstComPieces, secondPiece);
				}
				thirdPiece = 0;
				secondPiece++;
				if(secondPoints != -999) {
					points -= secondPoints;
				}
				secondPoints = 0;
			}
			if(points != -999) {
				firstBoard = copyBoard(displayBoard);
				firstPlayerPieces = copyPieces(fakePlayerPieces);
				firstComPieces[firstPiece] = copyPiece(fakeComPieces, firstPiece);
			}
			secondPiece = 0;
			leftOrRight++;
			if(leftOrRight > 1) {
				leftOrRight = 0;
				firstPiece++;
			}
			points = 0;
		}
		move[0] = Integer.toString(pieceToMove);
		if(directionToMove == 0) {
			move[1] = "LeftDown";
		}
		else {
			move[1] = "RightDown";
		}
		return move;
	}
	
	public static double calcComputer2Move(int [][] fakeCom2Pieces, int [][] fakeComPieces, char[][] displayBoard) {
		int firstPiece = 0;
		int leftOrRight = 0;
		double com2Points = 0;
		double highscore = 0;
		
		
		while(firstPiece < 12) {
			com2Points = moveCom2Piece(leftOrRight, fakeCom2Pieces[firstPiece], fakeComPieces, displayBoard);
			if(com2Points != -999) {
				if(com2Points > highscore) {
					highscore = com2Points;
				}
			}
			com2Points = 0;
			leftOrRight++;
			if(leftOrRight > 1) {
				leftOrRight = 0;
				firstPiece++;
			}
		}
		return -highscore;
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static double moveCom2Piece(int leftOrRight, int [] fakeCom2Piece, int [][] fakeComPieces, char [][] displayBoard) {
		Random rand = new Random();
		
		String move;
		double points = 0;
		
		boolean canMoveLeftDown = false;
		boolean canMoveRightDown = false;
		boolean canMoveLeftUp = canMoveLeftUp(fakeCom2Piece, displayBoard);
		boolean canMoveRightUp = canMoveRightUp(fakeCom2Piece, displayBoard);
		if(fakeCom2Piece[1] == 1) {
			canMoveLeftDown = canMoveLeftDown(fakeCom2Piece, displayBoard);
			canMoveRightDown = canMoveRightDown(fakeCom2Piece, displayBoard);
		}
		
		if(fakeCom2Piece[1] == 0) {
			if(leftOrRight == 0) {
				if(canMoveLeftUp) {
					move = "LeftUp";
					points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
					return points;
				}
				else {
					return -999;
				}
			}
			else if(leftOrRight == 1) {
				if(canMoveRightUp) {
					move = "RightUp";
					points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
					return points;
				}
				else {
					return -999;
				}
			}
		}
		else if(fakeCom2Piece[1] == 1) {
			if(canMoveLeftDown && !(canMoveRightDown) && !(canMoveLeftUp) && !(canMoveRightUp)) {
				move = "LeftDown";
				points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
				return points;
			}
			else if(canMoveRightDown && !(canMoveLeftDown) && !(canMoveLeftUp) && !(canMoveRightUp)) {
				move = "RightDown";
				points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
				return points;
			}
			else if(canMoveLeftUp && !(canMoveLeftDown) && !(canMoveRightDown) && !(canMoveRightUp)) {
				move = "LeftUp";
				points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
				return points;
			}
			else if(canMoveRightUp && !(canMoveLeftDown) && !(canMoveRightDown) && !(canMoveLeftUp)) {
				move = "RightUp";
				points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
				return points;
			}
			else {
				if(leftOrRight == 0) {
					if(canMoveLeftDown && canMoveLeftUp) {
						if(rand.nextInt(10) < 5) { // 50% chance to move left
							move = "LeftDown";
							points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
							return points;
						}
						else {
							move = "LeftUp";
							points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
							return points;
						}
					}
					else if(canMoveLeftDown){
						move = "LeftDown";
						points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
						return points;
					}
					else if(canMoveLeftUp){
						move = "LeftUp";
						points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
						return points;
					}
					else {
						return -999;
					}
				}
				else if(leftOrRight == 1) {
					if(canMoveRightDown && canMoveRightUp) {
						if(rand.nextInt(10) < 5) { // 50% chance to move left
							move = "RightDown";
							points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
							return points;
						}
						else {
							move = "RightUp";
							points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
							return points;
						}
					}
					else if(canMoveRightDown){
						move = "RightDown";
						points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
						return points;
					}
					else if(canMoveRightUp){
						move = "RightUp";
						points += moveComputer2(move, fakeCom2Piece, points, fakeComPieces, displayBoard);
						return points;
					}
					else {
						return -999;
					}
				}
			}
		}
		return points;
	}

	public static double moveComputerMainPiece(int piece, int leftOrRight, int isFirstPiece, int [] fakeComPiece, int [][] playerPieces, char [][] displayBoard) {
		Random rand = new Random();
		
		int randomResult;
		double points = 0;
		String move;
		boolean canMoveLeftDown = canMoveLeftDown(fakeComPiece, displayBoard);
		boolean canMoveRightDown = canMoveRightDown(fakeComPiece, displayBoard);
		boolean canMoveLeftUp = false;
		boolean canMoveRightUp = false;
		if(fakeComPiece[1] == 1) {
			canMoveLeftUp = canMoveLeftUp(fakeComPiece, displayBoard);
			canMoveRightUp = canMoveRightUp(fakeComPiece, displayBoard);
		}
		
		if(fakeComPiece[1] == 0) { // if the piece is not kinged
			if(isFirstPiece == 0) { // if it is not the first piece to move
				if(canMoveRightDown && canMoveLeftDown) {// if it can move both ways
					if(rand.nextInt(10) < 5) { // 50% chance to move left
						move = "LeftDown";
						points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
						return points;
					}
					else {
						move = "RightDown";
						points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
						return points;
					}
				}
				else if(canMoveRightDown) {
					move = "RightDown";
					points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
					return points;
				}
				else if(canMoveLeftDown) {
					move = "LeftDown";
					points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
					return points;
				}
				else {
					return -999; // can't move
				}
			}
			else if(isFirstPiece == 1) { // if it is the first piece
				if(leftOrRight == 0) {
					if(canMoveLeftDown) {
						move = "LeftDown";
						points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
						return points;
					}
					else {
						return -999;
					}
				}
				else if(leftOrRight == 1) {
					if(canMoveRightDown) {
						move = "RightDown";
						points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
						return points;
					}
					else {
						return -999;
					}
				}
			}
		}
		else if(fakeComPiece[1] == 1) { // kinged piece
			if(isFirstPiece == 0) { // if it is not the first piece to move
				if(canMoveLeftDown && !(canMoveRightDown) && !(canMoveLeftUp) && !(canMoveRightUp)) {
					move = "LeftDown";
					points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
					return points;
				}
				else if(canMoveRightDown && !(canMoveLeftDown) && !(canMoveLeftUp) && !(canMoveRightUp)) {
					move = "RightDown";
					points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
					return points;
				}
				else if(canMoveLeftUp && !(canMoveLeftDown) && !(canMoveRightDown) && !(canMoveRightUp)) {
					move = "LeftUp";
					points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
					return points;
				}
				else if(canMoveRightUp && !(canMoveLeftDown) && !(canMoveLeftUp) && !(canMoveRightDown)) {
					move = "RightUp";
					points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
					return points;
				}
				else {// if it can move multiple ways
					randomResult = rand.nextInt(40);
					if(randomResult < 10) { // be careful with these less thans because they are inpercise and include all values below like < 20 is a 50% chance
						if(canMoveLeftDown) {
							move = "LeftDown";
							points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
							return points;
						}
						else {
							return -999;
						}
					}
					else if(randomResult < 20) {
						if(canMoveRightDown) {
							move = "RightDown";
							points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
							return points;
						}
						else {
							return -999;
						}
					}
					else if(randomResult < 30) {
						if(canMoveLeftUp) {
							move = "LeftUp";
							points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
							return points;
						}
						else {
							return -999;
						}
					}
					else if(randomResult < 40){
						if(canMoveRightUp) {
							move = "RightUp";
							points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
							return points;
						}
						else {
							return -999;
						}
					}
				}
			}
			else if(isFirstPiece == 1) { // if it is the first piece
				if(canMoveLeftDown && !(canMoveRightDown) && !(canMoveLeftUp) && !(canMoveRightUp)) {
					move = "LeftDown";
					points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
					return points;
				}
				else if(canMoveRightDown && !(canMoveLeftDown) && !(canMoveLeftUp) && !(canMoveRightUp)) {
					move = "RightDown";
					points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
					return points;
				}
				else if(canMoveLeftUp && !(canMoveLeftDown) && !(canMoveRightDown) && !(canMoveRightUp)) {
					move = "LeftUp";
					points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
					return points;
				}
				else if(canMoveRightUp && !(canMoveLeftDown) && !(canMoveRightDown) && !(canMoveLeftUp)) {
					move = "RightUp";
					points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
					return points;
				}
				else {
					if(leftOrRight == 0) {
						if(canMoveLeftDown && canMoveLeftUp) {
							if(rand.nextInt(10) < 5) { // 50% chance to move left
								move = "LeftDown";
								points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
								return points;
							}
							else {
								move = "LeftUp";
								points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
								return points;
							}
						}
						else if(canMoveLeftDown){
							move = "LeftDown";
							points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
							return points;
						}
						else if(canMoveLeftUp){
							move = "LeftUp";
							points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
							return points;
						}
						else {
							return -999;
						}
					}
					else if(leftOrRight == 1) {
						if(canMoveRightDown && canMoveRightUp) {
							if(rand.nextInt(10) < 5) { // 50% chance to move left
								move = "RightDown";
								points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
								return points;
							}
							else {
								move = "RightUp";
								points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
								return points;
							}
						}
						else if(canMoveRightDown){
							move = "RightDown";
							points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
							return points;
						}
						else if(canMoveRightUp){
							move = "RightUp";
							points += moveComputer(move, fakeComPiece, playerPieces, displayBoard);
							return points;
						}
						else {
							return -999;
						}
					}
				}
			}
		}
		return points;
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static void displayBoard(char [][] board) {
		System.out.println();
		System.out.println("   0 1 2 3 4 5 6 7");
		for(int i = 0; i < board.length; i++) {
			System.out.print(i + " ");
			for(int j = 0; j < board[i].length; j++) {
				System.out.print('|');
				System.out.print(board[i][j]);
			}
			System.out.print('|');
			System.out.println();
		}
		System.out.println();
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static String [] getPlayerMove(int [][] playerPieces, char [][] board) { // TODO: input validation and kinged move check
		Scanner s = new Scanner(System.in);
		boolean canMoveLeft;
		boolean canMoveRight;
		boolean canMoveLeftKinged;
		boolean canMoveRightKinged;
		int piece = 999;
		String move;
		String kingedMove;
		String [] returnedMove = new String[2];
		
		while(true) {
			while(true) {
				System.out.print("Enter location of the piece you wish to move (i.e. 23 for second row third column): "); // TODO input validation
				String location = s.nextLine();
				if(Character.isDigit(location.charAt(0)) && Character.isDigit(location.charAt(1))) {
					for(int i = 0; i < playerPieces.length; i++) {
						if(playerPieces[i][2] == Character.getNumericValue(location.charAt(0)) && playerPieces[i][3] == Character.getNumericValue(location.charAt(1))) {
							piece = i;
							break;
						}
					}
				}
				else {
					System.out.println("\tInvalid input. Please enter only numbers.");
					continue;
				}
				if(piece == 999) {
					System.out.println("\tPiece does not exist at that location. Please enter another");
					continue;
				}
				else {
					break;
				}
			}
			if(playerPieces[piece][0] == 1) { // if piece is taken
				System.out.println("\tThat piece has been taken, please select another");
				continue;
			}
			else if(playerPieces[piece][1] == 0) { // not kinged move
				canMoveLeft = canMoveLeftUp(playerPieces[piece], board);
				canMoveRight = canMoveRightUp(playerPieces[piece], board);
				if(canMoveLeft || canMoveRight) {
					System.out.print("Enter direction you want piece to move, \"right\" or \"left\" or press \"q\" to select a different piece: ");
					move = s.nextLine().toLowerCase();
					while(move.charAt(0) != 'q') {
						if(move.charAt(0) == 'l') {
							if(canMoveLeft) {
								returnedMove[0] = Integer.toString(piece);
								returnedMove[1] = "LeftUp";
								return returnedMove;
							}
							else {
								System.out.println("\tCannot move that piece left. Please select another move: ");
								move = s.nextLine().toLowerCase();
								continue;
							}
						}
						else if(move.charAt(0) == 'r') {
							if(canMoveRight) {
								returnedMove[0] = Integer.toString(piece);
								returnedMove[1] = "RightUp";
								return returnedMove;
							}
							else {
								System.out.println("\tCannot move that piece right. Please select another move: ");
								move = s.nextLine().toLowerCase();
								continue;
							}
						}
						else {
							System.out.println("Invalid move. Please enter \"right\" or \"left\" or press \"q\" to select a different piece:  ");
							move = s.nextLine().toLowerCase();
							continue;
						}
					}
					if(move.charAt(0) == 'q') {
						continue;
					}
					break;
				}
				else {
					System.out.println("\tCannot move that piece. Please select another");
					continue;
				}
			}
			else if(playerPieces[piece][1] == 1){ // kinged move
				System.out.print("Enter direction you want piece to move, \"right\" or \"left\", or press \"q\" to select a different piece: ");
				move = s.nextLine().toLowerCase();
				while(move.charAt(0) != 'q') {
					if(move.charAt(0) == 'l') {
						canMoveLeft = canMoveLeftUp(playerPieces[piece], board);
						canMoveLeftKinged = canMoveLeftDown(playerPieces[piece], board);
						if(canMoveLeft && canMoveLeftKinged) {
							System.out.print("Would you like the piece to move \"up\" or \"down\" the board? (or press \"q\" to select a different piece): ");
							kingedMove = s.nextLine().toLowerCase();
							while(kingedMove.charAt(0) != 'q') {
								if(kingedMove.charAt(0) == 'u') {
									returnedMove[0] = Integer.toString(piece);
									returnedMove[1] = "LeftUp";
									return returnedMove;
								}
								else if(kingedMove.charAt(0) == 'd') {
									returnedMove[0] = Integer.toString(piece);
									returnedMove[1] = "LeftDown";
									return returnedMove;
								}
							}
							if(kingedMove.charAt(0) == 'q') {
								move = "q";
							}
						}
						else if(canMoveLeft) {
							returnedMove[0] = Integer.toString(piece);
							returnedMove[1] = "LeftUp";
							return returnedMove;
						}
						else if(canMoveLeftKinged) {
							returnedMove[0] = Integer.toString(piece);
							returnedMove[1] = "LeftDown";
							return returnedMove;
						}
						else {
							System.out.println("\tCannot move that piece left. Please select another move: ");
							move = s.nextLine().toLowerCase();
							continue;
						}
					}
					else if(move.charAt(0) == 'r') {
						canMoveRight = canMoveRightUp(playerPieces[piece], board);
						canMoveRightKinged = canMoveRightDown(playerPieces[piece], board);
						if(canMoveRight && canMoveRightKinged) {
							System.out.print("Would you like the piece to move \"up\" or \"down\" the board? (or press \"q\" to select a different piece): ");
							kingedMove = s.nextLine().toLowerCase();
							while(kingedMove.charAt(0) != 'q') {
								if(kingedMove.charAt(0) == 'u') {
									returnedMove[0] = Integer.toString(piece);
									returnedMove[1] = "RightUp";
									return returnedMove;
								}
								else if(kingedMove.charAt(0) == 'd') {
									returnedMove[0] = Integer.toString(piece);
									returnedMove[1] = "RightDown";
									return returnedMove;
								}
							}
							if(kingedMove.charAt(0) == 'q') {
								move = "q";
							}
						}
						else if(canMoveRight) {
							returnedMove[0] = Integer.toString(piece);
							returnedMove[1] = "RightUp";
							return returnedMove;
						}
						else if(canMoveRightKinged) {
							returnedMove[0] = Integer.toString(piece);
							returnedMove[1] = "RightDown";
							return returnedMove;
						}
						else {
							System.out.println("\tCannot move that piece Right. Please select another move: ");
							move = s.nextLine().toLowerCase();
							continue;
						}
					}
				}
			}
			else {
				System.out.print(playerPieces[piece][0]);
				System.out.print(playerPieces[piece][1]);
				System.out.print(playerPieces[piece][2]);
				System.out.print(playerPieces[piece][3]);
				System.out.println(playerPieces[piece][4]);
				System.out.println("\tCannot move that piece. Please select another");
				continue;
			}
		}
		returnedMove[1] = "void";
		return returnedMove;
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static void movePlayer(String [] playerMove, int [][] pieceToMove, int [][] comPieces, char [][] board) {
		int piece = Integer.parseInt(playerMove[0]);
		String move = playerMove[1];
		int row = pieceToMove[piece][2];
		int column = pieceToMove[piece][3];
		String canDoubleJump;
		boolean hasTaken = false;
		
		if(move.equals("LeftUp")){
			if(board[row - 1][column - 1] != 'C') { // if there is not a piece in the way
				board[row][column] = '_';
				board[row - 1][column - 1] = 'P';
				pieceToMove[piece][2] = row - 1;
				pieceToMove[piece][3] = column - 1;
			}
			else if(board[row - 1][column - 1] == 'C') { // if there is a piece in the way
				board[row][column] = '_';
				board[row - 2][column - 2] = 'P';
				board[row - 1][column - 1] = '_';
				pieceToMove[piece][2] = row - 2;
				pieceToMove[piece][3] = column - 2;
				takeComPiece(row - 1, column - 1, comPieces, board);
				hasTaken = true;
			}
		}
		if(move.equals("LeftDown")){
			if(board[row + 1][column - 1] != 'C') { // if there is not a piece in the way
				board[row][column] = '_';
				board[row + 1][column - 1] = 'P';
				pieceToMove[piece][2] = row + 1;
				pieceToMove[piece][3] = column - 1;
			}
			else if(board[row + 1][column - 1] == 'C') { // if there is a piece in the way
				board[row][column] = '_';
				board[row + 2][column - 2] = 'P';
				board[row + 1][column - 1] = '_';
				pieceToMove[piece][2] = row + 2;
				pieceToMove[piece][3] = column - 2;
				takeComPiece(row + 1, column - 1, comPieces, board);
				hasTaken = true;
			}
		}
		if(move.equals("RightUp")){
			if(board[row - 1][column + 1] != 'C') { // if there is not a piece in the way
				board[row][column] = '_';
				board[row - 1][column + 1] = 'P';
				pieceToMove[piece][2] = row - 1;
				pieceToMove[piece][3] = column + 1;
			}
			else if(board[row - 1][column + 1] == 'C') { // if there is a piece in the way
				board[row][column] = '_';
				board[row - 2][column + 2] = 'P';
				board[row - 1][column + 1] = '_';
				pieceToMove[piece][2] = row - 2;
				pieceToMove[piece][3] = column + 2;
				takeComPiece(row - 1, column + 1, comPieces, board);
				hasTaken = true;
			}
		}
		if(move.equals("RightDown")){
			if(board[row + 1][column + 1] != 'C') { // if there is not a piece in the way
				board[row][column] = '_';
				board[row + 1][column + 1] = 'P';
				pieceToMove[piece][2] = row + 1;
				pieceToMove[piece][3] = column + 1;
			}
			else if(board[row + 1][column + 1] == 'C') { // if there is a piece in the way
				board[row][column] = '_';
				board[row + 2][column + 2] = 'P';
				board[row + 1][column + 1] = '_';
				pieceToMove[piece][2] = row + 2;
				pieceToMove[piece][3] = column + 2;
				takeComPiece(row + 1, column + 1, comPieces, board);
				hasTaken = true;
			}
		}
		while(hasTaken == true) {
			canDoubleJump = canDoubleJump(pieceToMove[piece], board);
			if(!( canDoubleJump.equals("No") )) {// if they can double jump
				playerMove[1] = canDoubleJump;
				movePlayer(playerMove, pieceToMove, comPieces, board);
				continue;
			}
			else {
				break;
			}
		}
		
		if(pieceToMove[piece][2] == 0 && pieceToMove[piece][1] != 1) { //if the piece has made it to the other side and is not already kinged then king them
			pieceToMove[piece][1] = 1;
		}
		
	}
	
	public static double moveComputer(String move, int [] piece, int [][] playerPieces, char [][] board) {
		int row = piece[2];
		int column = piece[3];
		String canDoubleJump;
		boolean hasTaken = false;
		double points = 0;
		
		if(move.equals("LeftDown")) {
			if(board[row + 1][column - 1] != 'P') { // if there is not a piece in the way
				board[row][column] = '_';
				board[row + 1][column - 1] = 'C';
				piece[2] = row + 1;
				piece[3] = column - 1;
			}
			else if(board[row + 1][column - 1] == 'P') { // if there is a piece in the way
				board[row][column] = '_';
				board[row + 2][column - 2] = 'C';
				board[row + 1][column - 1] = '_';
				piece[2] = row + 2;
				piece[3] = column - 2;
				takePlayerPiece(row + 1, column - 1, playerPieces, board);
				points += 5;
				hasTaken = true;
			}
			if(column == 0) {
				points += 1;
			}
			else if(column == 6) {
				points -= .5;
			}
		}
		else if(move.equals("RightDown")) {
			if(board[row + 1][column + 1] != 'P') { // if there is not a piece in the way
				board[row][column] = '_';
				board[row + 1][column + 1] = 'C';
				piece[2] = row + 1;
				piece[3] = column + 1;
			}
			else if(board[row + 1][column + 1] == 'P') { // if there is a piece in the way
				board[row][column] = '_';
				board[row + 2][column + 2] = 'C';
				board[row + 1][column + 1] = '_';
				piece[2] = row + 2;
				piece[3] = column + 2;
				takePlayerPiece(row + 1, column + 1, playerPieces, board);
				points += 5;
				hasTaken = true;
			}
			if(column == 7) {
				points += 1;
			}
			else if(column == 1) {
				points -= .5;
			}
		}
		else if(move.equals("LeftUp")) {
			if(board[row - 1][column - 1] != 'P') { // if there is not a piece in the way
				board[row][column] = '_';
				board[row - 1][column - 1] = 'C';
				piece[2] = row - 1;
				piece[3] = column - 1;
			}
			else if(board[row - 1][column - 1] == 'P') { // if there is a piece in the way
				board[row][column] = '_';
				board[row - 2][column - 2] = 'C';
				board[row - 1][column - 1] = '_';
				piece[2] = row - 2;
				piece[3] = column - 2;
				takePlayerPiece(row - 1, column - 1, playerPieces, board);
				points += 5;
				hasTaken = true;
			}
			if(column == 0) {
				points += 1;
			}
			else if(column == 6) {
				points -= .5;
			}
		}
		else if(move.equals("RightUp")) {
			if(board[row - 1][column + 1] != 'P') { // if there is not a piece in the way
				board[row][column] = '_';
				board[row - 1][column + 1] = 'C';
				piece[2] = row - 1;
				piece[3] = column + 1;
			}
			else if(board[row - 1][column + 1] == 'P') { // if there is a piece in the way
				board[row][column] = '_';
				board[row - 2][column + 2] = 'C';
				board[row - 1][column + 1] = '_';
				piece[2] = row - 2;
				piece[3] = column + 2;
				takePlayerPiece(row - 1, column + 1, playerPieces, board);
				points += 5;
				hasTaken = true;
			}
			if(column == 7) {
				points += 1;
			}
			else if(column == 1) {
				points -= .5;
			}
		}
		while(hasTaken == true) {
			canDoubleJump = canDoubleJumpComputer(piece, board);
			if( !( canDoubleJump.equals("No")) ) {// if they can double jump
				move = canDoubleJump;
				points += moveComputer(move, piece, playerPieces, board);
				continue;
			}
			else {
				break;
			}
		}
		
		if(piece[2] == 7 && piece[1] != 1) { //if the piece has made it to the other side and is not already kinged then king them
			piece[1] = 1;
			points += 9;
		}
		return points;
	}
	
	public static double moveComputer2(String move, int [] piece, double points, int [][] comPieces, char [][] board) {
		int row = piece[2];
		int column = piece[3];
		String canDoubleJump;
		boolean hasTaken = false;
		
		if(move.equals("LeftDown")) {
			if(board[row + 1][column - 1] != 'C') { // if there is not a piece in the way
				board[row][column] = '_';
				board[row + 1][column - 1] = 'P';
				piece[2] = row + 1;
				piece[3] = column - 1;
			}
			else if(board[row + 1][column - 1] == 'C') { // if there is a piece in the way
				board[row][column] = '_';
				board[row + 2][column - 2] = 'P';
				board[row + 1][column - 1] = '_';
				piece[2] = row + 2;
				piece[3] = column - 2;
				takeComPiece(row + 1, column - 1, comPieces, board);
				points += 5;
				hasTaken = true;
			}
			if(column == 0) {
				points += 1;
			}
			else if(column == 6) {
				points -= .5;
			}
		}
		else if(move.equals("RightDown")) {
			if(board[row + 1][column + 1] != 'C') { // if there is not a piece in the way
				board[row][column] = '_';
				board[row + 1][column + 1] = 'P';
				piece[2] = row + 1;
				piece[3] = column + 1;
			}
			else if(board[row + 1][column + 1] == 'C') { // if there is a piece in the way
				board[row][column] = '_';
				board[row + 2][column + 2] = 'P';
				board[row + 1][column + 1] = '_';
				piece[2] = row + 2;
				piece[3] = column + 2;
				takeComPiece(row + 1, column + 1, comPieces, board);
				points += 5;
				hasTaken = true;
			}
			if(column == 7) {
				points += 1;
			}
			else if(column == 1) {
				points -= .5;
			}
		}
		else if(move.equals("LeftUp")) {
			if(board[row - 1][column - 1] != 'C') { // if there is not a piece in the way
				board[row][column] = '_';
				board[row - 1][column - 1] = 'P';
				piece[2] = row - 1;
				piece[3] = column - 1;
			}
			else if(board[row - 1][column - 1] == 'C') { // if there is a piece in the way
				board[row][column] = '_';
				board[row - 2][column - 2] = 'P';
				board[row - 1][column - 1] = '_';
				piece[2] = row - 2;
				piece[3] = column - 2;
				takeComPiece(row - 1, column - 1, comPieces, board);
				hasTaken = true;
				points += 5;
			}
			if(column == 0) {
				points += 1;
			}
			else if(column == 6) {
				points -= .5;
			}
		}
		else if(move.equals("RightUp")) {
			if(board[row - 1][column + 1] != 'C') { // if there is not a piece in the way
				board[row][column] = '_';
				board[row - 1][column + 1] = 'P';
				piece[2] = row - 1;
				piece[3] = column + 1;
			}
			else if(board[row - 1][column + 1] == 'C') { // if there is a piece in the way
				board[row][column] = '_';
				board[row - 2][column + 2] = 'P';
				board[row - 1][column + 1] = '_';
				piece[2] = row - 2;
				piece[3] = column + 2;
				takeComPiece(row - 1, column + 1, comPieces, board);
				hasTaken = true;
				points += 5;
			}
			if(column == 7) {
				points += 1;
			}
			else if(column == 1) {
				points -= .5;
			}
		}
		while(hasTaken == true) {
			canDoubleJump = canDoubleJumpComputer2(piece, board);
			if( !( canDoubleJump.equals("No")) ) {// if they can double jump
				move = canDoubleJump;
				points += moveComputer2(move, piece, points, comPieces, board);
				continue;
			}
			else {
				break;
			}
		}
		
		if(piece[2] == 7 && piece[1] != 1) { //if the piece has made it to the other side and is not already kinged then king them
			piece[1] = 1;
			points += 9;
		}
		return points;
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static String canDoubleJump(int [] piece, char [][] board) {
		Scanner s = new Scanner(System.in);
		
		int row = piece[2];
		int column = piece[3];
		String move;
		String kingedMove;
		
		boolean canDoubleJumpLeftUp = false;
		boolean canDoubleJumpLeftDown = false;
		boolean canDoubleJumpRightUp = false;
		boolean canDoubleJumpRightDown = false;
		
		if(column > 0 && row > 0) {
			if(board[row - 1][column - 1] == 'C') {
				if(column - 1 != 0 && row - 1 != 0 && board[row - 2][column - 2] != 'C') { // if the piece to the left is not on the top or side edge and there is not a piece behind it
					canDoubleJumpLeftUp = true;
				}
			}
		}
		if(column != 7 && column >= 0 && row > 0) {
			if(board[row - 1][column + 1] == 'C') {
				if(column + 1 != 7 && row - 1 != 0 && board[row - 2][column + 2] != 'C') { // if the piece to the right is not on the top or side edge and there is not a piece behind it
					canDoubleJumpRightUp = true;
				}
			}
		}
		if(piece[1] == 1) { // dont have to worry about non-kinged since they will never enter this and will always be false
			if(column > 0 && row != 7) {
				if(board[row + 1][column - 1] == 'C') {
					if(column - 1 != 0 && row + 1 != 7 && board[row + 2][column - 2] != 'C') { // if the piece to the left is not on the top or side edge and there is not a piece behind it
						canDoubleJumpLeftDown = true;
					}
				}
			}
			if(column != 7 && column >= 0 && row != 7) {
				if(board[row + 1][column + 1] == 'C') {
					if(column + 1 != 7 && row + 1 != 7 && board[row + 2][column + 2] != 'C') { // if the piece to the right is not on the top or side edge and there is not a piece behind it
						canDoubleJumpRightDown = true;
					}
				}
			}
		}
		if(piece[1] == 0) {
			if(canDoubleJumpLeftUp || canDoubleJumpRightUp) {
				while(true) {
					System.out.print("You can double jump. Would you like to jump to the \"right\" or \"left\" (or enter \"no\" to stay put): ");
					move = s.nextLine().toLowerCase();
					if(move.charAt(0) == 'l') {
						if(canDoubleJumpLeftUp) {
							return "LeftUp";
						}
						else {
							System.out.println("\tCannot double jump that way");
							continue;
						}
					}
					else if(move.charAt(0) == 'r') {
						if(canDoubleJumpRightUp) {
							return "RightUp";
						}
						else {
							System.out.println("\tCannot double jump that way");
							continue;
						}
					}
					else if(move.charAt(0) == 'n') {
						break;
					}
					else {
						System.out.println("\tInvalid input. Please enter again.");
					}
				}
			}
		}
		else if(piece[1] == 1) {
			if(canDoubleJumpLeftUp || canDoubleJumpLeftDown || canDoubleJumpRightUp || canDoubleJumpRightDown) {
				while(true) {
					System.out.print("You can double jump. Would you like to jump to the \"right\" or \"left\" (or enter \"no\" to stay put): ");
					move = s.nextLine().toLowerCase();
					if(move.charAt(0) == 'l') {
						if(canDoubleJumpLeftUp && canDoubleJumpLeftDown) {
							while(true) {
								System.out.println("Would you like to jump \"up\" or \"down\" (or enter \"no\" to stay put)?: ");
								kingedMove = s.nextLine().toLowerCase();
								if(kingedMove.charAt(0) == 'u') {
									return "LeftUp";
								}
								else if(kingedMove.charAt(0) == 'd') {
									return "LeftDown";
								}
								else if(kingedMove.charAt(0) == 'n') {
									break;
								}
								else {
									System.out.println("\tInvalid input. Please enter again");
									continue;
								}
							}
						}
						else if(canDoubleJumpLeftUp) {
							return "LeftUp";
						}
						else if(canDoubleJumpLeftDown) {
							return "LeftDown";
						}
						else {
							System.out.println("\tCannot double jump that way. Please enter a different input.");
						}
					}
					else if(move.charAt(0) == 'r') {
						if(canDoubleJumpRightUp && canDoubleJumpRightDown) {
							while(true) {
								System.out.println("Would you like to jump \"up\" or \"down\" (or enter \"no\" to stay put)?: ");
								kingedMove = s.nextLine().toLowerCase();
								if(kingedMove.charAt(0) == 'u') {
									return "RightUp";
								}
								else if(kingedMove.charAt(0) == 'd') {
									return "RightDown";
								}
								else if(kingedMove.charAt(0) == 'n') {
									break;
								}
								else {
									System.out.println("\tInvalid input. Please enter again");
									continue;
								}
							}
						}
						else if(canDoubleJumpRightUp) {
							return "RightUp";
						}
						else if(canDoubleJumpRightDown) {
							return "RightDown";
						}
						else {
							System.out.println("\tCannot double jump that way. Please enter a different input.");
							continue;
						}
					}
					else if(move.charAt(0) == 'n') {
						break;
					}
					else {
						System.out.println("\tInvalid input. Please enter again");
						continue;
					}
				}
			}
		}
		return "No";
	}
	
	public static String canDoubleJumpComputer(int [] piece, char [][] board) {
		Random rand = new Random();
		
		int row = piece[2];
		int column = piece[3];
		int randomResult;
		
		boolean canDoubleJumpLeftUp = false;
		boolean canDoubleJumpLeftDown = false;
		boolean canDoubleJumpRightUp = false;
		boolean canDoubleJumpRightDown = false;
		
		if(!(column <= 0) && row != 7) {
			if(board[row + 1][column - 1] == 'P') {
				if(column - 1 != 0 && row + 1 != 7 && board[row + 2][column - 2] != 'P') { // if the piece to the left is not on the top or side edge and there is not a piece behind it
					canDoubleJumpLeftDown = true;
				}
			}
		}
		if(column != 7 && !(column < 0) && row != 7) {
			if(board[row + 1][column + 1] == 'P') {
				if(column + 1 != 7 && row + 1 != 7 && board[row + 2][column + 2] != 'P') { // if the piece to the right is not on the top or side edge and there is not a piece behind it
					canDoubleJumpRightDown = true;
				}
			}
		}
		if(piece[1] == 1) { // dont have to worry about non-kinged since they will never enter this and will always be false
			if(!(column <= 0) && row > 0) {
				if(board[row - 1][column - 1] == 'P') {
					if(column - 1 != 0 && row - 1 != 0 && board[row - 2][column - 2] != 'P') { // if the piece to the left is not on the top or side edge and there is not a piece behind it
						canDoubleJumpLeftUp = true;
					}
				}
			}
			if(column != 7 && !(column < 0) && row > 0) {
				if(board[row - 1][column + 1] == 'P') {
					if(column + 1 != 7 && row - 1 != 0 && board[row - 2][column + 2] != 'P') { // if the piece to the right is not on the top or side edge and there is not a piece behind it
						canDoubleJumpRightUp = true;
					}
				}
			}
		}
		
		if(piece[1] == 0) {
			if(canDoubleJumpLeftDown || canDoubleJumpRightDown) {
				if(canDoubleJumpLeftDown && canDoubleJumpRightDown) { // possible place for improvement as if there is two options for double jump and one leads to a third jump and one does not we do not conciously check for that and choose the better path we just do a random. Goof for now though becuase that seems hard
					if(rand.nextInt(10) < 5) {
						return "LeftDown";
					}
					else {
						return "RightDown";
					}
				}
				else if(canDoubleJumpLeftDown) {
					return "LeftDown";
				}
				else if(canDoubleJumpRightDown) {
					return "RightDown";
				}
				else {
					return "No";
				}
			}
		}
		else if(piece[1] == 1) {
			if(canDoubleJumpLeftDown || canDoubleJumpRightDown || canDoubleJumpLeftUp || canDoubleJumpRightUp) {
				if(canDoubleJumpLeftDown && !(canDoubleJumpRightDown) && !(canDoubleJumpLeftUp) && !(canDoubleJumpRightUp)) {
					return "LeftDown";
				}
				else if(canDoubleJumpRightDown && !(canDoubleJumpLeftDown) && !(canDoubleJumpLeftUp) && !(canDoubleJumpRightUp)) {
					return "RightDown";
				}
				else if(canDoubleJumpLeftUp && !(canDoubleJumpLeftDown) && !(canDoubleJumpRightDown) && !(canDoubleJumpRightUp)) {
					return "LeftUp";
				}
				else if(canDoubleJumpRightUp && !(canDoubleJumpLeftDown) && !(canDoubleJumpRightDown) && !(canDoubleJumpLeftUp)) {
					return "RightUp";
				}
				else if(canDoubleJumpLeftDown && canDoubleJumpRightDown && canDoubleJumpLeftUp) {
					randomResult = rand.nextInt(30);
					if(randomResult < 10) { // careful with this
						return "LeftDown";
					}
					else if(randomResult < 20) {
						return "RightDown";
					}
					else if(randomResult < 30) {
						return "LeftUp";
					}
				}
				else if(canDoubleJumpLeftDown && canDoubleJumpRightDown && canDoubleJumpRightUp) {
					randomResult = rand.nextInt(30);
					if(randomResult < 10) { // careful with this
						return "LeftDown";
					}
					else if(randomResult < 20) {
						return "RightDown";
					}
					else if(randomResult < 30) {
						return "RightUp";
					}
				}
				else if(canDoubleJumpLeftDown && canDoubleJumpLeftUp && canDoubleJumpRightUp) {
					randomResult = rand.nextInt(30);
					if(randomResult < 10) { // careful with this
						return "LeftDown";
					}
					else if(randomResult < 20) {
						return "LeftUp";
					}
					else if(randomResult < 30) {
						return "RightUp";
					}
				}
				else if(canDoubleJumpRightDown && canDoubleJumpLeftUp && canDoubleJumpRightUp) {
					randomResult = rand.nextInt(30);
					if(randomResult < 10) { // careful with this
						return "RightDown";
					}
					else if(randomResult < 20) {
						return "LeftUp";
					}
					else if(randomResult < 30) {
						return "RightUp";
					}
				}
				else if(canDoubleJumpLeftDown && canDoubleJumpRightDown) {
					if(rand.nextInt(10) < 5) {
						return "LeftDown";
					}
					else {
						return "RightDown";
					}
				}
				else if(canDoubleJumpLeftDown && canDoubleJumpLeftUp) {
					if(rand.nextInt(10) < 5) {
						return "LeftDown";
					}
					else {
						return "LeftUp";
					}
				}
				else if(canDoubleJumpLeftDown && canDoubleJumpRightUp) {
					if(rand.nextInt(10) < 5) {
						return "LeftDown";
					}
					else {
						return "RightUp";
					}
				}
				else if(canDoubleJumpRightDown && canDoubleJumpLeftUp) {
					if(rand.nextInt(10) < 5) {
						return "RightDown";
					}
					else {
						return "LeftUp";
					}
				}
				else if(canDoubleJumpRightDown && canDoubleJumpRightUp) {
					if(rand.nextInt(10) < 5) {
						return "RightDown";
					}
					else {
						return "RightUp";
					}
				}
				else if(canDoubleJumpLeftUp && canDoubleJumpRightUp) {
					if(rand.nextInt(10) < 5) {
						return "LeftUp";
					}
					else {
						return "RightUp";
					}
				}
			}
		}
		return "No";
	}
	
	public static String canDoubleJumpComputer2(int [] piece, char [][] board) {
		Random rand = new Random();
		
		int row = piece[2];
		int column = piece[3];
		int randomResult;
		
		boolean canDoubleJumpLeftUp = false;
		boolean canDoubleJumpLeftDown = false;
		boolean canDoubleJumpRightUp = false;
		boolean canDoubleJumpRightDown = false;
		
		if(!(column <= 0) && row > 0) {
			if(board[row - 1][column - 1] == 'C') {
				if(column - 1 != 0 && row - 1 != 0 && board[row - 2][column - 2] != 'C') { // if the piece to the left is not on the top or side edge and there is not a piece behind it
					canDoubleJumpLeftUp = true;
				}
			}
		}
		if(column != 7 && !(column < 0) && row > 0) {
			if(board[row - 1][column + 1] == 'C') {
				if(column + 1 != 7 && row - 1 != 0 && board[row - 2][column + 2] != 'C') { // if the piece to the right is not on the top or side edge and there is not a piece behind it
					canDoubleJumpRightUp = true;
				}
			}
		}
		if(piece[1] == 1) { // dont have to worry about non-kinged since they will never enter this and will always be false
			if(!(column <= 0) && row != 7) {
				if(board[row + 1][column - 1] == 'C') {
					if(column - 1 != 0 && row + 1 != 7 && board[row + 2][column - 2] != 'C') { // if the piece to the left is not on the top or side edge and there is not a piece behind it
						canDoubleJumpLeftDown = true;
					}
				}
			}
			if(column != 7 && !(column < 0) && row != 7) {
				if(board[row + 1][column + 1] == 'C') {
					if(column + 1 != 7 && row + 1 != 7 && board[row + 2][column + 2] != 'C') { // if the piece to the right is not on the top or side edge and there is not a piece behind it
						canDoubleJumpRightDown = true;
					}
				}
			}
		}
		
		if(piece[1] == 0) {
			if(canDoubleJumpLeftUp || canDoubleJumpRightUp) {
				if(canDoubleJumpLeftUp && canDoubleJumpRightUp) { // possible place for improvement as if there is two options for double jump and one leads to a third jump and one does not we do not conciously check for that and choose the better path we just do a random. Goof for now though becuase that seems hard
					if(rand.nextInt(10) < 5) {
						return "LeftUp";
					}
					else {
						return "RightUp";
					}
				}
				else if(canDoubleJumpLeftUp) {
					return "LeftUp";
				}
				else if(canDoubleJumpRightUp) {
					return "RightUp";
				}
				else {
					return "No";
				}
			}
		}
		else if(piece[1] == 1) {
			if(canDoubleJumpLeftDown || canDoubleJumpRightDown || canDoubleJumpLeftUp || canDoubleJumpRightUp) {
				if(canDoubleJumpLeftDown && !(canDoubleJumpRightDown) && !(canDoubleJumpLeftUp) && !(canDoubleJumpRightUp)) {
					return "LeftDown";
				}
				else if(canDoubleJumpRightDown && !(canDoubleJumpLeftDown) && !(canDoubleJumpLeftUp) && !(canDoubleJumpRightUp)) {
					return "RightDown";
				}
				else if(canDoubleJumpLeftUp && !(canDoubleJumpLeftDown) && !(canDoubleJumpRightDown) && !(canDoubleJumpRightUp)) {
					return "LeftUp";
				}
				else if(canDoubleJumpRightUp && !(canDoubleJumpLeftDown) && !(canDoubleJumpRightDown) && !(canDoubleJumpLeftUp)) {
					return "RightUp";
				}
				else if(canDoubleJumpLeftDown && canDoubleJumpRightDown && canDoubleJumpLeftUp) {
					randomResult = rand.nextInt(30);
					if(randomResult < 10) { // careful with this
						return "LeftDown";
					}
					else if(randomResult < 20) {
						return "RightDown";
					}
					else if(randomResult < 30) {
						return "LeftUp";
					}
				}
				else if(canDoubleJumpLeftDown && canDoubleJumpRightDown && canDoubleJumpRightUp) {
					randomResult = rand.nextInt(30);
					if(randomResult < 10) { // careful with this
						return "LeftDown";
					}
					else if(randomResult < 20) {
						return "RightDown";
					}
					else if(randomResult < 30) {
						return "RightUp";
					}
				}
				else if(canDoubleJumpLeftDown && canDoubleJumpLeftUp && canDoubleJumpRightUp) {
					randomResult = rand.nextInt(30);
					if(randomResult < 10) { // careful with this
						return "LeftDown";
					}
					else if(randomResult < 20) {
						return "LeftUp";
					}
					else if(randomResult < 30) {
						return "RightUp";
					}
				}
				else if(canDoubleJumpRightDown && canDoubleJumpLeftUp && canDoubleJumpRightUp) {
					randomResult = rand.nextInt(30);
					if(randomResult < 10) { // careful with this
						return "RightDown";
					}
					else if(randomResult < 20) {
						return "LeftUp";
					}
					else if(randomResult < 30) {
						return "RightUp";
					}
				}
				else if(canDoubleJumpLeftDown && canDoubleJumpRightDown) {
					if(rand.nextInt(10) < 5) {
						return "LeftDown";
					}
					else {
						return "RightDown";
					}
				}
				else if(canDoubleJumpLeftDown && canDoubleJumpLeftUp) {
					if(rand.nextInt(10) < 5) {
						return "LeftDown";
					}
					else {
						return "LeftUp";
					}
				}
				else if(canDoubleJumpLeftDown && canDoubleJumpRightUp) {
					if(rand.nextInt(10) < 5) {
						return "LeftDown";
					}
					else {
						return "RightUp";
					}
				}
				else if(canDoubleJumpRightDown && canDoubleJumpLeftUp) {
					if(rand.nextInt(10) < 5) {
						return "RightDown";
					}
					else {
						return "LeftUp";
					}
				}
				else if(canDoubleJumpRightDown && canDoubleJumpRightUp) {
					if(rand.nextInt(10) < 5) {
						return "RightDown";
					}
					else {
						return "RightUp";
					}
				}
				else if(canDoubleJumpLeftUp && canDoubleJumpRightUp) {
					if(rand.nextInt(10) < 5) {
						return "LeftUp";
					}
					else {
						return "RightUp";
					}
				}
			}
		}
		return "No";
	}
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static void takeComPiece(int rowOfPieceToTake, int columnOfPieceToTake, int [][] comPieces, char [][] board) {
		for(int i = 0; i < comPieces.length; i++) {
			if(comPieces[i][2] == rowOfPieceToTake && comPieces[i][3] == columnOfPieceToTake) {
				comPieces[i][0] = 1;
				comPieces[i][2] = -1;
				comPieces[i][3] = -1;
			}
		}
	}
	
	public static void takePlayerPiece(int rowOfPieceToTake, int columnOfPieceToTake, int [][] playerPieces, char [][] board) {
		for(int i = 0; i < playerPieces.length; i++) {
			if(playerPieces[i][2] == rowOfPieceToTake && playerPieces[i][3] == columnOfPieceToTake) {
				playerPieces[i][0] = 1;
				playerPieces[i][2] = -1;
				playerPieces[i][3] = -1;
			}
		}
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static boolean canMoveLeftUp(int [] piece, char [][] board) {
		int row = piece[2];
		int column = piece[3];
		if(piece[4] == 1) {
			if(column > 0 && row > 0) { // put not kinged in call
				if(board[row - 1][column - 1] != 'C' && board[row - 1][column - 1] != 'P') {
					return true;
				}
				else if(board[row - 1][column - 1] == 'C') {
					if(column - 1 != 0 && row - 1 != 0 && board[row - 2][column - 2] != 'C' && board[row - 2][column - 2] != 'P') { // if the piece to the left is not on the top or side edge and there is not a piece behind it
						return true;
					}
				}
			}
		}
		else if(piece[4] == 0) { // COM
			if(column > 0 && row > 0) { // put not kinged in call
				if(board[row - 1][column - 1] != 'C' && board[row - 1][column - 1] != 'P') {
					return true;
				}
				else if(board[row - 1][column - 1] == 'P') {
					if(column - 1 != 0 && row - 1 != 0 && board[row - 2][column - 2] != 'P' && board[row - 2][column - 2] != 'C') { // if the piece to the left is not on the top or side edge and there is not a piece behind it
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean canMoveLeftDown(int [] piece, char [][] board) {
		int row = piece[2];
		int column = piece[3];
		if(piece[4] == 1) {
			if(column > 0 && row != 7) { // put not kinged in call
				if(board[row + 1][column - 1] != 'C' && board[row + 1][column - 1] != 'P') {
					return true;
				}
				else if(board[row + 1][column - 1] == 'C') {
					if(column - 1 != 0 && row + 1 != 7 && board[row + 2][column - 2] != 'C' && board[row + 2][column - 2] != 'P') { // if the piece to the left is not on the top or side edge and there is not a piece behind it
						return true;
					}
				}
			}
		}
		else if(piece[4] == 0) {
			if(column > 0 && row != 7) { // put not kinged in call
				if(board[row + 1][column - 1] != 'C' && board[row + 1][column - 1] != 'P') {
					return true;
				}
				else if(board[row + 1][column - 1] == 'P') {
					if(column - 1 != 0 && row + 1 != 7 && board[row + 2][column - 2] != 'P' && board[row + 2][column - 2] != 'C') { // if the piece to the left is not on the top or side edge and there is not a piece behind it
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static boolean canMoveRightUp(int [] piece, char [][] board) {
		int row = piece[2];
		int column = piece[3];
		if(piece[4] == 1) {
			if(column < 7 && column >= 0 && row > 0) {
				if(board[row - 1][column + 1] != 'C' && board[row - 1][column + 1] != 'P') {
					return true;
				}
				else if(board[row - 1][column + 1] == 'C') {
					if(column + 1 != 7 && row - 1 != 0 && board[row - 2][column + 2] != 'C'  && board[row - 2][column + 2] != 'P') { // if the piece to the right is not on the top or side edge and there is not a piece behind it
						return true;
					}
				}
			}
		}
		else if(piece[4] == 0) {
			if(column < 7 && column >= 0 && row > 0) {
				if(board[row - 1][column + 1] != 'C' && board[row - 1][column + 1] != 'P') {
					return true;
				}
				else if(board[row - 1][column + 1] == 'P') {
					if(column + 1 != 7 && row - 1 != 0 && board[row - 2][column + 2] != 'P' && board[row - 2][column + 2] != 'C') { // if the piece to the right is not on the top or side edge and there is not a piece behind it
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean canMoveRightDown(int [] piece, char [][] board) {
		int row = piece[2];
		int column = piece[3];
		if(piece[4] == 1) {
			if(column < 7 && column >= 0 && row < 7) {
				if(board[row + 1][column + 1] != 'C' && board[row + 1][column + 1] != 'P') {
					return true;
				}
				else if(board[row + 1][column + 1] == 'C') {
					if(column + 1 != 7 && row + 1 != 7 && board[row + 2][column + 2] != 'C'  && board[row + 2][column + 2] != 'P') { // if the piece to the right is not on the top or side edge and there is not a piece behind it
						return true;
					}
				}
			}
		}
		else if(piece[4] == 0) {
			if(column < 7 && column >= 0 && row < 7) {
				if(board[row + 1][column + 1] != 'C' && board[row + 1][column + 1] != 'P') {
					return true;
				}
				else if(board[row + 1][column + 1] == 'P') {
					if(column + 1 != 7 && row + 1 != 7 && board[row + 2][column + 2] != 'P'  && board[row + 2][column + 2] != 'C') { // if the piece to the right is not on the top or side edge and there is not a piece behind it
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------------------
	
	public static char [][] copyBoard(char [][] board){
		char [][] newBoard = new char[board.length][board[0].length];
		for(int i = 0; i < board.length; i++) { // copy board to display board
			for(int j = 0; j < board[i].length; j++) {
				newBoard[i][j] = board[i][j];
			}
		}
		return newBoard;
	}
	
	public static int [][] copyPieces(int [][] pieces){
		int [][] newPieces = new int[pieces.length][pieces[0].length];
		for(int i = 0; i < pieces.length; i++) { // copy playerPieces to fakePlayerPieces
			for(int j = 0; j < pieces[i].length; j++) {
				newPieces[i][j] = pieces[i][j];
			}
		}
		return newPieces;
	}
	
	public static int [] copyPiece(int [][] pieces, int piece) {
		int [] newPiece = new int[pieces[piece].length];
		for(int i = 0; i < pieces[piece].length; i++) {
			newPiece[i] = pieces[piece][i];
		}
		return newPiece;
	}
	
}
