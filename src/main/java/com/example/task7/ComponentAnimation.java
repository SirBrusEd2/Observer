package com.example.task7;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ComponentAnimation implements Observer {
    private GraphicsContext graphicsContext;
    private int period = 10; // По умолчанию 10 секунд
    private int lastAnimatedTime = 0;
    private boolean isActive = false;
    private Timeline timeline;
    private double progress = 0.0;

    public ComponentAnimation(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    @Override
    public void update(Subject st) {
        if (isActive) {
            TimeServer timeServer = (TimeServer) st;
            if (timeServer.getState() - lastAnimatedTime >= period) {
                animate();
                lastAnimatedTime = timeServer.getState();
            }
        }
    }

    private void animate() {
        // Очистка холста
        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());

        // Установка цвета заливки
        graphicsContext.setFill(Color.CORAL);

        // Рисование линейного индикатора
        double width = graphicsContext.getCanvas().getWidth();
        double height = graphicsContext.getCanvas().getHeight();
        double lineHeight = 20; // Высота линии индикатора
        double lineY = height / 2 - lineHeight / 2; // Вертикальное положение линии

        // Рисование фона линейного индикатора
        graphicsContext.setFill(Color.LIGHTGRAY);
        graphicsContext.fillRect(0, lineY, width, lineHeight);

        // Рисование прогресса линейного индикатора
        graphicsContext.setFill(Color.CORAL);
        graphicsContext.fillRect(0, lineY, progress * width, lineHeight);

        // Создание анимации прогресса
        timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            progress += 0.1; // Увеличиваем прогресс на 10% каждые 10 секунд
            if (progress > 1.0) {
                progress = 0.0; // Сбрасываем прогресс, если он достиг 100%
            }
            animate();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void start(int period) {
        this.period = period;
        isActive = true;
        animate(); // Запуск анимации сразу при старте
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop(); // Останавливаем анимацию
        }
        isActive = false;
        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight()); // Очищаем холст
    }
}