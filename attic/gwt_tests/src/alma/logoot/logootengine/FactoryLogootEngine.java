package alma.logoot.logootengine;

public class FactoryLogootEngine {

	public static ILogootEngine getInstance() {
		return new LogootEngine();
	}
}