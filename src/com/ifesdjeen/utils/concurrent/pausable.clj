(ns com.ifesdjeen.utils.concurrent.pausable ^{:doc "Cooperative thread implementation, as per
http://docs.oracle.com/javase/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html"}
    (:import [java.util.concurrent.atomic AtomicBoolean]))

(defprotocol IPausable
  (pause [_] "Pauses execution of the pausable thread")
  (running? [_] "Returns true if current thread should be running")
  (resume [_] "Resumes paused thread")
  (park [_] "Parks the thread if it is paused")

  (abort! [_] "Aborts thread execution, no subsequent resumes are possible.")
  (aborted? [_] "Returns true if current thread is aborted"))

(defprotocol IStoppable
  (stop [_]))

(deftype Pausable [is-running is-aborted]
  IPausable
  (pause [this]
    (.set is-running false))

  (resume [this]
    (.set is-running true))

  (running? [_]
    (.get is-running))

  (park [this]
    (let [current-thread (Thread/currentThread)]
      (try
        (locking current-thread
          (while (not (running? this))
            (.wait current-thread)))
        (catch InterruptedException e))))
  (abort! [_]
    (.set is-aborted true))

  (aborted? [_]
    (.get is-aborted)))


(defn pausable
  "Creates pausable thread, where f will be ran in loop until thead is paused or aborted.
   Returns pausable object that manipulates thead itself."
  [f]
  (let [p (Pausable. (AtomicBoolean. true) (AtomicBoolean. false))
        thread (Thread. (fn []
                          (while (not (aborted? p))
                            (do
                              (park p)
                              (f)))))]
    (.start thread)
    p))
