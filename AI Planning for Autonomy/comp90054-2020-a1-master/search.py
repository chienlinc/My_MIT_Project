# search.py
# ---------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).

"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()


def tinyMazeSearch(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

def depthFirstSearch(problem):
    """
    Search the deepest nodes in the search tree first.
    A sample depth first search implementation is provided for you to help you understand how to interact with the problem.
    """
    
    mystack = util.Stack()
    startState = (problem.getStartState(), '', 0, [])

    mystack.push(startState)
    visited = set()
    while mystack :
        state = mystack.pop()

        node, action, cost, path = state

        if node not in visited :
            visited.add(node)
            if problem.isGoalState(node) :
                path = path + [(node, action)]
                break;


            succStates = problem.getSuccessors(node)

            for succState in succStates :

                succNode, succAction, succCost = succState
                newstate = (succNode, succAction, cost + succCost, path + [(node, action)])

                mystack.push(newstate)
    actions = [action[1] for action in path]
    del actions[0]
    print("action")
    print(action)
    return actions

def breadthFirstSearch(problem):
    """Search the shallowest nodes in the search tree first."""
    "*** YOUR CODE HERE ***"
    myqueue = util.Queue()
    startState = (problem.getStartState(), '', 0, [])
    
    myqueue.push(startState)
    visited = set()
    while myqueue :

        state = myqueue.pop()

        node, action, cost, path = state

        if node not in visited :
            visited.add(node)
            if problem.isGoalState(node) :
                path = path + [(node, action)]
                break;
            succStates = problem.getSuccessors(node)

            for succState in succStates :

                succNode, succAction, succCost = succState
                newstate = (succNode, succAction, cost + succCost, path + [(node, action)])

                myqueue.push(newstate)
    actions = [action[1] for action in path]
    del actions[0]
    return actions

   # util.raiseNotDefined()

def uniformCostSearch(problem):

    print(problem)
    """Search the node of least total cost first."""
    "*** YOUR CODE HERE ***"
    myPQ = util.PriorityQueue()
    startState = (problem.getStartState(), '', 0, [])
    
    myPQ.push(startState, 0)
    visited = set()
    while myPQ :
        state = myPQ.pop()

        node, action, cost, path = state

        if node not in visited :
            visited.add(node)
            if problem.isGoalState(node) :
                path = path + [(node, action)]
                break;

            succStates = problem.getSuccessors(node)

            for succState in succStates :

                succNode, succAction, succCost = succState
                newstate = (succNode, succAction, cost + succCost, path + [(node, action)])
                
                newPath = path + [(node, action)] + [(succNode, succAction)]

                actions = []
                for i in range(len(newPath)):
                    actions.append(newPath[i][1])

                actions.pop(0)

                myPQ.push(newstate, problem.getCostOfActions(actions))

    actions = [action[1] for action in path]
    del actions[0]
    return actions


def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0

##########################################################################################

def aStarSearch(problem, heuristic=nullHeuristic):
    """Search the node that has the lowest combined cost and heuristic first."""
    "*** YOUR CODE HERE ***"

    myPQ = util.PriorityQueue()
    startState = (problem.getStartState(), '', 0, [])
    
    myPQ.push(startState, nullHeuristic(startState, problem))
    visited = set()
    while myPQ :
        state = myPQ.pop()

        node, action, cost, path = state

        if node not in visited :
            visited.add(node)
            if problem.isGoalState(node) :
                path = path + [(node, action)]
                break;

            succStates = problem.getSuccessors(node)

            for succState in succStates :

                succNode, succAction, succCost = succState
                newstate = (succNode, succAction, cost + succCost, path + [(node, action)])
                
                newPath = path + [(node, action)] + [(succNode, succAction)]

                actions = []
                for i in range(len(newPath)):
                    actions.append(newPath[i][1])

                actions.pop(0)

                myPQ.push(newstate, problem.getCostOfActions(actions)+nullHeuristic(newstate, problem))


    actions = [action[1] for action in path]
    del actions[0]
    return actions

    util.raiseNotDefined()

##########################################################################################

# to find the node with the lower manhattan heuristic 
def improve(problem, state):
    import searchAgents

    node, action, cost, path = state
    myqueue = util.Queue()
    myqueue.push(state)
    visited = set()

    while not myqueue.isEmpty():

        childState = myqueue.pop()
        childNode, childAction, childCost, childPath = childState

        if childNode not in visited:
            visited.add(childNode)
            
            if searchAgents.manhattanHeuristic(childNode, problem) < searchAgents.manhattanHeuristic(node, problem):

                return childState

            # find its successors from the childNode
            succStates = problem.getSuccessors(childNode)

            for succState in succStates:
                succNode, succAction, succCost = succState
        
                newstate = (succNode, succAction, childCost + succCost, childPath + [(childNode, childAction)])

                myqueue.push(newstate)

def enforcedHillClimbing(problem, heuristic=nullHeuristic):
    """COMP90054 your solution to part 1 here """

    startState = (problem.getStartState(), '', 0, [])
    node, action, cost, path = startState

    while not problem.isGoalState(node):

        # to get the node with the lower manhattan heuristic 
        startState = improve(problem, startState)
        node, action, cost, path = startState

    path = path + [(node, action)]     
    actions = [action[1] for action in path]
    del actions[0]
    
    return actions


##########################################################################################

# search for the node with minimum cost that higher than the threadhold
def search(final_path, g, bound, problem):
    import searchAgents

    state = final_path[-1]
    node, action, cost, path = state

    # node that have visited
    visited = set()
    for i in range(len(final_path)):
        visited.add(final_path[i][0])

    cur_actions = []
    for i in range(len(path)):
        cur_actions.append(path[i][1])
    if len(cur_actions) != 0:
        cur_actions.pop(0)

    # get the cost f = g + h(node)
    f = problem.getCostOfActions(cur_actions) + searchAgents.manhattanHeuristic(node, problem)

    if f > bound:
        return f

    if problem.isGoalState(node) :
        return "FOUND"
    
    min = float('inf')

    succStates = problem.getSuccessors(node)
    for succState in succStates :
        succNode, succAction, succCost = succState

        if succNode not in visited:
            
            newstate = (succNode, succAction, cost + succCost, path + [(node, action)])
            final_path.append(newstate)

            # search the childState of the succState
            t = search(final_path, (g + succCost), bound, problem)
            
            if t == "FOUND":
                return "FOUND"
            
            if t < min:
                min = t
            
            final_path.pop()
    return min

def idaStarSearch(problem, heuristic=nullHeuristic):
    """COMP90054 your solution to part 2 here """

    startState = (problem.getStartState(), '', 0, [])

    bound = nullHeuristic(startState, problem);

    final_path = [startState]

    while True:

        # start from  the root
        t = search(final_path, 0, bound, problem)

        if t == "FOUND":

            # return actions
            actions = []
            for i in range(len(final_path)):
                actions.append(final_path[i][1])
            actions.pop(0)

            return actions

        if t == float('inf'):
            return "NOT FOUND"
        
        # update the threshold
        bound = t


# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
ehc = enforcedHillClimbing
ida = idaStarSearch