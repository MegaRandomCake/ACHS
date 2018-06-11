import java.awt.Robot;
import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.*;

public class GetWindowSize {

	private interface User32 extends StdCallLibrary {
		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class,
				W32APIOptions.DEFAULT_OPTIONS);

		HWND FindWindow(String lpClassName, String lpWindowName);

		int GetWindowRect(HWND handle, int[] rect);
	}

	private static int[] getRect(String windowName) throws WindowNotFoundException,
	GetWindowRectException {
		HWND hwnd = User32.INSTANCE.FindWindow(null, windowName);
		if (hwnd == null) {
			throw new WindowNotFoundException("", windowName);
		}

		int[] rect = {0, 0, 0, 0};
		int result = User32.INSTANCE.GetWindowRect(hwnd, rect);
		if (result == 0) {
			throw new GetWindowRectException(windowName);
		}
		return rect;
	}

	@SuppressWarnings("serial")
	private static class WindowNotFoundException extends Exception {
		public WindowNotFoundException(String className, String windowName) {
			super(String.format("Window null for className: %s; windowName: %s", 
					className, windowName));
		}
	}

	private static int[] ratio(int[] sizes) {
		int length = sizes[2]-sizes[0];
		int height = (sizes[3]-sizes[1])/3;
		int goal = height * 4;
		if(height == goal) {
			return sizes;
		}
		goal = (length - goal)/2;
		sizes[0] += goal;
		sizes[2] -= goal;
		return sizes;
	}

	@SuppressWarnings("serial")
	public static class GetWindowRectException extends Exception {
		public GetWindowRectException(String windowName) {
			super("Window Rect not found for " + windowName);
		}
	}

	public static int[] FindScreenSize(int[] out) throws Exception  {
		Robot BeepBoop;
		BeepBoop = new Robot();
		int middle = (out[2]+out[0])/2;
		for(int i = out[1]+2; i < out[3]; i++) {
			int color = BeepBoop.getPixelColor(middle,i).getRGB();
			if(color != -1) {
				out[1] = i;
				return out;
			}
		}
		return out;

	}

	public static int[] go(String windowName) throws Exception {
		int[] rect;
		rect = GetWindowSize.getRect(windowName);
		rect = ratio(rect);
		rect = FindScreenSize(rect);
		return rect;

	}
}