package ranpanf;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.hbase.ZooKeeperConnectionException;

public class ExportMain {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ZooKeeperConnectionException 
	 */
	public static void main(String[] args) throws ZooKeeperConnectionException, IOException {
		// TODO Auto-generated method stub
		DirStorage ds = new DirStorage();
		String rootdir="E:\\foobar";
		String path="kafka_2.10-0.8.2.0";
		ds.exportDir(rootdir,path, "apache",0);
	}

}
