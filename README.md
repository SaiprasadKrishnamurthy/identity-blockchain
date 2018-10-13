A Brilliant writeup on Corda Blockchain: https://gendal.me/2017/02/21/the-corda-way-of-thinking/


**To setup**

To build the blockchain network: `./gradlew clean deployNodes`
To start the Blockchain Network: `./build/nodes/runnodes`

To start the webapp: `./gradlew runPartyAServer`

To open the webapp:  http://localhost:8080

To submit a data to the network:  `http://localhost:8080/api/v1/capture?to=O=PartyB,L=New%20York,C=US&data=Some Data`

The above means PartyA is submitting a piece of data to PartyB and watched by a WatchDog.

The webapp will show you the state of the transactions (facts) as seen by these three parties.

There's a simple SmartContract at the moment which fails the transaction if the submitted data length is equal to 4 in which case the transaction isn't committed into the ledger.

