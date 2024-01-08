(ns tw.client
  (:require [babashka.http-client :as http]
            [cheshire.core :as json]
            [tw.context :as context]))

(defn b64-encode [txt]
  (let [encoder (java.util.Base64/getEncoder)
        resultBytes (.encode encoder (.getBytes txt))]
    (String. resultBytes)))

(defn deep-merge [a & maps]
  (if (map? a)
    (apply merge-with deep-merge a maps)
    (apply merge-with deep-merge maps)))

(defn parse-body [req]
  (when (:body req)
    (json/parse-string (:body req) true)))

(defn get 
  ([path] (get path {}))
  ([path req-data] 
   (let [context (context/get-context)
         token (str "Basic " (b64-encode (:api-key context)))
         data (merge-with into {:headers {"Authorization" token}} req-data)
         req (http/get (str (:teamwork-base context) path) data)]
    (case (:status req)
      200 (parse-body req)
      req))))
