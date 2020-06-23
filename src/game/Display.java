package game;

public class Display {

	protected enum DesplayMessage {
		GameStart(getLineSeparator() +
				"###第%d回戦###" + getLineSeparator() +
				"場に積まれた札：0枚" + getLineSeparator() +
				"[%s]の持ち札：%s枚,獲得した札：%s枚" + getLineSeparator() +
				"[%s]の持ち札：%s枚,獲得した札：%s枚" + getLineSeparator() +
				"札を切りますか？('%s':札を切る,'%s':中断) >"),
		GameStiuation("CPUが切った札 : %s" + getLineSeparator() +
				"あなたが切った札 : %s" + getLineSeparator()),
		GameWin("あなたが獲得しました！" + getLineSeparator() + getLineSeparator()),
		GameLose("CPUが獲得しました..." + getLineSeparator() + getLineSeparator()),
		Gamedrow("引き分けです。" + getLineSeparator() + getLineSeparator()),
		GameEnd("###最終結果###" + getLineSeparator() +
				"[%s]が獲得した札:%d枚%n" + getLineSeparator() +
				"[%s]が獲得した札:%d枚%n" + getLineSeparator()),
		WinEnd("あなたが勝ちました、おめでとう！"),
		LoseEnd("CPUが勝ちました、残念...");

		public final String message;

		private DesplayMessage(String message) {
			this.message = message;
		}

		public String format(Object... args) {
			return String.format(message, args);
		}

	}
	
	public static void println(String string) {
		System.out.println(string);
	}

	public static String getLineSeparator() {
		return System.getProperty("line.separator");
	}
	
	/**
	 * ゲーム開始
	 * @param player1 
	 */
	public void viewGameStart(int roundCount,Player player2, Player player1,String PLAY,String BREAK) {
		println(DesplayMessage.GameStart.format(
				roundCount,
				player2.getName(),
				player2.getHandNum(),
				player2.getGotNum(),
				player1.getName(),
				player1.getHandNum(),
				player1.getGotNum(),
				PLAY,BREAK
				));
	}
	
	

}