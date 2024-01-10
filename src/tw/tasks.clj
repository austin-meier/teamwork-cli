(ns tw.tasks
  (:require [tw.client :as client]
            [tw.context :as context]
            [clojure.set :refer [map-invert]]
            [cheshire.core :as json]))

(def board-columns
  {"open" 628003
   "progress" 628004 
   "review" 628005
   "ready" 628006
   "staging" 628007
   "completed" 628008})

;; they dont give me a remaining or counter... so i guess i can just compare to the total returned per page and see what that means
(defn -get-tasks []
  (loop [page 1
         tasks []]
    (let [page-size 250
          body (client/get "/tasks.json"
                                   {:query-params {"page" page
                                                   "pageSize" page-size}})]
      (if (not= page-size (count (:todo-items body)))
        (into tasks (:todo-items body))
        (recur (inc page) (into tasks (:todo-items body)))))))

(defn index-task [task]
  (let [index (:tasks (context/get-context))
        task (if (string? (:id task)) (update task :id parse-long) task)]
    (if (contains? index (:id task))
      (assoc task :my-id (get index (:id task)))
      (context/index-task task))))

(defn index-tasks []
  (let [tasks (-get-tasks)]
    (map index-task tasks)
    true))

(defn lookup-table []
  (map-invert (:tasks (context/get-context))))

(defn get-task [task]
  (let [index (lookup-table)]
    (when (contains? index task) 
      (client/get (str "tasks/" (get index task) ".json")))))

(defn get-url [task-id]
  (let [context (context/get-context)
        index (lookup-table)]
    (str (:teamwork-base context) 
         "app/tasks/" 
         (get index task-id))))

(defn search [query]
  (->> (client/get "search.json" 
                   {:query-params 
                      {"searchFor" "tasks" 
                       "searchTerm" query}})
         :searchResult
         :tasks
         (map index-task)))

(defn add-task-to-board [task-id board]
  (let [column-id (get board-columns board)
        index (lookup-table)]
    (when (contains? index task-id)
      (client/post (str "/boards/columns/" column-id "/cards.json")
                   {:body (json/encode {:card {:taskId (get index task-id)}})}))))

(defn get-card-task [column-name card]
  {:column column-name
   :column-id (get board-columns column-name)
   :card-id (parse-long (:id card))
   :task-id (parse-long (:taskId card))})

(defn board-tasks [board]
  (let [column-id (get board-columns board)]
    (->>
      (client/get (str "/boards/columns/" column-id "/cards.json"))
      :cards
      (map #(get-card-task board %)))))

(defn get-boards []
  (reduce-kv (fn [m k _]
               (assoc m k (board-tasks k))) 
             {} board-columns))

(defn task-board [task-id]
  (->> (get-boards)
       vals
       (apply concat)
       (filter #(= task-id (:task-id %)))
       first))

(defn move-card [card-id old new]
  (let [new-col-id (get board-columns new)
        old-col-id (get board-columns old)]
    (client/put (str "boards/columns/cards/" card-id "/move.json") 
                  {:body (json/encode {:columnId new-col-id
                                       :oldColumnId old-col-id})})))

(defn move-task [task-id board]
  (let [index (lookup-table)] 
    (when (contains? index task-id) 
      (if-let [card (task-board (get index task-id))]
        (move-card (:card-id card) (:column card) board)
        (add-task-to-board task-id board)))))

(comment 

  (index-tasks)
  (count (-get-tasks))

  (get-task 1054)
  (get-url 277)

  (search "multiple elements")

  (board-tasks "open")
  (get-boards)

  (move-card 1827560 "review" "ready")

  (move-task 1054 "open")
  (move-task 1054 "progress")
  (move-task 1054 "review")
  (move-task 1054 "ready")
  (move-task 1054 "staging")


  (add-task-to-board 1054 "review")
  ;; get columns for development project
  (->> (client/get "projects/736024/boards/columns.json")
       :columns
       (map #((juxt :name :id) %)))


)

