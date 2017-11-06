package elements;

import java.util.Random;

public class Enemy extends Moving {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1174621981304625824L;
	private boolean eatingMode;
	private String image;

	public Enemy(int xPos, int yPos, String image) {
		super(xPos, yPos);
		setDirection(randInt(1, 4));
		this.image=image;
	}

	

	public boolean isEatingMode() {
		return eatingMode;
	}
//	@Override
//	public int getSpeed() {
//		if(!isEatingMode()) {
//			return this.speed;
//		} else {
//			return this.speed-3;
//		}
//	}

	public void setEatingMode(boolean eatingMode) {
		this.eatingMode = eatingMode;
	}

	@Override
	public void tick() {
		if (canMove(getOther()).size() > 0) {
			int random = randInt(1, 2);
			if (random == 1) {
				int richtung = randInt(1, canMove(getOther()).size());
				int richtung2 = canMove(getOther()).get(richtung - 1);
				setDirection(richtung2);
			}
		}

		if (getxPos() >= 1200) {
			setxPos(xPos -= 1200);
		} else if (getxPos() < 0) {
			setxPos(xPos += 1200);
		}
		if (getyPos() >= 700) {
			setyPos(yPos -= 700);
		} else if (getyPos() < 0) {
			setyPos(yPos += 700);
		}

		if (canMove(getDirection())) {
			if (getDirection() == 1 && canMove(1)) {
				setxPos(getxPos() + getSpeed());
			} else if (getDirection() == 2 && canMove(2)) {
				setxPos(getxPos() - getSpeed());
			} else if (getDirection() == 3 && canMove(3)) {
				setyPos(getyPos() - getSpeed());
			} else if (getDirection() == 4 && canMove(4)) {
				setyPos(getyPos() + getSpeed());
			}
		}

	}

	public static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public String getImage() {
		if (!isEatingMode()) {
			return image;
		} else {
			return "data/ghosts/ghostm.png";
		}
	}

}
