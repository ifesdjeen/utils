(ns com.ifesdjeen.utils.expiring-buffer
  (:use clojure.test
        com.ifesdjeen.utils.expiring-buffer))

(deftest expiring-buffer-test
  (testing :conj
    (is (=
         [20 30 40 50]
         (to-vec (conj
                  (ExpiringBuffer. (fn [a] (> 20 a)) [10 20 30 40] nil)
                  50)))))

  (testing :expire
    (is (= [10 20 30 40]
           (to-vec (expire (ExpiringBuffer. (fn [a] (> 1 a)) [10 20 30 40] nil)))))
    (is (= [20 30 40]
           (to-vec (expire (ExpiringBuffer. (fn [a] (> 20 a)) [10 20 30 40] nil)))))
    (is (= [40]
           (to-vec (expire (ExpiringBuffer. (fn [a] (>= 30 a)) [10 20 30 40] nil))))))

  (testing :next
    (is (= [20 30 40]
           (to-vec (next (ExpiringBuffer. (fn [a] (> 1 a)) [10 20 30 40] nil))))))

  (testing :count
    (is (= 4
           (count (ExpiringBuffer. (fn [a] (> 1 a)) [10 20 30 40] nil)))))

  (testing :nth
    (is (= 10 (nth (ExpiringBuffer. (fn [a] (> 1 a)) [10 20 30 40] nil) 0)))
    (is (= 20 (nth (ExpiringBuffer. (fn [a] (> 1 a)) [10 20 30 40] nil) 1)))
    (is (= "yo" (nth (ExpiringBuffer. (fn [a] (> 1 a)) [10 20 30 40] nil) 10 "yo")))))