(ns test-runner
  (:require [clojure.test :refer [run-tests]]
            [clojure.test-clojure.instr]
            [clojure.test-clojure.multi-spec]
            [clojure.test-clojure.spec]))

(defn run []
  (let [errors (->> (run-tests 'clojure.test-clojure.spec
                                'clojure.test-clojure.instr
                                'clojure.test-clojure.multi-spec)
                    ((juxt :fail :error))
                    (apply +))]
    (System/exit (if (pos? errors) 1 0))))
