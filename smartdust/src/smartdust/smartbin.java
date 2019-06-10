package smartdust;

import java.awt.Frame;

public class smartbin extends Frame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3494266427994049573L;
	protected static binAppUi appui;
	protected static prefrence pre;
	protected static db_java db;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		appui = new binAppUi();
		//new prefrence();
		db = new db_java();
		db.createDb();
	}

}
