package by.toukach.task;

import by.toukach.task.client.Client;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

/**
 * Точка входа в приложение.
 */
@Slf4j
public class Main {

  /**
   * Метод, запускающий приложения.
   *
   * @param args аргументы.
   * @throws InterruptedException выбрасывает исключение, когда не удалось приостановить поток.
   */
  public static void main(String[] args) throws InterruptedException {
    int range = 100;

    Client client = new Client(range);
    AtomicInteger accumulator = client.start();

    int accumulatedInt = accumulator.get();

    log.info("expected {}", (int) ((1 + range) * ((double) range / 2)));
    log.info("actual {}", accumulatedInt);
  }
}
