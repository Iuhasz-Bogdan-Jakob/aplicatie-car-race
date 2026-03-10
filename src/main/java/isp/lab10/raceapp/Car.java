package isp.lab10.raceapp;


import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Car extends Thread {
    private String name;
    private int distance = 0;
    private CarPanel carPanel;

    public Car(String name, CarPanel carPanel) {
        //set thread name;
        setName(name);
        this.name = name;
        this.carPanel = carPanel;
    }

    public void run() {
        while (distance < 400) {
            // simulate the car moving at a random speed
            int speed = (int) (Math.random() * 10) + 1;
            distance += speed;

            carPanel.updateCarPosition(name, distance);

            try {
                // pause for a moment to simulate the passage of time
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        carPanel.carFinished(name);
    }
}

class CarPanel extends JPanel {
    private int[] carPositions;
    private String[] carNames;
    private Color[] carColors;

    private List<String> finishedCars = new ArrayList<>();
    private PlaySound playSound;
    private Map<String, Long> finishTimes = new HashMap<>();
    private RaceTimer raceTimer;


    public CarPanel() {
        carPositions = new int[4];
        carNames = new String[]{"Red car", "Blue car", "Green car", "Yellow car"};
        carColors = new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
    }

    public void setRaceTimer(RaceTimer timer) {
        this.raceTimer = timer;
    }

    public void setPlaySound(PlaySound sound) {
        this.playSound = sound;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < 4; i++) {
            int yPos = 50 + i * 50; // Vertical position of the car
            int xPos = carPositions[i]; // Horizontal position of the car
            int carSize = 30; // Size of the car

            g.setColor(carColors[i]);
            g.fillOval(xPos, yPos, carSize, carSize);
            g.setColor(Color.BLACK);
            g.drawString(carNames[i], xPos, yPos - 5);
        }
    }

    public void updateCarPosition(String carName, int distance) {
        int carIndex = getCarIndex(carName);
        if (carIndex != -1) {
            carPositions[carIndex] = distance;
            repaint();
        }
    }

    public synchronized void carFinished(String carName) {
        if (!finishedCars.contains(carName)) {
            finishedCars.add(carName);
            finishTimes.put(carName, raceTimer.time);
            System.out.println(carName + " finished in time: " + raceTimer.time * 10 + " ms");

            if (finishedCars.size() == 4) {
                raceTimer.stopTimer();
                playSound.stopSound();

                System.out.println("Total race duration: " + (raceTimer.time * 10) + " ms");
                showResults();
            }
        }
    }

    private void showResults() {
        StringBuilder sb = new StringBuilder("Final Standings:\n");
        for (int i = 0; i < finishedCars.size(); i++) {
            String name = finishedCars.get(i);
            long time = finishTimes.get(name);
            sb.append((i + 1)).append(". ").append(name).append(" - ").append(time * 10).append(" ms\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private int getCarIndex(String carName) {
        for (int i = 0; i < 4; i++) {
            if (carNames[i].equals(carName)) {
                return i;
            }
        }
        return -1;
    }
}
