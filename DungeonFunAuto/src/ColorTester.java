import java.awt.Robot;

public class ColorTester {

	public static void main(String[] args) throws Exception {
		Robot BeepBoop = new Robot();
		
		Thread.sleep(5000);
		
		BeepBoop.mouseMove(640, 360);
		Thread.sleep(2000);
		BeepBoop.mouseMove(1280, 360);
		Thread.sleep(2000);
		BeepBoop.mouseMove(1280, 720);
		Thread.sleep(2000);
		BeepBoop.mouseMove(640, 720);
		
		System.out.println(BeepBoop.getPixelColor(640, 360).getRGB());
		System.out.println(BeepBoop.getPixelColor(1280, 360).getRGB());
		System.out.println(BeepBoop.getPixelColor(1280, 720).getRGB());
		System.out.println(BeepBoop.getPixelColor(640, 720).getRGB());
		
	}

}
