(ns com.ifesdjeen.utils.ring-buffer-test
  (:use clojure.test
        com.ifesdjeen.utils.ring-buffer))

(deftest ring-buffer-test
  (testing "Adding an item to ring buffer"
    (let [rb (ring-buffer 5)]
      (is (nil? (first rb)))

      (is (= 2 (first (conj rb 2))))
      (is (= [2 nil nil nil nil] (to-vec (conj rb 2))))
      (is (= [1 2 nil nil nil] (to-vec (conj (conj rb 1) 2))))
      (is (= '(2 nil nil nil) (next (conj (conj rb 1) 2))))
      (is (= (-> rb
                 (conj 1) (conj 2) (conj 3) (conj 4) (conj 5) (conj 6) (conj 7)
                 to-vec)
             [6 7 3 4 5])))))