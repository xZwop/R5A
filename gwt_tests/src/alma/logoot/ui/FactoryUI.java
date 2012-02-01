package alma.logoot.ui;

import alma.logoot.ui.gwt.R5A;

public class FactoryUI {

	private static IUI instance = null;

	public static IUI getInstance() {
		if (instance == null)
			instance = new R5A();
		return instance;
	}
}
