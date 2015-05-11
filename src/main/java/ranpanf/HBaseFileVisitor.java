package ranpanf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ranpanf.DirectoryWalker.FileVisitor;

public class HBaseFileVisitor{
	static final Logger L = LogManager.getLogger(HBaseFileVisitor.class);
	private HBaseConnection conn;
	private String tab;
	private byte[] cf;
	private byte[] c;
	
	public void init(String tab,String cf,String c){
		this.tab=tab;
		this.cf=cf.getBytes();
		this.c=c.getBytes();
	}
	public HBaseFileVisitor(String resource) throws FileNotFoundException, ZooKeeperConnectionException{
		conn=new HBaseConnection(resource);
	}
	
	public FileVisitor getWriteFileVisitor() throws IOException{
		final HTable table = conn.getTable(tab);
		final String host=InetAddress. getLocalHost().getHostName();
		return new FileVisitor(){
			public void visit(File f) throws Exception {
				if (f.isFile()){
					L.info("visit file: "+f.getAbsolutePath());
					if (f.length()>1024*1024*1024){
						L.info("give up file whose size is exceed 1G");
						return;
					}
					byte[] keyRow = (host+"@"+f.getAbsolutePath()).getBytes();
					byte[] content = new byte[(int)f.length()];
					FileInputStream fin = new FileInputStream(f);
					fin.read(content);
					fin.close();
					Put row = new Put(keyRow);
					row.add(cf,c,content);
					table.put(row);
				}else if (f.isDirectory()){
					L.info("visit directory: "+f.getAbsolutePath());
				}else{
					L.info("visit special file: "+f.getAbsolutePath());
				}
			}
		};
	}
}
