import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.input.ReversedLinesFileReader;

public class SomeWatchFile {

	File file;
	ReversedLinesFileReader RLFR;
	final Charset charset = Charset.defaultCharset();
	String lastEdit;
	

	public SomeWatchFile() throws IOException {
			this.file = getLatestFilefromDir("C:\\Users\\Casper\\AppData\\Local\\Blizzard\\Hearthstone\\Logs");
			this.RLFR = new ReversedLinesFileReader(file, charset);
			this.lastEdit = RLFR.readLine();
	}
	
	
	private File getLatestFilefromDir(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		
		if (files == null || files.length == 0) {
			return null;
		}

		File lastModifiedFile = files[0];
		for (int i = 1; i < files.length; i++) {
			if (lastModifiedFile.lastModified() < files[i].lastModified()) {
				lastModifiedFile = files[i];
			}
		}
			return lastModifiedFile;
		
		
	}
	
	public boolean EndOfMatch() throws IOException {
		String line = "", templast = "";
		
		line = RLFR.readLine();
		templast = line;
		do {
			if(line.equals(lastEdit)) {
				this.lastEdit = templast;
				return false;
			}
			
			if(line.contains("Something that signifies we are on the dungeon run menu")){
				return true;
			}
			
		} while((line = RLFR.readLine()) != null);
		
		this.lastEdit = templast;
		return false;
		
	}


	public void StartOfMatch() {
		
		
	}



}