import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

class Sudoku {

	 int[][] sectors;															// sector array
	 Character[][] board =null;
	 SortedSet<Character> valueSequenceSet;										// value set of possible values
	 static int size;															//size of matrix
	 int n;																		
	 
	 public Sudoku(Character[][] matrix, SortedSet<Character> valueSequence) {
		 this.board=matrix;
		 size = matrix.length;
		 valueSequenceSet = new TreeSet<Character>();
		 valueSequenceSet.addAll(valueSequence);
		 sectordefiner();
	 }
	 /**
	  * Defines sectors for grid provided. 
	  */
	 private void sectordefiner() {
			int[][] sectors=new int[size][];
			this.n = (int)Math.sqrt((double)size);
			int sizeCounter=0;
			
			for (int i = 0; i < size; i+=n) {
				
				for(int j=0; j<size; j+=n) {
					int[] sectarray = new int[4];
					sectarray[0]= j;
					sectarray[1]= j+n;
					sectarray[2]= i;
					sectarray[3]= i+n;
					sectors[sizeCounter]=sectarray;
					sizeCounter++;
				}
					
			}
			this.sectors=sectors;
		}

	 /**
	  * Method to find the next empty cell in the matrix
	  * @return chain of row and column coordinate
	  */
	 public static LinkedList<Integer> nextCellFiller(Character[][] board) 
		{
			LinkedList<Integer> linkList = new LinkedList<Integer>();
			for(int posX=0; posX<size; posX++)
			{
				for(int posY=0; posY<size; posY++)
				{
					if(board[posX][posY]=='.')
					{
						linkList.add(posX);
						linkList.add(posY);
						return linkList;
					}
				}
			}	
			linkList.add(-1);
			linkList.add(-1);
			return linkList;
		}
	
