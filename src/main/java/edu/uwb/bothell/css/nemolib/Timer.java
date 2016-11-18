package edu.uwb.bothell.css.nemolib;

/**
 * A simple class to keep track of program execution time
 */
public class Timer {
	private Long startTime;
	private Long stopTime;
	private Long time;
	private boolean started;

	/**
	 * Default constructor.
	 */
	public Timer() {
		startTime = 0L;
		stopTime = 0L;
		started = false;
	}

	/**
	 * Starts this timer.
	 */
	public void start() {
		startTime = System.currentTimeMillis();
		started = true;
	}

	/**
	 * Gets the time elapsed since this timer was started.
	 * @return Elapsed time in milliseconds since start was called; if timer
	 * hasn't been started, returns -1
	 */
	public long getCurrentTime() {
		if (!started) {
			return -1;
		}
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Stops this timer.
	 */
	public void stop() {
		stopTime = System.currentTimeMillis();
		time = stopTime - startTime;
		started = false;
	}

	/**
	 * Gets the elapsed time from when start was called until stop was called.
	 * @return elapsed time in milliseconds.
	 */
	public long getTime() {
		return time;
	}
}
