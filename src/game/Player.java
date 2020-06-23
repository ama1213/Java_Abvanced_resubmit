package game;

import java.util.List;

public class Player {
	private String name;
	private int gotNum,
	winCount;
	List<Card> hand;

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

	public void setGotNum(int gotNum) {
		this.gotNum = gotNum;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	public void judge(Player player1, Player player2,String fieldStockCnt,int CARD_PUT_OUT,int FIELD_STOCK_CNT,int ZERO,int fieldStockCount) {
		boolean player1Got = (player1.getHand().get(CARD_PUT_OUT).getStrength() > player2.getHand().get(CARD_PUT_OUT).getStrength());
		boolean player2Got = (player2.getHand().get(CARD_PUT_OUT).getStrength() > player1.getHand().get(CARD_PUT_OUT).getStrength());
		boolean draw = (player1.getHand().get(CARD_PUT_OUT).getStrength() == player2.getHand().get(CARD_PUT_OUT).getStrength());
		fieldStockCount += Integer.parseInt("fieldStockCnt");
		if(player1Got) {
			System.out.printf("[%s]が獲得しました！%n", player1.getName());
			player1.setGotNum(player1.getGotNum() + fieldStockCount);
			fieldStockCount = ZERO;
		}else if(player2Got) {
			System.out.printf("[%s]が獲得しました！%n", player2.getName());
			player2.setGotNum(player2.getGotNum() + fieldStockCount);
			fieldStockCount = ZERO;
		}else if(draw) {
			System.out.println("引き分けです。");
		}
	}

	Player(String name, int gotNum, List<Card> handPlayer) {
		this.name = name;
		this.gotNum = gotNum;
		this.hand = handPlayer;
	}

//	public String toBreakFormat() {
//		String toReturn = "";
//		for(int i = 0; i < hand.size();i++) {
//			toReturn += hand.get(i).toString();
//			if(i != hand.size() - 1) {
//				toReturn += ",";
//			}
//		}
//		return toReturn;
//	}
}
