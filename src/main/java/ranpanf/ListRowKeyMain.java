package ranpanf;

import java.io.File;
import java.io.IOException;

public class ListRowKeyMain {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		DirStorage ds = new DirStorage();
		//String prefix = "apache@\\kafka_2.10-0.8.2.0\\bin";
		long version = DirStorage.genVersion();
		ds.listRowKey("",0,20150409150925l+1);
//		for (int i=0;i<100;++i){
//			long n = DirStorage.genVersion();
//			if (n<0)System.out.println("n="+n);
//		}
	}

}
