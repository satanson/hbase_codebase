package ranpanf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.hadoop.hbase.client.HConnectionManager;


public class DirStorage {
	static final String GRAPH = "GRAPH";
	static final String CF = "C";
	static final Logger L = LogManager.getLogger();
	Configuration config=null;
	public DirStorage() throws FileNotFoundException{
		Configuration hbaseConfig = new Configuration();
		config=HBaseConfiguration.create(hbaseConfig);
		config.set("hbase.zookeeper.quorum", "node4,node5,node7,node8");
		L.info("hbase.zookeeper.quorum="+
				config.get("hbase.zookeeper.quorum"));
		L.info("hbase.zookeeper.property.clientPort="+
				config.get("hbase.zookeeper.property.clientPort"));
		L.info("zookeeper.znode.parent="+config.get("zookeeper.znode.parent"));
	}
	public HTable  getTable() throws IOException{
		return (HTable)HConnectionManager.createConnection(config).getTable(GRAPH);
	}
	public static long genVersion(){
		Random rand = new Random();
		return Math.abs(rand.nextLong()%10);
	}
	
	public void importDir(String rootdir,String path, String region) throws ZooKeeperConnectionException, IOException{
		String dir=rootdir+File.separator+path;
		List<File> files = walkDirectory(dir);
		
		if (files.size()==0){
			L.warn(dir+" is empty!");
		}
		
		HTable graphTable = getTable() ;
		
		List<Put> puts = new LinkedList<Put>();
		for (File f:files){	
			String keyRow = region+"@"+f.getAbsolutePath().substring(rootdir.length());
			byte[] content = new byte[(int)f.length()];
			FileInputStream fin = new FileInputStream(f);
			fin.read(content);
			Put put = new Put(keyRow.getBytes(),genVersion());
			put.add(CF.getBytes(), "F".getBytes(), content);
			
			puts.add(put);
			fin.close();
		}
		graphTable.put(puts);
	}
	
	public void exportDir(String rootdir,String path,String region,long ts) throws ZooKeeperConnectionException, IOException{
		HTable graphTable = getTable();
		Scan scan = new Scan();
		scan.setMaxVersions(1);
		scan.setTimeRange(0, ts+1);
		
		String prefix=region+"@"+File.separator+path;
		Filter filter = new PrefixFilter(prefix.getBytes());
		scan.setFilter(filter);
		
		ResultScanner scanner =  graphTable.getScanner(scan);
		Result row=null;
		
		while((row=scanner.next())!=null){
			String rowKey = new String(row.getRow());
			//NavigableMap<byte[], byte[]> columns=row.getFamilyMap(CF.getBytes());
			//String line=rowKey+": ";
			File f = new File(rootdir+rowKey.replaceFirst(region+"@", ""));
			File p = f.getParentFile();
			if (p!=null && !p.exists())mkdirRecursive(p.getAbsolutePath());
			
			FileOutputStream fout = new FileOutputStream(f);
			fout.write(row.getValue(CF.getBytes(), "F".getBytes()));
			fout.close();
			L.info("write into File "+f.getAbsolutePath());
		}
	}
	
	public void listRowKey(String prefix,long ts1, long ts2) throws IOException{
		Scan scan = new Scan();
		scan.setMaxVersions(1);
		scan.setTimeRange(ts1, ts2);
		Filter filter = new PrefixFilter(prefix.getBytes());
		scan.setFilter(filter);
			
		HTable graphTable=getTable();
		ResultScanner scanner =  graphTable.getScanner(scan);
		Result row=null;
		
		while((row=scanner.next())!=null){
			String rowKey = new String(row.getRow());
			long version = row.getColumnLatest(CF.getBytes(),"F".getBytes()).getTimestamp();
			System.out.println("version="+version+": "+rowKey);
		}
	}
	
	public List<File> walkDirectory(String path){
		List<File> files = new LinkedList<File>();
		File f = new File(path);
		if(f.isFile()){
			files.add(f);
		}
		else if (f.isDirectory()){
			File[] subfiles=f.listFiles();
			for (File subf:subfiles){
				if (subf.isFile()){
					files.add(subf);
				}
				else if (subf.isDirectory()) {
					files.addAll(walkDirectory(subf.getAbsolutePath()));
				}
			}
		}
		return files;
	}
	
	public static void mkdirRecursive(String dir){
		File f = new File(dir);
		File p = f.getParentFile();
		if (p!=null && !p.exists()){
			mkdirRecursive(p.getAbsolutePath());
		}
		
		if (!f.exists()){
			f.mkdir();
		}
	}
	
	public static void filterTable() throws IOException{
		Scan scan = new Scan();
		String prefix="/foo";
		Filter filter = new PrefixFilter(prefix.getBytes());
		scan.setFilter(filter);
		
		
		Configuration hbaseConfig =  new Configuration();
		hbaseConfig.set("hbase.zookeeper.quorum", "fcloud20");
		hbaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
		Configuration config = new Configuration();
		config = HBaseConfiguration.create(hbaseConfig);
		
		HTable graphTable=new HTable(config,"GRAPH");
		ResultScanner scanner =  graphTable.getScanner(scan);
		Result row=null;
		
		while((row=scanner.next())!=null){
			String rowKey = new String(row.getRow());
			NavigableMap<byte[], byte[]> columns=row.getFamilyMap("C".getBytes());
			String line=rowKey+": ";
			for(byte[] col:columns.keySet()){
				line+=new String(col)+" => "+new String(columns.get(col))+", ";
			}
			System.out.println(line);
			
		}
	}
	public static void main(String[] args) throws IOException{
		//filterTable();
		/*
		DirStorage ds = new DirStorage();
		String rootdir="E:\\hadoop_ecosystem\\kafka_2.10-0.8.2.0";
		String path = File.separator;
		List<File> files=ds.walkDirectory(rootdir+File.separator+path);
		for (File f:files){
			System.out.println(f.getCanonicalPath());
		}
		*/
		mkdirRecursive("E:\\foobarA\\foo\\bar\\ranpanf");
	}
}
