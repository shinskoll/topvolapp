package com.nfa019.topvol.bd;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = -8584505601918722824L;
	
	public DatabaseException(String msg) {
		super(msg);
	}
	
	public DatabaseException(Exception exp) {
		super(exp);
	}

}
