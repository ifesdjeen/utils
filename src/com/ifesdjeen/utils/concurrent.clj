(ns com.ifesdjeen.utils.concurrent
  (:import [java.util.concurrent CountDownLatch TimeUnit]))

;;
;; Countdown Latch
;;

(defn make-latch
  "Creates a new latch"
  [i]
  (CountDownLatch. i))

(defn get-count
  "Gets current count of latch"
  [latch]
  (.getCount latch))

(defn countdown
  "Countdown the `latch`"
  [latch]
  (.countDown latch))

(defmacro after-latch
  "Awaits for `latch` for `timeout` milliseconds, and executes `body` afterwards"
  [latch timeout & body]
  `(do
     (.await (deref ~latch) timeout TimeUnit/MILLISECONDS)
     ~@body))

;;
;; Thread
;;

(defn make-thread
  [f]
  (Thread. f))

(defn start
  [^Thread t]
  (.start t))

(defmacro run
  [& body]
  `(let [f# (fn* [] ~body)]
     (start (make-thread f#))))

(defn throttle
  "Throttles f execution for `throttle-for` milliseconds. Returns a future that contains function execution value.
   Function f is expected to produce side-effects, and not expected to behave as a future."
  [f throttle-for]
  (let [last-call (atom (System/currentTimeMillis))
        throttled (fn [& args]
                    (reset! last-call (System/currentTimeMillis))
                    (run
                      (Thread/sleep throttle-for)
                      (when (>= (- (System/currentTimeMillis) @last-call) throttle-for)
                        (apply f args))))]
    throttled))


(defmacro with-timeout
  [timeout & body]
  `(let [f# (future ~@body)]
     (.get f# ~timeout java.util.concurrent.TimeUnit/MILLISECONDS)))
