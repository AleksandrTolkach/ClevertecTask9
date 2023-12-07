package by.toukach.task.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Класс, представляющий запрос клиента.
 */
@Data
@Builder
@AllArgsConstructor
public class ClientRequest {

  private Integer value;
}
