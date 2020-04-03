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

(defn deck-split
  "Shuffle and split a deck of cards between num players"
  ([deck]
   (deck-split deck 2))
  ([deck num]
   (apply map vector (partition num (shuffle deck)))));; separate the shuffled deck between num players

(defn numeric-rank
  "Get the numeric rank of a card, based on its key/location? in the deck"
  [card] ; will get the rank/face of the card and give back its numeric-rank value
  (get numeric-ranks (get card :rank)))

;numeric-rank ALTERNATIVE:  (Doesn't use "numeric-ranks")
;(defn numeric-rank [card]
;  (.indexOf (get card :rank)))

;(def temp (last (new-deck))) ;;make a test card
;(get temp :rank)
;(get numeric-ranks (get temp :rank))
;(numeric-rank temp)

(defn print-card [card]
  (print-str (get card :rank) "of" (get card :suit)))

(defn round-result
  [winner winning-card loser loss-card round-num]
  "Message for the result of the round"
  (str "Round " round-num ": " winner " had " (print-card winning-card) " and took " (print-card loss-card) " from " loser "\n"))

(defn greet
  ([] (greet "human"))
  ([name] (println "Hello" name)))

(defn end-game? [p1-cards p2-cards]
  (cond
    (and (nil? (seq p1-cards)) (nil? (seq p2-cards))) (println-str "TIE! Both ran out of cards")
    (nil? (seq p2-cards)) (println-str "Player 1 won!")
    (nil? (seq p1-cards)) (println-str "Player 2 won!")
    :else false))

(defn round [p1-card p2-card]
  (let ;[[p1-suit-rank p1-rank] (map numeric-rank p1-card)
    ;[p2-suit-rank p2-rank] (map numeric-rank p2-card)]
    [p1-rank (numeric-rank p1-card)
     p2-rank (numeric-rank p2-card)]
    (cond
      (> p1-rank p2-rank) :player1
      (< p1-rank p2-rank) :player2
      (= p1-rank p2-rank) :war
      :else :ERROR ;Things are not quite working...
      )))

(defn get-sacrifices [p1-cards p2-cards]
  [(first p1-cards) (nth p1-cards 1) (nth p1-cards 2)
   (first p2-cards) (nth p2-cards 1) (nth p2-cards 2)])

(defn play-war
  ([p1-cards p2-cards]
   (greet)
   (play-war 1 p1-cards p2-cards []))
  ([rd-num p1-cards p2-cards atStake]
   (println (str "P1 count: " (count p1-cards) " and P2 count: " (count p2-cards)))
   (loop [p1-cards p1-cards
          p2-cards p2-cards]
     (if (not (end-game? p1-cards p2-cards))
       (let [p1-card (first p1-cards)
             p2-card (first p2-cards)]
        ;;This commented out block would only go through one round... could not have two expressions occur with "case"
        (case (round p1-card p2-card) ; who the winner is
          ;(while (not (end-game? (rest (rest p1-cards)) (rest (rest p2-cards)))) ;while warring and still enough cards remaining
          ;  (let [p1-sacrifices (concat p1-sacrifices (second p1-cards))
          ;        p2-sacrifices (concat p2-sacrifices (second p2-cards))
          ;        p1-remains (rest (rest p1-cards))
          ;        p2-remains (rest (rest p2-cards))
          ;        winner (round (first p1-remains) (first p2-remains))]
          ;    (while (= winner :war)
          ;      (print "WAR!" (str "Player 1's " (print-card p1-card) "wars with Player 2's " (print-card p2-card)))
          ;      (def winner (round (first p1-remains) (first p2-remains)))
          ;      )
          :war (do
                 (println (str "WAR!     P1 " p1-card " vs " p2-card ". Each player puts down 3 cards"))
                 (cond
                   (and (< (count p1-cards) 4) (< (count p2-cards) 4)) (if (< (count p1-cards) (count p2-cards))
                                                                          (println-str "Player 2 won! P1 didn't have enough cards left")
                                                                          (println-str "Player 1 won! P2 didn't have enough cards left"))
                   (< (count p1-cards) 4) (println-str "Player 2 won! P1 didn't have enough cards left")
                   (< (count p2-cards) 4) (println-str "Player 1 won! P2 didn't have enough cards left")
                   ) ;;end war cond... for if any players are unable to war
                 ;;War stuff
                 (def p1-now (rest (rest (rest p1-cards)))) ;; after players put down 3 cards at stake
                 (def p2-now (rest (rest (rest p2-cards)))) ;; after players put down 3 cards at stake
                 ;see if one doesn't have enough cards to continue
                 (play-war (inc rd-num) p1-now p2-now (get-sacrifices p1-cards p2-cards)))
          :player1 (do
                     (print (round-result "Player 1" p1-card "Player 2" p2-card rd-num))
                     (println (apply str (repeat 40 "*"))) ; line break between rounds
                     (play-war (inc rd-num) (concat (rest p1-cards) (concat [p1-card p2-card] atStake)) ; P1 pile with the cards won at the bottom/end
                            (rest p2-cards) [])) ; recur -> onto next round
          :player2 (do
                     (print (round-result "Player 2" p2-card "Player 1" p1-card rd-num))
                     (println (apply str (repeat 40 "*"))) ; line break between rounds
                     (play-war (inc rd-num) (rest p1-cards) ; recur -> onto next round
                            (concat (rest p2-cards) (concat [p2-card p1-card] atStake)) [])) ; P2 pile with cards won at bottom
         ))
         (if (end-game? p1-cards p2-cards) ;; if game is done:
           (end-game? p1-cards p2-cards)
         ))
    )))

;;use destructuring to separate the two players' piles (each with half the deck)
(let [[p1 p2] (deck-split (shuffle (new-deck)))]
  (play-war p1 p2))

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
