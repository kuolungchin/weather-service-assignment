/*
 * Personal Practice
 */

package assignment

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.numeric.Interval

package object openweather {
  // A string that must not be empty.
  type NonEmptyString = String Refined NonEmpty
  // A TCP port number which is valid in the range of 1 to 65535.
  type PortNumber = Int Refined Interval.Closed[W.`1`.T, W.`65535`.T]

  type ErrorOr[A] = Either[model.Error, A]
}
