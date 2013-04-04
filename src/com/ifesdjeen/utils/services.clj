(ns ^{:doc "Expiring buffer is a collection that supports expiring based
  on a funciton." }
  com.ifesdjeen.utils.services)

(def service-definitions (atom {}))
(def services (atom {}))

(defn- start*
  [f]
  (let [t (Thread. ^Runnable f)]
    (.start t)
    t))

(defprotocol IService
  (start! [_])
  (stop! [_])
  (conf [_])
  (thread [_]))

(defn make-default-service
  [opts {:keys [start stop pause]}]
  (let [thread (Thread. ^Runnable #(start opts))]
    (reify IService
      (start! [_]
        (.start thread))

      (stop! [_]
        (stop opts)
        (.stop thread))

      (conf [_] opts)
      (thread [_] thread))))

(defn register-service
  [name opts]
  (swap! service-definitions assoc name opts))

(defn start-all!
  []
  (doseq [[name {:keys [config] :as sd}] @service-definitions]
    (let [opts (if config (config) {})
          service (make-default-service opts sd)]
      (swap! services assoc name service)
      (start! service))))

(defn stop-all!
  []
  (doseq [[name {:keys [config] :as s}] @services]
    (stop! s)))

(defn reset-services!
  []
  (reset! services {})
  (reset! service-definitions {}))
