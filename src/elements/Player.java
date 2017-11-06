package elements;

public class Player extends Moving 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1322983344225969986L;
	private int lifes;
	protected transient int wishedMove = 0;
	private int points;
	
	public int getPoints() {
		return points;
	}


	public void getPoints(int points) {
		this.points = points;
	}


	public Player( String name, int xPos, int yPos) 
	{
		super(xPos,yPos);
		setLifes(3);
		this.name = name;
		setDirection( 1 );
	}
	

	public int getLifes() 
	{
		return lifes;
	}

	public void setLifes( int lifes ) 
	{
		this.lifes = lifes;
	}

	public int getWishedMove() 
	{
		return wishedMove;
	}

	public void setWishedMove( int wishedMove ) 
	{
		this.wishedMove = wishedMove;
	}

	public void removeLife() 
	{
		if ( getLifes() > 0 ) 
		{
			setLifes( getLifes() - 1 );
		}
	}
@Override
	public void tick() 
	{
		if ( canMove( getWishedMove() ) && getWishedMove() != 0 ) 
		{
			setDirection( getWishedMove() );
			setWishedMove( 0 );
		}

		if ( getxPos() >= 1200 ) 
		{
			setxPos( xPos -= 1200 );
		} 
		else if ( getxPos() < 0) 
		{
			setxPos( xPos += 1200 );
		}
		if ( getyPos() >= 695 ) 
		{
			setyPos( yPos -= 700 );
		} 
		else if ( getyPos() < 0 ) 
		{
			setyPos(yPos += 700);
		}
		if ( right == true && !canMove( 1 ) && wishedMove != getDirection() ) 
		{
			setWishedMove( 1 );
		}
		if ( right == true && canMove( 1 ) ) 
		{
			xPos += speed;
		}
		if ( left == true && !canMove( 2 ) && wishedMove != getDirection() ) 
		{
			setWishedMove( 2 );
		}
		if ( left == true && canMove( 2 ) ) 
		{
			xPos -= speed;
		}
		if ( up == true && !canMove( 3 ) && wishedMove != getDirection() ) 
		{
			setWishedMove( 3 );
		}
		if ( up == true && canMove( 3 ) ) 
		{
			yPos -= speed;
		}
		if ( down == true && !canMove( 4 ) && wishedMove != getDirection() ) 
		{
			setWishedMove( 4 );
		}
		if ( down == true && canMove( 4 ) ) 
		{
			yPos += speed;
		}

	}

}
