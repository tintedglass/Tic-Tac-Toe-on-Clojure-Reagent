(ns tic-tac-toe.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def app-consts
  { :min-board-size 3
    :max-board-size 9
    :min-win-length 3
    :max-win-length 7})

(defn blank-gameboard [n]
  (vec (repeat n (vec (repeat n :blank)))))

(defonce app-state
  (atom { :game-board   (blank-gameboard 3)
          :game-status :active
          :board-size (:min-board-size app-consts)
          :win-length (:min-win-length app-consts)}))

(defn reset-game! [board-size win-length]
  (swap! app-state assoc :board-size board-size)
  (swap! app-state assoc :win-length board-size)
  (swap! app-state assoc :game-board (blank-gameboard board-size)))

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

(defn select-component [value-atom options]
  [:select
    {:on-change #(reset! value-atom (int (-> % .-target .-value)))}
    (for [option options]
      ^{:key option} [:option option])])

(defn new-game-component []
  (let [{:keys [min-board-size
                min-win-length
                max-board-size
                max-win-length]} app-consts
        selected-size (atom min-board-size)
        selected-win-length (atom min-win-length)]
    [:div.new-game-component
     [:label "Board Size"
       [select-component selected-size (range min-board-size (inc max-board-size))]]
     [:label "Win Length"
       [select-component selected-win-length (range min-win-length (inc max-win-length))]]
     [:button
      {:on-click #(reset-game! @selected-size @selected-win-length)}  "new game"]]))

(defn tic-tac-app []
  [:div
   [:h1 "Tic Tac Toe"]
   [gameboard-component]
   [new-game-component]])

(reagent/render-component [tic-tac-app]
                          (. js/document (getElementById "app")))
