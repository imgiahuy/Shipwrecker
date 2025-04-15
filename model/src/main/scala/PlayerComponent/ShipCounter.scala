package PlayerComponent

case class ShipCounter(private var count: Int) {
  def value: Int = count // Expose value as immutable
  def decrease(): Unit = count -= 1
  def increase(): Unit = count += 1
  def update(value: Int): Unit = count = value // Optional: for external updates

}
