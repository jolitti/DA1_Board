public class Board
{
  private int[][] tiles; // Tiles
  private int size;      // Side length
  /*public void testPrint()
  {
    System.out.println("ciao besughi");
  }*/

  public Board(int[][] _tiles)
  {
    tiles = _tiles;
  }
  public Board(String tilesSerial)
  {
    
  }

  public String toString()
  {
    String result = "";
    for (int j=0;j<size;j++)
    {
      for (int i=0;i<size;i++)
      {
        result+=tiles[j][i];
      }
    }
    return result;
  }
}
