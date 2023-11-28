import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class DemoThread extends JFrame {

    private Image runnerImage;
    private JButton startButton;


    private double[] distances = new double[3];
    private int[] places = new int[3];

    private int circle1Radius = 100;
    private int circle2Radius = 70;
    private int circle3Radius = 40;

    private double angle1 = 0; //угол поворота
    private double angle2 = -0.3;
    private double angle3 = -1;

    private double distance1 = 0; // Расстояние бегуна 1
    private double distance2 = 0; // Расстояние бегуна 2
    private double distance3 = 0;

    private boolean stopRunners = false; //остановка

    private RunnerThread runnerThread1; //потоки для бегунов
    private RunnerThread runnerThread2;
    private RunnerThread runnerThread3;

    public DemoThread() {
        setTitle("10 seconds to run");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        setContentPane(new RaceTrack());
        Container content = getContentPane();

        startButton = new JButton("Старт");
        startButton.setPreferredSize(new Dimension(500, 50));
        startButton.setBackground(Color.white);
        startButton.setForeground(Color.BLACK);

        startButton.addActionListener(e -> { //действия после запуска
            startButton.setVisible(false); //отключить видимость кнопки

            Timer timer = new Timer(10000, actionEvent -> stopRunners());
            timer.setRepeats(false);
            timer.start();

            Random random = new Random();

            //создания бегунов с радиусами и скоростью
            runnerThread1 = new RunnerThread(circle1Radius, 5);
            runnerThread2 = new RunnerThread(circle2Radius, 3);
            runnerThread3 = new RunnerThread(circle3Radius, 2);

            //запуск потоков
            new Thread(runnerThread1).start();
            new Thread(runnerThread2).start();
            new Thread(runnerThread3).start();

            // Обновляем скорость каждые 0.5 секунд
            Timer dynamicSpeedTimer = new Timer(500, dynamicSpeedEvent -> {
                runnerThread1.updateDynamicSpeed(random.nextInt(10) + 1);
                runnerThread2.updateDynamicSpeed(random.nextInt(10) + 1);
                runnerThread3.updateDynamicSpeed(random.nextInt(10) + 1);
            });
            dynamicSpeedTimer.setRepeats(true); //повторение таймера
            dynamicSpeedTimer.start(); //запуск таймера
        });

        content.add(startButton);
    }

    //для отрисовки бегунов и дорожек
    private class RaceTrack extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            //нахождение центра панели для бегуна
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            //отрисовка окружностей
            g.drawOval(centerX - circle1Radius, centerY - circle1Radius, 2 * circle1Radius, 2 * circle1Radius);
            g.drawOval(centerX - circle2Radius, centerY - circle2Radius, 2 * circle2Radius, 2 * circle2Radius);
            g.drawOval(centerX - circle3Radius, centerY - circle3Radius, 2 * circle3Radius, 2 * circle3Radius);

            if (runnerImage != null) {
                drawRunner(g, centerX, centerY, circle1Radius, angle1);
                drawRunner(g, centerX, centerY, circle2Radius, angle2);
                drawRunner(g, centerX, centerY, circle3Radius, angle3);
            }

        }

        //отрисовка бегуна
        private void drawRunner(Graphics g, int centerX, int centerY, int radius, double angle) {
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));

            //отрисовка бегуна с учетом координат
            g.drawImage(runnerImage, x - 10, y - 10, 20, 20, this);
        }
    }

    private class RunnerThread implements Runnable {
        private int radius;
        private int initialSpeed;
        private int dynamicSpeed; // Динамическая скорость
        private double distance = 0;



        public RunnerThread(int radius, int initialSpeed) {
            this.radius = radius;
            this.initialSpeed = initialSpeed;
            this.dynamicSpeed = initialSpeed;
        }


        @Override
        public void run() {
            long startTime = System.currentTimeMillis(); //время прошедшее с начала выполнения

            while (!stopRunners) {
                long currentTime = System.currentTimeMillis(); //рассчет прошедшего времени
                double elapsedTime = (currentTime - startTime) / 1000.0; // конвертация в секунды


                //динамическое изменение угла поворота
                angle1 += Math.toRadians(dynamicSpeed);
                angle2 += Math.toRadians(dynamicSpeed);
                angle3 += Math.toRadians(dynamicSpeed);

                distance1 += runnerThread1.dynamicSpeed * 0.5;
                distance2 += runnerThread2.dynamicSpeed * 0.5;
                distance3 += runnerThread3.dynamicSpeed * 0.5;

                repaint(); //вызов метода paintComponent для перерисовки

                try { //для управления частотой обновления экрана
                    Thread.sleep(50);
                } catch (InterruptedException exc) {
                    exc.printStackTrace();
                }
            }
        }

        public double getDistance() {
            return distance;
        }

        public void updateDynamicSpeed(int newSpeed) {
            this.dynamicSpeed = newSpeed;
        }
    }

    private void stopRunners() {
        stopRunners = true;

        distances[0] = distance1;
        distances[1] = distance2;
        distances[2] = distance3;

        for (int i = 0; i < 3; i++) {
            places[i] = i + 1;
        }

        // Сортировка
        for (int i = 0; i < 3 - 1; i++) {
            for (int j = i + 1; j < 3; j++) {
                if (distances[i] < distances[j]) {

                    double tempDistance = distances[i];
                    distances[i] = distances[j];
                    distances[j] = tempDistance;


                    int tempPlace = places[i];
                    places[i] = places[j];
                    places[j] = tempPlace;
                }
            }
        }

        // Вывод результатов в консоль
        for (int i = 0; i < 3; i++) {
            System.out.println((i + 1) + " место: Бегун " + places[i] + " - Расстояние: " + distances[i]);
        }
    }

    public static void main(String[] args) {
        try {
            DemoThread demoThread = new DemoThread();
            demoThread.runnerImage = ImageIO.read(new File("src/runner.png"));
            demoThread.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
