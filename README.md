spec.alpha
========================================

## Babashka maintained fork

This fork of `spec.alpha` works in babashka. It is intended to be a feature
complete solution for using spec with babashka and replaces
[spartan.spec](https://github.com/borkdude/spartan.spec). See this [blog
post](https://blog.michielborkent.nl/using-clojure-spec-alpha-with-babashka.html)
for more background information.

### Usage

You can use in babashka by using these coordinates:

```
org.babashka/spec.alpha {:git/url "https://github.com/babashka/spec.alpha"
                         :git/sha "1a841c4cc1d4f6dab7505a98ed2d532dd9d56b78"}
```

or any later SHA.

### Core specs

If you need to use the `clojure.core.specs.alpha` namespace, then explicitly add
this to your dependencies, as babashka ignores this dependency by default,
unless you explicitly add it. Example `bb.edn`:


``` clojure
{:deps {org.babashka/spec.alpha {:git/url "https://github.com/babashka/spec.alpha"
                                 :git/sha "644a7fc216e43d5da87b07471b0f87d874107d1a"}
        org.clojure/core.specs.alpha {:mvn/version "0.2.62"}}}
```

Then you can use the core specs like this:

```
(ns spec-example
  (:require [clojure.core.specs.alpha :as csa]
            [clojure.spec.alpha :as s]))

(prn (s/conform ::csa/defn-args '[foo [x y z] (x y z)]))
;; => {:fn-name foo,
      :fn-tail [:arity-1 {:params {:params [[:local-symbol x] [:local-symbol y] [:local-symbol z]]},
                          :body [:body [(x y z)]]}]}
```

You need to do this even when libraries declare an explicit dependency on core
specs, since babashka assumes by default that you will not use them, as they are
a dependency of clojure. An example of such a library is
[better-cond](https://github.com/Engelberg/better-cond):

``` clojure
{:deps {org.babashka/spec.alpha {:git/url "https://github.com/babashka/spec.alpha"
                                 :git/sha "644a7fc216e43d5da87b07471b0f87d874107d1a"}
        org.clojure/core.specs.alpha {:mvn/version "0.2.62"}
        better-cond/better-cond {:mvn/version "2.1.1"}}}
```

``` clojure
(ns better-cond-example
  (:require [better-cond.core :as b]))

(b/defnc f [a]
  (odd? a) 1
  let [a (quot a 2)
       _ (prn a)]
  when-let [x (> a 25)]
  x)

(prn (f 100))
;; 50
;; true
```

### Implementation notes

To make it compatible, the following changes with the original spec.alpha were
introduced:

- Calls to `clojure.Compiler/demunge` are replaced with a function in
  `spec.alpha` called `demunge` which defers to `clojure.main/demunge`. This was
  done to note have to expose `clojure.lang.Compiler` in babashka. Vote
  [here](https://ask.clojure.org/index.php/11371/consider-adding-demunge-into-clojure-core)
  to get `demunge` into clojure.core.
- Interop on vars to turn them into symbols is replaced by
  looking up the var name and namespace via metadata.
- `((.dispatchFn mm)` was rewritten to `((.-dispatchFn mm)` to make the field
  invocation explicit, which is the only way SCI currently understands field
  interop.
- `clojure.lang.RT/checkSpecAsserts` is replaced by an internal atom since babashka doesn't have `clojure.lang.RT`


Run `bb test` to run tests.

Here follows the original README.

<hr>

spec is a Clojure library to describe the structure of data and functions. Specs can be used to validate data, conform (destructure) data, explain invalid data, generate examples that conform to the specs, and automatically use generative testing to test functions.

Clojure 1.9 depends on this library and provides it to users of Clojure. Thus, the recommended way to use this library is to add a dependency on the latest version of Clojure 1.9, rather than including it directly. In some cases, this library may release more frequently than Clojure. In those cases, you can explictly include the latest version of this library with the dependency info below.

NOTE: This library is alpha and subject to breaking changes. At a future point, there will be a non-alpha stable version of this library.

For more information:

* Rationale - https://clojure.org/about/spec
* Guide - https://clojure.org/guides/spec
* Spec split notice - https://groups.google.com/forum/#!msg/clojure/10dbF7w2IQo/ec37TzP5AQAJ

Releases and Dependency Information
========================================

Latest stable release: 0.3.214

* [All Released Versions](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.clojure%22%20AND%20a%3A%22spec.alpha%22)
* [Development Snapshot Versions](https://oss.sonatype.org/index.html#nexus-search;gav~org.clojure~spec.alpha~~~)

[deps.edn](https://clojure.org/guides/deps_and_cli) dependency information:

    org.clojure/spec.alpha {:mvn/version "0.3.214"}

[Leiningen](https://github.com/technomancy/leiningen) dependency information:

    [org.clojure/spec.alpha "0.3.214"]

[Maven](https://maven.apache.org/) dependency information:

    <dependency>
      <groupId>org.clojure</groupId>
      <artifactId>spec.alpha</artifactId>
      <version>0.3.214</version>
    </dependency>

Developer Information
========================================

* [API docs](https://clojure.github.io/spec.alpha/)
* [GitHub project](https://github.com/clojure/spec.alpha)
* [Changelog](https://github.com/clojure/spec.alpha/blob/master/CHANGES.md)
* [Bug Tracker](https://clojure.atlassian.net/browse/CLJ)
* [Continuous Integration](https://build.clojure.org/job/spec.alpha/)
* [Compatibility Test Matrix](https://build.clojure.org/job/spec.alpha-test-matrix/)

Copyright and License
========================================

Copyright (c) Rich Hickey, and contributors, 2018-2020. All rights reserved.  The use and distribution terms for this software are covered by the Eclipse Public License 1.0 (https://opensource.org/licenses/eclipse-1.0.php) which can be found in the file epl-v10.html at the root of this distribution. By using this software in any fashion, you are agreeing to be bound bythe terms of this license.  You must not remove this notice, or any other, from this software.
