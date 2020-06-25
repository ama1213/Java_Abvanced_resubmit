package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class WarGame {
	/**
	 * 改行
	 */
	public static final String NEW_LINE = System.lineSeparator();
	//ファイルの場所
	private static final String RESULT_FILE_NAME = "wargame_result.csv";
	private static final String RESULT_FILE = System.getProperty("user.home") + "/Desktop/" + RESULT_FILE_NAME;
	private static final String BREAK_FILE_NAME = "wargame_break.csv";
	private static final String BREAK_FILE = System.getProperty("user.home") + "/Desktop/" + BREAK_FILE_NAME;
	/**
	 * 定数
	 */
	static final int NUM_OF_PLAYER = 2, //プレイヤー数
			ZERO = 0, //0入力用
			CARD_PUT_OUT = 0, //手札から出すカードの位置
			INITIAL_VALUE = 0; //初期化用
	static final String PLAY = "d",
			BREAK = "q",
			YES = "y",
			NO = "n",
			GAME_CNT = "ゲーム数", //最後まで完了したゲーム数
			WIN_CNT = "勝利数", //プレイヤー１がゲームに勝利した数
			MAX_GOT_CNT = "最大獲得カード数", //プレイヤー１が１ゲームに獲得した最大枚数
			FIELD_STOCK_CNT = "場の札数", //場に溜まった札の数
			ROUND_CNT = "ラウンド数", //１ゲーム13ラウンド
			READED = "読込済み", //中断データ用
			UNREADED = "未読込"; //中断データ用
	/**
	 * 変数
	 */
	static int gameCount = 0, //最後まで完了したゲーム数
			winCount = 0, //プレイヤー１がゲームに勝利した数
			maxGotNum = 0, //最大獲得カード数
			fieldStockCount = 0, //場に積まれた札数
			roundCount = 1, //ラウンド数 1からスタート
			gotNumPlayer1 = 0, //中断データ用 プレーヤー1が獲得した数
			gotNumPlayer2 = 0; //中断データ用 プレーヤー2が獲得した数
	static boolean breakFileReaded = true;
	static Charset shiftjis = Charset.forName("Shift_JIS");
	static Scanner scanner = new Scanner(System.in);
	static List<Card> handPlayer1 = new ArrayList<Card>(); //プレイヤー１の手札作成
	static List<Card> handPlayer2 = new ArrayList<Card>(); //プレイヤー２の手札作成
	static List<String> resultDataHeader = new ArrayList<String>(); //ゲーム結果データのヘッダー
	static List<String> resultDataContents = new ArrayList<String>(); //ゲーム結果データの内容
	static List<String> breakDataHeader = new ArrayList<String>(); //中断データのヘッダー
	static List<String> breakDataContents = new ArrayList<String>(); //中断データの内容

	public static void main(String[] args) {
		/**
		* 定数
		*/
		final String NAME_PLAYER_1 = "あなた",
				NAME_PLAYER_2 = "CPU";
		final int COUNT_PER_GAME = 13; //1ゲームあたり13ラウンド(13回戦)

		/**
		* プログラム記述　ここから↓
		*/
		//ゲーム結果CSV読み込み処理
		readResult();
		//中断データCSV読み込み、ゲーム再開か新規ゲーム
		choiceRestart();
		//プレーヤー作成
		Player player1 = new Player(NAME_PLAYER_1, gotNumPlayer1, handPlayer1);
		Player player2 = new Player(NAME_PLAYER_2, gotNumPlayer2, handPlayer2);
		//ゲームのメイン処理	ラウンド13以内でループ
		while (roundCount <= COUNT_PER_GAME) {
			//ゲームのメイン文字列の表示
			printLines(player1, player2);
			//出したカードの勝敗判定
			judge(player1, player2);
			//出したカードを手札から削除
			player1.getHand().remove(CARD_PUT_OUT);
			player2.getHand().remove(CARD_PUT_OUT);
			roundCount++;
		}
		//ゲーム最終結果判定・表示
		result(player1, player2);
		//ゲーム結果CSV書き込み
		writeResult();
	}

	/**
	* プログラム記述　ここまで↑
	*/

	/**
	* メソッドメンバゾーン　ここから↓
	*/
	//ゲーム結果CSV読み込み
	static void readResult() {
		File gameResult = new File(RESULT_FILE);
		if (gameResult.exists()) {
			System.out.println(RESULT_FILE_NAME + "が見つかりました！読み込みます！");
			try (
					BufferedReader readerResult = Files.newBufferedReader(Paths.get(RESULT_FILE), shiftjis);) {
				for (int rowNum = 0; rowNum < 2; rowNum++) {
					String line = readerResult.readLine();
					if (rowNum == 1) {
						resultDataContents = Arrays.asList(line.split(","));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			gameCount = Integer.parseInt(resultDataContents.get(0));
			winCount = Integer.parseInt(resultDataContents.get(1));
			maxGotNum = Integer.parseInt(resultDataContents.get(2));
			System.out.printf("[%s]:%d,%n[%s]:%d,%n[%s]:%d%n",
					GAME_CNT, gameCount, WIN_CNT, winCount, MAX_GOT_CNT, maxGotNum);
		} else {
			System.out.println("game_result.csvが存在しません。");
		}
	}

	//中断データCSV読み込み、ゲーム再開か新規ゲームか
	static void choiceRestart() {
		File gameBreak = new File(BREAK_FILE);
		String choiceRestart;
		if (gameBreak.exists()) {
			try (
					BufferedReader reader = Files.newBufferedReader(Paths.get(BREAK_FILE), shiftjis);) {
				for (int rowNum = 0; rowNum < 5; rowNum++) {
					String line = reader.readLine();
					if (rowNum == 0) {
						breakDataHeader = Arrays.asList(line.split(","));
					} else if (rowNum == 1) {
						breakDataContents = Arrays.asList(line.split(","));
					} else if (rowNum == 2) {
						String[] readPlayer1Hand = line.split(",");
						for (int i = 1; i < readPlayer1Hand.length; i++) {
							String[] tmp1 = readPlayer1Hand[i].split("-");
							handPlayer1.add(new Card(Symbol.getSymbolValue(tmp1[0]), Rank.getRankValue(tmp1[1])));
						}
					} else if (rowNum == 3) {
						String[] readPlayer2Hand = line.split(",");
						for (int i = 1; i < readPlayer2Hand.length; i++) {
							String[] tmp2 = readPlayer2Hand[i].split("-");
							handPlayer2.add(new Card(Symbol.getSymbolValue(tmp2[0]), Rank.getRankValue(tmp2[1])));
						}
					} else if (rowNum == 4) {
						if (line == null) {
							breakFileReaded = false;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (breakFileReaded) {
				//中断データに「読込済み」の行があれば読み込まない
				System.out.print(NEW_LINE);
				System.out.println("中断データ（" + BREAK_FILE_NAME + ") は" + READED + "です");
				newGame();
			} else if (!breakFileReaded) {
				//中断データの読込
				System.out.println(NEW_LINE);
				System.out.println("中断データが見つかりました。");
				System.out.printf("中断したゲームを再開しますか？[%s / %s] >", YES, NO);
				INPUT_LOOP: while (true) {
					try {
						choiceRestart = scanner.next().toLowerCase();
						//YESorNO[y/n]のチェック
						checkChoiceYN(choiceRestart);
						break INPUT_LOOP;
					} catch (InputOutOfBoundException e) {
						System.out.println(e.getMessage());
					}
				}
				if (choiceRestart.equals(YES)) {
					resultDataHeader = breakDataHeader;
					gameCount = Integer.parseInt(breakDataContents.get(0));
					winCount = Integer.parseInt(breakDataContents.get(1));
					maxGotNum = Integer.parseInt(breakDataContents.get(2));
					fieldStockCount = Integer.parseInt(breakDataContents.get(3));
					roundCount = Integer.parseInt(breakDataContents.get(4));
					gotNumPlayer1 = Integer.parseInt(breakDataContents.get(5));
					gotNumPlayer2 = Integer.parseInt(breakDataContents.get(6));
					System.out.println("中断したゲームを再開します。");
				} else if (choiceRestart.equals(NO)) {
					newGame();
				}
			}
		} else {
			newGame();
		}
	}

	//新規ゲーム
	static void newGame() {
		System.out.println("新しくゲームを開始します。");
		List<Card> deck = Deck.newDeck();
		Collections.shuffle(deck);
		int cardsPerHand = deck.size() / NUM_OF_PLAYER; //配る手札の枚数決定
		handPlayer1 = Deck.deal(deck, cardsPerHand); //プレイヤー１の手札作成
		handPlayer2 = Deck.deal(deck, cardsPerHand); //プレイヤー2の手札作成
		resultDataHeader.add(0, GAME_CNT);
		resultDataHeader.add(1, WIN_CNT);
		resultDataHeader.add(2, MAX_GOT_CNT);
	}

	//ゲームのメイン文字列の表示
	static void printLines(Player player1, Player player2) {
		System.out.print(NEW_LINE);
		System.out.println("### 第" + roundCount + "回戦 ###");
		System.out.println("場に積まれた札:" + fieldStockCount + "枚");
		System.out.printf("[%s]持ち札: %s枚, 獲得札: %s枚%n",
				player2.getName(), player2.getHandNum(), player2.getGotNum());
		System.out.printf("[%s]持ち札: %s枚, 獲得札: %s枚%n",
				player1.getName(), player1.getHandNum(), player1.getGotNum());
		System.out.print(NEW_LINE);
		System.out.printf("札を切りますか？('%s':札を切る,'%s':中断) >", PLAY, BREAK);
		//ゲーム継続確認
		checkChoicePlayGame(player1, player2);
		System.out.printf("[%s]の札: [%s]%n", player2.getName(), player2.getHand().get(CARD_PUT_OUT));
		System.out.printf("[%s]の札: [%s]%n", player1.getName(), player1.getHand().get(CARD_PUT_OUT));
	}

	//ゲーム継続確認
	static void checkChoicePlayGame(Player player1, Player player2) {
		String choicePlayOrBreak;
		INPUT_LOOP: while (true) {
			try {
				choicePlayOrBreak = scanner.next().toLowerCase();
				//PLAYorBREAK[p/b]のチェック
				checkChoicePB(choicePlayOrBreak);
				break INPUT_LOOP;
			} catch (InputOutOfBoundException e) {
				System.out.println(e.getMessage());
			}
		}
		if (choicePlayOrBreak.equals(BREAK)) {
			//ゲームの中断処理
			System.out.println("ゲームを中断します");
			try (
					BufferedWriter outputBreak = Files.newBufferedWriter(Paths.get(BREAK_FILE), shiftjis);) {
				outputBreak.write(String.format("%s,%s,%s,%s,%s,%s,%s%n",
						GAME_CNT, WIN_CNT, MAX_GOT_CNT, FIELD_STOCK_CNT, ROUND_CNT, "獲得枚数p1", "獲得枚数p2"));
				outputBreak.write(String.format("%d,%d,%d,%d,%d,%d,%d%n",
						gameCount, winCount, maxGotNum, fieldStockCount, roundCount, player1.getGotNum(),
						player2.getGotNum()));
				outputBreak.write(player1.getName() + "," + player1.toBreakFormat() + NEW_LINE);
				outputBreak.write(player2.getName() + "," + player2.toBreakFormat() + NEW_LINE);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("中断データを保存しました。");
			System.out.print(NEW_LINE);
			System.out.println("END");
			System.exit(0);
		}
	}

	//出したカードの勝敗判定
	static void judge(Player player1, Player player2) {
		boolean player1Got = (player1.getHand().get(CARD_PUT_OUT).getStrength() > player2.getHand().get(CARD_PUT_OUT)
				.getStrength());
		boolean player2Got = (player2.getHand().get(CARD_PUT_OUT).getStrength() > player1.getHand().get(CARD_PUT_OUT)
				.getStrength());
		boolean draw = (player1.getHand().get(CARD_PUT_OUT).getStrength() == player2.getHand().get(CARD_PUT_OUT)
				.getStrength());
		fieldStockCount += NUM_OF_PLAYER;
		System.out.println(NEW_LINE);
		if (player1Got) {
			System.out.printf("[%s]が獲得しました！%n", player1.getName());
			player1.setGotNum(player1.getGotNum() + fieldStockCount);
			fieldStockCount = ZERO;
		} else if (player2Got) {
			System.out.printf("[%s]が獲得しました！%n", player2.getName());
			player2.setGotNum(player2.getGotNum() + fieldStockCount);
			fieldStockCount = ZERO;
		} else if (draw) {
			System.out.println("引き分けです。");
		}
	}

	//ゲーム最終結果判定・表示
	static void result(Player player1, Player player2) {
		boolean player1Win = (player1.getGotNum() > player2.getGotNum());
		boolean player2Win = (player2.getGotNum() > player1.getGotNum());
		System.out.print(NEW_LINE);
		System.out.println("### 最終結果 ###");
		System.out.printf("[%s]が獲得した札:%d枚%n", player2.getName(), player2.getGotNum());
		System.out.printf("[%s]が獲得した札:%d枚%n", player1.getName(), player1.getGotNum());
		System.out.print(NEW_LINE);
		if (player1Win) {
			System.out.printf("[%s]が勝ちました！おめでとう！%n", player1.getName());
			winCount++;
			if (player1.getGotNum() > maxGotNum) {
				maxGotNum = player1.getGotNum();
			}
		} else if (player2Win) {
			System.out.printf("[%s]が勝ちました！%n", player2.getName());
		}
	}

	//ゲーム結果CSV書き込み
	static void writeResult() {
		gameCount++;
		try (
				BufferedWriter outputResult = Files.newBufferedWriter(Paths.get(RESULT_FILE), shiftjis);) {
			outputResult.write(String.format("%s,%s,%s%n", GAME_CNT, WIN_CNT, MAX_GOT_CNT));
			outputResult.write(String.format("%d,%d,%d%n", gameCount, winCount, maxGotNum));
		} catch (IOException e) {
			e.printStackTrace();
		}
		File gameBreak = new File(BREAK_FILE);
		if (gameBreak.exists() && !breakFileReaded) {
			try (
					BufferedWriter outputBreak = new BufferedWriter(new FileWriter(BREAK_FILE, true));) {
				outputBreak.write(READED + NEW_LINE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.print(NEW_LINE);
		System.out.println("結果を保存しました。");
		System.out.printf("[%s]:%d,%n[%s]:%d,%n[%s]:%d%n",
				GAME_CNT, gameCount, WIN_CNT, winCount, MAX_GOT_CNT, maxGotNum);
		System.out.print(NEW_LINE);
		System.out.println("END");
	}

	/**
	* メソッドメンバゾーン		ここまで↑
	*/

	/**
	* 例外処理ゾーン		ここから↓
	*/
	//YESorNO[y/n]のチェック
	public static void checkChoiceYN(String input) throws InputOutOfBoundException {
		if (!input.equals(YES) && !input.equals(NO)) {
			throw new InputOutOfBoundException("'" + YES + "'か'" + NO + "'で入力してください");
		}
	}

	//PLAYorBREAK[p/b]のチェック
	public static void checkChoicePB(String input) throws InputOutOfBoundException {
		if (!input.equals(PLAY) && !input.equals(BREAK)) {
			throw new InputOutOfBoundException("'" + PLAY + "'か'" + BREAK + "'で入力してください");
		}
	}
}
/**
* 例外処理ゾーン		ここまで↑
*/
