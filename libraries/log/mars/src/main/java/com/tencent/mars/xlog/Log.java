// Tencent is pleased to support the open source community by making Mars available.
// Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.

// Licensed under the MIT License (the "License"); you may not use this file except in
// compliance with the License. You may obtain a copy of the License at
// http://opensource.org/licenses/MIT

// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language governing permissions and
// limitations under the License.

package com.tencent.mars.xlog;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/19 1:40
// Description: 
// Documentation:
// Reference:

import android.os.Looper;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.log.vastgui.core.base.LogInfo;

import java.util.HashMap;
import java.util.Map;

public class Log {
    public static final int LEVEL_ALL = 0;
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARNING = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;
    public static final int LEVEL_NONE = 6;
    private static LogImp logImp;
    private static final Map<String, LogInstance> sLogInstanceMap = new HashMap<>();

    public static void setLogImp(@NonNull LogImp imp) {
        logImp = imp;
    }

    @Nullable
    public static LogImp getImpl() {
        return logImp;
    }

    public static void appenderOpen(int level, int mode, String cacheDir, String logDir, String namePrefix, int cacheDays) {
        if (logImp != null) {
            logImp.appenderOpen(level, mode, cacheDir, logDir, namePrefix, cacheDays);
        }
    }

    public static void appenderClose() {
        if (logImp != null) {
            logImp.appenderClose();
            for (Map.Entry<String, LogInstance> entry : sLogInstanceMap.entrySet()) {
                closeLogInstance(entry.getKey());
            }
        }
    }

    public static void appenderFlush() {
        if (logImp != null) {
            logImp.appenderFlush(0, false);
            for (Map.Entry<String, LogInstance> entry : sLogInstanceMap.entrySet()) {
                entry.getValue().appenderFlush();
            }
        }
    }

    public static void appenderFlushSync(boolean isSync) {
        if (logImp != null) {
            logImp.appenderFlush(0, isSync);

        }
    }

    public static int getLogLevel() {
        if (logImp != null) {
            return logImp.getLogLevel(0);
        }
        return LEVEL_NONE;
    }

    public static void setConsoleLogOpen(boolean isOpen) {
        if (logImp != null) {
            logImp.setConsoleLogOpen(0, isOpen);
        }
    }

    public static void setMaxFileSize(long aliveSeconds) {
        if (null != logImp) {
            logImp.setMaxFileSize(0, aliveSeconds);
        }
    }

    public static void setMaxAliveTime(long aliveSeconds) {
        if (null != logImp) {
            logImp.setMaxAliveTime(0, aliveSeconds);
        }
    }

