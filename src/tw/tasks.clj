(ns tw.tasks
  (:require [tw.client :as client]
            [tw.context :as context]))

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


(defn get-tasks []
  (let [index (:tasks (context/get-context))
        tasks (-get-tasks)]
    (map (fn [task]
           (if (contains? index (:id task))
             (assoc task :my-id (get (:id task) index))
             (context/index-task task))) 
           tasks)))

(defn get-task [task]
  (let [index (:tasks (context/get-context))]
    (when (contains? index task) 
      (client/get (str "tasks/" (get index task) ".json")))))

(defn get-url [task]
  (let [context (context/get-context)]
    (str (:teamwork-base context) 
         "app/tasks/" 
         (get-in context [:tasks task]))))

(comment 

  (get-tasks)
  (count (get-tasks))

  (get-task 277)
  (get-url 277)

)

