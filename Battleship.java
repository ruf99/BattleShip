import java.util.Arrays;
import java.util.Scanner;

public class Battleship {
	public static void main(String[] args) {


		//Declaring all relevant data here, we need the size of the board which is 10x10, and the ships size. Though there is only 1 ship, it has dimensions 4.
		//the cells will have empty space until we randomly place ships in it. 

		final int size_Dimension = 10;
		char cells = ' ';
		final int ships_TotalSize = 4;


		Scanner input = new Scanner(System.in);

		//asking user for their name to welcome them. made using a while loop incase they enter a blank input.

		boolean data_validation = true;
		while(data_validation) {
			System.out.print("Welcome to Battleship. Please enter your name to begin: ");
			String user_Name = input.nextLine();

			String wrong_input = "";

			if (user_Name.equals(wrong_input)) {
				System.out.println();
				System.out.print("Oops! Please enter your name again: ");
				String answer_correct = input.nextLine();
				user_Questions(answer_correct);
				data_validation = false;
			}

			else{
				user_Questions(user_Name);
				break;
			}
		}

		//declare the boards. we need 2 boards to get this game to work. One is what we use to hide ships (board_Game), one is what the user will see (user_BoardView). Both are 10x10. 
		char[][] board_Game = create_TheGame(size_Dimension, cells, ships_TotalSize);
		char[][] user_BoardView = new char[size_Dimension][size_Dimension];


		//here we invoke the various methods created to display our results. there are multiple parameters entered to ensure a smooth functioning of the game.
		
		//this prints out the board_Game where ships are hidden. It is commented out, incase user wants to take a peek at the solution for whatever reason, they can remove the comment slashes. 
		//print_theBoard(board_Game);
		
		System.out.println();
		//this is the board the user will see. 

		user_BoardGame(user_BoardView);
		System.out.println();

		//using the designs that we have added, this method is invoked to print out the user's board. 
		print_theBoard(user_BoardView);
		System.out.println();

		//this is where all the code exists for the game logic.
		main_GameBegin(board_Game, user_BoardView, ships_TotalSize, size_Dimension);
		
		
		
	}


	public static void user_Questions(String name_OfUser){ //simply for letting the user know the instruction manual.
		System.out.println();
		System.out.print("Hi " + name_OfUser + "! :) Here are the rules to play Battleship.");
		System.out.println("You will be playing against the computer. I will hide one ship on this 10x10 board randomly, and you will guess the location.");
		System.out.print("Your goal is to destroy the ship with minimum guesses. I hope you have fun playing this game and enjoy your experience!");
		System.out.println();
	}

	public static void main_GameBegin(char[][] board_Game, char[][] user_BoardView, int ship_size, int size_Board) {
		
		Scanner input = new Scanner(System.in);
		int hits = 0;
		int misses = 0;
		while (hits != ship_size){
			System.out.print("Please enter target coordinates (eg. A1): ");
			String user_Target = input.nextLine();
			System.out.println();
			boolean answer = true;

			while (answer == true){
				
				/*CASE: THE USER HAS PUT CORRECT INPUT
				But it is at a location they have already selected for ship.
				We re-prompt in this case.
				*/

				if (user_BoardView[(int) user_Target.charAt(1) - 48][(int) user_Target.charAt(0) - 65] == 'X'){
					System.out.print("That is wrong input, choose another coordinate: ");
					user_Target = input.next();
					
				}

				/*CASE: THE USER HAS PUT CORRECT INPUT
				We check length of input. It should be 2. 
				We check if the first value (or 0 index) of the string input using charAt() is between A and J or not.
				We check if the second value (or 1 index) of the string input using charAt() is between 0 and 9 or not.
				*/

				else if (user_Target.length() == 2 && (int) user_Target.charAt(0) > 64 && (int) user_Target.charAt(0) < 75 && (int) user_Target.charAt(1) > 47 && (int) user_Target.charAt(1) < 58){
					answer = false;
				}
				
				/*CASE: THE USER HAS PUT INCORRECT INPUT.
				We re-prompt in this case.
				*/ 
				else if ((int) user_Target.charAt(0) < 64 || (int) user_Target.charAt(0) > 75 || (int) user_Target.charAt(1) < 47 || (int) user_Target.charAt(1) > 58){
					System.out.print("That is wrong input, choose another coordinate: ");
					user_Target = input.next();

				}

				else {
					System.out.print("Those are incorrect coordinates. Please try again: ");
					user_Target = input.next();
				}
			}
				//nested while loop ends here.			
			
				//the main while loop is still going. 
				//Checking if the board_Game has Ship hidden at selected location, if it does, then we convert the 'S' to an 'X' to indicate hit. 
				//if not, it is a miss. we indicate so by making the cell that had ' ' nothing in it, to '#' to indicate miss.
			if (board_Game[(int) user_Target.charAt(1) - 48][(int) user_Target.charAt(0) - 65] == 'S'){
				hits++;
				user_BoardView[(int) user_Target.charAt(1) - 48][(int) user_Target.charAt(0) - 65] = 'X';

			}
			
			else{
				user_BoardView[(int) user_Target.charAt(1) - 48][(int) user_Target.charAt(0) - 65] = '#';
				misses++;
				
			}
			//the board seen by user needs to keep updating otherwise they won't know what they are doing. for this purpose, we keep the print statement invoking user board within the while loop.
			print_theBoard(user_BoardView);
		}
		//the game is over since ship is found. program terminates. 
		System.out.println("Congratulations! Game over.");
		System.out.println("You took " + misses + " attempts to destroy the battleship.");

	}


