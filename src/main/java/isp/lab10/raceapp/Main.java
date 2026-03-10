package isp.lab10.raceapp;



import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Race!");

        JFrame frame = new JFrame("Car Race");
        JFrame semaphoreFrame = new JFrame("Semaphore");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        semaphoreFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CarPanel carPanel = new CarPanel();
        SemaphorePanel semaphorePanel = new SemaphorePanel();

        frame.getContentPane().add(carPanel);
        frame.pack();
        frame.setSize(500,300);
        frame.setVisible(true);

        semaphoreFrame.getContentPane().add(semaphorePanel);
        semaphoreFrame.pack();
        semaphoreFrame.setVisible(true);

        PlaySound playSound = new PlaySound();
        RaceTimer raceTimer = new RaceTimer();

        carPanel.setPlaySound(playSound);
        carPanel.setRaceTimer(raceTimer);

        Car car1 = new Car("Red car", carPanel);
        Car car2 = new Car("Blue car", carPanel);
        Car car3 = new Car("Green car", carPanel);
        Car car4 = new Car("Yellow car", carPanel);

        SemaphoreThread semaphoreThread = new SemaphoreThread(semaphorePanel);

        semaphoreThread.start();

        try {
            semaphoreThread.join();
        } catch(InterruptedException e) {
         e.printStackTrace();
       }

        raceTimer.start();
        playSound.playSound();

        car1.start();
        car2.start();
        car3.start();
        car4.start();

        try {
            car1.join();
            car2.join();
            car3.join();
            car4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
