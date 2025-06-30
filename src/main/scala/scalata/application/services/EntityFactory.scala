package scalata.application.services

trait EntityFactory[F <: EntityFactory[F, E, P], E, P]:
  def create(entityType: P): E
