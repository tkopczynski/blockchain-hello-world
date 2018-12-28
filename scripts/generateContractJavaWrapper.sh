#!/usr/bin/env bash

WEB3J="/Users/tkopczynski/bin/web3j-4.0.1/bin/web3j"
REPOROOT=$(git rev-parse --show-toplevel)

$WEB3J truffle generate $REPOROOT/build/contracts/$1.json -p pl.net.kopczynski.blockchain.contract -o $REPOROOT/src/main/java/