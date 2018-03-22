var tokenHolder = require('./tokenHolder')

class TokenWallet {
    constructor(tokenHolderParam, balance) {
        tokenHolder = tokenHolderParam;
        this._tokenHolder = tokenHolder;
        this._balance = balance;
    }


    get tokenHolder() {
        return this._tokenHolder;
    }

    get balance() {
        return this._balance;
    }
}