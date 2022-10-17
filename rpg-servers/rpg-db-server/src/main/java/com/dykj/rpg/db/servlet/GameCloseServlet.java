package com.dykj.rpg.db.servlet;

import com.dykj.rpg.db.queue.DbQueueManager;
import com.dykj.rpg.util.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 关闭游戏服务器接口
 * 
 * @author jassi.tang
 * 
 */
@Controller
public class GameCloseServlet {

	public GameCloseServlet() {
		super();
	}

	private static Logger log = LoggerFactory.getLogger(GameCloseServlet.class);

	@RequestMapping(value = "/closeGame")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String validateCode = request.getParameter("validateCode");
		boolean success = validateCode != null
				&& validateCode.equals(Md5Util.string2MD5(Md5Util.KEY));
		if (success) {
			response.getOutputStream().write("OK".getBytes());
			response.flushBuffer();
			log.info("close game server start ....");

			DbQueueManager.getInstance().flush();
			Thread.sleep(1000);
			//SpringContextLoader.getContext().destroy();
			System.exit(0);
			return;
		} else {
			log.warn("invalidate code");
			response.getOutputStream().write("invalidate code".getBytes());
			return;
		}
	}
}
