package de.neuwirthinformatik.alexander.mtuo;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Tasks {
    private static final Executor executor = Executors.newSingleThreadExecutor(); // change according to your requirements
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback<R> {
        void onComplete(R result);
    }
    public static void executeAsync(Runnable runnable) {
        executeAsync1(runnable);
    }

    public static void executeAsync1(Runnable runnable) {
        executor.execute(runnable);
    }

    public static void executeAsync2(Runnable runnable) {
        //android.os.AsyncTask.AsyncTask.execute(runnable);
    }

    public static <T> void executeAsync3(Runnable callable) {
        executeAsync(callable, (t) -> {
        });
    }

    public static <T> void executeAsync(Callable<T> callable) {
        executeAsync(callable, (t) -> {
        });
    }

    public static <T> void executeAsync(Runnable callable, Callback<T> callback) {
        executeAsync(() -> {
            callable.run();
            return null;
        });
    }

    public static <T> void executeAsync(Callable<T> callable, Callback<T> callback) {
        executor.execute(() -> {
            T tmp = null;
            try {
                tmp = callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            final T result = tmp;
            handler.post(() -> {
                callback.onComplete(result);
            });
        });
    }

    public static class ExampleLongRunning<T> implements Callable<T> {
        private final String input;

        public ExampleLongRunning(String input) {
            this.input = input;
        }

        @Override
        public T call() {
            // Some long running task
            return null;
        }
    }
}
