# Utils

Swiss-army knife.

# Usage

## Circular Buffer

Ciruclar buffer is FIFO buffer with a fixed size, that replaces it's oldest
elements when if it's full. Useful for monitoring, and inter-thread communication.

Usage:

```clojure
(def buf (circular-buffer 5))

(conj buf 2)
;; [2 nil nil nil nil]

(conj (conj buf 1) 2)
;; [1 2 nil nil nil nil]

(-> buf
    (conj 1) (conj 2) (conj 3) (conj 4) (conj 5) (conj 6) (conj 7))
;; (6 7 3 4 5)
```

## Expiring buffer

Expiring buffer is an unbound FIFO buffer, that supports expiry based on a funciton.
Implementation doesn't support timing expiry itself, since it's not always clear what
would be the best way to expire different buffers.

Expiring Buffer assumes that items are coming in an ascending order. It will disjoin/expire
only items that are in the head of list. If you can't guarantee order, use SortedExpiringBuffer
or implement your own expiring buffer that will look through all the elements during expity.
Nevertheless, that'd be an expensive operation.

Currently expire operation is synchronous. Because we operate on Vectors, and avoid leaking
subvectors out of impementation, some operations are linear time.

```clojure
(def buf (expiring-buffer (fn [a] (>= 20 a)) [10 20 30 40]))

(conj buf 50)
;; [30 40 50]

(conj (conj buf 50) 60)
;; [30 40 50 60]

(expire buf)
;; [30 40]
```

## License

Copyright © 2013 Alex P (stylefruits.de)

Distributed under the Eclipse Public License, the same as Clojure.
