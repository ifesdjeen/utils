(ns com.ifesdjeen.utils.services-test
  (:use clojure.test
        com.ifesdjeen.utils.services))

(deftest test-service
  (reset-services!)
  (register-service :test-service
                    {:config (fn [] (atom 1))
                     :start (fn [a] (swap! a + 2) (while true ))
                     :stop (fn [a] (swap! a + 3))})
  (start-all!)
  (Thread/sleep 100)
  (is (.isAlive (thread (:test-service @services))))
  (is (= 3 @(conf (:test-service @services))))

  (stop-all!)
  (is (not (.isAlive (thread (:test-service @services)))))
  (is (= 6 @(conf (:test-service @services)))))