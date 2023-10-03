package commons;

import java.io.File;

public class GlobalConstants {
	public static final String DEV_APP_URL = "";
	
	public static final long LONG_TIMEOUT = 30;
	public static final long SHOT_TIMEOUT = 5;
	public static final long LONG_TIMEOUT1 = 120;
	
	public static final String PROJECT_PATH = System.getProperty("user.dir");
	public static final String UPLOAD_FOLDER_PATH = PROJECT_PATH +File.separator + "uploadFiles" + File.separator;
	public static final String DOWNLOAD_FOLDER_PATH = PROJECT_PATH +File.separator + "downloadFiles";


}
