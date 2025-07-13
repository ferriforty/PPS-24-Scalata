package scalata.domain.util

object Geometry:
  opaque type Point2D = (Int, Int)

  object Point2D:
    inline def apply(x: Int, y: Int): Point2D = (x, y)

    def unapply(p: Point2D): Some[(Int, Int)] = Some((p._1, p._2))

  extension (p: Point2D)

    def x: Int = p._1
    def y: Int = p._2

    def distanceTo(to: Point2D): Int =
      math.abs(p.x - to.x) + math.abs(p.y - to.y)

    private def neighbours: List[Point2D] =
      List(
        Point2D(p.x + 1, p.y),
        Point2D(p.x - 1, p.y),
        Point2D(p.x, p.y + 1),
        Point2D(p.x, p.y - 1)
      )

    def neighboursFiltered(f: Point2D => Boolean): List[Point2D] =
      neighbours.filter(f)

    def rangeTo(dest: Point2D): List[Point2D] =
      (for
        i <- if p.x <= dest.x then p.x to dest.x else p.x to dest.x by -1
        j <- if p.y <= dest.y then p.y to dest.y else p.y to dest.y by -1
        if i > 0 && j > 0
      yield Point2D(i, j)).toList

    def moveBy(delta: Point2D): Point2D =
      Point2D(p.x + delta.x, p.y + delta.y)
