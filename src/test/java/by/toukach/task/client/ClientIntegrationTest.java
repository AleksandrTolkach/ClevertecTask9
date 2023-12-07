package by.toukach.task.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ClientIntegrationTest {

  @ParameterizedTest
  @MethodSource("provideRanges")
  public void startTest(int range, int expected) throws InterruptedException {
    // given
    Client client = new Client(range);

    // when
    int actual = client.start().get();

    // then
    assertThat(actual)
        .isEqualTo(expected);
  }

  public static Stream<Arguments> provideRanges() {
    return IntStream.generate(() -> ThreadLocalRandom.current().nextInt(0, 100))
        .limit(10)
        .mapToObj(i -> Arguments.arguments(i, (int) ((1 + i) * ((double) i / 2))));
  }
}