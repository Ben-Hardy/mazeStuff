package puzzles;

public class Maze {
	
	// The dimensional sizes of the maze
	int xSize;
	int ySize;
	
	// the maze itself. In this case represented by a 2D array
	int[][] maze;
	
	// The start and finish points in the map.
	int startX;
	int startY;
	int finishX;
	int finishY;

	// Prints out the maze
	public void printMaze() {
		for (int i = 0; i < ySize; i++) {
			for (int j = 0; j < xSize; j++) {
				System.out.print(maze[j][i] + " ");
			}
			System.out.println();
		}
	}
	
	// Creates a new maze. Usually this will be done by reading in info from a file
	public void instantiateMaze(int[][] newMaze, int xDim, int yDim, int sx, int sy, int fx, int fy) {
		maze = new int[xDim][yDim];
		xSize = xDim;
		ySize = yDim;
		startX = sx;
		startY = sy;
		finishX = fx;
		finishY = fy;
		
		for (int i = 0; i < ySize; i++) {
			for (int j = 0; j < xSize; j++) {
				maze[j][i] = newMaze[j][i];
			}
		}
		
		// Mark the start and finish points differently so that they can be easily spotted
		maze[startX][startY] = 4;
		maze[finishX][finishY] = 5;
	}
	


}
