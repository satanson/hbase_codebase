package ranpanf;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class HBaseDefaultConfig {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Configuration hbaseConfig = new Configuration();
		Configuration config=HBaseConfiguration.create(hbaseConfig);
		Iterator<Entry<String, String>> iter=config.iterator();
		while(iter.hasNext()){
			Entry<String, String> property=iter.next();
			System.out.println(property.getKey()+"="+property.getValue());
		}
	}

}
