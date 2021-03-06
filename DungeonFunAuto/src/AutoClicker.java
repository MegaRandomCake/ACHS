import java.awt.Color;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AutoClicker {
	enum xy {x,y};
	static Color c1, c2, c3;
	static int TR = -15265269, BR = -14412786, BL = -10011369, counter = 0;
	static int startX = 890, gapRightX = 270, CntlTRX = 1120, cardX = 250,
			CntlBRX = CntlTRX, cntlBLX = 560, playX = 1150, GerroshX = 250, HeroDiffX = 219;
	static int startY = 830, CntlTRY = 360, cardY = 505,
			CntlBRY = 720, CntlBLY = CntlBRY, playY = 900, GerroshY = 275, HeroDiffY = 237;
	static int[] sizes = new int[4]; //left top right bottom, topleft is (0,0).

	public void Auto() {
		Robot BeepBoop = Init.IsHearthstoneOpen();
		if(BeepBoop != null) {
			SomeWatchFile SWF = null;
			try {
				SWF = new SomeWatchFile();
			} catch (IOException e) {
			}


			System.out.println("Searching for Heartstone window");
			try {
				sizes = GetWindowSize.go("Hearthstone");
			} catch (Exception e) {
				System.out.println("An error orcurred.");
				System.exit(2);
			}

			Scanner sc = new Scanner(System.in);
			System.out.println("\nHow many wins are we in?");
			try {
				counter = sc.nextInt();
			} catch(InputMismatchException e) {
				System.out.println("Ugyldigt input, starter ny dungeon run");
				counter = 0;
			}
			sc.close();

			boolean wherearewe = false;
			while(true) {
				wherearewe = SWF.EndOfMatch();
				
				if(wherearewe == true) {
					wherearewe = false;
					pickerswitch(BeepBoop);
					sleeper(1000);
					while(SWF.StartOfMatch()) {
						counter--;
						pickerswitch(BeepBoop);
					}
				}
				
				if(counter == 8) {
					gameender();
				}
			}
		}
		else {
			System.exit(1);
		}

	}
	
	private static void gameender() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Press 0 to play again, or anything else to exit");
		int nextgame = 1;
		try {
			nextgame = sc.nextInt();
		}catch(InputMismatchException e){
			nextgame = 1;
		}
		sc.close();
		if(nextgame == 0) {
			return;
		}
		else {
			System.exit(0);
		}
	}

	//Manages if we need to pick a card, bundle or hero.
	private static void pickerswitch(Robot BeepBoop) {
		sleeper(1000);
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
		default: break;
		}
	}

	//Picks a random set of 3 normal cards to add to the deck.
	private static void clickonce(Robot BeepBoop) {
		clickanywhere(BeepBoop, cardX + Randomizer(gapRightX), cardY);
		sleeper(1500);
		clickanywhere(BeepBoop, calc(startX, xy.x), calc(startY, xy.y));
	}

	//Picks a random dungeon card to add to the deck.
	private static void clickmultiple(Robot BeepBoop) {
		clickanywhere(BeepBoop, cardX + Randomizer(gapRightX), cardY);
		sleeper(1500);
	}

	//Check if we are on the main menu of dungeon run.
	public static boolean colorchecker() {
		System.out.println(c1.getRGB() + " " + c2.getRGB() + " " + c3.getRGB() + " " + TR + " " + BR + " " + BL);
		return c1.getRGB() == TR && c2.getRGB() == BR && c3.getRGB() == BL;

	}

	//Calls clickanywhere to navigating picking a hero.
	public static void clickok(Robot BeepBoop) {
		clickanywhere(BeepBoop, calc(startX, xy.x), calc(startY, xy.y));
		clickanywhere(BeepBoop, calc(GerroshX,xy.x) + Randomizer(calc(HeroDiffX,xy.x)-sizes[0]), calc(GerroshY,xy.y) + Randomizer(calc(HeroDiffY,xy.y)-sizes[1]));
		clickanywhere(BeepBoop, calc(playX,xy.x), calc(playY,xy.y));
	}

	//Return a random integer from 0 to 2. Used for picking heroes and cards.
	public static int Randomizer(int number) {
		return number * (int) (Math.random() * 3);
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

	//Moves, click and releases the mouse, then calls the method 'sleeper'.
	public static void clickanywhere(Robot BeepBoop, int x, int y) {
		BeepBoop.mouseMove(x, y);
		BeepBoop.mousePress(InputEvent.BUTTON1_MASK);
		BeepBoop.mouseRelease(InputEvent.BUTTON1_MASK);
		sleeper(1500);
	}

	//Makes the thread sleep.
	public static void sleeper(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}	
	}
}
