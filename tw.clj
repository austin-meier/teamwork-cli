#!/usr/bin/env bb

(ns tw
  (:require [tw.tasks :as tasks]
            [tw.context :as context]
            [babashka.cli :as cli]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint]]))

(defn print-tasks! [cmd]
  (tasks/index-tasks)
  (context/save!)
  (println (str "Now tracking " (count (:tasks (context/get-context))) " tasks")))

(defn print-task! [cmd]
  (let [task-id (or (parse-long (first (:args cmd))) 0)]
    (pprint (tasks/get-task task-id))))

(defn print-task-url! [cmd]
  (let [task-id (or (parse-long (first (:args cmd))) 0)]
    (println (tasks/get-url task-id))))

(defn print-search! [cmd]
  (let [query (str/join " " (:args cmd))]
    (->> (tasks/search query)
         (map #(str (:my-id %) "\t-\t" (:name %)))
         (str/join "\n")
         println))
  (context/save!))

(defn move-task! [cmd]
  (let [task-id (or (parse-long (first (:args cmd))) 0)
        board (second (:args cmd))]
    (tasks/move-task task-id board)))

(def cli-opts
  {:task      {:alias   :t
               :desc    "A teamwork task id"
               :coerce [:long]
               :require true}})

(def table
  [{:cmds ["index"] :fn print-tasks! :desc "Index all current tasks"}
   {:cmds ["task"] :fn print-task! :desc "Get specific task by id"}
   {:cmds ["search"] :fn print-search! :desc "Search for tasks by query"}
   {:cmds ["move"] :fn move-task! :desc "Move a task to one of the boards"}
   {:cmds ["url"] :fn print-task-url! :desc "Get the URL of a task"}])

(defn help [_]
  (println
    (reduce (fn [acc, cmd]
              (str acc 
                   (str/join ", " (:cmds cmd)) "\n\t"
                   (:desc cmd) "\n")) 
            "" table)))

(def dispatch-table 
  (conj table {:cmds [] :fn help}))

(cli/dispatch dispatch-table *command-line-args*)
