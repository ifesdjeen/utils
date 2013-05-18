(ns com.ifesdjeen.utils.concurrent.pausable-test
  (:import [java.util.concurrent CountDownLatch])
  (:use clojure.test
        com.ifesdjeen.utils.concurrent.pausable))

(deftest test-pausable
  (let [latch (CountDownLatch. 5)
        pausable (pausable (fn []
                             (.countDown latch)
                             (Thread/sleep 10)))]
    (is (= 4 (.getCount latch)))
    (Thread/sleep 10)
    (is (= 3 (.getCount latch)))
    (pause pausable)
    (Thread/sleep 20)
    (is (= 3 (.getCount latch)))
    (resume pausable)
    (Thread/sleep 10)
    (is (= 3 (.getCount latch)))
    (abort! pausable)
    (Thread/sleep 50)
    (is (= 3 (.getCount latch)))))
