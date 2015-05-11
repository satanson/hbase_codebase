package ranpanf;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HTable;

public class IdleForeverMain {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		HBaseConnection conn=new HBaseConnection(args[0]);
		while(true){
			HTable tab = conn.getTable("");
			Thread.sleep(1000);
		}
	}

}
