package marketone;

import marketone.gui.MarketOneGUI;
import marketone.logic.MarketOneLogic;

/**
 * The main programme starts the application.
 * 
 * @author LIU Xiaofan
 * 
 */
public class MainProgram {

	private static MainProgram instance;

	public static MainProgram getInstance() {
		if (instance == null) {
			instance = new MainProgram();
		}
		return instance;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		getInstance().getGUI();
	}

	private MarketOneGUI marketOneGUI;

	private MarketOneLogic marketOneLogic;

	public MainProgram() {
		System.out.println("MarketOne starting...");
	}

	public void exit() {
		System.exit(0);
	}

	public MarketOneGUI getGUI() {
		if (marketOneGUI == null) {
			marketOneGUI = new MarketOneGUI();
			marketOneGUI.fetchData();
		}
		return marketOneGUI;
	}

	public MarketOneLogic getLogic() {
		if (marketOneLogic == null) {
			marketOneLogic = new MarketOneLogic();

		}
		return marketOneLogic;
	}
}
