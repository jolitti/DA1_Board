import java.util.Set;
import java.util.HashSet;
import java.util.PriorityQueue;

class Solver
{
	public static void main(String[] args)
	{
		Set<String> visited = new HashSet<String>();
		PriorityQueue<Board> queue = new PriorityQueue<Board>();

		Board testBoard = new Board("1 2 3 4 5 6 7 8 9 0");
		Board testBoard2 = new Board("4 1 3 0 2 5 7 8 6");
		queue.add(testBoard2);
		queue.add(testBoard);

		System.out.println(queue.remove());
		System.out.println(queue.remove());
	}
}
