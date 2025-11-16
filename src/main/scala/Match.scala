import java.time.LocalDateTime

object FoodDonationMatcher {

  /** Match surplus food from restaurants to nearby charities.
    *
    * @param restaurants
    *   List of restaurants with posted surplus food
    * @param charities
    *   List of charities
    * @param maxDistanceKm
    *   Maximum distance (in km) for matching
    * @return
    *   List of MatchResult
    */
  def matchFoodToCharity(
      restaurants: List[Restaurant],
      charities: List[Charity],
      maxDistanceKm: Double = 10.0
  ): List[MatchResult] = {
    // Keep track of matched surplus food to avoid duplicate matches
    val matches = scala.collection.mutable.ListBuffer[MatchResult]()
    val matchedSurplusIds = scala.collection.mutable.Set[String]()

    for {
      restaurant <- restaurants
      surplusFood <- restaurant.surplusFoodPosted
      if !matchedSurplusIds.contains(surplusFood.id)
    } {
      // Find the first charity that accepts this surplus and is nearby
      charities
        .find(charity =>
          isFoodTypeAcceptable(charity, surplusFood) &&
            isNearby(restaurant.location, charity.location, maxDistanceKm)
        )
        .foreach { charity =>
          matches += MatchResult(
            restaurantId = restaurant.id,
            charityId = charity.id,
            surplusFoodId = surplusFood.id,
            matchedAt = LocalDateTime.now()
          )
          matchedSurplusIds += surplusFood.id // prevent double matching
        }
    }

    matches.toList
  }

  /** Check if the charity accepts the food type */
  def isFoodTypeAcceptable(
      charity: Charity,
      surplusFood: SurplusFood
  ): Boolean = {
    charity.acceptedFoodTypes.contains(surplusFood.foodType)
  }

  /** Check if the charity is within the given distance from the restaurant */
  def isNearby(
      loc1: Location,
      loc2: Location,
      maxDistanceKm: Double
  ): Boolean = {
    calculateDistanceKm(loc1, loc2) <= maxDistanceKm
  }

  /** Calculate Haversine distance between two locations in kilometers */
  def calculateDistanceKm(loc1: Location, loc2: Location): Double = {
    val R = 6371.0 // Earth radius in km
    val lat1 = Math.toRadians(loc1.latitude)
    val lon1 = Math.toRadians(loc1.longitude)
    val lat2 = Math.toRadians(loc2.latitude)
    val lon2 = Math.toRadians(loc2.longitude)

    val dLat = lat2 - lat1
    val dLon = lon2 - lon1

    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(lat1) * Math.cos(lat2) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    R * c
  }
}
