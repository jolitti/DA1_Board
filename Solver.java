class Solver
{
	public static void main(String[] args)
	{
		/*Board b = new Board("0 1 2 3");
		Board b2 = new Board("1 0 2 3");
		//b.testPrint();
		System.out.println(b.toString());
		System.out.println(b.equals(b));
		System.out.println(b.equals(b2));*/

		Board testBoard = new Board("8 1 3 4 0 2 7 6 5");
		Board testBoard2 = new Board("4 1 3 0 2 5 7 8 6");
		Board testBoard3 = new Board("1 0 3 4 2 5 7 8 6");
		Board testBoard4 = new Board("0 1 3 4 2 5 7 8 6");
		System.out.println(testBoard4.manatthan());

		for (String s : args) {
			System.out.println(s);
		}
	}
}
