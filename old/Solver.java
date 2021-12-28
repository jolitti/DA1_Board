import java.util.Set;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

class Solver
{
	private static class Entry implements Comparable<Entry>
	{
		int key;
		Board value;

		Entry(int k, Board v) {key = k; value = v;}
		@Override
		public int compareTo(Entry o) {
			return key-o.key;
		}

	}
	public static void main(String[] args)
	{
		try {
		String filename = "puzzle.txt";
		if (args.length>0) filename = args[0];
		Scanner reader =  new Scanner(new File(filename));
		reader.nextLine(); // Discard side size, the constructor will deduce it
		Board startBoard = new Board(reader.nextLine());

		Set<String> visited = new HashSet<String>();
		PriorityQueue<Entry> queue = new PriorityQueue<>();
		
		queue.add(new Solver.Entry(startBoard.aStarDistance(),startBoard));
		visited.add(startBoard.toString());
		while (!queue.isEmpty())
		{
			Entry e = queue.remove();
			//System.out.println("Chosen board heuristic value: "+e.key);
			//System.out.println("Chosen board manhattan distance: "+e.value.manatthan());
			//System.out.println("Queue size: "+queue.size());
			/* for (Entry x : queue) 
			{
				System.out.print(x.key +" ");
			} */
			//System.out.println();
			Board state = e.value;
			if (state.manatthan()<=0)
			{
				ArrayList<String> prevStates = state.getPrevStates();
				System.out.println(prevStates.size());
				for (String s : prevStates) System.out.println(s);
				System.out.println(state);
				System.exit(0);
			}
			Board[] nextMoves = state.getMoves();
			//System.out.println("Number of next moves: "+nextMoves.length);
			for (Board s: nextMoves)
			{
				if (!visited.contains(s.toString()))
				{
					visited.add(s.toString());
					queue.add(new Entry(s.aStarDistance(),s));
				}
			}
		}
		}
		catch (FileNotFoundException exc)
		{
			System.out.println("Error: file not found. Aborting");
			System.exit(1);
		}
	}
}
