/*
# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
*/

'use strict';
const shim = require('fabric-shim');
const ClientIdentity = require('fabric-shim').ClientIdentity;

var Web3 = require('web3');
var web3;


/**
 * Connects to ethereum blockchain. First we search for a local peer
 * and if we can't connect we connect to rinkeby network.
 * @returns {boolean} true if connected, false if not.
 */
function initializeEthereumConnection(){
    if (typeof web3 !== 'undefined') {
        web3 = new Web3(web3.currentProvider);
    } else {
        // we first will try to connect to a local instance.
        // this local instance will be faster and should be running in docker, probably together with peers.
        web3 = new Web3(new Web3.providers.HttpProvider("http://127.0.0.1:8545"));
    }

    // if we couldn't connect to local instance, let's try on rinkeby.
    // Unfortunatelly, rinkeby nodes doesn't support filters, so we won't be able to "hear" deposit events in ethereum.
    if (!web3.isConnected()){
        web3 = new Web3(new Web3.providers.HttpProvider("https://rinkeby.infura.io/iDxnNnHTWuNN9INbkvXv"));
    }

    // we are connected, nothing else to do
    if(web3!=null && web3.isConnected()==true)  {
        return true;
    }

    // we couldn't connect.
    return false;
}

/**
 * loads a deployed contract from Ethereum.
 * This contract has already been deployed in the same network we are connected to.
 */
function loadContract(){
    // we know the abi of the contract
    var abriStr = '[ { "constant": false, "inputs": [ { "name": "newSellPrice", "type": "uint256" }, { "name": "newBuyPrice", "type": "uint256" } ], "name": "setPrices", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [], "name": "name", "outputs": [ { "name": "", "type": "string", "value": "Rodrigo" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [ { "name": "_spender", "type": "address" }, { "name": "_value", "type": "uint256" } ], "name": "approve", "outputs": [ { "name": "success", "type": "bool" } ], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [ { "name": "", "type": "address" } ], "name": "fabricBalanceOf", "outputs": [ { "name": "", "type": "uint256", "value": "0" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [], "name": "totalSupply", "outputs": [ { "name": "", "type": "uint256", "value": "2e+24" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [ { "name": "_from", "type": "address" }, { "name": "_to", "type": "address" }, { "name": "_value", "type": "uint256" } ], "name": "transferFrom", "outputs": [ { "name": "success", "type": "bool" } ], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [], "name": "decimals", "outputs": [ { "name": "", "type": "uint8", "value": "18" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [ { "name": "_value", "type": "uint256" } ], "name": "burn", "outputs": [ { "name": "success", "type": "bool" } ], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [], "name": "sellPrice", "outputs": [ { "name": "", "type": "uint256", "value": "0" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [ { "name": "", "type": "address" } ], "name": "balanceOf", "outputs": [ { "name": "", "type": "uint256", "value": "0" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [ { "name": "target", "type": "address" }, { "name": "mintedAmount", "type": "uint256" } ], "name": "mintToken", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": false, "inputs": [ { "name": "_from", "type": "address" }, { "name": "_value", "type": "uint256" } ], "name": "burnFrom", "outputs": [ { "name": "success", "type": "bool" } ], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [], "name": "buyPrice", "outputs": [ { "name": "", "type": "uint256", "value": "0" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [], "name": "owner", "outputs": [ { "name": "", "type": "address", "value": "0x567ec4d3a5506e76066bb6999474c48f810dc2f3" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": true, "inputs": [], "name": "symbol", "outputs": [ { "name": "", "type": "string", "value": "rod" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [], "name": "buy", "outputs": [], "payable": true, "stateMutability": "payable", "type": "function" }, { "constant": false, "inputs": [ { "name": "_to", "type": "address" }, { "name": "_value", "type": "uint256" } ], "name": "transfer", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [ { "name": "", "type": "address" } ], "name": "frozenAccount", "outputs": [ { "name": "", "type": "bool", "value": false } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [ { "name": "_spender", "type": "address" }, { "name": "_value", "type": "uint256" }, { "name": "_extraData", "type": "bytes" } ], "name": "approveAndCall", "outputs": [ { "name": "success", "type": "bool" } ], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": false, "inputs": [ { "name": "_number", "type": "uint8" } ], "name": "setFabricRequiredSignatures", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": true, "inputs": [ { "name": "", "type": "address" }, { "name": "", "type": "address" } ], "name": "allowance", "outputs": [ { "name": "", "type": "uint256", "value": "0" } ], "payable": false, "stateMutability": "view", "type": "function" }, { "constant": false, "inputs": [ { "name": "amount", "type": "uint256" } ], "name": "sell", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": false, "inputs": [ { "name": "from", "type": "address" }, { "name": "value", "type": "uint256" } ], "name": "fabricTransfer", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": false, "inputs": [ { "name": "target", "type": "address" }, { "name": "freeze", "type": "bool" } ], "name": "freezeAccount", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "constant": false, "inputs": [ { "name": "newOwner", "type": "address" } ], "name": "transferOwnership", "outputs": [], "payable": false, "stateMutability": "nonpayable", "type": "function" }, { "inputs": [ { "name": "initialSupply", "type": "uint256", "index": 0, "typeShort": "uint", "bits": "256", "displayName": "initial Supply", "template": "elements_input_uint", "value": "2000000" }, { "name": "tokenName", "type": "string", "index": 1, "typeShort": "string", "bits": "", "displayName": "token Name", "template": "elements_input_string", "value": "Rodrigo" }, { "name": "tokenSymbol", "type": "string", "index": 2, "typeShort": "string", "bits": "", "displayName": "token Symbol", "template": "elements_input_string", "value": "rod" }, { "name": "_fabricRequiredSignatures", "type": "uint8", "index": 3, "typeShort": "uint", "bits": "8", "displayName": "&thinsp;<span class=\\"punctuation\\">_</span>&thinsp;fabric Required Signatures", "template": "elements_input_uint", "value": "2" } ], "payable": false, "stateMutability": "nonpayable", "type": "constructor" }, { "anonymous": false, "inputs": [ { "indexed": false, "name": "owner", "type": "address" }, { "indexed": false, "name": "value", "type": "uint256" } ], "name": "FabricTransfer", "type": "event" }, { "anonymous": false, "inputs": [ { "indexed": false, "name": "target", "type": "address" }, { "indexed": false, "name": "frozen", "type": "bool" } ], "name": "FrozenFunds", "type": "event" }, { "anonymous": false, "inputs": [ { "indexed": true, "name": "from", "type": "address" }, { "indexed": false, "name": "value", "type": "uint256" } ], "name": "Burn", "type": "event" }, { "anonymous": false, "inputs": [ { "indexed": true, "name": "from", "type": "address" }, { "indexed": true, "name": "to", "type": "address" }, { "indexed": false, "name": "value", "type": "uint256" } ], "name": "Transfer", "type": "event" } ]';
    var abi = JSON.parse(abriStr);
    var contract = web3.eth.contract(abi);

    // we know the address of the contract
    var ethContract =  contract.at("0xA760C64DfDB1EE168f2CB7873759d917d919cfb1");
    return ethContract;
}


