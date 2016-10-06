import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import io.github.hamsters.FutureOption

case class User(id: Int)
case class Data(id: Int)

object repo {
  def update(id: Int) = Future(1)
  def save(id: Int) = Future(Data(2))
  def user(userId:Int) = Future(Some(User(userId)))
}

/*
repo.user renvoit un Future[Option[User]]
repo.save renvoit un Future[Data]
repo.update renvoit un Future[Int]
 */


type Result = String
val userOption = Some(0)

val myFunction : Future[Result] =
  userOption.map { userId =>
    repo.user(userId).flatMap { userOption =>
      userOption.map { user=>
        repo.save(user.id).flatMap { data=>
          repo.update(data.id).map { _ =>
            "redirect"
          }
        }
      }.getOrElse(Future(""))
    }
  }.getOrElse(Future(""))


implicit def toFutureOption[T](o: Option[T]) = FutureOption(Future.successful(o))
implicit def toFutureOption[T](f: Future[T]) = FutureOption(f.map(Some(_)))

val myFunctionWithFor : Future[Result] =
  for {
    userId <- userOption
    userOption <- repo.user(userId)
    user <- userOption
    data <- repo.save(user.id)
    _ <- repo.update(data.id)
  } yield {
    "redirect"
  }



