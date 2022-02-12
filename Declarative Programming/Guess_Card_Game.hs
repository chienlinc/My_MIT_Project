{- Project 2: Guess Card Game

Author: ChienLin Chen
Student ID: 900380

----------------------------------------------------------------------

The program implement code for both the answerer and guesser parts 
of the game. The answerer selects some numbers of cards to be the
answer for the game in the begining. Then, the guesser will repeatedly 
respond with a guess until he/she guess the correct answer. The aim is 
to guess the answer with the fewest possible guesses. 

In the guesser part, first, initialGuess function will provide an initial 
guess for the game. Then, the program will called nextGuess function 
repeatedly until it produces the answer for the guess. The nextGuess 
function analyze previous guess feedback to choose the best next guess. 
The feedback is gotten from the feedback funciton.

Constraints:
1. A complete standard deck of western playing cards (without jokers)
2. Two 1 - 4 cards are picked by answerer
3. Cards are from a single deck so cards cannot be repeated in either 
answer or guess
4. 10 seconds per test

-}

----------------------------------------------------------------------
----------------------------------------------------------------------

-- Haskell module

module Proj2 (feedback, initialGuess, nextGuess, GameState) where

----------------------------------------------------------------------

-- import module

import Card
import Data.List

----------------------------------------------------------------------
----------------------------------------------------------------------

-- To keep information between guesses

type GameState = [[Card]]

----------------------------------------------------------------------
----------------------------------------------------------------------

{- feedback: (1,2,3,4,5)

After the guesser tell the answerer the guess they decided, the answerer
reponds with five numbers as feedback

1. correct cards: The number of cards in the answer are also in the guess

2. lower_ranks: The number of cards in the answer have lower rank than
the lowest rank in the guess

3. correct ranks: The number of cards in the answer have the same rank as
a card in the guess. Only counting a card in the guess once

4. higher ranks: The number of cards in the answer have rank higher than
the highest rank in the guess 

5. correct suits: The number of cards in the answer have the same suit as 
a card in the guess. Only counting a card in the guess once

-}

feedback :: [Card] -> [Card] -> (Int, Int, Int, Int, Int)
feedback answer guess
    = (correct_cards, lower_ranks, correct_ranks, higher_ranks, correct_suits)
    where
        correct_cards = count_correct_cards answer guess
        lower_ranks = count_lower_ranks answer guess
        correct_ranks = count_correct_ranks answer guess
        higher_ranks = count_higher_ranks answer guess
        correct_suits = count_correct_suits answer guess

----------------------------------------------------------------------

-- To get the 1st feedback number

count_correct_cards :: [Card] -> [Card] -> Int
count_correct_cards xs ys = length (xs `intersect` ys)


-- To get the 2nd feedback number

count_lower_ranks :: [Card] -> [Card] -> Int
count_lower_ranks xs ys
    = length (filter (<min_rank) answer_ranks)
    where
        min_rank = minimum (map getRank ys)
        answer_ranks = map getRank xs


-- To get the 3rd feedback number

count_correct_ranks :: [Card] -> [Card] -> Int
count_correct_ranks xs ys
    = count_elements (map getRank xs) (map getRank ys)


-- To get the 4th feedback number

count_higher_ranks :: [Card] -> [Card] -> Int
count_higher_ranks xs ys
    = length (filter (>max_rank) answer_ranks)
    where
        max_rank = maximum (map getRank ys)
        answer_ranks = map getRank xs


-- To get the 5th feedback number

count_correct_suits :: [Card] -> [Card] -> Int
count_correct_suits xs ys
    = count_elements (map getSuit xs) (map getSuit ys)

----------------------------------------------------------------------

-- Get the Rank of the card

getRank :: Card -> Rank
getRank (Card _ rank) = rank


-- Get the Suit of the card

getSuit :: Card -> Suit
getSuit (Card suit _ ) = suit


-- To count a card that has the same rank/suit as the guess once

count_elements :: Eq a => [a] -> [a] -> Int
count_elements _ [] = 0
count_elements [] _ = 0
count_elements (x:xs) ys
    | elem x ys = 1 + count_elements xs (delete x ys)
    | otherwise = count_elements xs ys

