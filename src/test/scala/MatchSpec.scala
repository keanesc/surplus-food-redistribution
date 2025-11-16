import java.time.LocalDateTime
import scala.io.Source

class MatchSpec extends munit.FunSuite {

  def loadData(): (List[Restaurant], List[Charity]) = {
    val restaurants = Source
      .fromFile("data/restaurants.csv")
      .getLines()
      .drop(1)
      .map { line =>
        val Array(id, name, lat, lon) = line.split(",").map(_.trim)
        Restaurant(
          id,
          name,
          Location(lat.toDouble, lon.toDouble),
          "",
          List(
            SurplusFood(
              s"s-$id",
              id,
              Meal,
              "Test meals",
              10,
              None,
              LocalDateTime.now()
            )
          )
        )
      }
      .toList

    val charities = Source
      .fromFile("data/charities.csv")
      .getLines()
      .drop(1)
      .map { line =>
        val Array(id, name, lat, lon) = line.split(",").map(_.trim)
        Charity(
          id,
          name,
          Location(lat.toDouble, lon.toDouble),
          "",
          Set(Meal),
          List.empty
        )
      }
      .toList

    (restaurants, charities)
  }

  test("matchFoodToCharity finds matches when charities accept Meal") {
    val (restaurants, charities) = loadData()
    val matches = FoodDonationMatcher.matchFoodToCharity(
      restaurants,
      charities,
      maxDistanceKm = 10.0
    )

    // Each surplus should match to exactly one charity (3 restaurants = 3 matches)
    assertEquals(matches.length, restaurants.length)
    assertEquals(matches.map(_.restaurantId).toSet, restaurants.map(_.id).toSet)
    // Verify no duplicate surplus matches
    assertEquals(matches.map(_.surplusFoodId).distinct.length, matches.length)
  }

  test("no matches when charities do not accept Meal") {
    val (restaurants, _) = loadData()
    val charities = Source
      .fromFile("data/charities.csv")
      .getLines()
      .drop(1)
      .map { line =>
        val Array(id, name, lat, lon) = line.split(",").map(_.trim)
        Charity(
          id,
          name,
          Location(lat.toDouble, lon.toDouble),
          "",
          Set(NonPerishable),
          List.empty
        )
      }
      .toList

    val matches = FoodDonationMatcher.matchFoodToCharity(
      restaurants,
      charities,
      maxDistanceKm = 10.0
    )
    assertEquals(matches.length, 0)
  }
}
