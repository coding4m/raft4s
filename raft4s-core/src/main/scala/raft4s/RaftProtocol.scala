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

import akka.actor.ActorRef

/**
 * @author siuming
 */
object RaftProtocol {
  trait Format extends Serializable

  case class VoteRequest(term: Long, candidate: RaftNode, lastTerm: Long, lastIndex: Long)
  case class VoteResponse(term: Long, granted: Boolean)

  case class AppendRequest(
    term: Long,
    entries: Seq[Entry],
    prevTerm: Long,
    prevIndex: Long,
    leaderId: RaftNode,
    leaderCommit: Long)
  case class AppendResponse(term: Long, index: Long, retry: Boolean, done: Boolean)

  case class InstallRequest()
  case class InstallResponse()

  case class Replay(skip: Long, limit: Int, subscriber: Option[ActorRef])
  case class Read()
  case class Write()

  case class SaveSnapshot()
  case class SaveSnapshotSuccess()
  case class SaveSnapshotFailure()

  case class LoadSnapshot()
  case class LoadSnapshotSuccess()
  case class LoadSnapshotFailure()

  case class GetClock()
  case class GetClockSuccess(clock: WriteAheadClock)
  case class GetClockFailure(cause: Throwable)

  case class AdjustClock(clock: WriteAheadClock)
  case class AdjustClockSuccess()
  case class AdjustClockFailure(cause: Throwable)
}
