package ranpanf;
public class DumpDirectoryMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		HBaseFileVisitor v = new HBaseFileVisitor(args[0]);
		v.init(args[1], args[2], args[3]);
		DirectoryWalker.walkDir(args[4], v.getWriteFileVisitor());
	}
}
