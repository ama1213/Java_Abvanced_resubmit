package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* デッキクラス
*/
public class Deck {
	private static final List<Card> protoDesk = new ArrayList<Card>();
	static {
		for (Symbol symbol : Symbol.values()) {
			for (Rank rank : Rank.values()) {
				protoDesk.add(new Card(symbol, rank));
			}
		}
	}

	/**
	 * トランプをシャッフルする
	 */
	public static void shuffle() {
		Collections.shuffle(null);
	}


	/**
	* 初期化したデッキを返す
	*/
	public static ArrayList<Card> newDeck() {
		return new ArrayList<Card>(protoDesk);
	}

	/**
	* カードを配る
	*/
	static ArrayList<Card> deal(List<Card> deck, int n) {
		int deckSize = deck.size();
		List<Card> handView = deck.subList(deckSize - n, deckSize);
		ArrayList<Card> hand = new ArrayList<Card>(handView);
		handView.clear();
		return hand;
	}
}
