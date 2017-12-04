package io.github.hamsters;

import scala.meta._

object MetaHelper {

  def toType(arg: Type.Arg): Type = arg match {
    case Type.Arg.Repeated(tpe) => tpe
    case Type.Arg.ByName(tpe) => tpe
    case tpe: Type => tpe
  }

  def getOrCreateCompanion(claas : Any): Defn.Object = claas match {
    case Term.Block(Seq(cls@Defn.Class(_, name, _, ctor, _), companion: Defn.Object)) =>
      companion
    case cls@Defn.Class(_, name, _, ctor, _) =>
        q"object ${Term.Name(cls.name.value)} {}"
    case _ =>
        q"object ${Term.Name(claas.toString)} {}"
  }
}
