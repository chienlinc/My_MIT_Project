(define
    (domain pacman_mid)
    (:requirements :strips :typing :equality :adl)

    (:types pos
    )

    (:predicates
        (at ?pos - pos)
        (connected ?from ?to - pos)
        (ghost ?pos - pos)
        (food ?pos - pos)
        (capsule ?pos - pos)
        (ate_capsule)
    )

    (:action move
        :parameters (?start ?end -pos)
        :precondition 
        (and (at ?start) (connected ?start ?end) 
            (or 
                (not (ghost ?end)) 
                (and (ghost ?end) (ate_capsule))
            )
        )
        :effect (
            and (at ?end) (not (at ?start))
            (when (and (capsule ?end)) (and (not (capsule ?end)) (ate_capsule))) 
            (when (ate_capsule) (not (ghost ?end)))
            (when (food ?end) (not (food ?end)))
        )
    )
)