package util;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import entity.EventMessage;
import entity.Message;
import entity.TextMessage;

public class MessageUtil {

	private static Message resvMessage(HttpServletRequest request) throws IOException{
		Message recvmessage=null;
		Map<String, String> xMLMap=XMLUtil.getXMLContent(request);
		if("text".equals(xMLMap.get("MsgType"))){
			TextMessage textMessage=new TextMessage();
			textMessage.setToUserName(xMLMap.get("ToUserName"));
			textMessage.setFromUserName(xMLMap.get("FromUserName"));
			textMessage.setCreateTime(xMLMap.get("CreateTime"));
			textMessage.setMsgType(xMLMap.get("MsgType"));
			textMessage.setContent(xMLMap.get("Content"));
			recvmessage=textMessage;
		}
		else if("event".equals(xMLMap.get("MsgType"))){
			if("subscribe".equals(xMLMap.get("Event"))){
				EventMessage eventMessage=new EventMessage();
				eventMessage.setToUserName(xMLMap.get("ToUserName"));
				eventMessage.setFromUserName(xMLMap.get("FromUserName"));
				eventMessage.setCreateTime(xMLMap.get("CreateTime"));
				eventMessage.setMsgType(xMLMap.get("MsgType"));
				eventMessage.setEvent(xMLMap.get("Event"));
				recvmessage=eventMessage;
			}
		}
		return recvmessage;
	}
	private static Message replyMessage(Message recvMessage){
		Message replyMessage=null;
		if("text".equals(recvMessage.getMsgType())){
			if("?".equals(((TextMessage)recvMessage).getContent())||"？".equals(((TextMessage)recvMessage).getContent()))
				replyMessage=replyHelpMessage(recvMessage);
			else if("聊天帮助".equals(((TextMessage)recvMessage).getContent()))
				replyMessage=replyCommunicateHelpMessage(recvMessage);
			else if("装逼帮助".equals(((TextMessage)recvMessage).getContent()))
				replyMessage=replyZBHelpMessage(recvMessage);
			else if(((TextMessage)recvMessage).getContent().startsWith("ZB#")||((TextMessage)recvMessage).getContent().startsWith("zb#"))
				replyMessage=replyZBImageMessage(recvMessage);
			else{
				replyMessage=autoChat(recvMessage);
			}
		}
		else if("event".equals(recvMessage.getMsgType())){
			if("subscribe".equals(((EventMessage)recvMessage).getEvent())){
				TextMessage textMessage=new TextMessage();
				textMessage.setMsgType("text");
				textMessage.setToUserName(recvMessage.getFromUserName());
				textMessage.setFromUserName(recvMessage.getToUserName());
				textMessage.setCreateTime(Long.toString(new Date().getTime()));
				textMessage.setContent("只有长得帅的人才关注我");
				replyMessage=textMessage;
			}
		}
		return replyMessage;
	}
	public static String getReplyString(HttpServletRequest request) throws IOException{
		//消息获取
		Message recvMessage=resvMessage(request);
		//消息处理
		Message replyMessage=replyMessage(recvMessage);
		//消息格式转换
		String replyString=XMLUtil.toXMLString(replyMessage);
		
		return replyString;
	}
	private static TextMessage replyHelpMessage(Message recvMessage){
		String content="--请回复以获取帮助--\n--聊天帮助--\n--装逼帮助--";
		TextMessage textMessage=new TextMessage();
		textMessage.setMsgType("text");
		textMessage.setToUserName(recvMessage.getFromUserName());
		textMessage.setFromUserName(recvMessage.getToUserName());
		textMessage.setCreateTime(Long.toString(new Date().getTime()));
		textMessage.setContent(content);
		return textMessage;
	}
	private static TextMessage replyCommunicateHelpMessage(Message recvMessage){
		String content="智能聊天机器人，可以自由交谈，请勿以 zb# 及 ZB# 开头";
		TextMessage textMessage=new TextMessage();
		textMessage.setMsgType("text");
		textMessage.setToUserName(recvMessage.getFromUserName());
		textMessage.setFromUserName(recvMessage.getToUserName());
		textMessage.setCreateTime(Long.toString(new Date().getTime()));
		textMessage.setContent(content);
		return textMessage;
	}
	private static TextMessage replyZBHelpMessage(Message recvMessage) {
		String content="回复 ZB# 序号 姓名/地名\n获取趣味图片\n序号：\n";
		for(int i=0;i<ZBUtil.zbName.size();i++){
			content+=i+". "+ZBUtil.zbName.get(i);
			if(i<ZBUtil.zbName.size()-1)
				content+="\n";
		}
		TextMessage textMessage=new TextMessage();
		textMessage.setMsgType("text");
		textMessage.setToUserName(recvMessage.getFromUserName());
		textMessage.setFromUserName(recvMessage.getToUserName());
		textMessage.setCreateTime(Long.toString(new Date().getTime()));
		textMessage.setContent(content);
		return textMessage;
	}
	private static TextMessage replyZBImageMessage(Message recvMessage){
		String recvContent=((TextMessage)recvMessage).getContent();
		String recvParems=recvContent.substring(3,recvContent.length());
		Scanner scanner=new Scanner(recvParems);
		int type=scanner.nextInt();
		String str=scanner.next();
		scanner.close();
		String imageURL=null;
		try {
			imageURL = ZBUtil.getImageURL(type, str);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		String content="<a href=\""+imageURL+"\">"+ZBUtil.zbName.get(type)+" "+str+"</a>";
		TextMessage textMessage=new TextMessage();
		textMessage.setMsgType("text");
		textMessage.setToUserName(recvMessage.getFromUserName());
		textMessage.setFromUserName(recvMessage.getToUserName());
		textMessage.setCreateTime(Long.toString(new Date().getTime()));
		textMessage.setContent(content);
		return textMessage;
	}
	private static TextMessage autoChat(Message recvMessage){
		String content=AutoChatUtil.getRobotMessage(recvMessage);
		TextMessage textMessage=new TextMessage();
		textMessage.setMsgType("text");
		textMessage.setToUserName(recvMessage.getFromUserName());
		textMessage.setFromUserName(recvMessage.getToUserName());
		textMessage.setCreateTime(Long.toString(new Date().getTime()));
		textMessage.setContent(content);
		return textMessage;
	}
}
