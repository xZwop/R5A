package alma.logoot;

import java.math.BigInteger;

import alma.logoot.shared.LogootEngine;
import alma.logoot.shared.LogootIdContainer;
import alma.logoot.shared.LogootIdentifier;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("===== Test : Prefix =======================");
		LogootIdContainer id = new LogootIdContainer();
		id.add(new LogootIdentifier(15, 1, 1));
		id.add(new LogootIdentifier(25, 1, 2));
		System.out.println("LogootEngine.prefix(" + id + ", 1)\n\t"
				+ LogootEngine.prefix(id, 1));
		System.out.println("LogootEngine.prefix(" + id + ", 1)\n\t"
				+ LogootEngine.prefix(id, 2));

		System.out.println("===== Test : ConstructIdentifier ===========");
		LogootIdContainer p = new LogootIdContainer();
		LogootIdContainer q = new LogootIdContainer();

		p.add(new LogootIdentifier(19, 1, 1));
//		p.add(new LogootIdentifier(12, 1, 2));
//		q.add(new LogootIdentifier(40, 2, 1));
		q.add(new LogootIdentifier(20, 2, 2));

		LogootIdentifier rep = new LogootIdentifier(0, 3, 1);
		BigInteger r = new BigInteger("1945");

		System.out.println("p : " + p);
		System.out.println("q : " + q);
		System.out.println("r : " + r);
		System.out.println("rep : " + rep);
		System.out.println("LogootEngine.constructIdentifier(r, p, q, rep)\n\t"
				+ LogootEngine.constructIdentifier(r, p, q, rep));

		System.out.println("===== Test : GenerateLineIdentifier =========");
		System.out.println("LogootEngine.generateLineIdentier(p,  q, 1,rep)\n\t"
						+ LogootEngine.generateLineIdentier(p, q, 30, rep));

	}

}
