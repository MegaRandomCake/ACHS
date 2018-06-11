import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Init {
	public static Robot IsHearthstoneOpen() {
		String line = "", pidInfo ="";
		Robot BeepBoop;
		try {
			BeepBoop = new Robot();
		} catch (AWTException e) {
			BeepBoop = null;
		}

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

		if(pidInfo.contains("Hearthstone.exe") && BeepBoop != null)
		{
			return BeepBoop;
		}
		
		else {
			return null;
		}
	}
	
}
