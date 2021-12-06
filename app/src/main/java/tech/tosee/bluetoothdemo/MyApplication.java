package tech.tosee.bluetoothdemo;/*
 * @author: geh
 * @version: V100R001C01
 * @date: 2021/7/2
 */

import android.app.Application;
import android.content.Context;

import com.umeng.commonsdk.UMConfigure;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyApplication extends Application {
    // Activity栈读写锁
    private final ReentrantReadWriteLock ACTIVITY_STACK_LOCK = new ReentrantReadWriteLock();
    private final String UMENG_CHANNEL = "BLUETOOTH_DEMO";

    private final String UMENG_APPKEY = "612888e14bede245d9ecbdcb";
    // Service栈读写锁
    private final ReentrantReadWriteLock SERVICE_STACK_LOCK = new ReentrantReadWriteLock();
    protected static MyApplication appInstance;
    /**
     * 获取实例
     * @return
     */
    public static MyApplication getInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */
        UMConfigure.init(this,UMENG_APPKEY,UMENG_CHANNEL,UMConfigure.DEVICE_TYPE_PHONE , "");
    }

    /**
     * Context运行状态
     */
    private enum ContextRunningStatus {
        CREATE,
        RESUME,
        PAUSE,
        DESTROY
    }

    /**
     * Context维护
     */
    private static class ContextInstanceWrapper {
        /**
         * 名称
         */
        private String name;
        private WeakReference<Context> currentContext;
        private ContextRunningStatus status;

        public ContextInstanceWrapper(String name, Context currentContext, ContextRunningStatus status) {
            this.name = name;
            this.currentContext = new WeakReference<>(currentContext);
            this.status = status;
        }

        /**
         * 更新Context状态
         *
         * @param status
         */
        public void updateStatus(ContextRunningStatus status) {
            this.status = status;
        }

        /**
         * 生命周期校验
         *
         * @return
         */
        public boolean checkValid() {
            return status != ContextRunningStatus.DESTROY && this.currentContext.get() != null;
        }

        /**
         * 检测是否是目标Context
         *
         * @param context
         * @return
         */
        public boolean isTargetContext(Context context) {
            return context != null && context == this.currentContext.get();
        }

        /**
         * 是否正在运行
         * @return
         */
        public boolean isRunning() {
            return currentContext.get() != null &&
                    (status == ContextRunningStatus.CREATE ||
                            status == ContextRunningStatus.RESUME);
        }
    }

}
