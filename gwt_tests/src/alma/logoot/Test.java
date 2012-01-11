package alma.logoot;

import alma.logoot.engine.LogootEngine;
import alma.logoot.engine.LogootIdContainer;
import alma.logoot.engine.LogootIdentifier;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LogootIdContainer id=new LogootIdContainer();
		id.add(new LogootIdentifier(15, 1, 1));
		id.add(new LogootIdentifier(5, 1, 2));
		System.out.println(LogootEngine.prefix(id, 1));
		System.out.println(LogootEngine.prefix(id, 2));
	}

}
