package example.models

import java.time.Instant

object EventType extends Enumeration {
  type EventType = Value

  val Incoming = Value("Incoming")
  val Outgoing = Value("Outgoing")
}

case class ParkingEvent(time: Instant, eventType: EventType.EventType)

object ParkingEvent {
  object Implicits {
    implicit val incomingFirst: Ordering[ParkingEvent] = (x: ParkingEvent, y: ParkingEvent) =>
      (x -> y) match {
        case (ParkingEvent(i1, EventType.Incoming), ParkingEvent(i2, EventType.Outgoing)) if i1.equals(i2) => -1
        case (ParkingEvent(i1, EventType.Outgoing), ParkingEvent(i2, EventType.Incoming)) if i1.equals(i2) => 1
        case _ => x.time.compareTo(y.time)
      }

    implicit val incomingLast: Ordering[ParkingEvent] = (x: ParkingEvent, y: ParkingEvent) =>
      (x -> y) match {
        case (ParkingEvent(i1, EventType.Outgoing), ParkingEvent(i2, EventType.Incoming)) if i1.equals(i2) => -1
        case (ParkingEvent(i1, EventType.Incoming), ParkingEvent(i2, EventType.Outgoing)) if i1.equals(i2) => 1
        case _ => x.time.compareTo(y.time)
      }
  }
}