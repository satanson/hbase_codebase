package ranpanf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HBaseConnection {
	static final Logger L = LogManager.getLogger(HBaseFileVisitor.class);
	private Configuration config;
	private HConnection hconn;
	public HBaseConnection(String resource) throws FileNotFoundException, ZooKeeperConnectionException{
		Configuration hbaseConfig = new Configuration();
		hbaseConfig.addResource(new FileInputStream(resource));
		config=HBaseConfiguration.create(hbaseConfig);
		//config.addResource(new FileInputStream(resource));
		//config.set("hbase.zookeeper.quorum", "node4,node5,node7,node8");
		L.info("hbase.zookeeper.quorum="+
				config.get("hbase.zookeeper.quorum"));
		L.info("hbase.zookeeper.property.clientPort="+
				config.get("hbase.zookeeper.property.clientPort"));
		L.info("zookeeper.znode.parent="+config.get("zookeeper.znode.parent"));
		hconn=HConnectionManager.createConnection(config);
	}
	public HConnection getConnection(){
		return hconn;
	}
	public HTable  getTable(String tab) throws IOException{
		return (HTable)hconn.getTable(tab);
	}
}
