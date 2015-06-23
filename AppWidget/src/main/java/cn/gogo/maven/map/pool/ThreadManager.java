package cn.gogo.maven.map.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadManager {
	private static ThreadPoolExecutor mThreadPollExecutor = null;
	private static ThreadManager threadManager = null;

	private static final int CORE_POLL_SIZE = 5;
	private static final int MAX_POLL_SIZE = 100;
	private static final long KEEP_ALIVE_TIME = 5000;
	private static final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(
			10);

	public static ThreadManager getInstance() {
		if (threadManager == null) {
			threadManager = new ThreadManager();
		}
		return threadManager;
	}

	// 线程工厂
	private static ThreadFactory threadFactory = new ThreadFactory() {
		private final AtomicInteger integer = new AtomicInteger();

		public Thread newThread(Runnable r) {
			return new Thread(r, "myThreadPool thread:"
					+ integer.getAndIncrement());
		}
	};

	private ThreadManager() {
		mThreadPollExecutor = new ThreadPoolExecutor(CORE_POLL_SIZE,
				MAX_POLL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue,
				threadFactory);
	}

	public synchronized void execute(Runnable runnable) {
		mThreadPollExecutor.execute(runnable);
	}

	public synchronized void cancel(FutureTask<Runnable> futureTask) {
		futureTask.cancel(true);
	}

	public synchronized void shutDown() {
		mThreadPollExecutor.shutdown();
	}
}
