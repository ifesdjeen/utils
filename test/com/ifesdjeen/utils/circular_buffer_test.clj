(ns com.ifesdjeen.utils.circular-buffer-test
  (:use clojure.test
        com.ifesdjeen.utils.circular-buffer))

(deftest circular-buffer-test
  (testing "clojure.lang.ISeq"
    (let [cb (circular-buffer 5)]
      (is (nil? (first cb)))
      ;; (is (= '(2 nil nil nil nil) (conj cb 2)))
      (is (= 2 (first (conj cb 2))))
      (is (= [2] (to-vec (conj cb 2))))
      (is (= [1 2] (to-vec (conj (conj cb 1) 2))))
      (is (= '(2 nil nil nil) (next (conj (conj cb 1) 2))))


      (is (= (-> cb
                 (conj 1) (conj 2) (conj 3) (conj 4) (conj 5) (conj 6) (conj 7)
                 to-vec)
             [3 4 5 6 7]))

      (let [cb2 (conj (conj cb 1) 2)]
        (is (= 2 (nth cb2 1)))
        (is (= 10 (nth cb2 5 10))))))

  (testing "ICircularBuffer"
    (let [cb (circular-buffer 5)]
      (is (= 0 (index cb)))
      (is (= 1 (index (conj cb 2))))
      (is (= 0 (-> cb
                   (conj 1) (conj 2) (conj 3) (conj 4) (conj 5)
                   index)))
      (is (= 1 (-> cb
                   (conj 1) (conj 2) (conj 3) (conj 4) (conj 5) (conj 6)
                   index)))
      (is (-> cb
              (conj 1) (conj 2) (conj 3) (conj 4) (conj 5) (conj 6)
              full?)))
    (let [cb (-> (circular-buffer 5) (conj 1) (conj 2) (conj 3) (conj 4) (conj 5) (conj 6))]
      (is (= [4 5 6 2 100] (to-vec (conj (increment cb) 100))))
      (is (= [2 3 4 5 100] (to-vec (conj (decrement cb) 100))))
      (is (= [6 2 3 4 100] (-> cb decrement decrement (conj 100) to-vec)))
      (is (= [nil nil nil nil 100] (-> (circular-buffer 5) decrement (conj 100) to-vec))))


    (let [cb (-> (circular-buffer 5) (conj 1) (conj 2) (conj 3) (conj 4) (conj 5) (conj 6))]
      (is (= nil (current (circular-buffer 5))))
      (is (= 2 (current cb))))

    (let [cb (-> (circular-buffer 5) (conj 1) (conj 2) (conj 3) (conj 4) (conj 5) (conj 6))]
      (is (= 0 (rounds (circular-buffer 5))))
      (is (= 1 (rounds cb))))


    (is (= [] (vec (all-new (circular-buffer 5)))))
    (is (= [1 2 3] (vec (-> (circular-buffer 5) (conj 1) (conj 2) (conj 3) all-new))))
    (is (= [6 7] (vec (-> (circular-buffer 5) (conj 1) (conj 2) (conj 3) (conj 4) (conj 5) (conj 6) (conj 7) all-new))))

    (is (= [] (vec (all-old (circular-buffer 5)))))
    (is (= [] (vec (-> (circular-buffer 5) (conj 1) (conj 2) (conj 3) all-old))))
    (is (= [3 4 5] (vec (-> (circular-buffer 5) (conj 1) (conj 2) (conj 3) (conj 4) (conj 5) (conj 6) (conj 7) all-old))))))
