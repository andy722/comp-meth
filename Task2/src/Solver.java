/**
 * Does all the mathematics
 */
class Solver {
	private final Task task;

	public Solver(Task task) {
		this.task = task;
	}

	public Series initial() {
		Series series = new Series();

		double x = Task.xmin;
		do {
			series.add(x, task.g(x));
			x += task.h;
		} while(x<Task.xmax);
		series.add(x, task.g(x));
		series.setCaption("Исходная функция (" + series.getSize() + " узлов)");

		return series;
	}

	public Series solve(int methodIndex) {
		Series series = new Series();

		double x = Task.xmin, y = task.g(Task.xmin);
		series.add(x, y);
		do {
			y = task.m(methodIndex, x, y);
			x += task.h;
			series.add(new Double(x), new Double(y));
		} while(x<Task.xmax);

		series.setCaption("Метод " + methodIndex + " (" + series.getSize() + " узлов)");

		return series;
	}

	public Task getTask() {
		return task;
	}
}