package by.toukach.task.client;

import by.toukach.task.exception.ThreadException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Класс, представляющий клиента.
 */
public class Client {

  private volatile List<Integer> data;
  private final AtomicInteger accumulator = new AtomicInteger(0);
  private final CountDownLatch countDownLatch;
  private final Server server;
  private final ExecutorService executorService = Executors.newCachedThreadPool();

  public Client(int range) {
    data = IntStream.range(0, range)
        .boxed()
        .collect(Collectors.toList());

    countDownLatch = new CountDownLatch(range);
    server = new Server();
  }

  /**
   * Метод для запуска клиента.
   *
   * @return результат вычисления.
   * @throws InterruptedException выбрасывает исключение, когда не удалось приостановить поток.
   */
  public AtomicInteger start() throws InterruptedException {

    Lock lock = new ReentrantLock();

    while (!data.isEmpty()) {
      executorService.submit(() -> {

        lock.lock();

        if (data.isEmpty()) {
          return;
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(0, data.size());

        ClientRequest clientRequest;

        try {
          Integer value = data.get(randomIndex);
          clientRequest = ClientRequest.builder()
              .value(value)
              .build();

          data.remove(value);

        } finally {
          lock.unlock();
        }

        int timeToSleep = ThreadLocalRandom.current().nextInt(100, 500);

        try {
          Thread.sleep(timeToSleep);
        } catch (InterruptedException e) {
          throw new ThreadException("Не удалось приостановить поток", e);
        }

        Integer serverResourceSize = server.handle(clientRequest);
        accumulator.addAndGet(serverResourceSize);

        countDownLatch.countDown();
      });
    }

    countDownLatch.await();
    executorService.shutdown();

    return accumulator;
  }
}
