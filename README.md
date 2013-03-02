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

## License

Copyright © 2013 Alex P (stylefruits.de)

Distributed under the Eclipse Public License, the same as Clojure.
