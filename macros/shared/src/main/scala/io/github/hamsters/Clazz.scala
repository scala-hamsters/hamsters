package io.github.hamsters

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros
object Clazz {

  def impl[T: c.WeakTypeTag](c: Context) = {
    import c.universe._
    val traitType = weakTypeOf[T].typeSymbol.name.toString
    c.Expr[String](q"""$traitType""")
  }

  def getSimpleName[T] : String  = macro impl[T]

}