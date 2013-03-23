(ns com.ifesdjeen.utils.seq-utils)

(defn in?
  "Checks wether `value` is in `vector`, because `core#contains?` does completely different thing."
  [vec value]
  (some #(= value %) vec))


(defmacro multi-update
  [m & update-forms]
  `(-> ~m ~@(map #(cons 'update-in %) update-forms)))

(commeny (multi-update {:a 10 :b 20 :c 30}
                       ([:a] + 1)
                       ([:b] + 1 1)
                       ([:c] + 1 2)))
