package com.dykj.rpg.net.thread;

import com.dykj.rpg.net.core.ISession;
import com.dykj.rpg.net.core.ISyncDataTask;
import com.dykj.rpg.net.core.NetWork;
import com.dykj.rpg.net.handler.core.ClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class MessageExecutor {

    private static Logger logger = LoggerFactory.getLogger(MessageExecutor.class);

    /**
     * 在本服一些同步操作数据的
     *
     * @param syncDataTask
     */
    public static void exeSyncDataTask(ISyncDataTask syncDataTask) {
        ThreadUtil.getDefault().getThreadPool().getLogicThreadPool(syncDataTask.getTaskId()).execute(syncDataTask);
    }

    /**
     * 在本服一些同步操作数据的
     *
     * @param runnable
     */
    public static void exeSyncDataTask(int taskId, Runnable runnable) {
        ThreadUtil.getDefault().getThreadPool().getLogicThreadPool(taskId).execute(runnable);
    }


    /**
     * 在本服一些同步操作数据的
     *
     * @param runnable
     */
    public static void exeSendMsgTask(Runnable runnable) {
        ThreadUtil.getDefault().getThreadPool().getMSG_SEND_THREAD().execute(runnable);
    }

    /**
     * 执行客户端的msg
     *
     * @param msg
     * @param clientHandler
     */
    public static void exeClientMsg(byte[] msg, ISession session, ClientHandler clientHandler) {
        ExecutorService executorService = null;
        if (clientHandler.getThread().equals(CmdThreadEnum.LOGIN)) {
            executorService = ThreadUtil.getDefault().getThreadPool().getLoginThreadPool(session.getId());
        } else if (clientHandler.getThread().equals(CmdThreadEnum.LOGIC)) {
            executorService = ThreadUtil.getDefault().getThreadPool().getLogicThreadPool(session.getId());
        }
        if (executorService != null) {
            executorService.execute(() -> clientHandler.handler(msg, session));
        } else {
            clientHandler.handler(msg, session);
        }
    }
}
