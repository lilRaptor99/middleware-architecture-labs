import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

public class TemperatureSensorServer extends UnicastRemoteObject implements
		TemperatureSensor, Runnable {

	// Variable to hold the current temperature
	private volatile double temp;
	// Variable to hold the list of listeners
	private ArrayList<TemperatureListener> list = new ArrayList<TemperatureListener>();

	public TemperatureSensorServer() throws java.rmi.RemoteException {
		temp = 98.0;
	}

	public double getTemperature() throws java.rmi.RemoteException {
		return temp;
	}

	// Method to add a listener to the list
	public void addTemperatureListener(TemperatureListener listener)
			throws java.rmi.RemoteException {
		System.out.println("adding listener -" + listener);
		list.add(listener);
	}

	// Method to remove a listener from the list
	public void removeTemperatureListener(TemperatureListener listener)
			throws java.rmi.RemoteException {
		System.out.println("removing listener -" + listener);
		list.remove(listener);
	}

	// The thread run method to change the temperature and notify the listeners
	public void run() {
		Random r = new Random();
		for (;;) {
			try {
				// Sleep for a random amount of time
				int duration = r.nextInt() % 10000 + 200;
				// Check to see if negative, if so, reverse
				if (duration < 0) {
					duration = duration * -1;
					Thread.sleep(duration);
				}
			} catch (InterruptedException ie) {
			}

			// Get a number, to see if temp goes up or down
			int num = r.nextInt();
			if (num < 0) {
				temp += 0.5;
			} else {
				temp -= 0.5;
			}

			// Notify registered listeners
			notifyListeners();
		}
	}

	// Method to notify all the subscribed listeners of a temperature change
	private void notifyListeners() {
		for (TemperatureListener listener : list) {
			try {
				listener.temperatureChanged(temp);
			} catch (RemoteException re) {
				System.out.println("Remote exception in notifyListeners");
			}
		}
	}

	// Main method to start the server and register it with the RMI registry on the
	// local host
	public static void main(String[] args) {

		System.setProperty("java.security.policy", "file:allowall.policy");

		System.out.println("Loading temperature service");

		try {
			TemperatureSensorServer sensor = new TemperatureSensorServer();
			String registry = "localhost";

			String registration = "rmi://" + registry + "/TemperatureSensor";

			Naming.rebind(registration, sensor);

			Thread thread = new Thread(sensor);
			thread.start();
		} catch (RemoteException re) {
			System.err.println("Remote Error - " + re);
		} catch (Exception e) {
			System.err.println("Error - " + e);
		}

	}

}