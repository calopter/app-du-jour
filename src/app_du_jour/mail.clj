(ns app-du-jour.mail
  (:require [postal.core :as mail]))

(def user (System/getenv "UNAME"))
(def pass (System/getenv "PW"))
(def to (System/getenv "TO"))

(defn send-mail [message]
  (mail/send-message {:host "smtp.gmail.com"
                      :user user
                      :pass pass
                      :ssl :yes!!!11}
                     {:from user
                      :to to
                      :subject "App du Jour"
                      :body [{:type "text/html"
                              :content message}]}))
