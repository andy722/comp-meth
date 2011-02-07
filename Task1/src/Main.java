import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JTextField;

/**
 * Main form
 */
public class Main extends JFrame {

	private static final long serialVersionUID = -2194932245388775378L;

	private JPanel jContentPane = null;
	private JButton jButton = null;
	private JButton jButton2 = null;
	private JButton jButton3 = null;
	private JFileChooser jFileChooser = null;  //  @jve:decl-index=0:visual-constraint="342,7"

	private JTextField jTextFieldUB = null;

	private JTextField jTextFieldD0 = null;

	private JTextField jTextFieldUA = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JLabel jLabel2 = null;

	private JTextField jTextFieldN = null;

	private JLabel jLabel21 = null;

	private double d0, U_a, U_b, k_a, k_b;
	private int N;

	private JTextField jTextFieldKA = null;

	private JTextField jTextFieldKB = null;

	private JLabel jLabel3 = null;

	private JLabel jLabel4 = null;

	public Main() {
		super();
		initialize();
	}

	private void initialize() {
		this.setSize(289, 213);
		this.setContentPane(getJContentPane());
		this.setTitle("Решение задачи Коши");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setResizable(false);
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel4 = new JLabel();
			jLabel4.setBounds(new Rectangle(135, 165, 31, 16));
			jLabel4.setText("k_b");
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(135, 135, 31, 16));
			jLabel3.setText("k_a");
			jLabel21 = new JLabel();
			jLabel21.setBounds(new Rectangle(135, 105, 31, 15));
			jLabel21.setText("N");
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(135, 75, 31, 15));
			jLabel2.setText("d_0");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(135, 45, 31, 15));
			jLabel1.setText("U_b");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(135, 15, 32, 15));
			jLabel.setText("U_a");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getJButton2(), null);
			jContentPane.add(getJButton3(), null);
			jContentPane.add(getJTextFieldUB(), null);
			jContentPane.add(getJTextFieldD0(), null);
			jContentPane.add(getJTextFieldUA(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(jLabel2, null);
			jContentPane.add(getJTextFieldN(), null);
			jContentPane.add(jLabel21, null);
			jContentPane.add(getJTextFieldKA(), null);
			jContentPane.add(getJTextFieldKB(), null);

			jContentPane.add(jLabel3, null);
			jContentPane.add(jLabel4, null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(15, 15, 106, 29));
			jButton.setToolTipText("Графики обоих методов для данного разбиения");
			jButton.setText("Графики");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphK();
				}
			});
		}
		return jButton;
	}

	protected void graphK() {
		getInput();

		Task task = new Task(N, d0, U_a, U_b, k_a, k_b);

		Plot plot = new Plot(task);

		Series a = task.getApproximatedA();
		Series b = task.getApproximatedB();
		Series u = task.getInitial();

		u.setColor(Color.black);
		a.setColor(Color.green);
		b.setColor(Color.red);

		plot.add(u);
		plot.add(a);
		plot.add(b);

		plot.setVisible(true);
		plot.setSize(new Dimension(138, 144));
	}

	/**
	 * This method initializes jButton2
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setBounds(new Rectangle(15, 105, 106, 31));
			jButton2.setToolTipText("По каждому методу - графики для различных разбиений");
			jButton2.setText("Графики сходимости");
			jButton2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphAll();
				}
			});
		}
		return jButton2;
	}

	protected void getInput() throws NumberFormatException {
		N = Integer.parseInt(getJTextFieldN().getText());
		d0 = Double.parseDouble(getJTextFieldD0().getText());
		U_a = Double.parseDouble(getJTextFieldUA().getText());
		U_b = Double.parseDouble(getJTextFieldUB().getText());
		k_a = Double.parseDouble(getJTextFieldKA().getText());
		k_b = Double.parseDouble(getJTextFieldKB().getText());
	}

	/**
	 * For every method: graphs with increasing node number
	 */
	protected void graphAll() {
		Color[] colors = {
			Color.red,
			Color.blue,
			Color.green,
			Color.yellow,
			Color.pink,
			Color.cyan
		};

		getInput();

		Task task = new Task(Task.nByK(Task.maxK), d0, U_a, U_b, k_a, k_b);
		Plot plotA = new Plot(task);
		Plot plotB = new Plot(task);

		Series initial = task.getInitial();
		plotA.add(initial);
		plotB.add(initial);

		List<Series> variantsA = Task.getAllA(d0, U_a, U_b, k_a, k_b);
		List<Series> variantsB = Task.getAllB(d0, U_a, U_b, k_a, k_b);

		int i;

		i = 0;
		for (Series s : variantsA) {
			s.setColor(colors[i % colors.length]);
			plotA.add(s);
			i++;
		}

		i = 0;
		for (Series s : variantsB) {
			s.setColor(colors[i % colors.length]);
			plotB.add(s);
			i++;
		}

		plotA.show();
		plotB.show();
	}

	/**
	 * This method initializes jButton3
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton3() {
		if (jButton3 == null) {
			jButton3 = new JButton();
			jButton3.setBounds(new Rectangle(15, 60, 106, 31));
			jButton3.setToolTipText("Генерация отчета для всех разбиений и методов");
			jButton3.setText("Отчет");
			jButton3.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					report();
				}
			});
		}
		return jButton3;
	}

	private JFileChooser getJFileChooser() {
		if (jFileChooser==null) {
			jFileChooser = new JFileChooser();
			jFileChooser.setCurrentDirectory(new File("/home/hog/progs/vych"));
			jFileChooser.setName("Report");
		}
		return jFileChooser;
	}

	/**
	 * Generates report
	 * @throws FileNotFoundException
	 */
	protected void report() {
		try {
			getInput();
		} catch (Exception e) {
			System.err.println(e);
			return;
		}

		try {
			if (getJFileChooser().showDialog(this.getContentPane(), "Сохранение результатов") == JFileChooser.APPROVE_OPTION ) {
				File report = getJFileChooser().getSelectedFile();
				PrintStream out = new PrintStream(report);
				report(out);
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void report(PrintStream out) {
		reportBlockConvergence(out);
	}

	private double errorA(Task task) {
		Series initial = task.getInitial();
		Series method = task.getApproximatedA();

		double e = 0;
		for (int i = 0; i < task.getN(); i++)
			e = ( e < Math.abs(initial.asList().get(i).getY() - method.asList().get(i).getY()) ) ? Math.abs(initial.asList().get(i).getY() - method.asList().get(i).getY()) : e;

		return e;
	}

	private double errorB(Task task) {
		Series initial = task.getInitial();
		Series method = task.getApproximatedB();

		double e = 0;
		for (int i = 0; i < task.getN(); i++)
			e = ( e < Math.abs(initial.asList().get(i).getY() - method.asList().get(i).getY()) ) ? Math.abs(initial.asList().get(i).getY() - method.asList().get(i).getY()) : e;

		return e;
	}

	private void reportBlockConvergence(PrintStream out) {
		out.println("Точность методов");
		out.println();

		int N;
		double error;

		DecimalFormat ddf = new DecimalFormat();
		ddf.setMinimumFractionDigits(6);
		ddf.setMaximumFractionDigits(6);

		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(6);
		df.setMaximumFractionDigits(6);

		out.println("N\t\tError in A\tconvergence\t\tError in B\tconvergence");

		for (int k = Task.minK; k < Task.maxK; k++) {
			N = Task.nByK(k);
			Task task = new Task(N, d0, U_a, U_b, k_a, k_b);

			out.print(N + "\t\t");

			error = errorA(task);
			out.print(ddf.format(error) + "\t");
			if (k!=Task.minK) {
				Task prev = new Task(Task.nByK(k-1), d0, U_a, U_b, k_a, k_b);
				double prevError = errorA(prev);

				double q = Math.log( prevError/error) / Math.log(Task.mult);
				out.print(Math.round(q) + " (" + df.format(q) + ")");
			} else {
				out.print("?\t");
			}

			out.print("\t\t");

			error = errorB(task);
			out.print(ddf.format(error) + "\t");
			if (k!=Task.minK) {
				Task prev = new Task(Task.nByK(k-1), d0, U_a, U_b, k_a, k_b);
				double prevError = errorB(prev);

				double q = Math.log( prevError/error) / Math.log(Task.mult);
				out.print(Math.round(q) + " (" + df.format(q) + ")");
			} else {
				out.print("?");
			}

			out.println();
		}
	}

	/**
	 * This method initializes jTextFieldUB
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldUB() {
		if (jTextFieldUB == null) {
			jTextFieldUB = new JTextField();
			jTextFieldUB.setBounds(new Rectangle(180, 45, 91, 19));
			jTextFieldUB.setText("1");
		}
		return jTextFieldUB;
	}

	/**
	 * This method initializes jTextFieldD0
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldD0() {
		if (jTextFieldD0 == null) {
			jTextFieldD0 = new JTextField();
			jTextFieldD0.setBounds(new Rectangle(180, 75, 91, 19));
			jTextFieldD0.setText("1");
		}
		return jTextFieldD0;
	}

	/**
	 * This method initializes jTextFieldUA
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldUA() {
		if (jTextFieldUA == null) {
			jTextFieldUA = new JTextField();
			jTextFieldUA.setBounds(new Rectangle(180, 15, 91, 19));
			jTextFieldUA.setText("1");
		}
		return jTextFieldUA;
	}

	/**
	 * This method initializes jTextFieldN
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldN() {
		if (jTextFieldN == null) {
			jTextFieldN = new JTextField();
			jTextFieldN.setBounds(new Rectangle(180, 105, 91, 19));
			jTextFieldN.setText("12");
			jTextFieldN.setPreferredSize(new Dimension(12, 19));
		}
		return jTextFieldN;
	}

	/**
	 * This method initializes jTextFieldKA
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldKA() {
		if (jTextFieldKA == null) {
			jTextFieldKA = new JTextField();
			jTextFieldKA.setBounds(new Rectangle(180, 135, 91, 16));
		}
		return jTextFieldKA;
	}

	/**
	 * This method initializes jTextFieldKB
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldKB() {
		if (jTextFieldKB == null) {
			jTextFieldKB = new JTextField();
			jTextFieldKB.setBounds(new Rectangle(180, 165, 91, 16));
		}
		return jTextFieldKB;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
