(ns tw.context
  (:require [babashka.fs :as fs]
            [clojure.edn :as edn]))

(def CONTEXT-DIR (str (fs/home) "/.config/teamwork-cli/"))
(def CONTEXT-FILE "data.edn")
(def CONTEXT-PATH (str CONTEXT-DIR CONTEXT-FILE))

(def context (atom nil))

(def blank-context 
  {:teamwork-base "https://your-team-url.teamwork.com/"
   :api-key "INSERT API KEY IN QUOTES"
   :next-id 100
   :tasks {}})

(defn create-context! []
  (fs/create-dirs CONTEXT-DIR)
  (spit CONTEXT-PATH blank-context)
  blank-context)

(defn save! []
  (spit CONTEXT-PATH @context))

(defn verify-context [context]
  (if (= blank-context context)
    (throw (ex-info "Your Teamwork base URL and API-Key need configured. Please update them" {:context-location CONTEXT-PATH})) 
    context))

(defn get-context []
  (or
    @context
    (reset! context
      (if (fs/exists? CONTEXT-PATH)
        (verify-context (edn/read-string (slurp CONTEXT-PATH)))
        (do (create-context!) (get-context))))))

(defn index-task [task]
  (swap! context assoc-in [:tasks (:next-id @context)] (:id task))
  (swap! context update :next-id inc)
  (assoc task :my-id (get (:tasks @context) (:id task))))


(comment
  ;; Reset
  (reset! context nil)

  ;; Creating the local context
  (create-context!)

  ;; Loading the context
  (get-context) 

  (:tasks (get-context))

  ;; Save context
  (save!)


)
