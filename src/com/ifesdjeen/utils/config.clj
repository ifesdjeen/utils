(ns com.ifesdjeen.utils.config
  (:import [java.io File]))

;;
;; API
;;

(defprotocol FileBasedConfiguration
  (expand    [path] "Expands filesystem path to be absolute")
  (load-from [path] "Reads and evaluates configuration from given resource."))

(extend-protocol FileBasedConfiguration
  File
  (expand    [f] (.getAbsolutePath f))
  (load-from [f] (read-string (with-open [rdr (clojure.java.io/reader f)]
                                (reduce str "" (line-seq rdr)))))

  String
  (expand    [s] (.getAbsolutePath (File. s)))
  (load-from [s] (load-from (File. s))))
