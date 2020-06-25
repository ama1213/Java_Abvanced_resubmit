package game;

/**
* 	カードクラス
*/
public class Card {
	private final Symbol symbol;
	private final Rank rank;
	
	/**
	* getter
	*/
	public String getSymbol() {
		return symbol.getSymbol();
	}
	
	public String getLabel() {
		return rank.getLabel();
	}
	
	public int getStrength() {
		return rank.getStrength();
	}
	
	/**
	* コンストラクタ
	*/
	public Card(Symbol symbol, Rank rank) {
		this.symbol = symbol;
		this.rank = rank;
	}
	
	public String toString() {
		return symbol.getSymbol() + "-" + rank.getLabel();
	}

}
