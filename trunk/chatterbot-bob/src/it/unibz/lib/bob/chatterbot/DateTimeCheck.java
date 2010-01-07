package it.unibz.lib.bob.chatterbot;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Checks current time, provides methods to check for special time spans. (could
 * also be extended to check special dates.)
 * 
 * @author manuelkirschner
 * 
 */
public class DateTimeCheck {
	private static final int startHourMorning = 3;
	private static final int startHourMidday = 12;
	private static final int startHourEvening = 18;

	public static boolean isMorning() {
		GregorianCalendar gc = new GregorianCalendar();
		int currentHour = gc.get(Calendar.HOUR_OF_DAY);
		if (currentHour >= startHourMorning && currentHour < startHourMidday) {
			return true;
		}
		return false;
	}

	public static boolean isMidday() {
		GregorianCalendar gc = new GregorianCalendar();
		int currentHour = gc.get(Calendar.HOUR_OF_DAY);
		if (currentHour >= startHourMidday && currentHour < startHourEvening) {
			return true;
		}
		return false;
	}

	public static boolean isEvening() {
		GregorianCalendar gc = new GregorianCalendar();
		int currentHour = gc.get(Calendar.HOUR_OF_DAY);
		if (currentHour >= startHourEvening || currentHour < startHourMorning) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println("Morning: " + DateTimeCheck.isMorning());
		System.out.println("Midday: " + DateTimeCheck.isMidday());
		System.out.println("Evening: " + DateTimeCheck.isEvening());
	}
}
