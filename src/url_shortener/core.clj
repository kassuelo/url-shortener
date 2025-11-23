(ns url-shortener.core
  ;; importa as funções principais de rotas do Compojure (GET, POST, defroutes…)
  (:require [compojure.core :refer :all]
            ;; rotas padrão (404, resources etc.)
            [compojure.route :as route]
            ;; middlewares para ler parâmetros enviados pelo navegador (?url=xxx)
            [ring.middleware.params :refer [wrap-params]]
            ;; converte parâmetros de string → keyword (:url instead of "url")
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            ;; servidor embutido Jetty (para rodar http://localhost:3000)
            [ring.adapter.jetty :refer [run-jetty]]
            ;; gera páginas HTML facilmente via Clojure
            [hiccup.page :refer [html5]]
            ;; gera formulários em HTML
            [hiccup.form :refer [form-to text-field submit-button]])
  ;; permite gerar classe Java para rodar via `lein run`
  (:gen-class))

;; banco de dados em memória (um mapa vazio dentro de um atom)
(def urls (atom {}))
;; `atom` permite alterar estado de forma segura em Clojure

;; função que retorna a página inicial HTML
(defn home-page []
  (html5                                  ;; gera documento HTML5
   [:head [:title "Encurtador"]]           ;; título da aba do navegador
   [:body
    [:h1 "Encurtador de URL"]              ;; título da página
    ;; cria um formulário que envia POST para /shorten
    (form-to [:post "/shorten"]
             ;; campo de texto chamado "url"
             (text-field "url")
             ;; botão de enviar
             (submit-button "Encurtar"))]))

;; gera um ID aleatório de 6 caracteres (ex: Ab91kQ)
(defn random-id []
  (let [chars "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"]
    ;; cria string pegando 6 caracteres aleatórios da lista acima
    (apply str (repeatedly 6 #(rand-nth chars)))))

;; associa ID → URL no atom
(defn shorten-url [url]
  (let [id (random-id)]                   ;; gera ID
    ;; coloca no atom: ex { "Ab12CD" → "https://google.com" }
    (swap! urls assoc id url)
    id))                                  ;; retorna o ID gerado

;; define todas as rotas da sua aplicação
(defroutes app-routes

  ;; rota GET /
  (GET "/" []
    (println "URLs:" @urls)               ;; loga URLs armazenadas
    (home-page))                          ;; mostra página inicial

  ;; rota POST /shorten → recebe URL e gera ID
  (POST "/shorten" {params :params}       ;; extrai map de parâmetros
    (println "params recebidos:" params)  ;; log no terminal
    (let [url (:url params)               ;; pega parâmetro :url
          id  (shorten-url url)]          ;; gera ID e salva no atom
      {:status 200
       :headers {"Content-Type" "text/html"}
       ;; mostra link clicável com a URL encurtada
       :body (str "URL encurtada: <a href=\"/" id "\">http://localhost:3000/" id "</a>")}))

  ;; rota GET /:id → redireciona para a URL original
  (GET "/:id" [id]   ;; Rota dinâmica: qualquer coisa após "/" vira o parâmetro :id
    (println "Acessaram o ID:" id ", redirecionando para:" (@urls id))  ;; Loga o ID acessado e a URL correspondente

    ;; Tenta recuperar a URL original associada ao ID no atom `urls`
    (if-let [url (@urls id)]

      ;; Se encontrou a URL, responde com redirecionamento HTTP 302
      {:status 302
       :headers {"Location"
                 ;; Se a URL já começa com http:// ou https://, usa direto.
                 ;; Caso contrário, adiciona "https://" automaticamente.
                 (if (re-find #"^https?://" url)
                   url
                   (str "https://" url))}
       :body ""}  ;; Corpo vazio (normal em respostas de redirecionamento)

      ;; Se o ID não existir no atom, retorna 404 Not Found
      {:status 404 :body "Not found"})))


;; aplica middlewares:
;; wrap-keyword-params → converte "url" para :url
;; wrap-params → extrai parâmetros POST/GET
(def app
  (-> app-routes
      wrap-keyword-params
      wrap-params))

;; função principal executada quando roda `lein run`
(defn -main []
  (println "Servidor rodando em http://localhost:3000")
  ;; inicia servidor Jetty e bloqueia thread principal
  (run-jetty app {:port 3000 :join? true}))
