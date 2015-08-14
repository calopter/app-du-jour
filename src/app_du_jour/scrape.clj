(ns app-du-jour.scrape
  (:require [net.cgrand.enlive-html :as html]))

(def base-url "http://www.amazon.com")

(def new-url (str base-url "/mobile-apps/b/ref=nav_shopall_adr_free?ie=UTF8&node=2350149011"))

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn app-basic []
  (let [info (-> new-url
                 fetch-url
                 (html/select [:h3 :a])
                 first)
        name (-> info :content first)
        link (str base-url (get-in info [:attrs :href]))]
    (zipmap [:name :link] [name link])))

(defn feats-descrip [app]
  (let [app-page (-> app
                     :link
                     fetch-url)
        feats (-> app-page
                  (html/select [:div#feature-bullets-btf :li])
                  (->> (map #(first (:content %)))))
        descrip (-> app-page
                    (html/select [:div#mas-product-description :div])
                    first
                    :content
                    (->> (filter string?)
                         (apply str)
                         (drop 17)
                         (drop-last 13)
                         (apply str)))]
    (merge app {:features feats :description descrip})))

(defn app []
  (feats-descrip (app-basic)))
