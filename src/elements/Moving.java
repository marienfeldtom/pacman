package elements;

import java.util.ArrayList;
import java.util.Arrays;

public class Moving extends Element {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1695885226804170253L;
	protected int speed = 5;
	public boolean right, left, down, up;
	private transient int xSpawn;
	private transient int ySpawn;

	public Moving(int xPos, int yPos) {
		super(xPos, yPos);
		setxSpawn(xPos);
		setySpawn(yPos);
		// TODO Auto-generated constructor stub
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public int getxSpawn() {
		return xSpawn;
	}

	public void setxSpawn(int xSpawn) {
		this.xSpawn = xSpawn;
	}

	public int getySpawn() {
		return ySpawn;
	}

	public void setySpawn(int ySpawn) {
		this.ySpawn = ySpawn;
	}

	public ArrayList<Integer> getOther() {
		ArrayList<Integer> directions = new ArrayList<Integer>();
		if (getDirection() == 1) {
			directions.addAll(Arrays.asList(3, 4));
		}
		if (getDirection() == 2) {
			directions.addAll(Arrays.asList(3, 4));
		}
		if (getDirection() == 3) {
			directions.addAll(Arrays.asList(2, 1));
		}
		if (getDirection() == 4) {
			directions.addAll(Arrays.asList(2, 1));
		}
		return directions;
	}

	public ArrayList<Integer> canMove(ArrayList<Integer> directions) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int a : directions) {
			if (canMove(a)) {
				list.add(a);
			}
		}
		return list;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return this.speed;
	}

	public boolean canMove(int direction) {
		if (direction == 1) {
			if (isElement(getxPos() + getSpeed(), getyPos()) == true) {
				return false;
			}
		}
		if (direction == 2) {
			if (isElement(getxPos() - getSpeed(), getyPos()) == true) {
				return false;
			}
		}
		if (direction == 3) {
			if (isElement(getxPos(), getyPos() - getSpeed()) == true) {
				return false;
			}
		}
		if (direction == 4) {
			if (isElement(getxPos(), getyPos() + getSpeed()) == true) {
				return false;
			}

		}
		return true;

	}

	public void setDirection(int direction) {
		clearDirection();
		if (direction == 1) {
			right = true;
		} else if (direction == 2) {
			left = true;
		} else if (direction == 3) {
			up = true;
		} else if (direction == 4) {
			down = true;
		}

	}

	public void clearDirection() {
		right = false;
		left = false;
		down = false;
		up = false;
	}

	public int getDirection() {
		if (this.right == true) {
			return 1;
		}
		if (this.left == true) {
			return 2;
		}
		if (this.up == true) {
			return 3;
		}
		if (this.down == true) {
			return 4;
		}
		return 0;
	}

}
