(ns commiteth.eth.token-registry
  (:require [commiteth.eth.core :as eth]
            [commiteth.eth.web3j
             :refer [create-web3j creds]]
            [commiteth.config :refer [env]])
  (:import [org.web3j
            abi.datatypes.generated.Uint256
            abi.datatypes.Address
            abi.datatypes.Utf8String
            protocol.Web3j
            protocol.http.HttpService
            crypto.Credentials
            crypto.WalletUtils]
           commiteth.eth.contracts.TokenReg))

(defonce PARITY-MAINNET-ADDR "0x5f0281910af44bfb5fc7e86a404d0304b0e042f1")
(defonce STATUS-RINKEBY-ADDR "0x4826Ee32532EeA00Bb71C17Da491f1B2D2193C21")


(defn- load-tokenreg-contract [addr]
  (TokenReg/load addr
                 (create-web3j)
                 (creds)
                 (eth/gas-price)
                 (BigInteger/valueOf 21000)))


(defn load-parity-tokenreg-data
  "Construct a mapping of ERC20 token mnemonic -> token data (name, address, digits, owner) from data
  in Parity's mainnet token registry contract."
  ([]
   (load-parity-tokenreg-data PARITY-MAINNET-ADDR))
  ([addr]
   (let [contract (load-tokenreg-contract addr)]
     ;(assert (.isValid contract)) ;; web3j's isValid can't be trusted...
     (let [token-count (-> contract .tokenCount .get .getValue)]
       (println "token-count" token-count)
       (into {}
             (map (fn [[addr tla digits name owner]]
                    [(-> tla str keyword)
                     {:tla (str tla)
                      :name (str name)
                      :base (.getValue digits)
                      :address (str addr)
                      :owner (str owner)}])
                  (for [i (range token-count)]
                    (-> (.token contract
                                (org.web3j.abi.datatypes.generated.Uint256. i))
                        .get))))))))

(defn deploy-parity-tokenreg
  "Deploy an instance of parity token-registry to current network"
  []
  (let [web3j (create-web3j)]
    (TokenReg/deploy web3j
                     (creds)
                     (eth/gas-price)
                     (BigInteger/valueOf 4000000) ;; gas limit
                     BigInteger/ZERO)))