----------------------------------------------------------------------
----------------------------------------------------------------------

{- initialGuess (number_of_cards)

Provide an initial guess based on "Hint 4" and the number of cards 
to be guessed. Then, return an initial game state.

Hint 4:
1. Choose n cards of different suits
2. Choose n cards with 13/(n+1) ranks about equally distant from each
   other and from the top and bottom ranks.

-}

initialGuess :: Int -> ([Card], GameState)
initialGuess n
    | n <= 0 = error "Invalid Input"
    | otherwise = (initialGuess, initialGameState)
    where
        suits = take n [Club .. Spade]
        ranks = take n (chunks (13 `div` (n+1)) [R2 .. Ace])
        initialGuess = zipWith Card suits ranks
        all_cards = [Card s r | s <- [Club .. Spade], r <- [R2 .. Ace]]
        initialGameState = delete initialGuess (initialize_game_state n all_cards)

----------------------------------------------------------------------

-- Chunk and select the ranks that is 13/(n+1) distant between each others 
-- and from the top and bottom ranks

chunks :: Int -> [a] -> [a]
chunks _ [] = []
chunks distant xs
    | distant > length xs = []
    | otherwise = y:chunks distant zs
    where (y:zs) = drop (distant-1) xs


-- Initialize the gamestate by generating and merging cards n times

initialize_game_state :: Int -> [Card] -> [[Card]]
initialize_game_state n cards@(c:cs)
    | n == 1 = [[x] |x <- cards]
    | otherwise = [x++y | x <- front, y <- back]
    where
        front = [[x] |x <- cards]
        back = initialize_game_state (n-1) cs

----------------------------------------------------------------------
----------------------------------------------------------------------

{- nextGuess (previous_guess, previous_gamestate) previous_feedback

Use the feeback from previous guess and gamestate to determine the next guess
by applying ideas in "Hint 2" and "Hint 3". Then, return the updated gamestate. 
This function will called repeatedly until it provides the correct answer.

Hint 2: Only make guesses that are consistent with the feedback that reveived
        from previous guess. (possible_answers)

Hint 3: Choose the best next guess that leave the smallest list of 
        possible answers
1. Given a candidate guess, compute the feedback for each answer in 
   possible answers
2. Group all answers' feedback
3. Choose the guess that has smallest expected number of remaining possible
   answers which is determined by the sum of the squares of the group sizes
   divided by the sum of group sizes

-}

nextGuess :: ([Card], GameState) -> (Int, Int, Int, Int, Int) -> ([Card], GameState)
nextGuess (preGuess, preGameState) preFeedback = (nextGuess, nextGameState)
    where
        possible_answers = [x | x <- preGameState, feedback x preGuess == preFeedback]
        nextGuess = chooseCandidate possible_answers
        nextGameState = delete nextGuess possible_answers
        
----------------------------------------------------------------------

-- Choose the best next guess which return the smallest group size. Due to 
-- the time limit of 10 seconds, when the number of possible answers is greater 
-- than 2000, the first half of them is chosen as new possible answers

chooseCandidate :: [[Card]] -> [Card]
chooseCandidate xs
    | length xs > 2000 = xs !! ((length xs) `div` 2)
    | otherwise = nextGuess
    where
        all_guess_sizes = [(getSize x xs, x) | x <- xs]
        (_, nextGuess) = head (sort all_guess_sizes)
        
-- Get the group size by computing the feedback for each answer in possible
-- answers. Then, group these feedbacks and compute the group size.

getSize :: [Card] -> [[Card]] -> Int
getSize guess possible_answers = size
    where
        answer_feedbacks = [feedback x guess | x <- possible_answers]
        group_answer_feedbacks = group (sort answer_feedbacks)
        group_length_answer_feedbacks = map length group_answer_feedbacks
        (s, ss) = sum_sumsquare group_length_answer_feedbacks
        size = ss `div` s

-- Get the sum of squares and sum from a list of groups' length

sum_sumsquare :: [Int] -> (Int, Int)
sum_sumsquare [] = (0,0)
sum_sumsquare (x:xs) = 
    let (s, ss) = sum_sumsquare xs
    in (s+x, ss+x*x)
    