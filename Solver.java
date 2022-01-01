import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.ArrayList;

public class Solver {

    private class Entry implements Comparable<Entry>
    {
        public Board state;
        public Entry parent;
        public int step, key;

        public Entry(Board _state)
        {
            state = _state;
            step = 0;
            parent = null;
            key = state.manhattan() + step;
        }
        public Entry(Board _state, Entry _parent)
        {
            state = _state;
            parent = _parent;
            step = parent.step + 1;
            key = state.manhattan() + step;
        }
        public int compareTo(Entry e) {return key - e.key;}

        public ArrayList<String> descendanceLine()
        {
            ArrayList<String> ans = new ArrayList<>();
            Entry e = this;
            while (e != null)
            {
                ans.add(0, e.state.toString());
                e = e.parent;
            }

            return ans;
        }
    }

    private Board startBoard;
    private HashSet<String> visited;
    private PriorityQueue<Entry> queue;
    private Board target;

    private Solver(Board _startBoard)
    {
        startBoard = _startBoard;
        queue = new PriorityQueue<>();
        visited = new HashSet<>();
        target = new Board(startBoard.size());

        queue.add(new Entry(startBoard));
        visited.add(startBoard.toString());
    }

    private Entry solve()
    {
        while (queue.size()>0)
        {
            Entry nextEntry = queue.remove();
            //System.out.println(nextEntry.step);
            Board b = nextEntry.state;
            if (b.equals(target)) return nextEntry;

            for (Board move : b.nextMoves())
            {
                if (!visited.contains(move.toString()))
                {
                    queue.add(new Entry(move,nextEntry));
                    visited.add(move.toString());
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        String filename = "puzzles/puzzle.txt";
        if (args.length>0) filename = args[0];
        
        try
        {
            Scanner scan = new Scanner(new File(filename));
            scan.nextLine();
            Board initialState = new Board(scan.nextLine());
            Solver solver = new Solver(initialState);

            Entry e = solver.solve();
            System.out.println(e.step);

            for (String s : e.descendanceLine()) {
                System.out.println(s);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found!");
            System.exit(1);
        }
    }
}
