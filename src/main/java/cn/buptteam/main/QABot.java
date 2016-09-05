package cn.buptteam.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by bitholic on 16/9/5.
 */
public class QABot {
    public static Logger logger = LogManager.getLogger(QABot.class);

    public static void main(String[] args) {
        logger.info("欢迎使用QABot自动问答机器人.");
    }
}
