import java.util.LinkedList;
import java.util.List;

/**
 * Variants A and B
 */
class Task {

	public static final int minK = 1, maxK = 7;
	public static final int mult = 3;

	public static int nByK(int k) {
		return 3 * Math.round( (float) Math.pow(mult, k));
	}

	/* Input parameters */

	/** Number of nodes */
	public final int N;

	/** Step */
	public final double h;

	protected final double d0;

	protected final double U_a, U_b;

	protected final double k_a, k_b;

	/* Calculated once for given input */

	/** Grid */
	protected double x[];
	/** Values of initial function */
	protected double v[];

	protected final double A, C;

	/** Answer - approximated values */
	protected Series nodesA, nodesB;

	/* Possible functions */
	protected double p(double x) {
		//return x*x;
		 return Math.exp(x);
	}

	protected double g1(double x) {
		return Math.exp(x);
	}

	protected double g2(double x) {
		return Math.exp(-x);
	}

	/* Calculated. d1 = d1(V, boundary conditions) */
	protected double d1(double x) {
		//return -1.5*(-2*U_b+U_a/Math.E)/(-1/Math.E+4*Math.E);
		return 0.5*k_a*(-2*U_b+U_a/Math.E)/(-1/Math.E+4*Math.E);
	}

	protected double d2(double x) {
		return 0.5*k_b*(2*Math.E*U_a - 1*U_b) / (-1/Math.E + 4*Math.E);
		//return (6*Math.E*U_a - 3*U_b) / (-2/Math.E + 8*Math.E);
	}

	/** Precise answer function */
	protected double V(double x) {
		return d0*(0.5-x)*Math.sin(2*Math.PI*x)*Math.sin(2*Math.PI*x) + d1(x)*g1(x) + d2(x)*g2(x);
	}

	/** V'' */
	protected double ddV(double x) {
//		8 d0 \[Pi]^2 (0.5- x) Cos[2 \[Pi] x]^2 +
//		 0.0475953 \[ExponentialE]^x k_a (U_a/\[ExponentialE] - 2 U_b) +
//		 0.0475953 \[ExponentialE]^-x k_b (2 \[ExponentialE] U_a - U_b) -
//		 8 d0 \[Pi] Cos[2 \[Pi] x] Sin[2 \[Pi] x] -
//		 8 d0 \[Pi]^2 (0.5- x) Sin[2 \[Pi] x]^2
		// initial
		//return (-4*Math.PI*d0*Math.sin(4*Math.PI*x) + 8*Math.PI*Math.PI*d0*(0.5-x)*Math.cos(4*Math.PI*x) + d1(x)*g1(x) + d2(x)*g2(x));

		return 8*d0*Math.PI*Math.PI*(0.5-x)*Math.cos(2*Math.PI*x)*Math.cos(2*Math.PI*x)
			+ 0.0475953*Math.exp(x)*k_a*(U_a/Math.E - 2*U_b)
			+ 0.0475953*Math.exp(-x)*k_b*(2*Math.E*U_a - U_b)
			- 8*d0*Math.PI*Math.cos(2*Math.PI*x)*Math.sin(2*Math.PI*x)
			- 8*d0*Math.PI*Math.PI*(0.5-x)*Math.sin(2*Math.PI*x)*Math.sin(2*Math.PI*x);
	}

	/** Right side of differential equation */
	protected double f(double x) {
		return ddV(x) - p(x) * V(x);
	}

	/**
	 * @param N number of nodes
	 */
	public Task(int N, double d0, double U_a, double U_b, double k_a, double k_b) {
		this.N = N;
		this.d0 = d0;
		this.U_a = U_a;
		this.U_b = U_b;

		this.k_a = k_a;
		this.k_b = k_b;

		this.h = 1./N;

		A=1/(h*h);
		C=1/(h*h);

		x = new double[N+1];
		v = new double[N+1];

		/* Fills the grid */
		for(int i=0; i<=N; i++) {
			x[i] = h*i;
			v[i] = V(x[i]);
		}
	}

	/**
	 * @return Grid
	 */
	public double[] getX() {
		return x;
	}

	/**
	 * @return Initial function
	 */
	public Series getInitial() {
		Series series = new Series("Точное решение");
		for (int i = 0; i<N; i++)
			series.add(x[i], v[i]);
		series.incWidth();
		return series;
	}

