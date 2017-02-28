package puzzles;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * This class reads in a maze from a file.
 * 
 * The file must have the following format:
 * xdim ydim
 * sx sy
 * fx fy
 * *the maze itself. This should match your x and y dimensions with a single space between
 * each number. Use 0 to represent a wall and 1 to represent a valid point in which you can
 * move. In the case of this maze solver, moves can only be done vertically or horizontally,
 * not diagonally.*
 * 
 * an example of a 5x5 maze would be this:
 * 5 5
 * 0 0
 * 4 0
 * 1 0 0 0 1
 * 1 1 0 0 1
 * 0 1 0 0 1
 * 0 1 0 1 1
 * 0 1 1 1 0
 * 
 * for clarity: sx and sy are the x and y coordinates for the start point in the maze.
 * similarly, fx and fy are the x and y coordinates for the end point of the maze.
 * The coordinates are zero-indexed so a spot on the top row that is 3 columns over
 * would be 2 0.
 * 
 */

public class MazeReader {
	int xSize;
	int ySize;
	int[][] maze;
	int startX;
	int finishX;
	int startY;
	int finishY;
	
	public void readMaze(String fileName) {
		File f = new File(fileName);
		//System.out.println(f.getAbsolutePath()); // use this if you are having trouble finding your current directory.
		try {
			Scanner s = new Scanner(f);
			s = s.useDelimiter("\n"); // read in one line at a time
			String[] line;
			 line = s.nextLine().split(" ");
			 
			 // the first line read in will have the dimensions
			 xSize = Integer.parseInt(line[0]);
			 ySize = Integer.parseInt(line[1]);
			 
			 maze = new int[xSize][ySize];
			 // the next line read in will have the start point.
			 line = s.nextLine().split(" ");
			 startX = Integer.parseInt(line[0]);
			 startY = Integer.parseInt(line[1]);
			 		
			 // next up will be the end point.
			 line = s.nextLine().split(" ");
			 finishX = Integer.parseInt(line[0]);
			 finishY = Integer.parseInt(line[1]);
			 
			 int lineCounter = 0;
			 
			 // now we will copy in the maze to our 2D array line-by-line
			 while (s.hasNextLine()) {
				  line = s.nextLine().split(" ");
				  for (int i = 0; i < xSize; i++) {
					  maze[i][lineCounter] = Integer.parseInt(line[i]);
				  }
				  lineCounter++;
			 }
			 s.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Oh no!");
			e.printStackTrace();
		}
		
	}
	
}
