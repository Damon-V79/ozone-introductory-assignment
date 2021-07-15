package example.models

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.Instant

class ParkingEventSpec extends AnyFlatSpec with Matchers {

  import ParkingEvent.Implicits.{incomingFirst, incomingLast}

  "incomingFirst" should "должен возвращать корректное значение при сравнении величин" in {
    val now = Instant.now
    incomingFirst.compare(ParkingEvent(now, EventType.Incoming), ParkingEvent(now, EventType.Outgoing)) shouldBe -1
    incomingFirst.compare(ParkingEvent(now, EventType.Outgoing), ParkingEvent(now, EventType.Incoming)) shouldBe 1

    incomingFirst.compare(ParkingEvent(now.minusMillis(1000), EventType.Incoming), ParkingEvent(now, EventType.Outgoing)) shouldBe -1
    incomingFirst.compare(ParkingEvent(now.plusMillis(1000), EventType.Incoming), ParkingEvent(now, EventType.Outgoing)) shouldBe 1

    incomingFirst.compare(ParkingEvent(now, EventType.Incoming), ParkingEvent(now, EventType.Incoming)) shouldBe 0
    incomingFirst.compare(ParkingEvent(now.minusMillis(1000), EventType.Incoming), ParkingEvent(now, EventType.Outgoing)) shouldBe -1
    incomingFirst.compare(ParkingEvent(now.plusMillis(1000), EventType.Incoming), ParkingEvent(now, EventType.Outgoing)) shouldBe 1

    incomingFirst.compare(ParkingEvent(now, EventType.Outgoing), ParkingEvent(now, EventType.Outgoing)) shouldBe 0
    incomingFirst.compare(ParkingEvent(now.minusMillis(1000), EventType.Outgoing), ParkingEvent(now, EventType.Incoming)) shouldBe -1
    incomingFirst.compare(ParkingEvent(now.plusMillis(1000), EventType.Outgoing), ParkingEvent(now, EventType.Incoming)) shouldBe 1
  }

  "incomingLast" should "должен возвращать корректное значение при сравнении величин" in {
    val now = Instant.now
    incomingLast.compare(ParkingEvent(now, EventType.Incoming), ParkingEvent(now, EventType.Outgoing)) shouldBe 1
    incomingLast.compare(ParkingEvent(now, EventType.Outgoing), ParkingEvent(now, EventType.Incoming)) shouldBe -1

    incomingLast.compare(ParkingEvent(now.minusMillis(1000), EventType.Incoming), ParkingEvent(now, EventType.Outgoing)) shouldBe -1
    incomingLast.compare(ParkingEvent(now.plusMillis(1000), EventType.Incoming), ParkingEvent(now, EventType.Outgoing)) shouldBe 1

    incomingLast.compare(ParkingEvent(now, EventType.Incoming), ParkingEvent(now, EventType.Incoming)) shouldBe 0
    incomingLast.compare(ParkingEvent(now.minusMillis(1000), EventType.Incoming), ParkingEvent(now, EventType.Outgoing)) shouldBe -1
    incomingLast.compare(ParkingEvent(now.plusMillis(1000), EventType.Incoming), ParkingEvent(now, EventType.Outgoing)) shouldBe 1

    incomingLast.compare(ParkingEvent(now, EventType.Outgoing), ParkingEvent(now, EventType.Outgoing)) shouldBe 0
    incomingLast.compare(ParkingEvent(now.minusMillis(1000), EventType.Outgoing), ParkingEvent(now, EventType.Incoming)) shouldBe -1
    incomingLast.compare(ParkingEvent(now.plusMillis(1000), EventType.Outgoing), ParkingEvent(now, EventType.Incoming)) shouldBe 1
  }

}
