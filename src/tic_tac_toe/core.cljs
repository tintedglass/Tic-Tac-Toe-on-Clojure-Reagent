(ns tic-tac-toe.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def app-consts
  { :min-board-size 3
    :max-board-size 9
    :min-win-length 3
    :max-win-length 7})

(defn blank-board [n]
  (vec (repeat n (vec (repeat n :blank)))))

(defonce app-state
  (let [{:keys [min-board-size min-win-length]} app-consts]
    (atom { :board  (blank-board min-board-size)
            :game-status :active

            :board-size  min-board-size
            :win-length  min-win-length})))

(defn update-board! [new-board new-game-status]
  (swap! app-state assoc :board new-board)
  (swap! app-state assoc :game-status new-game-status))

(defn reset-app-state! [board-size win-length]
  (swap! app-state assoc :board-size board-size)
  (swap! app-state assoc :win-length win-length)
  (update-board! (blank-board board-size) :active))

(defn wins? [player]
  false)

(defn draw? []
  false)

(defn determine-game-status [board]
  (cond
    (wins? :x) :x-wins
    (wins? :o) :o-wins
    (draw?)    :draw
    :else      :active))

(defn player-move [board row column]
  (let [new-board (assoc-in board [row column] :x)
        new-game-status (determine-game-status new-board)]
    (update-board! new-board new-game-status)))

(defn blank-space-component [board row column]
  [:button {:on-click #(player-move board row column)} "B"])

(defn played-space-component [player]
  [:button {:disabled "disabled"} player])

(defn board-component-at [board row column]
 (case (get-in board [row column])
   :blank [blank-space-component board row column]
   :x     [played-space-component "X"]
   :o     [played-space-component "O"]))

(defn gameboard-component []
  (let [{:keys [board board-size]} @app-state]
    [:div
     (for [row (range board-size)]
       ^{:key row}
       [:p
         (for [column (range board-size)]
           ^{:key column}
           [board-component-at board row column])])]))

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
      {:on-click #(reset-app-state! @selected-size @selected-win-length)}  "new game"]]))

(defn status-component [game-status]
  (let [{:keys [game-status]} @app-state]
    (case game-status
      :x-wins [:p "X Wins!"]
      :o-wins [:p "O Wins!"]
      :draw   [:p "Draw"]
      :active [:p "Game On"])))

(defn tic-tac-app []
  [:div
   [:h1 "Tic Tac Toe"]
   [status-component]
   [gameboard-component]
   [new-game-component]])

(reagent/render-component [tic-tac-app]
                          (. js/document (getElementById "app")))
