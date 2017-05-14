package io.github.hamsters

import scala.meta._

class ValidationMacro extends scala.annotation.StaticAnnotation {


  inline def apply(defn: Any): Any = meta {

    val arityRange = Range(1, 22)

    val typeParams = List(tparam"R1", tparam"R2", tparam"R3", tparam"R4", tparam"R5", tparam"R6", tparam"R7", tparam"R8",
      tparam"R9", tparam"R10", tparam"R11", tparam"R12", tparam"R13", tparam"R14", tparam"R15", tparam"R16", tparam"R17",
      tparam"R18", tparam"R19", tparam"R20", tparam"R21", tparam"R22", tparam"R22") //no way to build tparam with String interpolation :-( see https://github.com/scalameta/scalameta/issues/230
    val typeNames = typeParams map (n => Type.Name(n.name.value))

    val params = Range(arityRange.start, arityRange.end + 1) map (i => param"""${Term.Name(s"e$i")} : ${t"""Either[L, ${Type.Name(s"R$i")}]"""} """)
    defn match {
      case q"object $t { ..$body }" =>
        val resul =
          q"""
           object $t {
            ..$body
            ..${
            arityRange.map { index =>
              q"""def result[L,..${typeParams.take(index + 1)}](..${params.take(index + 1)}) : Either[List[L], (..${typeNames.take(index + 1)})] = {
              failures(..${params.take(index + 1).map(p => Term.Name(p.name.value))}) match {
                 case Nil => Right(..${params.take(index + 1).map(p =>q"""${Term.Name(p.name.value)}.get""")})
                 case f : List[L] => Left(f)
              }
            }"""
            }
          }
           }
         """
        //abort(resul.syntax)
        resul

    }
  }
}

