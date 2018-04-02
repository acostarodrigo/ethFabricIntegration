/*
# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
*/

'use strict';
const shim = require('fabric-shim');
const ClientIdentity = require('fabric-shim').ClientIdentity;

let Chaincode = class {

    // The Init method is called when the Smart Contract 'fabcar' is instantiated by the blockchain network
    // Best practice is to have any Ledger initialization in separate function -- see initLedger()
    async Init(stub) {
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

    async transfer(stub, args){
        if (args.length != 3){
            throw new Error('Incorrect number of arguments. Expecting source, destination address and value.');
        }

        // requester must have balance
        let cid = new ClientIdentity(stub);
        let caller = cid.getID();
        let indexName = 'owner-address';
        let search = await stub.getStateByPartialCompositeKey(indexName, [caller, args[0]]);

        console.info(search);
        while (true){
            let response = search.next();
            console.info(response);
            if (!response || !response.value || !response.value.key) {
                return;
            }

            let attributes;
            ({
                objectType,
                attributes
            } = await stub.splitCompositeKey(response.value.key));

            let owner = attributes[0];
            let from = attributes[1];

            console.info(owner + ' ' + from);
        }



        // destination address must exists
        // I think I need to create a composite key and allow to search for the address.
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
