package io.github.hamsters

object Implicits {
  implicit class RightEither[L, R](e: Either[L, R]) {
    def map[R2](f: R => R2) = e.right.map(f)
    def flatMap[R2](f: R => Either[L, R2]) = e.right.flatMap(f)
    def filterWith(p: (R) => Boolean) = e.right.filter(p)
    def filter(p: (R) => Boolean) = filterWith(p)
  }


}
