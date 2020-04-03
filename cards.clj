(ns class452.core)

;;;; Game of War (two players)
;The cards are all dealt equally to each player.
;Each round, both players put down a card, face up, at the same time. Whoever has the highest value card, wins the round and takes both cards.
;The won cards are added to the bottom of the winners deck.
;Aces are high.
;If both cards are of equal value - three cards are dealt from each hand face down and then 1 more face up to war again. the winner takes all the cards. If this ties repeat the process again.
;The player that runs out of cards loses.

(def suites [:clubs :diamonds :hearts :spades])
(def ranks [2 3 4 5 6 7 8 9 10 :J :Q :K :A])
(def numeric-ranks {2 2 3 3 4 4 5 5 6 6 7 7 8 8 9 9 10 10 :J 11 :Q 12 :K 13 :A 14})
;(def numeric-ranks {2 2 3 3 4 4 5 5 6 6 7 7 8 8 9 9 10 10 :J 11 :Q 12 :K 13 :A 14 :spades 1 :clubs 2 :diamonds 3 :hearts 4})

;; each card is represented as a literal map
(defn new-deck []
  (map-indexed             ;
    #(assoc %2 :card-num (inc %1))  ; each card has an ID as well
    ; #(…​) - Anonymous function
    ; %: Numbers can be placed directly after the % to indicate the argument positions (1-based)
    ; Need to declare them in the order you’d expect an external caller to pass them in.
    ; Of the :card-num term/pair, Associate the (%2) second item coming in (aka :card-num key) with the first thing that came in (the previous card's card-num value) incremented
    (for [suit suites              ; for each suit...
          rank ranks] ; did not work, for each card of a suit, rank value will be 1 to 13 (order of cards matches order of rank already)
      {:suit suit :rank rank})
    ) ;;end (map-indexed...)
  )

(defn numeric-rank
  "Get the numeric rank of a card, based on its key/location? in the deck"
  [card] ; will get the rank/face of the card and give back its numeric-rank value
  (get numeric-ranks (get card :rank) 0))

;(numeric-rank :J)

(defn round
  [p1-card p2-card]
  ;(print p1-card)
  (let [[p1-suit-rank p1-rank] (map numeric-rank p1-card)
        [p2-suit-rank p2-rank] (map numeric-rank p2-card)]
    ;(println "p1-card: " (map numeric-rank p1-card))
    ;(println "p2-card: " (map numeric-rank p2-card))
    (cond
      (or (> p1-rank p2-rank) (> p1-suit-rank p2-suit-rank)) :player1
      (or (< p1-rank p2-rank) (< p1-suit-rank p2-suit-rank)) :player2
      ;(= p1-rank p2-rank) :war
      ;:else :error
      :else :player2 ;Things are not quite working...
      ; The ordering (which wins) is messed up...
      ; SOOOOO, I am just gonna say that player2 wins the round for now (will fix during next week)
      )))

(defn print-card [card]
  (print-str (get card :rank) "of" (get card :suit)))

(defn round-result
  [winner winning-card loser loss-card round-num]
  "Message for the result of the round"
  ;;'("Round" round-num ":" winner "had " (print-card winning-card) "and took" (print-card loss-card) "from" loser "\n"))
  (str "Round " round-num ": " winner " had " (print-card winning-card) " and took " (print-card loss-card) " from " loser "\n"))

;(round-result "Player 2" (first (new-deck)) "Player 1" (first (rest (new-deck))) (inc 3))

(defn greet
  ([] (greet "human"))
  ([name] (println "Hello" name)))

(defn war-game
  [p1-cards p2-cards]
  (greet)
  (def rd-num 0)
  (loop [p1-cards p1-cards
         p2-cards p2-cards]
    (cond
      (nil? (seq p2-cards)) (println-str "Player 1 won!")
      (nil? (seq p1-cards)) (println-str "Player 2 won!")
      :else
      (let [p1-card (first p1-cards)
            p2-card (first p2-cards)]
        ;;This commented out block would only go through one round... could not have two expressions occur with "case"
        (case (round p1-card p2-card) ; who the winner is
          :player1 (do
                     (inc rd-num)
                     (print (round-result "Player 1" p1-card "Player 2" p2-card rd-num))
                     (println (apply str (repeat 40 "*"))) ; line break between rounds
                     (recur (concat (rest p1-cards) [p1-card p2-card]) ; P1 pile with the cards won at the bottom/end
                            (rest p2-cards))) ; recur -> onto next round
          :player2 (do
                     (inc rd-num)
                     (print (round-result "Player 2" p2-card "Player 1" p1-card rd-num))
                     (println (apply str (repeat 40 "*"))) ; line break between rounds
                     (recur (rest p1-cards) ; recur -> onto next round
                            (concat (rest p2-cards) [p2-card p1-card]))) ; P2 pile with cards won at bottom
          ;:war (do
          ;       (print "WAR"))
          ;:error (do
          ;         (print "SOMETHING WENT WRONG. Cards were: ")
          ;         (println (print-card p1-card) (print-card p2-card)))
          )
        ;;This block used if statement... but I also did it better with usage of "case" above :)
        ;(def winner (round p1-card p2-card))
        ;(if (= winner :player1)
        ;  ;if the winner is player 1:
        ;  (do
        ;    (print-str (round-result "Player 1" p1-card "Player 2" p2-card (inc rd-num)))
        ;    (recur (concat (rest p1-cards) [p1-card p2-card])
        ;           (rest p2-cards)))
        ;  ;else, so winner is player 2:
        ;  (do
        ;    (print-str (round-result "Player 2" p2-card "Player 1" p1-card (inc rd-num)))
        ;    (recur (rest p1-cards)
        ;           (concat (rest p2-cards) [p2-card p1-card])))
        ;  ) ; end (if (= winner :player1) ...
        )) ;;end (cond...)
    ))


(defn deck-split
  "Shuffle and split a deck of cards between num players"
  ([deck]
   (deck-split deck 2))
  ([deck num]
   (apply map vector (partition num (shuffle deck)))));; separate the shuffled deck between num players

;;use destructuring to separate the two players' piles (each with half the deck)
(let [[p1 p2] (deck-split (shuffle (new-deck)))]
  (war-game p1 p2))

(def d (new-deck))
(first d)
(numeric-rank (first d))


;;;; UNUSED STUFF:
;(defn deal-card
;  "INPUTS: The deck, the hand being dealt a card, whether the card is visible (face up) or not
;  OUTPUT: (deck without the first card), (hand with new/dealt card)"
;  ([deck hand visibility] ;;NOTE: visibility option not used yet.
;   (let [card (first deck)] ;the card at top of deck (to be dealt)
;    (vec [(rest deck) (conj hand [card visibility])])))
;  ([deck hand]
;   (let [card (first deck)] ;the card at top of deck (to be dealt)
;     (vec [(rest deck) (conj hand [card])])))
;  )
;
;;;"show"/print the card at the top of the deck and then put it at the bottom
;(defn top-to-bottom [deck]
;  ((print (first deck))
;   (conj (rest deck) (first deck))))
