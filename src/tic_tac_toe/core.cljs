(ns tic-tac-toe.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(defn blank-gameboard [n]
  (vec (repeat n (vec (repeat n :blank)))))

(def app-state
  (atom { :game-board   (blank-gameboard 3)
          :game-status :active
          :board-size  3
          :win-length  3}))

(defn reset-gameboard! [board-size]
  (swap! app-state assoc :board-size board-size)
  (swap! app-state assoc :game-board (blank-gameboard board-size)))

(defn reset-game [board-size]
  (reset-gameboard! board-size))

(defn blank-space-component [row column]
  [:button "B"])

(defn played-space-component [row column player]
  [:button {:disabled "disabled"} player])

(defn board-component-at [board row column]
 (case (get-in board [row column])
   :blank [blank-space-component row column]
   :x     [played-space-component row column "X"]
   :o     [played-space-component row column "O"]))

(defn gameboard-component []
  (let [game-board (:game-board @app-state)
        board-size (:board-size @app-state)]
    [:div
     (for [row (range board-size)]
       ^{:key row}
       [:p
         (for [column (range board-size)]
           ^{:key column}
           [board-component-at game-board row column])])]))

(defn tic-tac-app []
  [:div
   [:h1 "Tic Tac Toe"]
   [gameboard-component]
   [:br]
   [:button {:on-click #(reset-game 3)}  "new game"]])

(reagent/render-component [tic-tac-app]
                          (. js/document (getElementById "app")))
