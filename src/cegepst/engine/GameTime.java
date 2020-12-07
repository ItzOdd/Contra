package cegepst.engine;

import java.util.concurrent.TimeUnit;

public class GameTime {

    private static final int FPS_TARGET = 120;
    private static int currentFps;
    private static int fpsCount;
    private static long fpsTimeDelta;
    private static long gameStartTime;
    private static long sleep;
    private long syncTime;

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static int getCurrentFps() {
        return (currentFps > 0) ? currentFps : fpsCount;
    }

    public static long getElapsedTime() {
        return System.currentTimeMillis() - gameStartTime;
    }

    public static long getTimeToSleep() {
        return sleep;
    }

    public static String getElapsedFormattedTime() {
        long time = System.currentTimeMillis() - gameStartTime;
        long hours = TimeUnit.MILLISECONDS.toHours(time);
        time -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        time -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        //time -= TimeUnit.SECONDS.toMillis(seconds);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public GameTime() {
        updateSyncTime();
        gameStartTime = System.currentTimeMillis();
        fpsTimeDelta = 0;
        currentFps = 0;
    }

    public static void waitSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        fpsCount++;
        long currentSecond =
                TimeUnit.MILLISECONDS.toSeconds(getElapsedTime());
        if (fpsTimeDelta != currentSecond) {
            currentFps = fpsCount;
            fpsCount = 0;
        }
        fpsTimeDelta = currentSecond;
    }

    public void synchronize() {
        update();
        try {
            Thread.sleep(getSleepTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateSyncTime();
    }

    private void updateSyncTime() {
        syncTime = System.currentTimeMillis();
    }

    public long getSleepTime() {
        long targetTime = 1000L / FPS_TARGET;
        sleep = targetTime -
                (System.currentTimeMillis() - syncTime);
        if (sleep < 0) {
            sleep = 4;
        }
        return sleep;
    }
}
