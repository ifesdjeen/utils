(ns com.ifesdjeen.utils.expiring-buffer
  (:use clojure.test
        com.ifesdjeen.utils.expiring-buffer))

(deftest expiring-buffer-test
  (testing :conj
    (is (=
         [20 30 40 50]
         (to-vec (conj
                  (expiring-buffer (fn [a] (> 20 a)) [10 20 30 40])
                  50)))))

  (testing :expire
    (is (= [10 20 30 40]
           (to-vec (expire (expiring-buffer (fn [a] (> 1 a)) [10 20 30 40])))))
    (is (= [20 30 40]
           (to-vec (expire (expiring-buffer (fn [a] (> 20 a)) [10 20 30 40])))))
    (is (= [40]
           (to-vec (expire (expiring-buffer (fn [a] (>= 30 a)) [10 20 30 40]))))))

  (testing :next
    (is (= [20 30 40]
           (to-vec (next (expiring-buffer (fn [a] (> 1 a)) [10 20 30 40]))))))

  (testing :count
    (is (= 4
           (count (expiring-buffer (fn [a] (> 1 a)) [10 20 30 40])))))

  (testing :nth
    (is (= 10 (nth (expiring-buffer (fn [a] (> 1 a)) [10 20 30 40]) 0)))
    (is (= 20 (nth (expiring-buffer (fn [a] (> 1 a)) [10 20 30 40]) 1)))
    (is (= "yo" (nth (expiring-buffer (fn [a] (> 1 a)) [10 20 30 40]) 10 "yo")))))