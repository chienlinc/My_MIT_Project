(define
    (domain pacman_simple)
    (:requirements :strips :typing :equality :adl)

    (:types pos
    )

    (:predicates
        (at ?pos - pos)
        (connected ?from ?to - pos)      
        (ghost ?pos - pos)
        (food ?pos -pos)
    )

    (:action move
        :parameters (?start ?end -pos)
        :precondition (and (at ?start) (connected ?start ?end) (not (ghost ?end))) 
        :effect (
            and (at ?end) (not (at ?start)) (not (food ?end))
        )
    )
)