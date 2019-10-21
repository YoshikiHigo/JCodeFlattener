package example;

public class OverrideTest1 {

	@Override
	public String toString() {
		return Double.toString(Math.PI * Math.E);
	}
}
