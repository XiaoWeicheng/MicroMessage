package util;

import entity.LinkRobotMessage;
import entity.ManuRobotMessage;
import entity.Message;
import entity.News;
import entity.NewsRobotMessage;
import entity.RobotMessage;
import entity.TextMessage;
import util.tuling.PostServer;

public class AutoChatUtil {

	private static String Turing="http://www.tuling123.com/openapi/api";
	public static String getRobotMessage(Message recvMessage) {
		String robotMessageContent="";
		String message=((TextMessage)recvMessage).getContent();
		String userID=recvMessage.getFromUserName();
		String jsonString=JsonUtil.getJsonString(message, userID);
		String jsonRobotString=PostServer.SendPost(jsonString, Turing);
		RobotMessage robotMessage=JsonUtil.getRobotMessage(jsonRobotString);
		switch (robotMessage.getCode()) {
		case 100000:
			robotMessageContent+=robotMessage.getText();
			break;
		case 200000:
			robotMessageContent+="<a href='"+((LinkRobotMessage)robotMessage).getUrl()+"' >"+
								((LinkRobotMessage)robotMessage).getText()+"</a>";
			break;
		case 302000:
			robotMessageContent+=robotMessage.getText()+"\n";
			for(int i=0;i<((NewsRobotMessage)robotMessage).getList().size();i++){
				News news =((NewsRobotMessage)robotMessage).getList().get(i);
				robotMessageContent+="标题："+news.getArticle()+"\n来源："+news.getSource()
									+"<a href='"+news.getDetailurl()+"' >新闻详情</a>";
				if(i!=((NewsRobotMessage)robotMessage).getList().size()-1)
					robotMessageContent+="\n";
			}
			break;
		case 308000:
			robotMessageContent+=robotMessage.getText()+"\n"
								+"菜名："+((ManuRobotMessage)robotMessage).getName()
								+"菜谱信息："+((ManuRobotMessage)robotMessage).getInfo()
								+"<a href='"+((ManuRobotMessage)robotMessage).getDetailurl()+"' >新闻详情</a>";
			
			break;
		default:
			robotMessageContent+="大赵赵累了，要休息了";
			break;
		}
		return robotMessageContent;
	}
}
