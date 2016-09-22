package io.github.hamsters

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object UnionMacro {


  def impl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    def argByXY(idx: Int, i: Int): c.universe.Tree = if (idx == i) {
      q"""Some(t)"""
    }
    else {
      q"""None"""
    }

    val result = {
      annottees.map(_.tree).toList match {
        case q"class $name[..$tpes](..$lFields) extends ..$parents  { ..$body }" :: Nil =>
          val typesName: Seq[c.universe.TypeName] = tpes.map { case TypeDef(_, typeName, _, _) => typeName }
          q"""
            class $name[..$tpes](..$lFields) extends ..$parents   {
              ..$body
             ..${
            typesName.zipWithIndex.map { case (currentTypeName, y) =>

              val unionXType2UnionXMethodName = TermName("toUnion" + y)

              val unionXType =q"""${TermName("Union" + tpes.size)}"""
              val constructorArgs = for (x <- typesName.indices) yield q"""${argByXY(x, y)}"""

              val manifests: Seq[c.universe.Tree] = tpes.zipWithIndex.map { case (TypeDef(_, typeName, _, _), indice) =>
                val manifestTypeName = AppliedTypeTree(Ident(TypeName("Manifest")), List(Ident(TypeName("T1"))))
                val manifestVariableName = TermName(s"m$indice")
                q"""$manifestVariableName :  $manifestTypeName"""
              }

              q"""
               implicit def $unionXType2UnionXMethodName(t: $currentTypeName)(implicit ..$manifests) = $unionXType[..$typesName](..$constructorArgs);
               """
            }
          }
            }
          """
        case _ => c.abort(c.enclosingPosition, "UnionMacro annotation only works on class")
      }
    }
    c.Expr[Any](result)

  }
}

class UnionMacro extends StaticAnnotation {
  def macroTransform(annottees: Any*) = macro UnionMacro.impl
}