/**
 * Validates if there is a deposit Request in the Ethereum smart contract for the passed address
 * @param requester the address we are going to verify.
 * @returns {boolean}
 */
function depositRequestExists(requester){
    // let's connect to ETH
    if (initializeEthereumConnection() == false)
        return false;

    // loads the contract
    var contract = loadContract();

    // if we couldn't load the contract, there is not much we can do.
    if (contract == null)
        return false;

    // request fabricBalanceOf method for the passed address
    return contract.fabricBalanceOf(requester);
}

let Chaincode = class {
    // Best practice is to have any Ledger initialization in separate function -- see initLedger()
    async Init(stub) {
        console.info('=========== Instantiated fabcar chaincode ===========');
        return shim.success();
    }

    // The Invoke method is called as a result of an application request to run the Smart Contract
    // The calling application program has also specified the particular smart contract
    // function to be called, with arguments
    async Invoke(stub) {
        let ret = stub.getFunctionAndParameters();
        console.info(ret);

        let method = this[ret.fcn];
        if (!method) {
            console.error('no function of name:' + ret.fcn + ' found');
            throw new Error('Received unknown function ' + ret.fcn + ' invocation');
        }
        try {
            let payload = await method(stub, ret.params);
            return shim.success(payload);
        } catch (err) {
            console.log(err);
            return shim.error(err);
        }
    }


    // We search for a deposit request in ethereum network for the requested address.
    // In order to mint new tokens, the address requester must has call the smart contract in ethereum.
    // before minting new tokens we validate this request exists.
    async mintTokens(stub, args){
        if (args.length != 1) {
            throw new Error('Incorrect number of arguments. Expecting address.');
        }

        // the request doesn't exists.
        var value = depositRequestExists(args[0]);
        if (value == '0' && value == false){
            throw new Error('No deposit request exists in Ethereum contract');
        }

        // this needs to change, I need to get the owner from another chaincode.
        let cid = new ClientIdentity(stub);
        var wallet = {
            owner: cid.getID(),
            address: args[0],
            value: value
        }

        // save the key pair into state chain.
        await stub.putState(args[0], Buffer.from(JSON.stringify(wallet)));

        // create the index with the owner and address
        let indexName = 'owner-address';
        let index = await stub.createCompositeKey(indexName, [wallet.owner, wallet.address]);
        await stub.putState(index,  Buffer.from('\u0000'));
    }

    async getBalance(stub, args) {
        if (args.length != 1) {
            throw new Error('Incorrect number of arguments. Expecting address');
        }
        let address = args[0];

        let wallet = await stub.getState(address); //gets the wallet for the specified address
        if (!wallet || wallet.toString().length <= 0) {
            throw new Error(address + ' does not exist: ');
        }
        console.log(wallet.toString());
        return wallet;
    }
};

shim.start(new Chaincode());
