/*
 * Copyright 2016 - 2017 Forever High Technology <http://www.foreverht.com> - all rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package raft4s

import akka.actor.{ ActorRef, ActorSelection, ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider, FSM, Stash }
import akka.pattern._
import akka.util.Timeout
import com.typesafe.config.Config

import scala.concurrent.duration._

/**
 * @author siuming
 */
object Raft extends ExtensionId[Raft] with ExtensionIdProvider {
  override def get(system: ActorSystem) = super.get(system)
  override def lookup() = Raft
  override def createExtension(system: ExtendedActorSystem) = new Raft(system)
}
class Raft(system: ActorSystem) extends Extension {

  val replicator: ActorRef = ???

  private[raft4s] def replicatorOf(node: RaftNode): ActorSelection = {
    val protocol = system match {
      case sys: ExtendedActorSystem => sys.provider.getDefaultAddress.protocol
      case _                        => "akka.tcp"
    }

    import node._
    import RaftReplicator._
    system.actorSelection(s"$protocol://$host:$port/$Name")
  }
}
object RaftReplicator {
  val Name = "raft-replicator"

  val ElectionTimer = "election-timer"
  val HeartbeatTimer = "heartbeat-timer"

  case object ElectionDue
  case object HeartbeatDue

  sealed trait State
  case object Leader extends State
  case object Follower extends State
  case object Candidate extends State

  case object StepUp extends State
  case object StepDown extends State

  sealed trait Data
  case object EmptyData extends Data
  case class LeaderData(
    term: Long,
    voted: Option[RaftNode],
    commitTerm: Long,
    commitIndex: Long,
    lastTerm: Long,
    lastIndex: Long,
    nextIndexes: Long,
    matchIndexes: Long) extends Data
  case class FollowerData(
    term: Long,
    voted: Option[RaftNode],
    commitTerm: Long,
    commitIndex: Long,
    lastTerm: Long,
    lastIndex: Long) extends Data
  case class CandidateData(
    term: Long,
    voted: Option[RaftNode],
    commitTerm: Long,
    commitIndex: Long,
    lastTerm: Long,
    lastIndex: Long) extends Data

  case class Stay(data: Data)
  case class Goto(state: State, data: Data)
}
class RaftReplicator(wal: ActorRef, links: Seq[RaftLink]) extends FSM[RaftReplicator.State, RaftReplicator.Data] with Stash {
  import RaftProtocol._
  import RaftReplicator._

  val settings = new RaftSettings(context.system.settings.config)
  implicit val timeout = Timeout(30.seconds)

  startWith(StepUp, EmptyData)

  // step up, wait clock.
  when(StepUp) {

    case Event(GetClockSuccess, _) =>
      goto(Follower) using FollowerData(0, None, 0, 0, 0, 0) //todo
    case Event(GetClockFailure, _) =>
      stop(FSM.Failure("todo"))

    case Event(_, _) =>
      stash()
      stay()
  }

  when(Leader) {

    case Event(vote: VoteRequest, data) =>
      ???
    case Event(append: AppendRequest, data) =>
      ???
    case Event(append: AppendResponse, data) =>
      ???
    case Event(install: InstallRequest, data) =>
      ???

    case Event(Stay(data), _) =>
      stay() using data
    case Event(Goto(state, data), _) =>
      goto(state) using data
    case Event(HeartbeatDue, data) =>
      // send append entries request.
      ???
  }

  when(Follower) {

    case Event(request: ClientRequest, data) =>
      // forward to leader
      ???
    case Event(vote: VoteRequest, data) =>
      ???
    case Event(append: AppendRequest, data) =>
      ???
    case Event(install: InstallRequest, data) =>
      ???

    case Event(Stay(data), _) =>
      stay() using data
    case Event(Goto(state, data), _) =>
      goto(state) using data
    case Event(ElectionDue, data) =>
      goto(Candidate) using data
  }

  when(Candidate) {
    case Event(request: ClientRequest, data) =>
      // reject
      ???
    case Event(request: VoteRequest, data) =>
      ???
    case Event(request: VoteResponse, data) =>
      ???
    case Event(append: AppendRequest, data) =>
      ???
    case Event(install: InstallRequest, data) =>
      ???

    case Event(Stay(data), _) =>
      stay() using data
    case Event(Goto(state, data), _) =>
      goto(state) using data

    case Event(ElectionDue, _) =>
      ???
  }

  whenUnhandled {
    case Event(_, _) =>
      ???
  }

  onTransition {
    case _ -> StepUp =>
      import context.dispatcher
      (wal ? GetClock).recover {
        case e: Throwable => ???
      } pipeTo self
    case _ -> Leader =>
      setHeartbeatTimer()
      cancelElectionTimer()
    case _ -> Follower =>
      setElectionTimer()
      cancelHeartbeatTimer()
    case _ -> Candidate =>
      setElectionTimer()
      cancelHeartbeatTimer()
  }

  private def setElectionTimer() =
    setTimer(ElectionTimer, ElectionDue, null, repeat = false)

  private def cancelElectionTimer() =
    cancelTimer(ElectionTimer)

  private def setHeartbeatTimer() =
    setTimer(HeartbeatTimer, HeartbeatDue, null, repeat = true)

  private def cancelHeartbeatTimer() =
    cancelTimer(HeartbeatTimer)

  private def handleVoteRequest(context: RaftContext, request: VoteRequest) = {
    if (request.term < context.term) {
      //reject.
    }

    if (request.lastTerm < context.lastTerm) {
      // reject.
    }

    if (request.lastTerm == context.lastTerm && request.lastIndex < context.lastIndex) {
      // reject
    }

    self ! Goto(Follower, null)
  }
  private def handleVoteResponse(context: RaftContext, response: VoteResponse) = {
    ???
  }
  private def handleAppendRequest(context: RaftContext, request: AppendRequest) = {
    if (request.term > context.term) {

    }
  }
  private def handleAppendResponse(context: RaftContext, response: AppendResponse) = {

  }
  private def handleInstallRequest() = {

  }
}
class RaftSettings(config: Config) {

}

