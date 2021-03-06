/*
 * Copyright 2015 Heiko Seeberger
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

package de.heikoseeberger.akkasse

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.unmarshalling.{ FromEntityUnmarshaller, Unmarshaller }
import akka.http.scaladsl.util.FastFuture
import akka.stream.scaladsl.Source
import scala.concurrent.ExecutionContext

/**
 * Importing [[EventStreamUnmarshalling.fromEntityUnmarshaller]] lets an `akka.http.model.HttpEntity`
 * with a `text/event-stream` media type be unmarshallable to an `akka.stream.scaladsl.Source[ServerSentEvent]`.
 *
 * ``Attention``: An implicit `scala.concurrent.ExecutionContext` has to be in scope!
 */
object EventStreamUnmarshalling extends EventStreamUnmarshalling

/**
 * Mixing in this trait lets an `akka.http.model.HttpEntity`
 * with a `text/event-stream` media type be unmarshallable to an `akka.stream.scaladsl.Source[ServerSentEvent]`.
 *
 * '''Attention''': An implicit `scala.concurrent.ExecutionContext` has to be in scope!
 */
trait EventStreamUnmarshalling {

  private val maxSize = bufferMaxSize

  /**
   * The maximum buffer size for the event Stream parser; 8KiB by default. It bounds the line size of individual
   * events, i.e. the number of bytes before a line break.
   */
  protected def bufferMaxSize: Int = 8192

  implicit final def fromEntityUnmarshaller: FromEntityUnmarshaller[Source[ServerSentEvent, Any]] = {
    def source(entity: HttpEntity) = {
      val source = entity
        .dataBytes
        .transform(() => new LineParser(maxSize))
        .transform(() => new ServerSentEventParser(maxSize))
      FastFuture.successful(source)
    }
    Unmarshaller(_ => source).forContentTypes(MediaTypes.`text/event-stream`)
  }
}