	/**
	 * @return Approximated function, method A
	 */
	public Series getApproximatedA() {
		if (nodesA==null)
			genNodesA();
		return nodesA;
	}

	/**
	 * @return Approximated function, method A
	 */
	public Series getApproximatedB() {
		if (nodesB==null)
			genNodesB();
		return nodesB;
	}

	private Series array2Series(double[] array, String caption) {
		Series series = new Series(caption);
		for (int i=0; i<=N; i++)
			series.add(x[i], array[i]);
		return series;
	}

	private void genNodesA() {
		double u[] = new double[N+1];

		double P[] = new double[N+1];
		double Q[] = new double[N+1];

//		P[0] = 1 / (1+3*h); 			// depends on boundary conditions
//		Q[0] = 3*U_a*h / (1+3*h); 		// depends on boundary conditions

		P[0] = 1 / (1-k_a*h); 			// depends on boundary conditions
		Q[0] = -k_a*U_a*h / (1-k_a*h); 		// depends on boundary conditions

		for(int i=1; i<=(N-1); i++ ) {
			P[i] = -C / (A*P[i-1] + B(i));
			Q[i] = (G(i) - A*Q[i-1]) / (A*P[i-1] + B(i));
		}

		// depends on boundary conditions
//		u[N] = (3*h*U_b + Q[N-1]) / (1 + 3*h - P[N-1]);
		u[N] = (k_b*h*U_b + Q[N-1]) / (1 + k_b*h - P[N-1]);


		for(int i=N-1; i>=0; i-- )
			u[i] = P[i] * u[i+1] + Q[i];

		nodesA = array2Series(u, "Метод A (" + N + " узлов)");
	}

	private void genNodesB() {
		double u[] = new double[N+1];

		double P[] = new double[N+1];
		double Q[] = new double[N+1];

//		P[0] = (-A-C) / (B(0) - 6*A*h); 				// depends on boundary conditions
//		Q[0] = (G(0) - 6*A*h*U_a) / (B(0) - 6*A*h); 	// depends on boundary conditions
		P[0] = (-A-C) / (B(0) + 2*k_a*A*h); 				// depends on boundary conditions
		Q[0] = (G(0) + 2*k_a*A*h*U_a) / (B(0) + 2*k_a*A*h); 	// depends on boundary conditions


		for(int i=1; i<=(N-1); i++ ) {
			P[i] = -C / (A*P[i-1] + B(i));
			Q[i] = (G(i) - A*Q[i-1]) / (A*P[i-1] + B(i));
		}

		// depends on boundary conditions
//		u[N] = (G(N) - 6*C*h*U_b - C*Q[N-1] - A*Q[N-1] ) / (B(N) - 6*C*h + P[N-1]*(A+C));
		u[N] = (G(N) - 2*k_b*C*h*U_b - C*Q[N-1] - A*Q[N-1] ) / (B(N) - 2*k_b*C*h + P[N-1]*(A+C));

		for(int i=N-1; i>=0; i-- )
			u[i] = P[i] * u[i+1] + Q[i];

		nodesB = array2Series(u, "Метод B (" + N + " узлов)");
	}

	protected double B(int i) {
		return -2/(h*h) - p(x[i]);
	}

	protected double G(int i) {
		return f(x[i]);
	}

	public static List<Series> getAllA(double d0, double U_a, double U_b, double k_a, double k_b) {
		List<Series> list = new LinkedList<Series>();

		for (int k = minK; k < maxK; k++) {
			int N = nByK(k);
			Task task = new Task(N, d0, U_a, U_b, k_a, k_b);

			Series result = task.getApproximatedA();
			list.add(result);
		}

		return list;
	}

	public static List<Series> getAllB(double d0, double U_a, double U_b, double k_a, double k_b) {
		List<Series> list = new LinkedList<Series>();

		for (int k = minK; k < maxK; k++) {
			int N = nByK(k);
			Task task = new Task(N, d0, U_a, U_b, k_a, k_b);

			Series result = task.getApproximatedB();
			list.add(result);
		}

		return list;
	}

	public double getUA() {
		return U_a;
	}

	public double getUB() {
		return U_b;
	}

	public int getN() {
		return N;
	}
}
