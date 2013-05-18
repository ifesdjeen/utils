(ns com.ifesdjeen.utils.time-utils
  (:import [java.util Date Calendar]))

(def precision-day Calendar/DAY_OF_MONTH)
(def precision-hour Calendar/HOUR_OF_DAY)
(def precision-minute Calendar/MINUTE)
(def precision-second Calendar/SECOND)
(def precision-millisecond Calendar/MILLISECOND)

(def precisions [precision-day precision-hour precision-minute precision-second precision-millisecond])

(defn round-to
  [date precision]
  (let [calendar (Calendar/getInstance)]
    (.setTime calendar date)
    (doseq [c precisions]
      (when (< precision c)
        (.add calendar c (- (.get calendar c)))))
    (.getTime calendar)))

(defn group-aggregate
  [series accessor-date accessor-val precision agg]
  (into {}
        (map
         (fn [[k c]]
           [k (reduce agg (map accessor-val c))])
         (group-by #(round-to (accessor-date %) precision) series))))
