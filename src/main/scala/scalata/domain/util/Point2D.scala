package scalata.domain.util

case class Point2D(
                    x: Int,
                    y: Int
                  ):
  def ==(other: Point2D): Boolean =
    this.x == other.x && this.y == other.y

  def distanceTo(to: Point2D): Int =
    Math.abs(this.x - to.x) + Math.abs(this.y - to.y)

  private def neighbors(): List[Point2D] =
    List(
      Point2D(this.x + 1, this.y),
      Point2D(this.x - 1, this.y),
      Point2D(this.x, this.y + 1),
      Point2D(this.x, this.y - 1)
    )

  def neighborsFiltered(f: Point2D => Boolean): List[Point2D] =
    neighbors().filter(f)

  def rangeTo(dest: Point2D): List[Point2D] =
    (for
      i <- if (this.x <= dest.x) this.x to dest.x else this.x to dest.x by -1
      if i > 0
      j <- if (this.y <= dest.y) this.y to dest.y else this.y to dest.y by -1
      if j > 0
    yield Point2D(i, j)).toList

  def moveBy(dx: Int, dy: Int): Point2D =
    Point2D(this.x + dx, this.y + dy)
