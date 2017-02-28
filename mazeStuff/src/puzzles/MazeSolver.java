package puzzles;

import java.util.Stack;

/*
 * This class contains a maze solver. To see the format of the maze, check out the other
 * classes here.
 * 
 * The basic gist of this solver is that it uses a stack to keep track of where the cursor
 * representing movement has gone. If it hits a loop or a dead end, it will backtrack until
 * it hits another path that it hasn't yet explored. If it backtracks all the way to the
 * start, then it has nowhere else to explore and the maze must be unsolvable as the solver
 * would have run into another path to explore.
 * 
 * The solver checks in a clockwise fashion starting from the north
 */

public class MazeSolver {
	
	Maze m;
	MazeReader mr;

	int curX;
	int curY;
	
	/*
	 * Basic idea here:
	 * in the maze print out:
	 * 0 = wall
	 * 1 = unexplored square
	 * 2 = explored square
	 * 4 = start
	 * 5 = finish
	 * C = current location
	 */
	
	// the path that was being followed. It pushes to the stack when moving to an unexplored
	// square and pops when going back to an explored square. The cursor will only go to an
	// explored square when backtracking.
	Stack<int[]> prevPos;
	
	// Constructor for this class. You have to give it a string that represents a filename
	// of a maze you want to solve. See MazeReader for the format of the maze file.
	public MazeSolver(String fileName) {
		m = new Maze();
		mr = new MazeReader();
		mr.readMaze(fileName);
		m.instantiateMaze(mr.maze, mr.xSize, mr.ySize, mr.startX, mr.startY, mr.finishX, mr.finishY);
		curX = m.startX;
		curY = m.startY;
		prevPos = new Stack<int[]>();
	}
	
	// adds a path location to the stack keeping track of where the cursor has gone
	public void addToPath(int x, int y) {
		int[] spot = new int[2];
		spot[0] = x;
		spot[1] = y;
		prevPos.push(spot);
	}
	
	// goes back one move. It pops the current location off the path and then moves the
	// cursor back one square to the previously visited square
	public void goBack() {
		if (prevPos.size() <= 1) {
			System.out.println("Out of squares to go back to!");
			return;
		}
		prevPos.pop();
		int[] temp = prevPos.peek();
		curX = temp[0];
		curY = temp[1];
	}
	
	// move to a given x,y coordinate. While doing so add it to the path stack and mark
	// the square as one that has been moved to AKA set it to 2.
	public void move(int x, int y) {
		curX = x;
		curY = y;
		addToPath(x,y);
		m.maze[x][y] = 2;
	}
	
	// See if you are beside the exit. If you are, stop exploring and just finish the maze
		public int checkWinningMove() {
			// check north
			if (curY - 1 >= 0)
				if (m.maze[curX][curY - 1] == 5)
					return 1;
			// check east
			if (curX + 1 <= m.xSize -1 )
				if (m.maze[curX + 1][curY] == 5)
					return 2;
			// check south
			if (curY + 1 <= m.ySize - 1)
				if (m.maze[curX][curY + 1] == 5)
					return 3;
			// check west
			if (curX - 1 >= 0)
				if (m.maze[curX - 1][curY] == 5)
					return 4;
			// You can't win this move, return 0
			return 0;
		}
	
	// finds the first available move, going clockwise from the north.
	// We use the numbers 1-4 to represent the 4 compass directions and 0 to represent
	// no available move
	public int checkAvailableMove() {
		// north
		if (curY - 1 >= 0)
			if (m.maze[curX][curY - 1] == 1)
				return 1;
		// east
		if (curX + 1 <= m.xSize -1 )
			if (m.maze[curX + 1][curY] == 1)
				return 2;
		// south
		if (curY + 1 <= m.ySize - 1)
			if (m.maze[curX][curY + 1] == 1)
				return 3;
		// west
		if (curX - 1 >= 0)
			if (m.maze[curX - 1][curY] == 1)
				return 4;
		// if no move is available, return 0
		return 0;
	}

