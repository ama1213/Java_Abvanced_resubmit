package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WarGame {

	static final int NUM_OF_PLAYER = 2,
			ZERO = 0,
			CARD_PUT_OUT = 0,
			INITIAL_VALUE = 0;
	static final String PLAY = "d",
			BREAK = "q",
			YES = "y",
			NO = "n",
			GAME_CNT = "ゲーム数",
			WIN_CNT = "勝利数",
			MAX_GOT_CNT = "最大獲得カード数",
			FIELD_STOCK_CNT = "場の札数",
			ROUND_CNT = "ラウンド数",
			READED = "読込済み",
			UNREADED = "未読込";

	static int gameCount = 0,
			winCount = 0,
			maxGotNum = 0,
			fieldStockCount = 0,
			roundCount = 1,
			gotNumPlayer1 = 0,
			gotNumPlayer2 = 0;
	static boolean breakFileReaded = true;
	static Display display = new Display();
	static Scanner scanner = new Scanner(System.in);
	static List<Card> handPlayer1 = new ArrayList<Card>();
	static List<Card> handPlayer2 = new ArrayList<Card>();
	static List<String> resultDataHeader = new ArrayList<String>();
	static List<String> resultDateCountents = new ArrayList<String>();
	static List<String> breakDataHeader = new ArrayList<String>();
	static List<String> breakDataContents = new ArrayList<String>();

	public static void main(String[] args) {
		final String NAME_PLAYER_1 = "あなた",
				NAME_PLAYER_2 = "CPU";
		final int  COUNT_PER_GAME = 13;
//		readResult();
//		choiceRestart();
		Player player1 = new Player(NAME_PLAYER_1, gotNumPlayer1, handPlayer1);
		Player player2 = new Player(NAME_PLAYER_2, gotNumPlayer2, handPlayer2);
		while(roundCount <= COUNT_PER_GAME) {
			display.viewGameStart(roundCount,player1, player2, PLAY, BREAK);
			player2.judge(player1,
					player2,
					FIELD_STOCK_CNT,
					CARD_PUT_OUT,
					fieldStockCount,
					NUM_OF_PLAYER,
					ZERO
					);
//			result(player1, player2);
		}

	}
}
