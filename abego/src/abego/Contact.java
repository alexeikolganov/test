package abego;

public class Contact 
{
	public int id;
	public String name;
	public double x;
	public double y;
	public double lineY;
	public double lineX;
	public double leftLineX1;
	public double leftLineX2;
	public double rightLineX1;
	public double rightLineX2;
	public double topLineY1;
	public double topLineY2;
	public double bottomLineY1;
	public double bottomLineY2;
	
	public Contact( int id, String name )
	{
		this.id 		= id;
		this.name 		= name;
	}
	/**
	 * 						   topLineY1
	 * 				_______________|_______________
	 *              |	       topLineY2		  |
	 *              |							  |
	 * leftLineX1 --| leftLineX2      rightLineX1 |-- rightLineX2
	 *              |							  |
	 * 				|________bottomLineY1_________|
	 * 							   |
	 * 						 bottomLineY2
	 * @param x
	 * @param y
	 */
	public void setCoordinates( double x, double y )
	{
		this.x 			 	= x;
		this.y 			 	= y;
		this.lineX 		 	= this.x + UI.BLOCK_WIDTH / 2;
		this.lineY 		 	= this.y + UI.BLOCK_HEIGHT / 2;
		this.leftLineX1  	= this.x - UI.MARGIN / 2;
		this.leftLineX2  	= this.x;
		this.rightLineX1 	= this.x + UI.BLOCK_WIDTH;
		this.rightLineX2 	= this.x + UI.BLOCK_WIDTH + UI.MARGIN / 2;
		this.topLineY1   	= this.y - UI.MARGIN / 2;
		this.topLineY2   	= this.y;
		this.bottomLineY1   = this.y + UI.BLOCK_HEIGHT;
		this.bottomLineY2   = this.y + UI.BLOCK_HEIGHT + UI.MARGIN / 2;
	}

}