	// Prints the current status of the maze. It shows the maze and also where the cursor
	// currently is. The cursor is represented with a C.
	public void printStatus() {
		for (int i = 0; i < m.ySize; i++) {
			for (int j = 0; j < m.xSize; j++) {
				if (j == curX && i == curY)
					System.out.print("C ");
				else System.out.print(m.maze[j][i] + " ");
			}
			System.out.println();
		}
	}
	
	// Resets the maze. Mostly useful for if you are manually moving around the cursor.
	public void reset() {
		curX = m.startX;
		curY = m.startY;
		for (int i = 0; i < m.ySize; i++) {
			for (int j = 0; j < m.xSize; j++) {
				if (m.maze[j][i] == 2)
					m.maze[j][i] = 1;			
			}
		}
		prevPos.clear();
	}
	
	// Prints out the path stack from top to bottom so you can follow it backward.
	public void printPathInReverse() {
		for (int i = 0; i < prevPos.size(); i++) {
			System.out.println(prevPos.elementAt(i)[0] + ", " + prevPos.elementAt(i)[1]);
		}
	}
	
	/*
	 * This is the actual solver function. See the blurb above to see how the algorithm
	 * works. The verbosity variable toggles
	 */
	public void solve(boolean verbosity) {
		
		// variable that stores the direction code provided by the move checker
		int curMove;
		
		// flags to indicate if a move was found, the puzzle is uncompleteable, or if
		// the end is found
		boolean moved = false;
		boolean failed = false;
		boolean won = false;
		
		// a counter to keep track of moves.
		int moveCounter = 0;
		while (!won) {
			if (verbosity == true) {
				System.out.println("Move " + moveCounter + ":");
				System.out.println("Current location: " + curX + ", " + curY);
				// what the flags were set to last turn
				System.out.println("\nFlag status:\nmoved: " + moved + "\nfailed: " + failed + "\nwon: " + won + "\n");
				printStatus();
			}
			
			moved = false;
			
			// see if we can win this round. Usually will return 0 except beside exit
			curMove = checkWinningMove();
			if (curMove > 0) {
				won = true;
				moved = true;
				if (curMove == 1)
					move(curX, curY - 1);
				if (curMove == 2)
					move(curX + 1, curY);
				if (curMove == 3)
					move(curX, curY + 1);
				if (curMove == 4)
					move(curX - 1, curY);
			}
			
			// see if a move to an unexplored square is available. Moves there if one is.
			curMove = checkAvailableMove();
			if (curMove > 0 && !won) {
				moved = true;
				if (curMove == 1)
					move(curX, curY - 1);
				else if (curMove == 2)
					move(curX + 1, curY);
				else if (curMove == 3)
					move(curX, curY + 1);
				else if (curMove == 4)
					move(curX - 1, curY);
			}
			
			// if no moves are available and no more backtracking is possible. Set up failstate
			if (!moved && prevPos.size() <= 1 && !won) {
				if (verbosity == true) {
					System.out.println("Path size: " + prevPos.size());
					System.out.println("Failed to find solution!");
					printStatus();
				}
				failed = true;
				won = true; // you didn't actually win. Sorry. This just exits the loops
			} 
			else if (!moved && !won) { // otherwise backtrack one square and continue, looking for an alternate route
				if (verbosity == true) {
					System.out.println("Path size: " + prevPos.size());
					printPathInReverse();
					printStatus();
				}
				goBack();
			}
			// move onto the next move! Yay!
			moveCounter++;
		}
		// print ending results!
		if (curX == m.finishX && curY == m.finishY) {
			System.out.println("Solved the maze in " + moveCounter + " moves!");
			System.out.println("Final result: ");
			printStatus();
		}
		else {
			System.out.println("The maze wasn't solved. Take a look and see why: ");
			printStatus();
		}
	}
	
	
	public static void main(String[] args) {
		// set the maze file name here
		String mazeName = "src/puzzles/maze.txt";
		if (args.length > 0)
			mazeName = args[0];
		MazeSolver m = new MazeSolver(mazeName);
		m.printStatus();
		m.solve(false);

	}
	
}
