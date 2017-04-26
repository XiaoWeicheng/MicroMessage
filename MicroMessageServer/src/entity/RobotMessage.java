package entity;

public class RobotMessage {

	public static int code_=100000;
	private int code;
	private String text;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isText() {
		return code_==code;
	}
	
}
