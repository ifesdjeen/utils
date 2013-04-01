(ns com.ifesdjeen.utils.seq-utils)

(defn in?
  "Checks wether `value` is in `vector`, because `core#contains?` does completely different thing."
  [vec value]
  (some #(= value %) vec))

(defmacro multi-update
  [m & update-forms]
  `(-> ~m ~@(map #(cons 'update-in %) update-forms)))
