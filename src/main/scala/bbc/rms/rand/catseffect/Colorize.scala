package bbc.rms.rand.catseffect

/**
 * from https://github.com/inner-product/essential-effects-code/tree/solutions/exercises/src/main/scala/com.innerproduct.ee
 */
object Colorize {
  def apply(a: Any): String =
    s"${colors(a.hashCode.abs % numColors)}$a${Console.RESET}"

  def reversed(a: Any): String =
    s"${Console.REVERSED}${apply(a)}"

  private val colors = List(
    Console.WHITE,
    Console.BLACK + Console.WHITE_B,
    Console.RED,
    Console.GREEN,
    Console.YELLOW,
    Console.BLUE,
    Console.MAGENTA,
    Console.CYAN
  )
  private val numColors = colors.size - 1
}