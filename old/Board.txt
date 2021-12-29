import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;

/** Representation of the state of a sliding tile puzzle */
public class Board implements Comparable<Board>
{
  // Cartesian fundamental vectors
  private static final int[][] directions ={
    new int[]{1,0},
    new int[]{0,1},
    new int[]{-1,0},
    new int[]{0,-1}
  };

  private int[][] tiles; // Array of tile values
  private int size;      // Side length
  private int[] zeropos; // Position of empty tile (cached for speed)
  //private String stringprecomp; // String repr (cached for speed)
  private ArrayList<String> prevStates;

  private int[] findZero()
  {
    for (int j = 0;j<size;j++)
    {
      for (int i = 0;i<size;i++) if (tiles[j][i]==0) return new int[]{i,j};
    }
    throw new IllegalStateException("Board has no empty tile. Should be impossible");
  }

  public Board(int[][] _tiles)
  {
    tiles = _tiles;
    size = tiles.length;
    zeropos = findZero();
  }
  private Board(int[][] _tiles, int[] _zeropos, ArrayList<String> _prevStates)
  {
    tiles = _tiles;
    size = tiles.length;
    zeropos = _zeropos;
    prevStates = new ArrayList<String>(_prevStates);
  }
  public Board(String tilesSerial)
  {
    String[] t = tilesSerial.split(" ");
    size = (int) Math.floor(Math.sqrt(t.length));
    tiles = new int[size][size];

    for (int j=0;j<size;j++)
    {
      for (int i=0;i<size;i++)
      {
        tiles[j][i]=Integer.parseInt(t[j*size+i]);
      }
    }
    zeropos = findZero();
    prevStates = new ArrayList<String>();
  }

  /**
   * Get a serialized representation of the board state.
   * This can be used to generate a new Board object
   */
  public String toString()
  {
    String result = "";
    for (int j=0;j<size;j++)
    {
      for (int i=0;i<size;i++)
      {
        result+=tiles[j][i] + " ";
      }
    }
    int resLen = result.length();
    result = result.substring(0, resLen-1);
    return result;
  }
  public String toPrettyString()
  {
    String ans = "";
    for (int[] l: tiles)
    {
      for (int x : l) ans+=x;
      ans += "\n";
    }
    ans = ans.substring(0,ans.length()-1); // Discard last newline
    return ans;
  }
  private int[][] deepCopyTiles()
  {
    int ans[][] = new int[size][size];
    for (int j=0;j<size;j++)
    {
      for (int i=0;i<size;i++) ans[j][i] = tiles[j][i];
    }
    return ans;
  }

  public boolean equals(Board b)
  {
    for (int i=0;i<size; i++) {
      if (!Arrays.equals(tiles[i], b.tiles[i])) return false;
    }
    return true;
  }
  // Private methods for internal vector operations
  
  private boolean isValidPos(int[] p)
  {
    int x = p[0];
    int y = p[1];
    if ((x<0 || x>=size)||(y<0 || y>=size)) return false;
    else return true;
  }
  /**
   * Interrupt execution if any of the coords in arguments are invalid
   * @throws IllegalArgumentException if points are outside board
   * @param coords - List of points to validate
   */
  private void validateCoords(int... coords) throws IllegalArgumentException
  {
    for (int x : coords)
    {
      if (x < 0 || x >= size) throw new IllegalArgumentException("Invalid posion in board");
    }
  }
  private static int vecDistance(int[] a, int[] b)
  {
    return Math.abs(b[0]-a[0])+Math.abs(b[1]-a[1]);
  }
  private int[] targetPos(int a)
  {
    if (a <= 0) return new int[] {size-1,size-1};
    int y = (a-1)%size;
    int x = Math.floorDiv(a-1, size);
    return new int[]{x,y};
  }
  /**
   * Return new copy of this board with two tiles swapped (representing one move)
   * @param a - Array of two ints
   * @param b . Array of two ints
   */
  private Board swapTile(int[] a, int[] b)
  {
    int ax = a[0];int ay = a[1];int bx = b[0];int by = b[1];
    validateCoords(ax,ay,bx,by);
    int[] newZero = zeropos.clone();
    int[][] newTiles = deepCopyTiles();
    int temp = newTiles[ay][ax];
    newTiles[ay][ax] = newTiles[by][bx];
    newTiles[by][bx] = temp;
    if (newTiles[ay][ax] == 0) newZero = new int[]{ax,ay};
    else if (newTiles[by][bx] == 0) newZero = new int[]{bx,by};
    Board nBoard = new Board(newTiles,newZero,prevStates);
    nBoard.prevStates.add(this.toString());
    return nBoard;
  }
  /**
   * Element-wise addition of arrays of two ints
   * @param a - First addend
   * @param b - Second addend
   */
  private int[] addVec(int[] a,int[] b)
  {
    return new int[]{a[0]+b[0], a[1]+b[1]};
  }

  /** Sum of manhattan distances between
   *  current tile positions and their
   *  target positions
    */
  public int manatthan()
  {
    int sum = 0;
    for (int j=0;j<size;j++)
    {
      for (int i=0;i<size;i++)
      {
        int num = tiles[i][j];
        int[] target = targetPos(num);
        int dist = vecDistance(new int[]{i,j}, target);
        //System.out.println(num + " (" + target[0] + " " + target[1] + ") " + dist);
        if (num>0) sum += dist;
      }
    }
    return sum;
  }
  /**
   * How relatively close the two boards are
   * to being solved
   * @param b - The board to compare
   * @return < 0 if the first board is closer to solved
   */
  public int compareTo(Board b)
  {
    return (this.manatthan()) - (b.manatthan());
  }
  public int aStarDistance()
  {
    return manatthan()+prevStates.size();
  }

  /**
   * Get a list of all adjacent moves
   */
  public Board[] getMoves()
  {
    ArrayList<Board> ans = new ArrayList<Board>();
    for (int[] d : directions)
    {
      int[] swappos = addVec(zeropos, d);
      if (isValidPos(swappos)) ans.add(swapTile(swappos, zeropos));
    }
    Board[] ansarr = new Board[ans.size()];
    return ans.toArray(ansarr);
  }

  public ArrayList<String> getPrevStates()
  {
    return new ArrayList<String>(prevStates);
  }
}