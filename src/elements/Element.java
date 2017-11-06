package elements;

import java.io.Serializable;
import java.util.ArrayList;

public class Element implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3380283582766931317L;
	protected int xPos;
	protected int yPos;

	protected String name;

	protected transient ArrayList<Integer[]> wallCoordinates = new ArrayList<>();

	public Element(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}

	public boolean isElement(int x, int y) {
		boolean col = false;
		for (Integer s[] : wallCoordinates) {
			if (x < s[0] + 50 && x + 50 > s[0] && y < s[1] + 50 && 50 + y > s[1]) {
				col = true;
			}
		}
		return col;

	}

	public ArrayList<Integer[]> getWallCoordinates() {
		return wallCoordinates;
	}

	public void setWallCoordinates(ArrayList<Integer[]> wallCoordinates) {
		this.wallCoordinates = wallCoordinates;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public void tick() {

	}

}