    /**
     * Send a {@link Log#LEVEL_FATAL} log message.
     *
     * @since 1.3.4
     */
    public static void f(final String tag, LogInfo logInfo, final String msg) {
        if (logImp != null && logImp.getLogLevel(0) <= LEVEL_FATAL) {
            logImp.logF(0, tag, logInfo.getFileName(), logInfo.getMethodName(), logInfo.getLineNumber(),
                    Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), msg);
        }
    }

    /**
     * Send a {@link Log#LEVEL_ERROR} log message.
     *
     * @since 1.3.4
     */
    public static void e(final String tag, LogInfo logInfo, final String msg) {
        if (logImp != null && logImp.getLogLevel(0) <= LEVEL_ERROR) {
            logImp.logE(0, tag, logInfo.getFileName(), logInfo.getMethodName(), logInfo.getLineNumber(),
                    Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), msg);
        }
    }

    /**
     * Send a {@link Log#LEVEL_WARNING} log message.
     *
     * @since 1.3.4
     */
    public static void w(final String tag, LogInfo logInfo, final String msg) {
        if (logImp != null && logImp.getLogLevel(0) <= LEVEL_WARNING) {
            logImp.logD(0, tag, logInfo.getFileName(), logInfo.getMethodName(), logInfo.getLineNumber(),
                    Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), msg);
        }
    }

    /**
     * Send a {@link Log#LEVEL_INFO} log message.
     *
     * @since 1.3.4
     */
    public static void i(final String tag, LogInfo logInfo, final String msg) {
        if (logImp != null && logImp.getLogLevel(0) <= LEVEL_INFO) {
            logImp.logI(0, tag, logInfo.getFileName(), logInfo.getMethodName(), logInfo.getLineNumber(),
                    Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), msg);
        }
    }

    /**
     * Send a {@link Log#LEVEL_DEBUG} log message.
     *
     * @since 1.3.4
     */
    public static void d(final String tag, LogInfo logInfo, final String msg) {
        if (logImp != null && logImp.getLogLevel(0) <= LEVEL_DEBUG) {
            logImp.logD(0, tag, logInfo.getFileName(), logInfo.getMethodName(), logInfo.getLineNumber(),
                    Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), msg);
        }
    }

    /**
     * Send a {@link Log#LEVEL_VERBOSE} log message.
     *
     * @since 1.3.4
     */
    public static void v(final String tag, LogInfo logInfo, final String msg) {
        if (logImp != null && logImp.getLogLevel(0) <= LEVEL_VERBOSE) {
            logImp.logV(0, tag, logInfo.getFileName(), logInfo.getMethodName(), logInfo.getLineNumber(),
                    Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), msg);
        }
    }

    private static LogInstance openLogInstance(int level, int mode, String cacheDir, String logDir, String namePrefix, int cacheDays) {
        synchronized (sLogInstanceMap) {
            if (sLogInstanceMap.containsKey(namePrefix)) {
                return sLogInstanceMap.get(namePrefix);
            }
            LogInstance instance = new LogInstance(level, mode, cacheDir, logDir, namePrefix, cacheDays);
            sLogInstanceMap.put(namePrefix, instance);
            return instance;
        }
    }

    private static void closeLogInstance(String namePrefix) {
        synchronized (sLogInstanceMap) {
            if (null != logImp) {
                if (sLogInstanceMap.containsKey(namePrefix)) {
                    LogInstance logInstance = sLogInstanceMap.remove(namePrefix);
                    logImp.releaseXlogInstance(namePrefix);
                    logInstance.mLogInstancePtr = -1;
                }
            }
        }
    }

    private static LogInstance getLogInstance(String namePrefix) {
        synchronized (sLogInstanceMap) {
            if (sLogInstanceMap.containsKey(namePrefix)) {
                return sLogInstanceMap.get(namePrefix);
            }
            return null;
        }
    }

    public interface LogImp {

        void logV(long logInstancePtr, String tag, String filename, String funcname, int linuxTid, int pid, long tid, long maintid, String log);

        void logI(long logInstancePtr, String tag, String filename, String funcname, int linuxTid, int pid, long tid, long maintid, String log);

        void logD(long logInstancePtr, String tag, String filename, String funcname, int linuxTid, int pid, long tid, long maintid, String log);

        void logW(long logInstancePtr, String tag, String filename, String funcname, int linuxTid, int pid, long tid, long maintid, String log);

        void logE(long logInstancePtr, String tag, String filename, String funcname, int linuxTid, int pid, long tid, long maintid, String log);

        void logF(long logInstancePtr, String tag, String filename, String funcname, int linuxTid, int pid, long tid, long maintid, String log);

        int getLogLevel(long logInstancePtr);

        void setAppenderMode(long logInstancePtr, int mode);

        long openLogInstance(int level, int mode, String cacheDir, String logDir, String namePrefix, int cacheDays);

        long getXlogInstance(String namePrefix);

        void releaseXlogInstance(String namePrefix);

        void appenderOpen(int level, int mode, String cacheDir, String logDir, String namePrefix, int cacheDays);

        void appenderClose();

        void appenderFlush(long logInstancePtr, boolean isSync);

        void setConsoleLogOpen(long logInstancePtr, boolean isOpen);

        void setMaxFileSize(long logInstancePtr, long aliveSeconds);

        void setMaxAliveTime(long logInstancePtr, long aliveSeconds);

    }
    
    private static class LogInstance {

        private long mLogInstancePtr = -1;

        private String mPrefix = null;

        private LogInstance(int level, int mode, String cacheDir, String logDir, String namePrefix, int cacheDays) {
            if (logImp != null) {
                mLogInstancePtr = logImp.openLogInstance(level, mode, cacheDir, logDir, namePrefix, cacheDays);
                mPrefix = namePrefix;
            }
        }

        public void f(String tag, final String format, final Object... obj) {
            if (logImp != null && getLogLevel() <= LEVEL_FATAL && mLogInstancePtr != -1) {
                final String log = obj == null ? format : String.format(format, obj);
                logImp.logF(mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }
        }

        public void e(String tag, final String format, final Object... obj) {
            if (logImp != null && getLogLevel() <= LEVEL_ERROR && mLogInstancePtr != -1) {
                String log = obj == null ? format : String.format(format, obj);
                if (log == null) {
                    log = "";
                }
                logImp.logE(mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }
        }

        public void w(String tag, final String format, final Object... obj) {
            if (logImp != null && getLogLevel() <= LEVEL_WARNING && mLogInstancePtr != -1) {
                String log = obj == null ? format : String.format(format, obj);
                if (log == null) {
                    log = "";
                }
                logImp.logW(mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }
        }

        public void i(String tag, final String format, final Object... obj) {
            if (logImp != null && getLogLevel() <= LEVEL_INFO && mLogInstancePtr != -1) {
                String log = obj == null ? format : String.format(format, obj);
                if (log == null) {
                    log = "";
                }
                logImp.logI(mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }
        }

        public void d(String tag, final String format, final Object... obj) {
            if (logImp != null && getLogLevel() <= LEVEL_DEBUG && mLogInstancePtr != -1) {
                String log = obj == null ? format : String.format(format, obj);
                if (log == null) {
                    log = "";
                }
                logImp.logD(mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }
        }

        public void v(String tag, final String format, final Object... obj) {
            if (logImp != null && getLogLevel() <= LEVEL_VERBOSE && mLogInstancePtr != -1) {
                String log = obj == null ? format : String.format(format, obj);
                if (log == null) {
                    log = "";
                }
                logImp.logV(mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }
        }


        public void printErrStackTrace(String tag, Throwable tr, final String format, final Object... obj) {
            if (logImp != null && getLogLevel() <= LEVEL_ERROR && mLogInstancePtr != -1) {
                String log = obj == null ? format : String.format(format, obj);
                if (log == null) {
                    log = "";
                }
                log += "  " + android.util.Log.getStackTraceString(tr);
                logImp.logE(mLogInstancePtr, tag, "", "", Process.myTid(), Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
            }
        }


        public void appenderFlush() {
            if (logImp != null && mLogInstancePtr != -1) {
                logImp.appenderFlush(mLogInstancePtr, false);
            }
        }

        public void appenderFlushSync() {
            if (logImp != null && mLogInstancePtr != -1) {
                logImp.appenderFlush(mLogInstancePtr, true);
            }
        }

        public int getLogLevel() {
            if (logImp != null && mLogInstancePtr != -1) {
                return logImp.getLogLevel(mLogInstancePtr);
            }
            return LEVEL_NONE;
        }

        public void setConsoleLogOpen(boolean isOpen) {
            if (null != logImp && mLogInstancePtr != -1) {
                logImp.setConsoleLogOpen(mLogInstancePtr, isOpen);
            }
        }

    }
}