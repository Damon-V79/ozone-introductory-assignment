package example

import example.models.CarEvent
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.Instant

class CarCounterImplSpec extends AnyFlatSpec with Matchers {

  "getMaxCars" should "должен возвращать 0, если стоянка не использовалась" in new CarCounterImplSpecWiring {
    import example.models.ParkingEvent.Implicits.incomingFirst

    val carEvents: Seq[CarEvent] = Seq.empty
    counter.getMaxCars(carEvents) shouldBe 0
  }

  /**
   *                                   max 3 cars
   *   car1      <---------------------|---------------------------------->
   *   car2                     <------|---------------------------------->
   *   car3      <---------------------|------>
   *   car4                            |                    <------------->
   *          ---+--------------+-------------+-------------+-------------+---
   *             now - 1000     now - 750     now - 500     now - 250     now
   */

  it  should "должен возвращать корректное значение, если стоянка использовалась" in new CarCounterImplSpecWiring {
    import example.models.ParkingEvent.Implicits.incomingFirst

    val now = Instant.now()
    val carEvents: Seq[CarEvent] = List(
      CarEvent(now.minusSeconds(1000), now),
      CarEvent(now.minusSeconds(750), now),
      CarEvent(now.minusSeconds(1000), now.minusSeconds(500)),
      CarEvent(now.minusSeconds(250), now)
    )
    counter.getMaxCars(carEvents) shouldBe 3
  }

  /**
   *  Для вырожденных случаев (когда в один момент времени одна машина уезжает, а вторая приезжает), есть 2 варианта решения.
   *  Рассмотрим вариант, когда покидающая стоянку машина считается еще находящейся на стоянке
   *
   *                            max 4 cars
   *   car1      <--------------|----------------------------------------->
   *   car2                     <----------------------------------------->
   *   car3      <-------------->
   *   car4      <-------------->
   *          ---+--------------+-------------+-------------+-------------+---
   *             now - 1000     now - 750     now - 500     now - 250     now
   */

  it  should "должен возвращать корректное значение, если часть машин уезжала (но считается находящейся на стоянке), а другая часть в этот момент приехала" in new CarCounterImplSpecWiring {
    import example.models.ParkingEvent.Implicits.incomingFirst

    val now = Instant.now()
    val carEvents: Seq[CarEvent] = List(
      CarEvent(now.minusSeconds(1000), now),
      CarEvent(now.minusSeconds(750), now),
      CarEvent(now.minusSeconds(1000), now.minusSeconds(750)),
      CarEvent(now.minusSeconds(1000), now.minusSeconds(750))
    )
    counter.getMaxCars(carEvents) shouldBe 4
  }

  /**
   *  Для вырожденных случаев (когда в один момент времени одна машина уезжает, а вторая приезжает), есть 2 варианта решения.
   *  Рассмотрим вариант, когда покидающая стоянку машина считается уже не находящейся на стоянке
   *
   *                            max 3 cars
   *   car1      <--------------|----------------------------------------->
   *   car2                     <----------------------------------------->
   *   car3      <-------------->
   *   car4      <-------------->
   *          ---+--------------+-------------+-------------+-------------+---
   *             now - 1000     now - 750     now - 500     now - 250     now
   */

  it  should "должен возвращать корректное значение, если часть машин уезжала и уже не считается находящейся на стоянке, а другая часть в этот момент приехала" in new CarCounterImplSpecWiring {
    import example.models.ParkingEvent.Implicits.incomingLast

    val now = Instant.now()
    val carEvents: Seq[CarEvent] = List(
      CarEvent(now.minusSeconds(1000), now),
      CarEvent(now.minusSeconds(750), now),
      CarEvent(now.minusSeconds(1000), now.minusSeconds(750)),
      CarEvent(now.minusSeconds(1000), now.minusSeconds(750))
    )
    counter.getMaxCars(carEvents) shouldBe 3
  }

  protected trait CarCounterImplSpecWiring {

    val counter = new CarCounterImpl

  }

}