	/**
	 * Checks validity of element as specific location
	 * @param linkedList - Chain of position coordinates
	 * @param element - Element to be validated
	 * @return True if element is found to be valid for the given location in grid
	 */
	public Boolean isValid(LinkedList<Integer> linkedList, Character element)
	{
		int i= linkedList.peekFirst();
		int j= linkedList.peekLast();
		int secTopX, secTopY;
		Boolean rowFlag=true;
		for (int k = 0; k < size; k++) {
			
			if(element.equals(board[i][k]))
			{
				rowFlag=false;
				break;
			}
		}
		
		if(rowFlag)
		{
			Boolean columnFlag=true;
			for (int k = 0; k < size; k++) {
				if(element.equals(board[k][j]))
				{
					columnFlag=false;
					break;
				}
			}
			
			if(columnFlag)
			{
				secTopX = n*(i/n);
				secTopY = n*(j/n);
				for(int p=secTopX ; p< secTopX+n; p++)
				{
					for(int q=secTopY; q < secTopY+n ; q++)
					{
						if(board[p][q].equals(element))
						{
							return false;
						}
					}
					
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to recursively find new elements to fill and make predictions on them
	 * @param linkedList
	 * @return operation success or failure.
	 */
	public Boolean solveSudoku(LinkedList<Integer> linkedList)
	{
		LinkedList<Integer> list = nextCellFiller(board);
		if(list.peekFirst()==-1)
			return true;
		
		Iterator<Character> iterator = valueSequenceSet.iterator();
		while (iterator.hasNext()) 
		{
			
			Character possibleElement = iterator.next();
			if(isValid(list, possibleElement))
			{
				HashMap<LinkedList<Integer>,Character> changes= makePredictions(list, possibleElement);
				if(solveSudoku(linkedList))
					return true;
				
				undoChanges(changes);
			}
		}
		return false;
	}
	/**
	 * Method to undo the changes made by makePredictions method 
	 * @param changes - Map of changes made until method invocation with coordinate values list as key.
	 */
	private void undoChanges(HashMap<LinkedList<Integer>, Character> changes) {
		
		Iterator<LinkedList<Integer>> iter = changes.keySet().iterator();
		while(iter.hasNext())
		{
			LinkedList<Integer> loc=iter.next();
			board[loc.peekFirst()][loc.peekLast()] ='.';
		}
	}

	/**
	 * Method to make further predictions from possible value consideration
	 * @param list - Linked List of position coordinates of value considered
	 * @param possibleElement - Argument Variable of element whose insertion is considered
	 * @return Map object for the changes made by the method with coordinates position linked list as key.
	 */
	private HashMap<LinkedList<Integer>,Character> makePredictions(LinkedList<Integer> list, Character possibleElement) {
		
		int i=list.peekFirst();
		int j= list.peekLast();
		board[i][j] = possibleElement;
		HashMap<LinkedList<Integer>, Character> changes = new HashMap<LinkedList<Integer>, Character>();
		changes.put(list, possibleElement);												//variable to track record of changes
		Boolean predictionCompleted = false;
		
		while(!predictionCompleted)														//iterate until last change took place
		{
			predictionCompleted = true;
			HashMap<LinkedList<Integer>, SortedSet<Character>> sectorInfo = new HashMap<LinkedList<Integer>, SortedSet<Character>>();	//variable to map grid positions with possible values set
			SortedSet<Character> completeSet =new TreeSet<Character>();
			for( int k=0; k< sectors.length ; k++)										//iterate across predefined sectors
			{
				sectorInfo = new HashMap<LinkedList<Integer>, SortedSet<Character>>();
				completeSet.addAll(valueSequenceSet);
				
					for(int row= sectors[k][0]; row< sectors[k][1]; row++ )				//logic to remove values already present in sector
					{
						for(int col= sectors[k][2]; col< sectors[k][3]; col++ )
						{
							if(!board[row][col].equals('.') &&  completeSet.contains(board[row][col]))
							{
								completeSet.remove(board[row][col]);
							}
						}
					}
					
					for(int row= sectors[k][0]; row< sectors[k][1]; row++ )				// logic to attach a copy of possible values to every element of sector
					{
						for(int col= sectors[k][2]; col< sectors[k][3]; col++ )
						{
							if( board[row][col].equals('.'))
							{
								LinkedList<Integer> posList= new LinkedList<Integer>();
								posList.add(row); 
								posList.add(col);
								sectorInfo.put(posList, completeSet);
							}
						}
					}
					
					Iterator<LinkedList<Integer>> iter= sectorInfo.keySet().iterator();
					SortedSet<Character> coordPossiblities =new TreeSet<Character>();
					SortedSet<Character> coordPossiblities2=new TreeSet<Character>();							//temporary variable for modifications
					while (iter.hasNext())																		//iterate through sector info gathered
					{
						LinkedList<Integer> coordinates = iter.next();
						coordPossiblities=sectorInfo.get(coordinates);
						coordPossiblities2.addAll(coordPossiblities);
						SortedSet<Character> rowSet= new TreeSet<Character>();
						SortedSet<Character> colSet= new TreeSet<Character>();
						
						for (int col=0; col<board.length; col++)											
						{
							rowSet.add(board[coordinates.peekFirst()][col]);									//Set to keep track of values available across row
						}
						coordPossiblities2.removeAll(rowSet);													
						for(int row=0; row<board.length; row++)
						{
							colSet.add(board[row][coordinates.peekLast()]);										//Set to keep track of values available across column
						}
						coordPossiblities2.removeAll(colSet);
						
						if(coordPossiblities2.size()==1)														//save the change if possible values for specific position is reduced to one value
						{
							Character value= coordPossiblities2.first();
							coordPossiblities2.remove(coordPossiblities2.first());
							if(isValid(coordinates, value))
							{
								board[coordinates.peekFirst()][coordinates.peekLast()] = value;
								changes.put(coordinates, value);
								predictionCompleted = false;
							}
						}
					}	
			}	
		}
		return changes;
	}
	/**
	 * Method to print solved Sudoku 
	 */
	public void printSudoku()
	{
		for(int i=0; i<board.length; i++)
		{
			for(int j=0; j<board.length;j++)
			{
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
}