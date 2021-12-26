import java.lang.Math;
import java.util.Arrays;


public class Board implements Comparable<Board>
{
  private int[][] tiles; // Array of tile values
  private int size;      // Side length
  /*public void testPrint()
  {
    System.out.println("ciao besughi");
  }*/

  public Board(int[][] _tiles)
  {
    tiles = _tiles;
    size = tiles.length;
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
        tiles[i][j]=Integer.parseInt(t[j*size+i]);
      }
    }
  }

  public String toString()
  {
    String result = "";
    for (int j=0;j<size;j++)
    {
      for (int i=0;i<size;i++)
      {
        result+=tiles[i][j] + " ";
      }
    }
    int resLen = result.length();
    result = result.substring(0, resLen-1);
    return result;
  }

  public boolean equals(Board b)
  {
    for (int i=0;i<size; i++) {
      if (!Arrays.equals(tiles[i], b.tiles[i])) return false;
    }
    return true;
  }

  private int vecDistance(int[] a, int[] b)
  {
    return Math.abs(b[0]-a[0])+Math.abs(b[1]-a[1]);
  }
  private int[] targetPos(int a)
  {
    if (a <= 0) return new int[] {size-1,size-1};
    int x = (a-1)%size;
    int y = Math.floorDiv(a-1, size);
    return new int[]{x,y};
  }

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

  public int compareTo(Board b)
  {
    return this.manatthan() - b.manatthan();
  }

  public Board[] getMoves()
  {
    //TODO

    return null;
  }
}