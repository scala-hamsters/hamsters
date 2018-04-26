package io.github.hamsters

trait Lens[Source, Property] {self =>

  def get: Source => Property
  def set: Source => Property => Source

  def composeLens[NextProperty](next: Lens[Property, NextProperty]): Lens[Source, NextProperty] = new Lens[Source, NextProperty] {
    override def get: Source => NextProperty = source => next.get(self.get(source))
    override def set: Source => NextProperty => Source = source => target => self.set(source)(next.set(self.get(source))(target))
  }
}
