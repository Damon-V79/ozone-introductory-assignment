package example

import example.models.{CarEvent, EventType, ParkingEvent}

trait CarCounter {

  def getMaxCars(events: Seq[CarEvent])(implicit o: Ordering[ParkingEvent]): Int

}

class CarCounterImpl extends CarCounter {

  override def getMaxCars(events: Seq[CarEvent])
                         (implicit o: Ordering[ParkingEvent]): Int = {
    events
      .flatMap(ce => List(ParkingEvent(ce.start, EventType.Incoming), ParkingEvent(ce.end, EventType.Outgoing)))
      .sorted
      .foldLeft((0, 0)) {
        case ((acc, counter), ParkingEvent(_, EventType.Incoming)) =>
          val newCounterValue = counter + 1
          if(acc < newCounterValue) newCounterValue -> newCounterValue
          else acc -> newCounterValue

        case ((acc, counter), ParkingEvent(_, EventType.Outgoing)) =>
          acc -> (counter - 1)
      }
      ._1
  }

}
