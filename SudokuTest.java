import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class SudokuTest {

	public static void main(String[] args) {

		System.out.println("Enter size of grid: ");
		Scanner in= new Scanner(System.in);
		int size = in.nextInt();
		if(size<4)
		{
			System.out.println("Minimum soduku grid size must be of 2x2!");
			System.exit(0);
		}
		else
		{
			int n = (int)Math.sqrt(size);
			if(n*n !=size)
			{
				System.out.println("Provided sudoku size is not supported! Size must be from sequence 4,9,16,25...");
				System.exit(0);
			}
		}
		System.out.println("Enter possible grid values: ");
		String valueString = in.next();
		if(valueString.length()!=size)
		{
			System.out.println("Provided size doesn't matches with values provided.");
			System.exit(0);
		}
		
		SortedSet<Character> valueSet= new TreeSet<Character>();											//Variable to handle possible values set
		for (int pos = 0; pos < valueString.length(); pos++) {
			char ch= valueString.charAt(pos);
			if(!valueSet.contains(ch))
				valueSet.add(ch);
			else																						  	//To check all possible values are unique
			{
				System.out.println("Repetition of possible values!");
				System.exit(0);
			}
			
		}
		in.nextLine();
		System.out.println("Enter matrix line by line (or multiline): ");
		Character[][] matrix= new Character[size][size];
		
		List<String> tokens = new ArrayList<String>();												
		for(int pos=0; pos<size;pos++)
		{
			tokens.add(in.nextLine());																	//Take matrix input row by row for size given
		}
		
		if(tokens.size() < size)																		
		{
			System.out.println("Insufficient input provided! ");
		}
			
		for (int i = 0; i < size; i++)
        {
			String line= tokens.get(i);
			if(line.length()<size)
			{
				System.out.println("Insufficient characters in row "+ (i+1)+" of matrix!");
				System.exit(0);
			}
            for (int j = 0; j < size; j++)
            {
                if(!valueSet.contains(line.charAt(j)) && line.charAt(j)!='.')
                {
                	System.out.println("Matrix input out of the provided possible value set!");
                	System.exit(0);
                }
            	matrix[i][j] = line.charAt(j);
            }
        }
		in.close();
		Sudoku s=new Sudoku(matrix, valueSet);
		LinkedList<Integer> linkedList= new LinkedList<Integer>();
		if(s.solveSudoku(linkedList))																//calling Sudoku solver method
		s.printSudoku();
		else
		System.out.println("no solution");	
	}
}
