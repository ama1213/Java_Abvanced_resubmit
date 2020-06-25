package game;

/**
* カードマーク
*/
public enum Symbol {
	DIAMOND("ダイヤ"),
	SPADE("スペード");
	
	private String suit;
	
	/**
	* getter
	*/
	public String getSymbol() {
		return suit;
	}
	
	/**
	* コンストラクタ
	*/
	Symbol(String mark) {
		this.suit = mark;
	}
	
	static Symbol getSymbolValue(String mark) {
		for(Symbol z : Symbol.values()) {
			if(z.getSymbol().equals(mark)) {
				return z;
			}
		}
		return null;
	}
}