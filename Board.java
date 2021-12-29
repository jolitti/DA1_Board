import java.util.function.Consumer;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

// Immutable board state
public class Board {
    private static class Vector2
    {
        int x,y;
        public Vector2(int _x, int _y) {x=_x;y=_y;}
        public int manLength() {return Math.abs(x)+Math.abs(y);}
        public Vector2 sum(Vector2 v) {return new Vector2(x+v.x, y+v.y);}
        public Vector2 diff(Vector2 v) {return new Vector2(x-v.x, y-v.y);}
        public boolean inBounds(int l) {return y>=0 && y<l && x>=0 && x<l;}

        public String toString() {return "("+x+","+y+")";}
        public Vector2 copy() {return new Vector2(x, y);}
        public static Vector2 tileDefaultPos(int t, int size) 
        {
            if (t<=0) return new Vector2(size-1, size-1);
            t -= 1;
            int y = (int) Math.floorDiv(t,size);
            int x = (int) t%size;
            return new Vector2(x, y);
        }
        public static int manhattanCost(Vector2 pos, int t, int size)
        {return pos.diff(tileDefaultPos(t, size)).manLength();}
    }

    private static final Vector2[] dirs = {
        new Vector2(1, 0),
        new Vector2(0, 1),
        new Vector2(-1, 0),
        new Vector2(0, -1)
    };

    private int size; // Length of the side of the square
    private int[][] tiles; // Representation of the tiles in the state
    private int manhattan; // Precomputed manhattan distance
    private String repr; // Precomputed string representation
    private Vector2 zeropos; // Precomputed vector representing position of zero

    private int getTile(Vector2 pos) {return tiles[pos.y][pos.x];}
    private int setTile(Vector2 pos, int newVal) 
    {
        int oldVal = getTile(pos);
        tiles[pos.y][pos.x] = newVal;
        return oldVal;
    }

    /**
     * Evaluate expensive properties una tantum
     */
    private void precompute()
    {
        if (size<=0) size = tiles.length;

        if (repr==null || repr.length()<=0)
        {
            // Get repr
            var reprWrapper = new Object(){String value = "";};
            iterateOnTiles(t -> reprWrapper.value += t+" ");
            int len = reprWrapper.value.length();
            repr = reprWrapper.value.substring(0,len-1); // Cut the last space character
        }
        if (manhattan<=0)
        {
            // Get manhattan value
            var manWrapper = new Object(){Integer value = 0;};
            iterateOnPositions(p ->
            {
                int value = getTile(p);
                if (value >= 0)
                {
                    int partialCost = Vector2.manhattanCost(p, value, size);
                    manWrapper.value += partialCost;
                }
            });
            manhattan = manWrapper.value;
        }
        if (zeropos==null)
        {
            var posHolder = new Object(){Vector2 ans = null;};
            iterateOnPositions(p -> {
                if (getTile(p) == 0) 
                {
                    posHolder.ans = p;
                    return;
                }
            });
            zeropos = posHolder.ans;
            if (zeropos==null) throw new IllegalStateException("Board has no zero value");
        }
    }

    private int[][] tileDeepCopy()
    {
        int[][] newTiles = new int[size][size];
        iterateOnPositions(p ->
        {
            newTiles[p.y][p.x] = getTile(p);
        });
        return newTiles;
    }

    /**
     * Solved board state
     * @param _size - Size of the board
     */
    public Board(int _size)
    {
        size = _size;
        tiles = new int[size][size];
        // Default initialization
        var iterHolder = new Object(){Integer value = 1;};
        iterateOnPositions(p ->{
            if (iterHolder.value >= size*size) setTile(p, 0);
            else setTile(p, iterHolder.value);
            iterHolder.value+=1;
        });
        zeropos = new Vector2(size-1, size-1);
        precompute();
    }
    
    /**
     * Constructor as from the specifications
     * @param _tiles - Tiles object (not to be accessed by others)
     */
    public Board(int[][] _tiles)
    {
        this.tiles = _tiles;
        precompute();
    }
    /**
     * Constructor that saves the O(n^2) zeropos computation
     * @param _tiles - Int matrix
     * @param _zeropos - Precomputed position of the zero tile
     */
    public Board(int[][] _tiles, Vector2 _zeropos)
    {
        zeropos = _zeropos.copy();
        this.tiles = _tiles;
        repr = null;
        precompute();
    }
    
    /**
     * Builds board from the string representation of another board
     * @param seed - As would be output by toString()
     */
    public Board(String seed)
    {
        ArrayList<String> nums = new ArrayList<>(Arrays.asList(seed.split(" ")));
        size = (int) Math.floor(Math.sqrt(nums.size()));
        tiles = new int[size][size];
        Iterator<String> iter = nums.iterator();
        iterateOnPositions(p -> {
            setTile(p, Integer.parseInt(iter.next()));
        });
        precompute();
    }

    /**
     * Lambda operations on all tiles, in row-column order
     * @param action - Lambda function
     */
    private void iterateOnTiles(Consumer<? super Integer> action)
    {
        for (int j=0;j<size;j++) 
        {
            for (int i=0;i<size;i++) 
            {
                action.accept(getTile(new Vector2(i, j)));
            }
        }
    }
    /**
     * Lambda operation on all tiles positions, for functions
     * that need to know the position they're operating on
     * @param action - Function to apply to given vector
     */
    private void iterateOnPositions(Consumer<Vector2> action)
    {
        for (int j=0;j<size;j++) 
        {
            for (int i=0;i<size;i++) 
            {
                Vector2 position = new Vector2(i, j);
                action.accept(position);
            }
        }
    }

    public String toString() {return repr;}
    public int manhattan() {return manhattan;}

    public Board[] nextMoves()
    {
        ArrayList<Board> ans = new ArrayList<>();
        
        for (Vector2 dir : dirs)
        {
            Vector2 swapPos = dir.sum(zeropos);
            if (!swapPos.inBounds(size)) continue;
            int[][] newTiles = tileDeepCopy();
            Vector2 newZeropos = swapPos.copy();
            newTiles[zeropos.y][zeropos.x] = newTiles[swapPos.y][swapPos.x];
            newTiles[swapPos.y][swapPos.x] = 0;
            ans.add(new Board(newTiles,newZeropos));
        }

        Board[] arrayAns = ans.toArray(new Board[ans.size()]);
        return arrayAns;
    }

    public static void main(String[] args) {
        Board b = new Board("1 2 3 8 0 4 7 6 5");
        System.out.println(b.repr);
        for (Board move : b.nextMoves())
        {
            System.out.println(move.repr);
        }
    }
}
