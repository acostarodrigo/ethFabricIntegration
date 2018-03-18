/*
# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
*/

'use strict';
const shim = require('fabric-shim');
const util = require('util');

var Web3 = require('web3');
var web3;

/**
 * Connects to ethereum blockchain on rinkeby
 * @returns {boolean}
 */
function initializeEthereumConnection(){
    if (typeof web3 !== 'undefined') {
        web3 = new Web3(web3.currentProvider);
    } else {
        // set the provider you want from Web3.providers
        web3 = new Web3(new Web3.providers.HttpProvider("https://rinkeby.infura.io/iDxnNnHTWuNN9INbkvXv"));
    }

    if(web3!=null && web3.isConnected()==true)  {
        return true;
    }

    return false;
}

/**
 * loads a deployed contract
 */
function loadContract(){
    var abriStr = '[{"constant":true,"inputs":[{"name":"index","type":"uint8"}],"name":"getAuthority","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"uint256"}],"name":"authorities","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"requiredSignatures","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"value","type":"uint256"}],"name":"withdrawn","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"inputs":[{"name":"authorities_param","type":"address[]"},{"name":"requiredSignatures_param","type":"uint8"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"payable":true,"stateMutability":"payable","type":"fallback"},{"anonymous":false,"inputs":[{"indexed":false,"name":"recipient","type":"address"},{"indexed":false,"name":"value","type":"uint256"}],"name":"Deposit","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"sender","type":"address"},{"indexed":false,"name":"value","type":"uint256"}],"name":"Withdrawn","type":"event"}]';
    var abi = JSON.parse(abriStr);
    var contract = web3.eth.contract(abi);

    var ethBridge =  contract.at("0x5318618CeDed558c9aCdB3D226257b3C46fAae37");

    return ethBridge;
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

    async queryCar(stub, args) {
        if (args.length != 1) {
            throw new Error('Incorrect number of arguments. Expecting CarNumber ex: CAR01');
        }
        let carNumber = args[0];

        let carAsBytes = await stub.getState(carNumber); //get the car from chaincode state
        if (!carAsBytes || carAsBytes.toString().length <= 0) {
            throw new Error(carNumber + ' does not exist: ');
        }
        console.log(carAsBytes.toString());
        return carAsBytes;
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

    async initLedger(stub, args) {
        console.info('============= START : Initialize Ledger ===========');
        let cars = [];
        cars.push({
            make: 'Rodrigo',
            model: 'Prius',
            color: 'blue',
            owner: 'Tomoko'
        });
        cars.push({
            make: 'Ford',
            model: 'Mustang',
            color: 'red',
            owner: 'Brad'
        });
        cars.push({
            make: 'Hyundai',
            model: 'Tucson',
            color: 'green',
            owner: 'Jin Soo'
        });
        cars.push({
            make: 'Volkswagen',
            model: 'Passat',
            color: 'yellow',
            owner: 'Max'
        });
        cars.push({
            make: 'Tesla',
            model: 'S',
            color: 'black',
            owner: 'Adriana'
        });
        cars.push({
            make: 'Peugeot',
            model: '205',
            color: 'purple',
            owner: 'Michel'
        });
        cars.push({
            make: 'Chery',
            model: 'S22L',
            color: 'white',
            owner: 'Aarav'
        });
        cars.push({
            make: 'Fiat',
            model: 'Punto',
            color: 'violet',
            owner: 'Pari'
        });
        cars.push({
            make: 'Tata',
            model: 'Nano',
            color: 'indigo',
            owner: 'Valeria'
        });
        cars.push({
            make: 'Holden',
            model: 'Barina',
            color: 'brown',
            owner: 'Shotaro'
        });
				

        for (let i = 0; i < cars.length; i++) {
            cars[i].docType = 'car';
            await stub.putState('CAR' + i, Buffer.from(JSON.stringify(cars[i])));
            console.info('Added <--> ', cars[i]);
        }
        console.info('============= END : Initialize Ledger ===========');
    }

    async createCar(stub, args) {
        console.info('============= START : Create Car ===========');
        if (args.length != 5) {
            throw new Error('Incorrect number of arguments. Expecting 5');
        }

        var car = {
            docType: 'car',
            make: args[1],
            model: args[2],
            color: args[3],
            owner: args[4]
        };

        await stub.putState(args[0], Buffer.from(JSON.stringify(car)));
        console.info('============= END : Create Car ===========');
    }

    async queryAllCars(stub, args) {

        let startKey = 'CAR0';
        let endKey = 'CAR999';

        let iterator = await stub.getStateByRange(startKey, endKey);

        let allResults = [];
        while (true) {
            let res = await iterator.next();

            if (res.value && res.value.value.toString()) {
                let jsonRes = {};
                console.log(res.value.value.toString('utf8'));

                jsonRes.Key = res.value.key;
                try {
                    jsonRes.Record = JSON.parse(res.value.value.toString('utf8'));
                } catch (err) {
                    console.log(err);
                    jsonRes.Record = res.value.value.toString('utf8');
                }
                allResults.push(jsonRes);
            }
            if (res.done) {
                console.log('end of data');
                await iterator.close();
                console.info(allResults);
                return Buffer.from(JSON.stringify(allResults));
            }
        }
    }

    async changeCarOwner(stub, args) {
        console.info('============= START : changeCarOwner ===========');
        if (args.length != 2) {
            throw new Error('Incorrect number of arguments. Expecting 2');
        }

        let carAsBytes = await stub.getState(args[0]);
        let car = JSON.parse(carAsBytes);
        car.owner = args[1];

        await stub.putState(args[0], Buffer.from(JSON.stringify(car)));
        console.info('============= END : changeCarOwner ===========');
    }
};

shim.start(new Chaincode());
