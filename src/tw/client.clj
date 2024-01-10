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

(defn -request 
  ([path fn] (-request path fn {}))
  ([path fn req-data] 
   (let [context (context/get-context)
         token (str "Basic " (b64-encode (:api-key context)))
         data (merge-with into {:throw false :headers {"Authorization" token}} req-data)
         req (fn (str (:teamwork-base context) path) data)]
    (parse-body req))))

(defn get [path & others] 
  (apply -request path http/get others))

(defn put [path & others] 
  (apply -request path http/put others))

(defn post [path & others] 
  (apply -request path http/post others))
