#### CHP [![endorse](https://api.coderwall.com/runexec/endorsecount.png)](https://coderwall.com/runexec)
ClojureHomePage is a Compojure based web framework that allows you to write the backend and frontend with Clojure.

You can <br />
* Run Clojure inside an HTML file with the ```<clj></clj>``` tags
* Have multiple method handlers under a single route (get, post, put, delete, and head)
* Easily get common web headers (ex. ($ user-agent))
* Easily get web headers (ex. ($$ cache-control))
* Easily get environmental variables (ex. (env java.vm.name))
* Generate HTML with a drop-in replacement for common Hiccup forms
* Generate JavaScript / ECMAScript with ClojureScript
* Generate CSS with Garden
* Manipulate databases with KormaSQL

This page serves as project documentation.<br />

1. [Example CHTML & Routes](#example-chtml--routes)
2. [Install](#getting-started)
3. [How?](#how)
4. [Tutorial](https://github.com/runexec/chp/tree/master/tutorial/01)
5. [Clojure HTML Generation](#clojure-and-html-generation)
6. [Clojure and CSS Generation](#clojure-and-css-generation)
7. [Clojure and JavaScript Generation](#clojure-and-javascript-generation)
8. [Clojure and SQL](#clojure-and-sql)
9. [Session handling, Cookies, and Compojure](#session-handling-cookies-and-compojure)
10. [Ring and port configuration](#ring-configuration)

#### Getting started

1) Download & Run

```bash
git clone https://github.com/runexec/chp
cd chp
lein ring server
```

2) Edit the default app-routes template located at the bottom of src/chp/handler.clj

#### How?

By default, the CHTML files are located in chp-root folder of the project folder.
When a CHTML file is parsed, all public variables of the chp.handler namespace
are accessible during the evaluation of the ```<clj></clj>``` tags. Use print
 or println within the tags to have the results displayed.


#### Example CHTML & Routes

The following link is the chtml page that is used in the example below. 
<a href="https://github.com/runexec/chp/blob/master/chp-root/test-page.chtml">
   test-page.chtml
</a>


```clojure
(defroutes app-routes
  (chp-route "/chtml" 
             (binding [*title* "Test Page Example"]
               (or (chp-parse (str root-path "test-page.chtml"))
                   "error")))
  (chp-route "/"
             (let [display (str (format "Method %s <br />" (escape ($ method)))
                                (format "URI %s <br />" (escape ($ uri)))
                                (format "Params %s <br />" (escape ($ params)))
                                (format "Header Values <p>%s</p>"
                                        (with-out-str
                                          (doseq [[k v] (escape-map ($ headers))]
                                            (println k "=" v "<br />"))))
                                (format "name %s : ip %s"
                                        ($ server-name)
                                        ($ server-ip)))]
               (chp-body {:-get (str "Get => " display)
                          :-post (str "Post => " display)
                          :-not-found "Sorry, but this page doesn't exist"})))
  (chp-route "/testing"
             (or (chp-when :get
                           (str (format "chp-body wasn't used to access %s from %s with %s"
                                        ($ uri) ($ ip) ($ user-agent))
                                (format "<p>Tracking you? DNT HTTP Header = %s</p>" ($$ dnt))
                                (format "<p>HTTP Header cache-control = %s</p>" ($$ cache-control))))
                 "Not Found"))
  (chp-route "/chp"
             (or (chp-parse (str root-path "chp-info.chtml"))
                 "error"))
  (route/resources "/")
  (route/not-found "Not Found"))
```


#### Clojure and HTML Generation

The following methods presented in the documentation below are 
accessible from within CHTML files by default. These abstractions
are drop-in replacements for the Hiccup API located at http://weavejester.github.io/hiccup/.
Please note that these forms DO NOT generate Hiccup code, but HTML.


```clojure
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Form Fields

(escape string)

(url-encode string)

(check-box attr-map? name)
(check-box attr-map? name checked?)
(check-box attr-map? name checked? value)

(drop-down attr-map? name options)
(drop-down attr-map? name options selected)

(email-field attr-map? name)
(email-field attr-map? name value)

(file-upload attr-map? name)

(form-to attr-map? [method action] & body)

(hidden-field attr-map? name)
(hidden-field attr-map? name value)

(label attr-map? name text)

(password-field attr-map? name)
(password-field attr-map? name value)

(radio-button attr-map? group)
(radio-button attr-map? group checked?)
(radio-button attr-map? group checked? value)

(reset-button attr-map? text)

(select-options attr-map? coll)
(select-options attr-map? coll selected)

(submit-button attr-map? text)

(text-area attr-map? name)
(text-area attr-map? name value)

(text-field attr-map? name)
(text-field attr-map? name value)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Common Elements

(javascript-tag script)

(image attr-map? src)
(image attr-map? src alt)

(link-to attr-map? url & content)

(mail-to attr-map? e-mail & [content])

(ordered-list attr-map? coll)

(unordered-list attr-map? coll)

```

#### Clojure and CSS Generation

ClojureHomePage uses the Garden CSS generation library by default.
The Garden documentation page is located at https://github.com/noprompt/garden

#### Clojure and JavaScript Generation

ClojureHomePage uses ClojureScript and lein-cljsbuild to generate javascript.
CHP uses the directory resources/cljs/ as the default cljs source code directory.

1. [lein-cljsbuild Documentation](https://github.com/emezeske/lein-cljsbuild/)
2. [ClojureScript Documentation](https://github.com/clojure/clojurescript)

#### Clojure and SQL 

ClojureHomePage uses the SQLKorma DSL by default. korma.db is required as kdb and korma.core is required as kc

1. [Korma Documentation](http://www.sqlkorma.com/)

#### Session handling, Cookies, and Compojure

Because CHP is Compojure based, you can use Compojure and Ring extensions. Already included, but not loaded by default, the lib-noir library is a great helper library for Clojure web development.


1. [lib-noir API](http://yogthos.github.io/lib-noir/index.html)
2. [lib-noir Github](https://github.com/noir-clojure/lib-noir)
3. [Ring CSRF protection](https://github.com/weavejester/ring-anti-forgery)
4. [Ring Middleware Extensions](https://github.com/search?q=ring+middleware&ref=cmdform&type=Repositories)

#### Ring configuration

The default configuration for CHP is located in project.clj

```clojure
:ring {:port 8000
       :auto-reload? true
       :auto-refresh? true
       :reload-paths ["src/chp/"]
       :handler chp.handler/app}
```

1. [Lein-ring documentation](https://github.com/weavejester/lein-ring)

