package game;

import java.util.List;

/**
* プレイヤークラス
*/
public class Player {
	private String name;
	private int gotNum,
	winCount;
	List<Card> hand;

	/**
	* getter
	*/
	public String getName() {
		return this.name;
	}

	public int getGotNum() {
		return this.gotNum;
	}

	public int getWinCount() {
		return this.winCount;
	}

	public List<Card> getHand() {
		return this.hand;
	}

	public int getHandNum() {
		return this.hand.size();
	}

	/**
	* setter
	*/
	public void setGotNum(int gotNum) {
		this.gotNum = gotNum;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	/**
	* コンストラクタ
	*/
	public Player(String name, int gotNum, List<Card> handPlayer) {
		this.name = name;
		this.gotNum = gotNum;
		this.hand = handPlayer;
	}

	public String toBreakFormat() {
		String toReturn = "";
		for(int i = 0; i < hand.size();i++) {
			toReturn += hand.get(i).toString();
			if(i != hand.size() - 1) {
				toReturn += ",";
			}
		}
		return toReturn;
	}
}
