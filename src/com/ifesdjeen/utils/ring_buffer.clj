(ns com.ifesdjeen.utils.ring-buffer)

(deftype RingBuffer [^long idx ^long max-size arr]
  clojure.lang.ISeq
  (first [_] (aget arr (if (neg? idx) 0 idx)))
  (count [this] idx)
  (seq [this] (seq arr))
  (next [this] (next (seq arr)))
  (cons [this elem]
    (let [new-arr (make-array Object max-size)
          _ (System/arraycopy arr 0 new-arr 0 max-size)
          inc-idx (inc idx)
          new-idx (if (= inc-idx max-size)
                    (rem inc-idx max-size)
                    inc-idx)]
      (aset new-arr new-idx elem)
      (RingBuffer. new-idx
                   max-size
                   new-arr))))

(defn ring-buffer
  "Creates a new ring-buffer"
  [max-size]
  (RingBuffer. -1 max-size (make-array Object max-size)))

(defn to-vec
  [rb]
  (vec (.arr rb)))
