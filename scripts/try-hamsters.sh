#!/bin/sh
COURSIER_URL=https://git.io/vgvpD
test -e ~/.coursier/coursier || \
  (mkdir -p ~/.coursier && curl -L -s --output ~/.coursier/coursier $COURSIER_URL && chmod +x ~/.coursier/coursier)
~/.coursier/coursier launch -q -P \
  com.lihaoyi:ammonite_2.12.3:1.0.1 \
  io.github.scala-hamsters:hamsters_2.12:2.6.0 \
  -- --predef-code 'import io.github.hamsters._;
                    import HList._;
                    import EmptyOptionValues._;
                    import FutureOps._;
                    import Validation._;
                    import Retry._;
                    import scala.concurrent.Future' < /dev/tty
