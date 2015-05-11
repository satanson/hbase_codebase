package ranpanf;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.hbase.ZooKeeperConnectionException;

public class ImportMain {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ZooKeeperConnectionException 
	 */
	public static void main(String[] args) throws ZooKeeperConnectionException, IOException {
		// TODO Auto-generated method stub
		DirStorage ds = new DirStorage();
		String rootdir="E:\\hadoop_ecosystem\\hbase-0.94.17\\";
		String path="conf";
		ds.importDir(rootdir,path, "apache");
	}

}
