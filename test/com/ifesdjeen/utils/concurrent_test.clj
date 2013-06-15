(ns com.ifesdjeen.utils.core-test
  (:use clojure.test
        com.ifesdjeen.utils.concurrent))

(alter-var-root #'*out* (constantly *out*))

(deftest throttle-test
  (let [latch (make-latch 5)
        fun   (throttle #(do (countdown latch) 1) 100)]
    (fun)
    (Thread/sleep 10)
    (fun)
    (Thread/sleep 10)
    (fun)
    (Thread/sleep 10)
    (fun)
    (Thread/sleep 300)
    (is (= 4 (.getCount latch)))))
