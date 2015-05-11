package ranpanf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DirectoryWalker {
	public static interface FileVisitor{
		public void visit(File f) throws Exception;
	}
	public static void walkDir(String dir,FileVisitor visitor) throws Exception{
		List<File> files = new LinkedList<File>();
		files.add(new File(dir));
		while(!files.isEmpty()){
			File f = files.remove(0);
			visitor.visit(f);
			if (f.isDirectory()){
				files.addAll(Arrays.asList(f.listFiles()));
			}
		}
	}
}
