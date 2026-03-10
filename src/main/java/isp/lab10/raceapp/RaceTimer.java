package isp.lab10.raceapp;

public class RaceTimer extends Thread{
    public boolean run = true;
    public long time = 0;

    public void stopTimer() {
        run = false;
    }

    public void run() {
        while (run) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            time++;

        }
    }
}
