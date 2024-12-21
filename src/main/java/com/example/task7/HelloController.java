package com.example.task7;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField textField;
    @FXML
    private Button textStartButton;
    @FXML
    private Button textStopButton;
    @FXML
    private TextField bellField;
    @FXML
    private Button bellStartButton;
    @FXML
    private Button bellStopButton;
    @FXML
    private Button clockStartButton;
    @FXML
    private Button clockStopButton;
    @FXML
    private Canvas canvas;
    @FXML
    private TextField delayField; // Добавляем поле для задержки
    @FXML
    private Button delayStartButton; // Добавляем кнопку для запуска с задержкой

    private TimeServer timeServer;
    private ComponentText componentText;
    private ComponentMusic componentMusic;
    private ComponentAnimation componentAnimation;

    @FXML
    public void initialize() {
        timeServer = new TimeServer();
        componentText = new ComponentText(textField);

        // Инициализация ComponentMusic
        int bellDelay = 5; // Значение по умолчанию
        if (!bellField.getText().isEmpty()) {
            try {
                bellDelay = Integer.parseInt(bellField.getText());
            } catch (NumberFormatException e) {
                statusLabel.setText("Ошибка: неверное значение для задержки музыки");
            }
        }
        componentMusic = new ComponentMusic(bellDelay);

        // Инициализация ComponentAnimation
        componentAnimation = new ComponentAnimation(canvas.getGraphicsContext2D());

        // Подписка наблюдателей на таймер
        timeServer.attach(componentText);
        timeServer.attach(componentMusic);
        timeServer.attach(componentAnimation);
    }

    @FXML
    public void start() {
        try {
            timeServer.start();
            statusLabel.setText("Таймер активен");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Ошибка при запуске таймера: " + e.getMessage());
        }
    }

    @FXML
    public void stop() {
        timeServer.stop();
        componentAnimation.stop(); // Останавливаем анимацию
        componentMusic.stop(); // Останавливаем музыку
        statusLabel.setText("Таймер остановлен");
    }

    @FXML
    public void reset() {
        timeServer.reset();
        componentAnimation.stop(); // Останавливаем анимацию
        componentMusic.stop(); // Останавливаем музыку
        statusLabel.setText("Таймер сброшен");
    }

    @FXML
    public void startText() {
        componentText.start();
    }

    @FXML
    public void stopText() {
        componentText.stop();
    }

    @FXML
    public void startBell() {
        try {
            int bellDelay = Integer.parseInt(bellField.getText());
            componentMusic.start(bellDelay);
        } catch (NumberFormatException e) {
            statusLabel.setText("Ошибка: неверное значение для задержки музыки");
        }
    }

    @FXML
    public void stopBell() {
        componentMusic.stop();
    }

    @FXML
    public void startClock() {
        if (componentAnimation != null) {
            componentAnimation.start(20); // По умолчанию 20 секунд
        } else {
            System.err.println("ComponentAnimation не инициализирован!");
        }
    }

    @FXML
    public void stopClock() {
        componentAnimation.stop();
    }

    @FXML
    public void startWithDelay() {
        try {
            int delay = Integer.parseInt(delayField.getText());
            timeServer.startWithDelay(delay);
            statusLabel.setText("Таймер запущен с задержкой " + delay + " секунд");
        } catch (NumberFormatException e) {
            statusLabel.setText("Ошибка: неверное значение для задержки");
        }
    }

    public Node getAnimationRectangle() {
        return canvas;
    }
}
