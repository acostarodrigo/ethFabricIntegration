# ethFabricIntegration

The Ethereum - Fabric integration presented here is based on the model designed by Parity called Parity-Bridge for Ethereum networks. It introduces changes to be optimized for Hyperledger Fabric blockchain.

## Functionality

This implementation allows the integration of two separate blockchains allowing the transference of tokens created in main Ethereum into Fabric. And the transference of those tokens back into Ethereum.

Transactions involving tokens “transferred” from ETH, are free in Fabric blockchain because miners don’t need gas to process them.


## General description

The solution consists of:

ERC20 Ethereum smart contract which includes:

* Methods to deposit and withdrawn tokens. Withdrawn operation is only allowed by contract owner.

* Java app server listening to events on ethereum blockchain to trigger actions in fabric.

* Chaincode implementation in fabric with the following methods:
Peer verification of transaction calling method in ethereum contract.
Initialize token transfer back to ETH

* Java app server listening to events in Fabric and triggering method to call ERC20 smart contract in Ethereum.


### Next Steps
As this is a proposal, I need to validate that in practice is possible. Theoretically it should. 
I would like to create a small test with basic functionality to validate it.

* Build SmartContract in Ethereum testnet (*done*)
* Build Fabric test blockchain
* Build app that includes logic

