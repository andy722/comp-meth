/**
 * Represents container for two elements.
 *
 * @param <T>
 */
public class Pair<T> {
	private final T x, y;

	public T getX() {
		return x;
	}

	public T getY() {
		return y;
	}

	public Pair(T x, T y) {
		this.x = x;
		this.y = y;
	}

	private String trimmed(T x) {
		String xStr = x.toString();
		int dot = xStr.indexOf(".");
		if(dot<=0)
			return xStr;

		String val1 = xStr.substring(dot+1);
		String val0 = xStr.substring(0, dot);
		return val0 + '.' + (val1.length() > 2 ? val1.substring(0, 2) : val1);
	}

	public String toString() {
		return "[" + trimmed(x) + ", " + trimmed(y) + "]";
	}
}
