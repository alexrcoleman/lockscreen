import java.awt.Robot;
import java.awt.Window;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import javax.swing.JDialog;

public class WindowsSecurity implements Runnable {
	private Window frame;
	private boolean running;

	public WindowsSecurity(Window window) {
		this.frame = window;
		this.running = true;
		new Thread(this).start();
	}

	public void stop() {
		this.running = false;
	}

	public void run() {
		this.frame.setAlwaysOnTop(true);
		if (this.frame instanceof JFrame)
			((JFrame) this.frame).setDefaultCloseOperation(0);
		if (this.frame instanceof JDialog)
			((JDialog) this.frame).setDefaultCloseOperation(0);
		System.out.println("Terminating explorer");
		kill("explorer.exe"); // <--- Kills explorer, preventing any escape;
								// remove if you want
								// not sure if I like or not
		System.out.println("Waiting for lock release");
		while (running) {
			sleep(30L);
		}
		System.out.println("Restarting explorer");
		launch("explorer.exe");
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {

		}
	}

	private void kill(String string) {
		try {
			Runtime.getRuntime().exec("taskkill /F /IM " + string).waitFor();
		} catch (Exception e) {
		}
	}

	private void launch(String string) {
		try {
			Runtime.getRuntime().exec(string).waitFor();
		} catch (Exception e) {
		}
	}
}