import java.util.LinkedList;
import java.util.List;

/**
 * 11 variant
 *
 */
class Task {
	public static final double xmin = 0.1;
	public static final double xmax = 1;

	final double p = 1;

	public final static int minK = 0;
	public final static int maxK = 5;

	public final double h;

	/** Number of nodes */
	public final int N;

	protected double g(double x) {
		return 1 + Math.sqrt(x);
	}

	protected double dg(double x) {
		return 1 / (2*Math.sqrt(x));
	}

	protected double f(double x, double y) {
		return dg(x) + p * (y - g(x));
	}

	/**
	 *
	 * @param N number of nodes
	 */
	public Task(int k) {
		this.N =  3 * ( (int)Math.round(Math.pow(3, k)) );
		this.h = (xmax-xmin)/(N-1);
	}

	/**
	 * Method a
	 * @return y_{j+1} = y_{j+1}(y_j)
	 */
	public double m1(double x_j, double y_j) {
		return y_j + h * f(x_j, y_j);
	}

	/**
	 * Method c
	 * @return y_{j+1} = y_{j+1}(y_j)
	 */
	public double m2(double x_j, double y_j) {
		double k1 = h * f(x_j, y_j);
		double k2 = h * f(x_j + h, y_j + k1);
		return y_j + (k1 + k2)/2;
	}

	/**
	 * Method f
	 * @return y_{j+1} = y_{j+1}(y_j)
	 */
	public double m3(double x_j, double y_j) {
		double k1 = h * f(x_j, y_j);
		double k2 = h * f(x_j + h/2, y_j + k1/2);
		double k3 = h * f(x_j + h/2, y_j + k2/2);
		double k4 = h * f(x_j + h, y_j + k3);

		return y_j + (k1 + 2*k2 + 2*k3 + k4)/6;
	}

	public double m(int methodIndex, double x_j, double y_j) throws UnsupportedOperationException
	{
		if (methodIndex==1)
			return m1(x_j, y_j);
		else if (methodIndex==2)
			return m2(x_j, y_j);
		else if (methodIndex==3)
			return m3(x_j, y_j);
		else
			throw new UnsupportedOperationException();
	}

	public double g0() {
		return g(xmin);
	}

}
