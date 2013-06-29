(ns chp.login
  (:require [korma.db
             :as kdb]
            [korma.core
             :as kc])
  (:use [chp.db
         :only [*db*]]
        [chp.password
         :only [password]]))
  
(kdb/defdb korma-db *db*)


(defn login?
  [user
   secret
   salt 
   & {:keys
      [username-column
       password-column]
      :or {username-column :name
           password-column :password}}]
  (let [[uc pc] [(keyword username-column)
                 (keyword password-column)]
        login {uc user
               pc (password salt secret)}]
    (->> (kc/where login)
         (kc/select {:table "user"})
         first
         boolean)))

(defn admin?
  [user 
   & {:keys
      [username-column
       admin-column]
      :or {username-column :name
           admin-column :admin}}]
  (let [uc (keyword username-column)
        ac (keyword admin-column)
        admin {uc user
               ac true}]
    (->> (kc/where admin)
         (kc/select {:table "user"})
         first
         boolean)))
