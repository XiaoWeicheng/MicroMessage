package entity;

public class LinkRobotMessage extends RobotMessage {

	public static int code_=200000;
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
