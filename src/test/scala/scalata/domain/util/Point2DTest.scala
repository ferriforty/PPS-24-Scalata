package scalata.domain.util

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Point2DTest extends AnyFlatSpec with Matchers:

  "Point2D equality" should "work correctly for same and different points" in:
    val point1 = Point2D(3, 4)
    val point2 = Point2D(3, 4)
    val point3 = Point2D(5, 6)

    point1 == point2 shouldBe true
    point1 == point3 shouldBe false

  "Point2D distanceTo" should "calculate Manhattan distance correctly" in:
    val from = Point2D(1, 1)
    val to = Point2D(4, 5)

    from.distanceTo(to) shouldBe 7
    from.distanceTo(from) shouldBe 0

  "Point2D neighborsFiltered" should "return filtered neighbors" in:
    val point = Point2D(5, 5)
    val allNeighbors = point.neighborsFiltered(_ => true)
    val rightNeighbor = point.neighborsFiltered(_.x > 5)

    allNeighbors should have size 4
    rightNeighbor should contain only Point2D(6, 5)

  "Point2D rangeTo" should "generate rectangular range" in:
    val from = Point2D(1, 1)
    val to = Point2D(2, 2)
    val range = from.rangeTo(to)

    range should contain theSameElementsAs List(
      Point2D(1, 1),
      Point2D(1, 2),
      Point2D(2, 1),
      Point2D(2, 2)
    )

  "Point2D moveBy" should "move point by offsets" in:
    val point = Point2D(3, 4)

    point.moveBy(Point2D(2, 3)) shouldBe Point2D(5, 7)
    point.moveBy(Point2D(-1, -2)) shouldBe Point2D(2, 2)
    point.moveBy(Point2D(0, 0)) shouldBe point
