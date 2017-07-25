(ns starfield.core
  (:require [quil.core :as quil]
            [quil.middleware :as qmiddleware])
  (:gen-class))

(def width 600)
(def height 600)
(def num-stars 200)


(def speed 15)

(defn ->star []
  (let [x (- (rand width)
             (/ width 2))
        y (- (rand height)
             (/ height 2))
        z (rand width)]
    {:x  x :y  y
     :sx x :sy y
     :z  z :pz z}))


(defn setup []
  (quil/frame-rate 60)
  {:stars (take num-stars (repeatedly ->star))})


(defn draw [{:keys [stars]}]

  (quil/translate (/ width 2) (/ height 2))
  (quil/background 0)
  (quil/fill 0 250 0)
  (quil/stroke 200 100 200)

  (doseq [{:keys [x y z pz] :as star} stars]
    (let [sx (quil/map-range (/ x z) 0 1 0 width)
          sy (quil/map-range (/ y z) 0 1 0 width)
          r (quil/map-range z 0 width 8 0)
          px (quil/map-range (/ x pz) 0 1 0 width)
          py (quil/map-range (/ y pz) 0 1 0 height)]
      (quil/ellipse sx sy r r)                              ;draw star
      (quil/line px py sx sy))))                            ;draw trail



(defn update-star [{:keys [z] :as star}]
  (let [{:keys [z] :as star} (-> star
                                 (assoc :pz z)
                                 (update :z #(- % speed)))]
    (if (< z 1)
      (->star)
      star)))


(defn next-state [prev-state]
  (update prev-state :stars (partial map update-star)))


(defn -main []
  (quil/defsketch starfield
                  :title "Starfield"
                  :size [width height]
                  :setup setup
                  ;:key-pressed events/key-pressed-handler
                  :update next-state
                  :draw draw
                  :features [:keep-on-top]
                  :middleware [qmiddleware/fun-mode qmiddleware/pause-on-error]))


(comment
  (quil/map-range 1 2 40 23 1))