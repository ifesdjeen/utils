(ns ^{:doc "Ciruclar buffer is FIFO buffer with a fixed size, that replaces it's oldest
  elements when if it's full.

  It acts like an immutable data structure, even though has an Java Array underneath.

  We've decided not to implement IPersistentStack interface, since in different use cases
  pop and peek may have different meanings. Instead, we implement `all-new` and `all-old`." }
  com.ifesdjeen.utils.circular-buffer)

(defprotocol ICircularBuffer
  (full? [this] "Wether collection wass filled at least once or no")
  (increment [this] "Returns CircularBuffer with same backing array, where index is incremented,
so that current element would get to new generation and wouldn't get overriden during next update.")
  (decrement [this] "Returns CircularBuffer, with same backing array, where index is decremented,
so that current element would get to old generation and get overriden during next update.")
  (index [this] "Returns current array index. Always smaller than max-size. If buffer size is 5, and
6 elements were inserted, index is 0.")
  (current [this] "Returns current (oldest) element. The one that will be overriden by next insert.")
  (to-vec [this] "Converts buffer to vector")
  (all-old [this] "Returns sequence of old values for current buffer.")
  (all-new [this] "Returns sequence of new values for current buffer.")
  (rounds [this] "Returns how many rounds current Ring Buffer made, overriding old values."))

(deftype CircularBuffer [^clojure.lang.BigInt idx ^long max-size arr _meta]
  clojure.lang.ISeq
  (first [_] (aget arr 0))
  (seq [_] (seq arr))
  (next [_] (next (seq arr)))
  ;; Adds an element, possibly overriding old elements in the buffer, if
  ;; capacity was reached
  (cons [this elem]
    (let [new-arr (make-array Object max-size)
          _ (System/arraycopy arr 0 new-arr 0 max-size)
          new-idx (.index this)]
      (aset new-arr new-idx elem)
      (CircularBuffer. (inc idx)
                       max-size
                       new-arr
                       _meta)))

  clojure.lang.Indexed
  (nth [_ i]
    (aget arr i))
  (nth [this i not-found]
    (if (and (>= i 0) (< i max-size))
      (.nth this i)
      not-found))

  clojure.lang.Counted
  (count [_] idx)

  clojure.lang.IMeta
  (meta [_] _meta)

  ICircularBuffer
  (full? [_]
    (> (rem idx max-size) 0))

  (index [_]
    (if (>= idx max-size)
      (rem idx max-size)
      idx))
  (increment [_]
    (CircularBuffer. (inc idx) max-size arr _meta))
  (decrement [_]
    (let [dec-idx (dec idx)]
      (CircularBuffer. (if (< dec-idx 0) (dec max-size) dec-idx) max-size arr _meta)))
  (current [this]
    (.nth this (index this)))
  (to-vec
    [rb]
    (vec (.arr rb)))
  (rounds [_]
    (quot idx max-size))

  (all-new [this]
    (seq
     (java.util.Arrays/copyOfRange arr 0 (index this))))

  (all-old [this]
    (if (= (rounds this) 0)
      '()
      (seq
       (java.util.Arrays/copyOfRange arr (index this) max-size)))))

(defn circular-buffer
  "Creates new Circular Buffer of specified size."
  [max-size]
  (CircularBuffer. 0 max-size (make-array Object max-size) nil))
