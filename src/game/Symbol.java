package game;

public enum Symbol {
	DIAMOND("♦"),
	SPADE("♠");
	
	private String suit;
	
	public String getSymbol() {
		return suit;
	}
	
	Symbol(String mark) {
		this.suit = mark;
	}
	
//	static Symbol getSymbolValue(String mark) {
//		for(Symbol z : Symbol.values()) {
//			if(z.getSymbol().equals(mark)) {
//				return z;
//			}
//		}
//		return null;
//	}
}