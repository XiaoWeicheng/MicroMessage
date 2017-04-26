package connect;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sx.utils.SHA1Util;
import util.MessageUtil;

/**
 * Servlet implementation class MMConnect
 */
@WebServlet("/MMConnect")
public class MMConnect extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public MMConnect() {
        // TODO Auto-generated constructor stub
    }

		

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String token="19960220";
		String signature=request.getParameter("signature");
		String timestamp=request.getParameter("timestamp");
		String nonce=request.getParameter("nonce");
		String echostr=request.getParameter("echostr");
		String []args={token,timestamp,nonce};
  		Arrays.sort(args);
 		String str=args[0]+args[1]+args[2];
		String newStr=SHA1Util.toSha1(str);
		if(signature.equalsIgnoreCase(newStr)){
			response.getWriter().print(echostr);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		//��Ϣ����
		String replyString=MessageUtil.getReplyString(request);
		response.getWriter().print(replyString);
		doGet(request, response);
	}

}
