package by.toukach.task.client;

import by.toukach.task.exception.ThreadException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс представляющий сервер.
 */
public class Server {

  private volatile List<Integer> resource = new ArrayList<>();
  private final Lock lock = new ReentrantLock();

  /**
   * Метод для обработки запросов клиента.
   *
   * @param clientRequest запрос клиента.
   * @return размер ресурсов.
   */
  public Integer handle(ClientRequest clientRequest) {

    int timeToSleep = ThreadLocalRandom.current().nextInt(100, 1000);

    try {
      Thread.sleep(timeToSleep);
    } catch (InterruptedException e) {
      throw new ThreadException("Не удалось приостановить поток", e);
    }

    lock.lock();

    try {
      resource.add(clientRequest.getValue());
      return resource.size();

    } finally {
      lock.unlock();
    }
  }
}
