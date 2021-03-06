package com.distributed.transaction.service.dubbo.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author yubing
 *
 */
public class TransactionServiceLauncher {
	
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionServiceLauncher.class);

	public static final String SHUTDOWN_HOOK_KEY = "server.shutdown.hook";

	private static volatile boolean running = true;

	public static void main(String[] args) {
		long t = System.currentTimeMillis();
		try {
		
			logger.info("distributed transaction manager  init.....");
			System.setProperty("dubbo.application.logger", "slf4j");
			
			
			

			final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
					"classpath*:distributed-transaction-service.xml");
			
			
			logger.info("user.dir: " + System.getProperty("user.dir"));
			
			if ("true".equals(System.getProperty(SHUTDOWN_HOOK_KEY))) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run() {
						try {
							context.stop();
							logger.warn("server stopped!!!");
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						} catch (Throwable t) {
							logger.error(t.getMessage());
						}
						synchronized (TransactionServiceLauncher.class) {
							running = false;
							TransactionServiceLauncher.class.notify();
						}
					}
				});
			}
			

			context.start();
			logger.info("distributed transaction manager Started!  take " + (System.currentTimeMillis() - t) + " ms");
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			System.exit(1);
		}
		synchronized (TransactionServiceLauncher.class) {
			while (running) {
				try {
					TransactionServiceLauncher.class.wait();
				} catch (Throwable e) {
				}
			}
		}

	}

}
