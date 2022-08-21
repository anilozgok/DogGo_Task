//creating WalkingState model
case class WalkingState(walkingId: UUID, ownerId: UUID, dogId: UUID, address: Address,
                        deliveredBy: DeliveredBy, walkingType: ServiceType, status: OrderStatus,
                        duration: Int, note: Option[String], doorKeeperPhone: Option[String], walks: Seq[Walk],
                        operationalNote: Option[String])

//creating Walk model
case class Walk(walkId: UUID, walkerId: Option[UUID], paymentId: Option[UUID],
                status: WalkStatus, startTime: Option[DateTime], endTime: Option[DateTime],
                checkinTime: Option[DateTime], checkoutTime: Option[DateTime], confirmTime: Option[DateTime],
                heat: Option[Heat], ratingByOwner: Option[WalkRating], ratingByWalker: Option[WalkRating],
                cancelledBy: Option[UserType], distance: Option[Double], paths: Seq[Geo], pins: Seq[WalkingPin],
                ownerFeedback: Option[OwnerFeedBack], walkerFeedback: Option[WalkerFeedBack], cancelReason: Option[String])


//checking if walk halted or cancelled if it is halted or cancelled it will return true otherwise it will return false.
private def isHaltedOrCancelled(walk: Walk): Boolean = {
walk.status.equals(WalkStatus.Halted) || walk.status.equals(WalkStatus.Cancelled)
}

//checking the walk if it is nearly finished or not started if it is nearly finished it will return true it will return false.
private def isNearlyFinished(walks: Seq[Walk]): Boolean = {
val notStartedWalks: Seq[Walk] = walks.filterNot(w => w.status == WalkStatus.Finished || w.status == WalkStatus.Cancelled || w.status == WalkStatus.Halted) // getting not started walks which contains cancelled, finished and halted walks.
val isNearlyFinished: Boolean = if (notStartedWalks.size == 2) true else false // if not started walks is two walk is nearly finished and true otherwise false.
isNearlyFinished
}

// steps to follow on cancel walk command
private def cancelWalkCommand(walkingId, walkId, cancelledBy, cancelReason), ctx, state) = {
state.get.walks.find(walk => walk.walkId == walkId) match {
  case None => reject[PaymentIdReply](s"Walking:$walkingId with walk:$walkId not exists.", ctx) // case will be executed when trying to cancel walk that is not exist.
  
  // canceling walk that are not cancelled or halted and not finishing nearly.
  case Some(walk) if !isHaltedOrCancelled(walk) && !isNearlyFinished(state.get.walks) => onCommandCancelWalk(walkingId, walkId, state.get.ownerId, walk.paymentId, cancelledBy, cancelReason, ctx) 

  // canceling walk that are not cancelled or halted and finishing nearly
  case Some(walk) if !isHaltedOrCancelled(walk) && isNearlyFinished(state.get.walks) => onCommandCancelWalkWithNearlyFinished(walkingId, walkId, state.get.ownerId, walk.paymentId, cancelledBy, cancelReason, ctx) 

  //cannot cancelling walks that are not halted or cancelled
  case Some(walk) if isHaltedOrCancelled(walk) => reject[PaymentIdReply](s"Walk:$walkId can not be cancelled. Current walk status is: ${walk.status}. Only walk with not 'halted' or 'cancelled' walk statuses can be cancelled.", ctx)
}