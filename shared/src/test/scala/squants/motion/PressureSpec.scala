/*                                                                      *\
** Squants                                                              **
**                                                                      **
** Scala Quantities and Units of Measure Library and DSL                **
** (c) 2013-2015, Gary Keorkunian                                       **
**                                                                      **
\*                                                                      */

package squants.motion

import org.scalatest.{ Matchers, FlatSpec }
import squants.space.SquareMeters
import squants.QuantityParseException

/**
 * @author  garyKeorkunian
 * @since   0.1
 *
 */
class PressureSpec extends FlatSpec with Matchers {

  behavior of "Pressure and its Units of Measure"

  it should "create values using UOM factories" in {
    Pascals(10).toPascals should be(10)
    Bars(10).toBars should be(10)
    PoundsPerSquareInch(1).toPoundsPerSquareInch should be(1)
    StandardAtmospheres(1).toStandardAtmospheres should be(1)
  }

  it should "create values from properly formatted Strings" in {
    Pressure("10.22 Pa").get should be(Pascals(10.22))
    Pressure("10.22 bar").get should be(Bars(10.22))
    Pressure("10.22 psi").get should be(PoundsPerSquareInch(10.22))
    Pressure("10.22 zz").failed.get should be(QuantityParseException("Unable to parse Pressure", "10.22 zz"))
    Pressure("zz Pa").failed.get should be(QuantityParseException("Unable to parse Pressure", "zz Pa"))
  }

  it should "properly convert to all supported Units of Measure" in {
    val tolerance = 0.0000000000000000001
    val x = Pascals(1)
    x.toPascals should be(1)
    x.toBars should be(1e-5)
    x.toPoundsPerSquareInch should be(Newtons(1).toPoundForce / SquareMeters(1).toSquareInches +- tolerance)
    x.toStandardAtmospheres should be(1d / 101325d)
  }

  it should "return properly formatted strings for all supported Units of Measure" in {
    Pascals(1).toString(Pascals) should be("1.0 Pa")
    Bars(1).toString(Bars) should be("1.0 bar")
    PoundsPerSquareInch(1).toString(PoundsPerSquareInch) should be("1.0 psi")
    StandardAtmospheres(1).toString(StandardAtmospheres) should be("1.0 atm")
  }

  it should "return Force when multiplied by Area" in {
    Pascals(1) * SquareMeters(1) should be(Newtons(1))
  }

  behavior of "PressureConversions"

  it should "provide aliases for single unit values" in {
    import PressureConversions._

    pascal should be(Pascals(1))
    bar should be(Bars(1))
    psi should be(PoundsPerSquareInch(1))
    atm should be(StandardAtmospheres(1))
  }
  it should "provide implicit conversion from Double" in {
    import PressureConversions._

    val d = 10d
    d.pascals should be(Pascals(d))
    d.bars should be(Bars(d))
    d.psi should be(PoundsPerSquareInch(d))
    d.atm should be(StandardAtmospheres(d))
  }

  it should "provide Numeric support" in {
    import PressureConversions.PressureNumeric

    val ps = List(Pascals(100), Pascals(10))
    ps.sum should be(Pascals(110))
  }
}
