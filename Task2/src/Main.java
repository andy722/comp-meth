import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.text.TabStop;

/**
 * Main window
 */
public class Main extends JFrame {

	private static final long serialVersionUID = -2194932245388775378L;

	private JPanel jContentPane = null;
	private JSlider jSlider = null;
	private JLabel jLabel = null;
	private JButton jButton = null;
	private JButton jButton2 = null;
	private JButton jButton3 = null;
	private JFileChooser jFileChooser = null;

	public Main() {
		super();
		initialize();
	}

	private void initialize() {
		this.setSize(300, 170);
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
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(15, 15, 46, 31));
			jLabel.setText("k = " + getJSlider().getValue());
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJSlider(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getJButton2(), null);
			jContentPane.add(getJButton3(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jSlider
	 *
	 * @return javax.swing.JSlider
	 */
	private JSlider getJSlider() {
		if (jSlider == null) {
			jSlider = new JSlider();
			jSlider.setBounds(new Rectangle(75, 15, 211, 32));
			jSlider.setPaintLabels(true);
			jSlider.setPaintTicks(true);
			jSlider.setSnapToTicks(true);
			jSlider.setToolTipText("k");
			jSlider.setMinimum(Task.minK);
			jSlider.setMaximum(Task.maxK);
			jSlider.setValue(1);
			jSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					jLabel.setText("k = " + getJSlider().getValue());
				}
			});
		}
		return jSlider;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(15, 60, 121, 29));
			jButton.setToolTipText("Все графики для данного разбиения");
			jButton.setText("График для k");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphK();
				}
			});
		}
		return jButton;
	}

	protected void graphK() {
		Plot plot = new Plot();

		Solver solver = new Solver(new Task(getJSlider().getValue()));
		plot.add(solver.initial());

		Series a = solver.solve(1);
		a.setColor(Color.red);
		plot.add(a);

		Series c = solver.solve(2);
		c.setColor(Color.green);
		plot.add(c);

		Series f = solver.solve(3);
		f.setColor(Color.blue);
		plot.add(f);

		plot.setVisible(true);
	}

	/**
	 * This method initializes jButton2
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setBounds(new Rectangle(15, 105, 271, 31));
			jButton2.setToolTipText("По каждому методу - графики для различных разбиений");
			jButton2.setText("График для всех k");
			jButton2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graphAll();
				}
			});
		}
		return jButton2;
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

		for (int methodIndex = 1; methodIndex <= 3; methodIndex++ ) {
			Plot plot = new Plot();
			for (int k = Task.minK; k <= Task.maxK; k++) {
				Solver solver = new Solver(new Task(k));

				Series s = solver.solve(methodIndex);
				s.setColor(colors[k-Task.minK]);
				plot.add(s);
				if (k==Task.maxK) {
					Series i = solver.initial();
					i.setColor(Color.black);
					plot.add(i);
				}
			}
			plot.show();
		}
	}

	/**
	 * This method initializes jButton3
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton3() {
		if (jButton3 == null) {
			jButton3 = new JButton();
			jButton3.setBounds(new Rectangle(165, 60, 121, 31));
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
		reportBlock1(out);
		reportBlock2(out);
	}

	private double error(Solver solver, int methodIndex) {
		Series initial = solver.initial();
		Series method = solver.solve(methodIndex);

		double e = 0;
		for (int i = 0; i < solver.getTask().N; i++)
			e = ( e < Math.abs(initial.asList().get(i).getY() - method.asList().get(i).getY()) ) ? Math.abs(initial.asList().get(i).getY() - method.asList().get(i).getY()) : e;

		return e;
	}

	private void reportBlock2(PrintStream out) {
		out.println("Точность методов");
		out.println();

		out.println("Метод 1");
		out.println("k \t число узлов \t диаметр \t погрешность \t C1 \t Q");
		for (int k = Task.minK; k < Task.maxK; k++) {
			Task task = new Task(k);
			Solver solver = new Solver(task);
			out.print(k + "\t" + task.N + "\t" + task.h + "\t");

			double e = error(solver, 1);
			double q = Math.log( e/error(new Solver(new Task(k+1)), 1)) / Math.log(3);
			double c1 = error(solver, 1) / Math.pow(task.h, Math.round(q));

			out.print(e + "\t" + c1 + "\t" + q + " (" + Math.round(q) + ")");
			out.println();
		}

		out.println("\nМетод 2");
		out.println("k \t число узлов \t диаметр \t погрешность \t C1 \t Q");
		for (int k = Task.minK; k < Task.maxK; k++) {
			Task task = new Task(k);
			Solver solver = new Solver(task);
			out.print(k + "\t" + task.N + "\t" + task.h + "\t");

			double e = error(solver, 2);
			double q = Math.log( e/error(new Solver(new Task(k+1)), 2)) / Math.log(3);
			double c1 = error(solver, 1) / Math.pow(task.h, Math.round(q));

			out.print(e + "\t" + c1 + "\t" + q + " (" + Math.round(q) + ")");
			out.println();
		}

		out.println("\nМетод 3");
		out.println("k \t число узлов \t диаметр \t погрешность \t C1 \t Q");
		for (int k = Task.minK; k < Task.maxK; k++) {
			Task task = new Task(k);
			Solver solver = new Solver(task);
			out.print(k + "\t" + task.N + "\t" + task.h + "\t");

			double e = error(solver, 3);
			double q = Math.log( e/error(new Solver(new Task(k+1)), 3)) / Math.log(3);
			double c1 = error(solver, 1) / Math.pow(task.h, Math.round(q));

			out.print(e + "\t" + c1 + "\t" + q + " (" + Math.round(q) + ")");
			out.println();
		}
	}

	private void reportBlock1(PrintStream out) {
		out.println("Значения в узлах");
		out.println();
		for (int k = Task.minK; k <= Task.maxK; k++) {
			Task task = new Task(k);
			Solver solver = new Solver(task);
			out.println("k = " + k + "; количество узлов = " + task.N);

			Series initial = solver.initial();
			Series m1 = solver.solve(1);
			Series m2 = solver.solve(2);
			Series m3 = solver.solve(3);

			out.println("№ узла \t x \t y \t Метод 1 \t погрешность \t Метод 2 \t погрешность \t Метод 3 \t погрешность");
			for (int i = 0; i < task.N; i++) {
				out.print(i + "\t");
				out.print(initial.asList().get(i).getX() + "\t");
				out.print(initial.asList().get(i).getY() + "\t");

				out.print(m1.asList().get(i).getY() + "\t");
				out.print(Math.abs(initial.asList().get(i).getY() - m1.asList().get(i).getY()) + "\t");

				out.print(m2.asList().get(i).getY() + "\t");
				out.print(Math.abs(initial.asList().get(i).getY() - m2.asList().get(i).getY()) + "\t");

				out.print(m3.asList().get(i).getY() + "\t");
				out.print(Math.abs(initial.asList().get(i).getY() - m3.asList().get(i).getY()) + "\t");

				out.println();
			}
			out.println();
		} /* k */
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
