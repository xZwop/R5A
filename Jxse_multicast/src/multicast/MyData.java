package multicast;

import java.io.Serializable;

public class MyData implements Serializable{

	private static final long serialVersionUID = 2889299217500391638L;
	
	private String message;

	public MyData(String message) {
		super();
		this.message = message;
	}

	public MyData(byte[] data, int length) {
		this.message = new String(data, 0, length);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public byte[] getBytes() {
		return message.getBytes();
	}

	public int length() {
		return message.length();
	}
}
