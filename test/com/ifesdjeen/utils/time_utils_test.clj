(ns com.ifesdjeen.utils.time-utils-test
  (:use clojure.test
        com.ifesdjeen.utils.time-utils))

(deftest round-to-test
  (testing "Hour rounding"
    (is (= 1364842800000 (.getTime (round-to (java.util.Date. 1364844423553) precision-hour)))))
  (testing "Minute rounding"
    (is (= 1364844420000 (.getTime (round-to (java.util.Date. 1364844423553) precision-minute))))))

(deftest group-aggregate-test
  (let [series [{:date (java.util.Date. 1364842800000) :value 1}
                {:date (java.util.Date. 1364842810000) :value 2}
                {:date (java.util.Date. 1364842820000) :value 5}

                {:date (java.util.Date. 1364842700000) :value 1}
                {:date (java.util.Date. 1364842710000) :value 2}
                {:date (java.util.Date. 1364842720000) :value 1}

                {:date (java.util.Date. 1364838600000) :value 1}
                {:date (java.util.Date. 1364838610000) :value 2}
                {:date (java.util.Date. 1364838620000) :value 8}]]
    (is (= [8 4 11]
           (map second (group-aggregate series :date :value precision-hour +))))))