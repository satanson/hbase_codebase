package ranpanf;

import java.io.File;
import java.io.FileNotFoundException;

public class HbaseConfigTest {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String configfile=args[0]+File.separator+"hbase-site.xml";
		System.out.println(configfile);
		DirStorage ds = new DirStorage();
	}

}
