  
  //creating model for WalkingState
case class WalkingState(walkingId: UUID, ownerId: UUID, dogId: UUID, address: Address,
                        deliveredBy: DeliveredBy, walkingType: ServiceType, status: OrderStatus,
                        duration: Int, note: Option[String], doorKeeperPhone: Option[String], walks: Seq[Walk],
                        operationalNote: Option[String])

//creating model for Walk 
case class Walk(walkId: UUID, walkerId: Option[UUID], paymentId: Option[UUID],
                status: WalkStatus, startTime: Option[DateTime], endTime: Option[DateTime],
                checkinTime: Option[DateTime], checkoutTime: Option[DateTime], confirmTime: Option[DateTime],
                heat: Option[Heat], ratingByOwner: Option[WalkRating], ratingByWalker: Option[WalkRating],
                cancelledBy: Option[UserType], distance: Option[Double], paths: Seq[Geo], pins: Seq[WalkingPin],
                ownerFeedback: Option[OwnerFeedBack], walkerFeedback: Option[WalkerFeedBack], cancelReason: Option[String])


// possible statuses for WalkStatus
object WalkStatus extends Enumeration {
  type WalkStatus = Value
  val Matched, Unmatched, Confirmed, Started, Finished, Cancelled, Halted = Value 
  implicit val format: Format[WalkStatus] = JsonFormats.enumFormat(this)
}

// possible statuses for OrderStatus
object OrderStatus extends Enumeration {
  type OrderStatus = Value
  val Requested, Started, Finished, Cancelled, Halted = Value 

  implicit val format: Format[OrderStatus] = JsonFormats.enumFormat(this)
}


//steps to follow when walk cancelled
  private def onEventWalkCancelled(walkId: UUID, cancelledBy: UserType, cancelReason: Option[String], state: Option[WalkingState]): Option[WalkingState] = {
    val walkingState: WalkingState = state.get // getting walking state
    val walk: Option[Walk] = walkingState.walks.find(_.walkId == walkId) // finding walks that matches walkingState
    val updatedWalk: Walk = walk.get.copy(status = WalkStatus.Cancelled, cancelledBy = Some(cancelledBy), cancelReason = cancelReason) // updating walk (becasue it is cancelled)and it's properties such as cancel reason, who cancelled etc. 
    val updatedWalks: Seq[Walk] = walkingState.walks.filterNot(_.walkId == walkId) :+ updatedWalk // getting walks that same walkingState as updatedWalk

    val allWalksHalted: Seq[Walk] = updatedWalks.filterNot(walk => walk.status == WalkStatus.Cancelled) // getting all the walks that cancelled
    val newStatus: OrderStatus.Value = if (allWalksHalted.isEmpty) {
      OrderStatus.Cancelled // if there is no cancelled walks OrderStatus wil be Cancelled
    } else if (allWalksHalted.forall(walk => walk.status == WalkStatus.Halted || walk.status == WalkStatus.Finished)) {
      OrderStatus.Finished // if all the walks are cancelled or finished OrderStatus will be Finished
    } else {
      OrderStatus.Started // if walks is not cancelled or finished OrderStatus will be Started.
    }

    Some(walkingState.copy(status = newStatus, walks = updatedWalks)) // cancelling walk and assigning new status
  }



