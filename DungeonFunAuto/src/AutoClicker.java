import java.awt.Color;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AutoClicker {
	enum xy {x,y};
	static Color c1, c2, c3;
	static int TR = -15265269, BR = -14412786, BL = -10011369;
	static int startX = 890, gapRightX = 310, CntlTRX = 1120, cardX = 240,
			CntlBRX = CntlTRX, cntlBLX = 560, playX = 1150, GerroshX = 250, HeroDiffX = 219;
	static int startY = 830, CntlTRY = 360, cardY = 450,
			CntlBRY = 720, CntlBLY = CntlBRY, playY = 900, GerroshY = 275, HeroDiffY = 237;
	static int[] sizes = new int[4]; //left top right bottom, topright is (0,0).
	
	//Yes, main should never ever throw Exception. No, I don't care enough to fix it atm.
	public static void main(String[] args) throws Exception {

		String line = "", pidInfo ="";
		Robot BeepBoop = new Robot();

		int counter = 0;

		Scanner sc = new Scanner(System.in);
		
		Process process = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");

		BufferedReader input =  new BufferedReader(new InputStreamReader(process.getInputStream()));

		while ((line = input.readLine()) != null) {
			pidInfo+=line; 
		}

		input.close();
		
		if(pidInfo.contains("Hearthstone.exe"))
		{
			System.out.println("Searching for Heartstone window");
			sizes = FindScreenSize();
			ratio();
			TR = BeepBoop.getPixelColor(calc(CntlTRX,xy.x),calc(CntlTRY,xy.y)).getRGB();
			BR = BeepBoop.getPixelColor(calc(CntlBRX,xy.x),calc(CntlBRY,xy.y)).getRGB();
			BL = BeepBoop.getPixelColor(calc(cntlBLX,xy.x),calc(CntlBLY,xy.y)).getRGB();
			System.out.println("\nHow many wins are we in?");
			try {
				counter = sc.nextInt();
			} catch(InputMismatchException e) {
				System.out.println("Ugyldigt input, starter ny dungeon run");
			}
			sc.close();

			while(true) {
				c1 = BeepBoop.getPixelColor(calc(CntlTRX,xy.x),calc(CntlTRY,xy.y));
				c2 = BeepBoop.getPixelColor(calc(CntlBRX,xy.x),calc(CntlBRY,xy.y));
				c3 = BeepBoop.getPixelColor(calc(cntlBLX,xy.x),calc(CntlBLY,xy.y));
				if(colorchecker()) {
					Thread.sleep(1000);
					switch(counter) {
					case 0:
						clickok(BeepBoop);
						counter++;
						break;
					case 1: case 3: case 5: case 7:
						clickmultiple(BeepBoop);
					case 2: case 4: case 6:
						clickonce(BeepBoop);
						counter++;
						break;
					default: counter = 0;
					}
				}
				Thread.sleep(4500);
			}
		}
		else {
			sc.close();
			System.exit(0);
		}

	}
	
	//If the ratio of Hearthstone is not 4:3 make it that, because anything between 16:9 and 4:3 is the same as 4:3 but with noise in the sides.
	private static void ratio() {
		int length = sizes[2]-sizes[0];
		int height = (sizes[1]-sizes[3])/3;
		int goal = height * 4;
		
		if(height == goal) {
			return;
		}
		
		goal = (length - goal)/2;
		sizes[0] += goal;
		sizes[2] -= goal;
	}

	//Picks a random set of 3 normal cards to add to the deck.
	private static void clickonce(Robot BeepBoop) throws Exception {
		clickanywhere(BeepBoop, cardX + Randomizer(gapRightX), cardY);
		Thread.sleep(1500);
		clickanywhere(BeepBoop, calc(startX, xy.x), calc(startY, xy.y));
	}

	//Picks a random dungeon card to add to the deck.
	private static void clickmultiple(Robot BeepBoop) throws Exception {
		clickanywhere(BeepBoop, cardX + Randomizer(gapRightX), cardY);
		Thread.sleep(1500);
	}

	//Check if we are on the main menu of dungeon run.
	public static boolean colorchecker() {
		System.out.println(c1.getRGB() + " " + c2.getRGB() + " " + c3.getRGB() + " " + TR + " " + BR + " " + BL);
		return c1.getRGB() == TR && c2.getRGB() == BR && c3.getRGB() == BL;
		
	}

	//Calls clickanywhere to navigating picking a hero.
	public static void clickok(Robot BeepBoop) throws Exception {
		clickanywhere(BeepBoop, calc(startX, xy.x), calc(startY, xy.y));
		clickanywhere(BeepBoop, calc(GerroshX,xy.x) + Randomizer(calc(HeroDiffX,xy.x)), calc(GerroshY,xy.y) + Randomizer(calc(HeroDiffY,xy.y)));
		clickanywhere(BeepBoop, calc(playX,xy.x), calc(playY,xy.y));
	}

	//Return a random integer from 1 to 3. Used for picking heroes and cards.
	public static int Randomizer(int number) {
		return number * (int) (Math.random() * 2 + 1);
	}
	
	//Find the pixel by scaling from 1440:1080 to the current 4:3 in sizes[].
	public static int calc(int number, xy a) {
		if(a == xy.x) {
			return sizes[0] + ((number*(sizes[2]-sizes[0]))/1440);
		}
		else {
			return sizes[1] + ((number*(sizes[3]-sizes[1]))/1080);
		}

	}

	//Moves, click and releases the mouse, then makes the thread sleep.
	public static void clickanywhere(Robot BeepBoop, int x, int y) throws Exception {
		BeepBoop.mouseMove(x, y);
		BeepBoop.mousePress(InputEvent.BUTTON1_MASK);
		BeepBoop.mouseRelease(InputEvent.BUTTON1_MASK);
		Thread.sleep(1500);
	}

	public static int[] FindScreenSize() throws Exception {
		//TODO replace this with reading from the option.txt file, where everything useful is stored.
		GetWindowRect GWR = new GetWindowRect();
		return GWR.go("Hearthstone");
	}

}
