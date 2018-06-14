import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Init {
	public static Robot IsHearthstoneOpen() {
		Robot BeepBoop = RobotBuilder();
		boolean HSO = HSOpen();

		if(HSO == true && BeepBoop != null)
		{
			return BeepBoop;
		}

		else {
			return null;
		}
	}

	private static Robot RobotBuilder() {
		Robot BeepBoop;
		try {
			BeepBoop = new Robot();
		} catch (AWTException e) {
			System.out.println("Error creating Bot for autoclicking.");
			BeepBoop = null;
		}
		return BeepBoop;
	}

	private static boolean HSOpen() {
		while(true) {
			String line = "", pidInfo ="";
			Process process;
			try {
				process = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");


				BufferedReader input =  new BufferedReader(new InputStreamReader(process.getInputStream()));

				while ((line = input.readLine()) != null) {
					pidInfo+=line; 
				}

				input.close();

			} catch (IOException e1) {
				pidInfo = "";
			}
			
			if(pidInfo.contains("Hearthstone.exe")) {
				return true;
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			
		}

	}

}
