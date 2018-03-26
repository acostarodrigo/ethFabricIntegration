module.exports = class TokenWallet {
    constructor(balance) {
        this._balance = balance;
    }
    get balance() {
        return this._balance;
    }
}