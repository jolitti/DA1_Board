class Solver
{
	public static void main(String[] args)
	{
		Board b = new Board("0 1 2 3");
		Board b2 = new Board("1 0 2 3");
		//b.testPrint();
		System.out.println(b.toString());
		System.out.println(b.equals(b));
		System.out.println(b.equals(b2));
	}
}
