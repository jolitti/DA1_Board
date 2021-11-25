import java.lang.Math;
import java.util.Arrays;

public class Board
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
    size = (int) Math.floor(Math.sqrt(tilesSerial.length()));
    tiles = new int[size][size];
    String[] t = tilesSerial.split(" ");

    for (int j=0;j<size;j++)
    {
      for (int i=0;i<size;i++)
      {
        tiles[j][i]=Integer.parseInt(t[j*size+i]);
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
        result+=tiles[j][i] + " ";
      }
    }
    return result;
  }

  public boolean equals(Board b)
  {
    for (int i=0;i<size; i++) {
      if (!Arrays.equals(tiles[i], b.tiles[i])) return false;
    }
    return true;
  }
}
