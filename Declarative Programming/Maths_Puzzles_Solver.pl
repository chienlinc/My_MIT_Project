%% Maths Puzzles Solver
%
% Author: Chienlin Chen
% Student ID: 900380
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
%% Load SWI-Prolog libary module
%
%%%%%%%%%%%%% clpfd %%%%%%%%%%%%%
%
%% all_distinct(+Vars)
%
% True iff Vars are pairwise distinct.
% 
%
%% ?X #= ?Y 
%
% The arithmetic expression X equals Y.
%
%
%% sum(+Vars, +Rel, ?Expr) 
%
% The sum of elements of the list Vars is in relation Rel to Expr.
%
%
%% transpose(+Matrix, ?Transpose) 
%
% Transpose a list of lists of the same length.
%
%
%% label(+Vars)
%
% Equivalent to labeling([], Vars). Assign a value to each variable in Vars.
%
%
%% +Vars ins +Domain  
%
% The variables in the list Vars are elements of Domain.
%
%%%%%%%%%%%%% lists %%%%%%%%%%%%%
%
%% nth1(?Index, ?List, ?Elem)
%
% Is true when Elem is the Index’th element of List. Counting starts at 1.
%
%%%%%%%%%%%%% apply %%%%%%%%%%%%%
%
%% maplist(:Goal, ?List)
%
% True if Goal is successfully applied on all matching elements of the List.

:- ensure_loaded(library(clpfd)).
:- ensure_loaded(library(lists)).
:- ensure_loaded(library(apply)).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% puzzle_solution(+Puzzle)
%
%%%%%%%%%%%%%%% Puzzle %%%%%%%%%%%%%%%%
%
% Puzzle Requirements:
%
% Requirement 1:
% All the header squares of the puzzle (plus the ignored corner square) are
% bound to integers. The upper left corner of the puzzle is not meaningful.
%
% Requirement 2:
% The row and column headings are not considered to be part of the row 
% or column, and so may be filled with a number larger than a single digit. 
%
% Requirement 3: 
% When the puzzle is originally posed, most or all of the squares 
% will be empty, with the headings filled.
%
%%%%%%%%%%%%% Constraints %%%%%%%%%%%%%
%
% Solve the puzzle that satisfying the following constraints:
% 
% Constraint 1:
% Each square to be filled in with a single digit 1–9 (zero is not permitted).
% 
% Constraint 2:
% Each row and each column contains no repeated digits.
%
% Constraint 3: 
% All squares on the diagonal line from upper left to lower right contain 
% the same value
%
% Constraint 4:
% The heading of reach row and column (leftmost square in a row and 
% topmost square in a column) holds either the sum or the product 
% of all the digits in that row or column
%
% Constraint 5:
% A proper maths puzzle will have at most one solution.

puzzle_solution(Rows) :-  
    diagonal(Rows),   
    check_valid_digits(Rows),
    check_sum_or_product(Rows),
    transpose(Rows, Columns),
    check_sum_or_product(Columns),
    distinct_elements(Rows),
    distinct_elements(Columns),
    maplist(label, Rows).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% distinct_elements(+Inputs)
%
% Check each row and each column contains no repeated digits by applying
% clpfd library function: all_distinct/1 and 
% apply library function: maplist/2 to each list in Inputs. 
% (Constraint 2)

distinct_elements([_|Inputs]) :- maplist(all_distinct, Inputs).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% same_elements(+List)
%
% Check if the elements in the List are same.

same_elements([]).
same_elements([_]).
same_elements([X,X|Xs]) :- same_elements([X|Xs]).

%% diagonal(+Rows)
%
% Remove the Header row and call diagonal(+Input, +Count, +List) 
% with Count Accumlator set to 1 and List Accumulator set to []. 
% Get all diagonal values from Input.
% When no Input, check whether the List contains same elements.
% (Constraint 3)

diagonal([_|Inputs]) :- diagonal(Inputs, 1, []).
diagonal([], _, Diagonal) :- same_elements(Diagonal).
diagonal([[_|Tail]|List_of_List], Count, Diagonal) :-
    nth1(Count, Tail, X),
    Diagonal1 = [X|Diagonal],
    Count1 is Count + 1,
    diagonal(List_of_List, Count1, Diagonal1).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% check_valid_digits(+Rows)
%
% Remove the header row and call valid _digits/1

check_valid_digits([_|Inputs]) :- valid_digits(Inputs).

%% valid_digits(+Inputs)
%
% Check input row without header is integer from 1 to 9
% (Constraint 1)

valid_digits([]).
valid_digits([[_|Tail]|List_of_List]) :-
    Tail ins 1..9 , valid_digits(List_of_List).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% check_sum_or_product(+Rows)
%
% Remove the header row and call sum_or_product/1

check_sum_or_product([_|Inputs]) :- sum_or_product(Inputs).

%% sum_or_product(+Inputs)
%
% check whether the tail of the input row is sum/product to its header.
% used clpfd library function: sum/3 
% (Constraint 4)

sum_or_product([]).
sum_or_product([[Head|Tail]|List_of_List]) :-
    (sum(Tail, #=, Head); product_list(Head, Tail)),
    sum_or_product(List_of_List).

%% product_list(?Head, ?Tail)
%
% Call product_list/3 with Accumulator set to 1.
% Check whether the tail of the input row is product to its header.

product_list(Head, Tail) :- product_list(Head, Tail, 1).
product_list(Head, [], Head).
product_list(Head, [X|Xs], A) :-
    A1 #= A * X,
    product_list(Head, Xs, A1).
    