	public static char[][] create_TheGame(int size, char ocean, int total_ships) { //this "fills" the cells with empty space using an advanced for loop. 
		char[][] board_Game = new char[size][size];
		for (char[] rows : board_Game) {
			Arrays.fill(rows, ocean);
		}
		return this_Is_The_RandomShipBoard(board_Game, total_ships); //we invoke another method here to randomly hide ships.
	}

	public static char[][] user_BoardGame(char[][] user_BoardView) { //this solely created the empty board we need for the user. 
		for (int row = 0; row < user_BoardView.length; row++){
			for (int coloumn = 0; coloumn < user_BoardView[row].length; coloumn++){
				user_BoardView[row][coloumn] = ' ';
			}
		}
		return user_BoardView;
	}

	public static char[][] this_Is_The_RandomShipBoard(char[][] board_Game, int total_ships) { // this is an important method. it hides the ships. 

		int random_accumulator = 0; //accumulator for the while loop
		int board_GameLength = board_Game.length; //10
		int place = (int) (Math.random() + 1); //we need to use random to hide ships. I added 0.5 to this because I wanted a greater amount of possibilities. 

		while (random_accumulator < 1) {
			if ((place == 0) || (place <= 0.75)) { // evenly dividing the possible outcome.
				int i = (int) (Math.random() * (board_Game[1].length - total_ships)); //random orientation for rows
				int j = (int) (Math.random() * board_Game[0].length); //random orientation for coloumns

				//we also check if it fits inside our 2d array or not.
				if (i < board_Game[0].length && (j + total_ships) < board_Game[1].length){
					for (int x = 0; x < total_ships; x++){
						board_Game[j][i + x] = 'S';
					}
						random_accumulator++;
				}
			return board_Game; //returns back to board_Game with hidden ships.
								
			}

			else if ((place > 0.75) || (place >= 1)) {	//this is the same code, it's only a different orientation of the ships.

				int x = (int) (Math.random() * board_Game[1].length);
				int y = (int) (Math.random() * (board_Game[0].length - total_ships));

				if (y < board_Game[1].length && (x + total_ships) < board_Game[0].length){
					for (int i = 0; i < total_ships; i++){
						board_Game[y + i][x] = 'S';
						random_accumulator++;
					}	
				}

			
			}
			
		}return board_Game;
	}

	public static void print_theBoard(char[][] board_Game) {

		/* Important Note: 
		Discussed briefly the pseudocode with Kelly Feldman for the designs. */

		int board_GameLength = board_Game.length;
		System.out.print(" ");
		char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };
		for (int i = 0; i < board_Game[0].length; i++) {
			System.out.print("   " + letters[i]);
		}
		System.out.println();

		for (int x = 0; x < board_Game.length; x++) {
			System.out.print("");
        	System.out.println("  +---+---+---+---+---+---+---+---+---+---+");
        	System.out.print(x + " |");

			for (int y = 0; y < board_Game.length; y++) {
				System.out.print(board_Game[x][y]);
				System.out.print("  |");
			}
			System.out.println("");	
			
				
		}
		System.out.println("  +---+---+---+---+---+---+---+---+---+---+");
	}
}