/*
# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
*/

'use strict';
const shim = require('fabric-shim');
const util = require('util');

var Web3 = require('web3');
var TokenHolder = require('./tokenHolder')
var TokenWallet = require('./tokenWallet')
var web3;

/**
 * Connects to ethereum blockchain on rinkeby
 * @returns {boolean}
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

    return false;
}

/**
 * loads a deployed contract from Ethereum.
 * This contract has already been deployed in the same network we are connected to.
 */
function loadContract(){
    // we know the abi of the contract
    var abriStr = '[{"constant":true,"inputs":[{"name":"index","type":"uint8"}],"name":"getAuthority","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"uint256"}],"name":"authorities","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"requiredSignatures","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"value","type":"uint256"}],"name":"withdrawn","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"inputs":[{"name":"authorities_param","type":"address[]"},{"name":"requiredSignatures_param","type":"uint8"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"payable":true,"stateMutability":"payable","type":"fallback"},{"anonymous":false,"inputs":[{"indexed":false,"name":"recipient","type":"address"},{"indexed":false,"name":"value","type":"uint256"}],"name":"Deposit","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"sender","type":"address"},{"indexed":false,"name":"value","type":"uint256"}],"name":"Withdrawn","type":"event"}]';
    var abi = JSON.parse(abriStr);
    var contract = web3.eth.contract(abi);

    // we know the address of the contract
    var ethContract =  contract.at("0x5318618CeDed558c9aCdB3D226257b3C46fAae37");
    return ethContract;
}

let Chaincode = class {

    // The Init method is called when the Smart Contract 'fabcar' is instantiated by the blockchain network
    // Best practice is to have any Ledger initialization in separate function -- see initLedger()
    async Init(stub) {
        console.info('=========== Instantiated fabcar chaincode ===========');
        return shim.success();
    }

    // The Invoke method is called as a result of an application request to run the Smart Contract
    // 'fabcar'. The calling application program has also specified the particular smart contract
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

    async verifyEthContract(stub, args) {
        if (args.length != 1) {
            throw new Error('Incorrect number of arguments. Expecting CarNumber ex: CAR01');
        }
        let carAsBytes = await stub.getState(args[0]);
        let car = JSON.parse(carAsBytes);


        if (initializeEthereumConnection() == false)
            return;

        var contract = loadContract();
        car.owner = contract.authorities(0);
        await stub.putState(args[0], Buffer.from(JSON.stringify(car)));
        console.info('============= END : changeCarOwner ===========');
    }


    async generateTokens(stub, args){
        var tokenHolder = new TokenHolder(args[0]);
        var tokenWallet = new TokenWallet(tokenHolder, args[1]);

        await stub.putState(tokenHolder.address, Buffer.from(JSON.stringify(tokenWallet)));
    }
};

shim.start(new Chaincode());
