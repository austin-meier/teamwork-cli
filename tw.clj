#!/usr/bin/env bb

(ns tw
  (:require [tw.tasks :as tasks]
            [tw.context :as context]
            [babashka.cli :as cli]
            [clojure.string :as str]))



(defn print-tasks! [cmd]
  (prn (tasks/get-tasks))
  (context/save!))

(defn print-task! [cmd]
  (let [task-id (or (parse-long (first (:args cmd))) 0)]
    (println (tasks/get-task task-id))))

(defn print-task-url! [cmd]
  (let [task-id (or (parse-long (first (:args cmd))) 0)]
    (println (tasks/get-url task-id))))

(def cli-opts
  {:task      {:alias   :t
               :desc    "A teamwork task id"
               :coerce [:long]
               :require true}})

(def table
  [{:cmds ["tasks"] :fn print-tasks! :desc "Get all current tasks" :usage ""}
   {:cmds ["task"] :fn print-task! :desc "Get specific task by id"}
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


