import java.time.LocalDateTime

// Location data for restaurants and charities (for matching purposes)
case class Location(latitude: Double, longitude: Double)

// A type of food (e.g., perishable, non-perishable, meal, raw, etc.)
sealed trait FoodType
case object Perishable extends FoodType
case object NonPerishable extends FoodType
case object Meal extends FoodType
case object Ingredient extends FoodType

// Restaurant entity
case class Restaurant(
    id: String,
    name: String,
    location: Location,
    contactInfo: String, // Could be email or phone number
    surplusFoodPosted: List[SurplusFood] = List.empty
)

// Charity entity
case class Charity(
    id: String,
    name: String,
    location: Location,
    contactInfo: String, // Could be email or phone number
    acceptedFoodTypes: Set[FoodType] = Set.empty,
    acceptedDonations: List[Donation] = List.empty
)

// Surplus food posted by a restaurant
case class SurplusFood(
    id: String,
    restaurantId: String, // Reference to the Restaurant
    foodType: FoodType,
    description: String, // E.g., "5 boxes of unsold sandwiches"
    quantity: Int, // E.g., 10 sandwiches
    expiryDate: Option[LocalDateTime], // For perishable foods
    postedAt: LocalDateTime
)

// Donation made by a restaurant to a charity
case class Donation(
    id: String,
    surplusFoodId: String, // Reference to the SurplusFood
    charityId: String, // Reference to the Charity
    quantityDonated: Int,
    donationDate: LocalDateTime
)

// Match result between a charity and available surplus food
case class MatchResult(
    restaurantId: String,
    charityId: String,
    surplusFoodId: String,
    matchedAt: LocalDateTime
)

// Analytics results for tracking donations
case class DonationAnalytics(
    totalDonations: Int,
    totalQuantityDonated: Int,
    mostDonatedFoodType: FoodType,
    restaurantStats: Map[
      String,
      Int
    ], // Map of restaurant ID to number of donations made
    charityStats: Map[
      String,
      Int
    ] // Map of charity ID to number of donations received
)
