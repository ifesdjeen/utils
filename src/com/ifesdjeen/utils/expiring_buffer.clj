(ns ^{:doc "Expiring buffer is a collection that supports expiring based
  on a funciton." }
  com.ifesdjeen.utils.expiring-buffer)

;;
;; Implementation
;;
(defn- nonchunked-butlast
  [v]
  (subvec (vec v) 0 (- (count v) 1)))

(defn- nonchunked-next
  [v]
  (vec (subvec v 1 (count v))))

(defn- nonchunked-subvec
  [v from]
  (vec (subvec v from (count v))))

(defn- first-nonexpired-index
  [expire-fn v]
  (if (not (empty? v))
    (let [l (count v)]
      (loop [i 0]
        (if (and (< (inc i) l) (expire-fn (get v i)))
          (recur (inc i))
          i)))
    0))

(defn- expire-intern
  [expire-fn coll]
  (nonchunked-subvec coll (first-nonexpired-index expire-fn coll)))

;;
;; API
;;

(defprotocol IExpiringBuffer
  (expire [_])
  (to-vec [this] "Converts buffer to vector"))

(deftype ExpiringBuffer [expire-fn coll _meta]
  clojure.lang.ISeq
  (first [_] (first coll))
  (next [_] (ExpiringBuffer. expire-fn (nonchunked-next coll) _meta))
  (seq [_] (seq coll))
  (cons [this v]
    (let [new-coll (expire-intern expire-fn coll)]
      (ExpiringBuffer. expire-fn (conj new-coll v) _meta)))

  clojure.lang.Indexed
  (nth [_ i]
    (nth coll i))
  (nth [this i not-found]
    (nth coll i not-found))

  clojure.lang.Counted
  (count [_] (count coll))

  clojure.lang.IMeta
  (meta [_] _meta)

  IExpiringBuffer
  (to-vec [_]
    coll)

  (expire [_]
    (ExpiringBuffer. expire-fn (expire-intern expire-fn coll) _meta))

  Object
  (toString [_]
    coll))
