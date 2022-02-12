(define
    (domain pacman_hard)
    (:requirements :strips :typing :equality :adl)

    (:types pos obj
    )

    (:predicates
        (at ?pos - pos)
        (connected ?from ?to - pos)
        (ghost ?pos - pos)
        (food ?pos - pos)
        (capsule ?pos - pos)
        (capsule_effect1)
        (capsule_effect2)
    )

    (:action move
        :parameters (?start ?end -pos)
        :precondition 
        (and (at ?start) (connected ?start ?end) 
        (or
            (capsule ?end)
            (and (not (ghost ?end)) (not (food ?end)) (not (capsule ?end)) (not (capsule_effect1)) (not (capsule_effect2)))
            (and (capsule_effect1) (capsule_effect2) (not (food ?end)) (not (ghost ?end)))
            (and (capsule_effect2) (not (capsule_effect1)) (not (food ?end)) (not (ghost ?end)))
            (and (capsule_effect1) (capsule_effect2) (ghost ?end))
            (and (capsule_effect2) (not (capsule_effect1)) (ghost ?end))
            (and (not (exists (?x -pos) (ghost ?x))) (food ?end))
            (and (exists (?x -pos) (and (food ?x) (not (= ?x ?end)))) (food ?end))
        )
            
        )
        :effect (
            and (at ?end) (not (at ?start))
            (when (and (not (exists (?x -pos) (ghost ?x))) (food ?end)) (not (food ?end)))
            (when (and (exists (?x -pos) (and (food ?x) (not (= ?x ?end)))) (capsule_effect1) (capsule_effect2) (food ?end)) (and (not (capsule_effect1)) (capsule_effect2) (not (food ?end))))
            (when (and (exists (?x -pos) (and (food ?x) (not (= ?x ?end)))) (not (capsule_effect1)) (capsule_effect2) (food ?end)) (and (not (capsule_effect1)) (not (capsule_effect2)) (not (food ?end))))
            (when (and (exists (?x -pos) (and (food ?x) (not (= ?x ?end)))) (food ?end)) (not (food ?end)))
            (when (and (capsule ?end)) (and (not (capsule ?end)) (capsule_effect1) (capsule_effect2)))
            (when (and (capsule_effect1) (capsule_effect2) (not (food ?end))) (and (not (capsule_effect1)) (capsule_effect2)))
            (when (and (capsule_effect1) (capsule_effect2) (ghost ?end)) (and (not (capsule_effect1)) (capsule_effect2) (not (ghost ?end))))
            (when (and (not (capsule_effect1)) (capsule_effect2) (not (food ?end))) (and (not (capsule_effect1)) (not (capsule_effect2))))
            (when (and (not (capsule_effect1)) (capsule_effect2) (ghost ?end)) (and (not (capsule_effect1)) (not (capsule_effect2)) (not (ghost ?end))))
        )
    )
)