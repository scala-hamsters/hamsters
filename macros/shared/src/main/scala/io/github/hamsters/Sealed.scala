package io.github.hamsters

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

object Sealed {
  def impl[T: c.WeakTypeTag](c: Context): c.universe.Apply = {
    import c.universe._
    val traitType = weakTypeOf[T].typeSymbol
    if (!traitType.asClass.isSealed) {
      c.abort(c.enclosingPosition, "Can only enumerate values of a sealed trait")
    }

    val children = traitType.asClass.knownDirectSubclasses

    Apply(
      Select(
        reify(Set).tree,
        TermName("apply")
      ),
      children.map(c => Ident(c.asInstanceOf[scala.reflect.internal.Symbols#Symbol].sourceModule.asInstanceOf[Symbol])).toList
    )
  }

  def values[T] : Set[T]  = macro impl[T]
}
